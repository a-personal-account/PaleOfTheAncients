package paleoftheancients.bandit.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import paleoftheancients.bandit.actions.AddDoomSpaceAction;
import paleoftheancients.bandit.actions.TransformEmptySpaceAction;
import paleoftheancients.bandit.board.BanditBoard;

public class BoardBoundEnemyPower extends BoardBoundPower {

    public BoardBoundEnemyPower(AbstractCreature owner, BanditBoard board, int amount) {
        super(owner, board);
        this.amount = amount;
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        StringBuilder builder = new StringBuilder();
        builder.append(DESCRIPTIONS[1]).append(DESCRIPTIONS[3]).append(this.amount).append(DESCRIPTIONS[4])
                .append(DESCRIPTIONS[this.amount > 1 ? 6 : 5]).append(DESCRIPTIONS[7]);
        this.description = builder.toString();
    }

    @Override
    public void atEndOfRound() {
        this.addToBot(new AddDoomSpaceAction(board, this.amount));
        for(int i = 0; i < this.amount; i++) {
            this.addToBot(new TransformEmptySpaceAction(board, board.getRandomRareSquare()));
        }
    }
}
