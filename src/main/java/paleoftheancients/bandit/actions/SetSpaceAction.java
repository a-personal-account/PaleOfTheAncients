package paleoftheancients.bandit.actions;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import paleoftheancients.bandit.board.AbstractBoard;
import paleoftheancients.bandit.board.spaces.AbstractSpace;

public class SetSpaceAction extends AbstractGameAction {
    private final float STARTING_DURATION = 0.1F;
    private AbstractBoard board;
    private Class<? extends AbstractSpace> clz;
    private int index;

    public SetSpaceAction(AbstractBoard board, Class<? extends AbstractSpace> clz, int index) {
        this.duration = STARTING_DURATION;

        this.board = board;
        this.clz = clz;
        this.index = index;
    }

    @Override
    public void update() {
        if(duration == STARTING_DURATION) {
            this.addToTop(new SFXAction("ORB_LIGHTNING_EVOKE", MathUtils.random(-0.1F, 0.1F)));
            AbstractSpace space = board.squareList.get(index);
            board.transform(index, board.swapSquare(clz, space.x, space.y));
        }
        this.tickDuration();
    }
}
