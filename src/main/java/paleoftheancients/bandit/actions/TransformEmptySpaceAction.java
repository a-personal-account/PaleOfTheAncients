package paleoftheancients.bandit.actions;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import paleoftheancients.bandit.board.AbstractBoard;
import paleoftheancients.bandit.board.spaces.AbstractSpace;
import paleoftheancients.bandit.board.spaces.symmetrical.EmptySpace;

import java.util.ArrayList;

public class TransformEmptySpaceAction extends AbstractGameAction {
    private final float STARTING_DURATION = 0.1F;
    private AbstractBoard board;
    private Class<? extends AbstractSpace> clz;

    public TransformEmptySpaceAction(AbstractBoard board, Class<? extends AbstractSpace> clz) {
        this.duration = STARTING_DURATION;

        this.board = board;
        this.clz = clz;
    }

    @Override
    public void update() {
        if(this.duration == STARTING_DURATION) {
            ArrayList<AbstractSpace> eligible = new ArrayList<>();
            for (AbstractSpace s : board.squareList) {
                if (s instanceof EmptySpace) {
                    eligible.add(s);
                }
            }

            if (!eligible.isEmpty()) {
                this.addToTop(new SFXAction("ORB_LIGHTNING_EVOKE", MathUtils.random(-0.1F, 0.1F)));
                AbstractSpace s = eligible.get(AbstractDungeon.monsterRng.random(eligible.size() - 1));
                board.transform(s, board.swapSquare(clz, s.x, s.y));
            }
        }
        this.tickDuration();
    }
}
