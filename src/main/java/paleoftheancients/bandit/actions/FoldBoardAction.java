package paleoftheancients.bandit.actions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import paleoftheancients.bandit.board.AbstractBoard;
import paleoftheancients.bandit.board.spaces.AbstractSpace;

public class FoldBoardAction extends AbstractGameAction {
    private int offset;
    private AbstractBoard board;
    private float phase;
    private float velocity;
    private Vector2[] angles;
    private float startingAngle;
    private float endAngle;

    public FoldBoardAction(AbstractBoard board, float startingAngle, float endAngle, int offset) {
        this.board = board;
        this.offset = offset;
        this.startingAngle = startingAngle;
        this.endAngle = endAngle;
        this.velocity = 0F;
        this.phase = startingAngle;
    }

    @Override
    public void update() {
        AbstractSpace space;
        AbstractSpace referenceSpace = board.squareList.get(offset);
        if(this.velocity == 0F) {
            this.angles = new Vector2[board.squareList.size() - offset - 1];
            for(int i = 1; i < board.squareList.size() - offset; i++) {
                space = board.squareList.get(i + offset);
                angles[i - 1] = new Vector2(space.x - referenceSpace.x, space.y - referenceSpace.y);
            }
        } else {
            for(int i = 1; i < board.squareList.size() - offset; i++) {
                space = board.squareList.get(i + offset);
                space.rotation = -this.phase;
                float newAngle = (float)(Math.toRadians(this.phase + 90F - startingAngle) - angles[i - 1].angleRad());
                space.x = referenceSpace.x + (int)(angles[i - 1].len() * Math.sin(newAngle));
                space.y = referenceSpace.y + (int)(angles[i - 1].len() * Math.cos(newAngle));
                space.hb.move(space.x + space.hb.width / 2F, space.y + space.hb.height / 2F);
            }
            if(this.phase >= endAngle) {
                //for(int i = 1; i <= length; i++) {
                //    board.squareList.get(i + offset).rotation = 0F;
                //}
                this.isDone = true;
                return;
            }
        }
        this.velocity += Gdx.graphics.getDeltaTime();
        this.phase += this.velocity;
        if(this.phase >= endAngle) {
            this.phase = endAngle;
        }
    }
}
