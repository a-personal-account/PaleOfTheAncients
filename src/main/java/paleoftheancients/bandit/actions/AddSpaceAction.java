package paleoftheancients.bandit.actions;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import paleoftheancients.bandit.board.AbstractBoard;
import paleoftheancients.bandit.board.spaces.AbstractSpace;

public class AddSpaceAction extends AbstractGameAction {
    private final float STARTING_DURATION = 0.05F;
    private AbstractBoard board;
    private Class<? extends AbstractSpace> clz;
    private int x, y;

    public AddSpaceAction(AbstractBoard board, Class<? extends AbstractSpace> clz, int x, int y) {
        this.duration = STARTING_DURATION;

        this.board = board;
        this.clz = clz;
        this.x = x;
        this.y = y;
    }

    @Override
    public void update() {
        if(this.duration == STARTING_DURATION) {
            this.addToTop(new SFXAction("BLUNT_HEAVY", MathUtils.random(-0.1F, 0.1F)));
            board.squareList.add(board.swapSquare(clz, x, y));
        }
        this.tickDuration();
    }
}
