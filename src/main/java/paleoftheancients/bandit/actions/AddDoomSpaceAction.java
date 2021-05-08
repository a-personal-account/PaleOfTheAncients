package paleoftheancients.bandit.actions;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import paleoftheancients.bandit.board.AbstractBoard;
import paleoftheancients.bandit.board.rarespaces.DoomSpace;
import paleoftheancients.bandit.board.spaces.AbstractSpace;
import paleoftheancients.bandit.board.spaces.symmetrical.EmptySpace;

import java.util.ArrayList;

public class AddDoomSpaceAction extends AbstractGameAction {
    private final float STARTING_DURATION = 0.1F;
    private AbstractBoard board;
    private int amount;

    public AddDoomSpaceAction(AbstractBoard board, int amount) {
        this.duration = STARTING_DURATION;

        this.board = board;
        this.amount = amount;
    }

    @Override
    public void update() {
        if(this.duration == STARTING_DURATION) {
            int count = 0;
            for (AbstractSpace s : board.squareList) {
                if (s instanceof DoomSpace) {
                    count++;
                }
            }

            for(int i = count; i < amount; i++) {
                this.addToTop(new TransformEmptySpaceAction(board, DoomSpace.class));
            }
        }
        this.tickDuration();
    }
}
