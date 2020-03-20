package paleoftheancients.bandit.powers;

import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import paleoftheancients.bandit.board.AbstractBoard;
import paleoftheancients.bandit.board.BanditBoard;

public class BoardBoundPlayerPower extends BoardBoundPower {

    public BoardBoundPlayerPower(AbstractCreature owner, BanditBoard board) {
        super(owner, board);
        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[1] + DESCRIPTIONS[2];
    }

    @Override
    public void onCardDraw(AbstractCard card) {
        board.processCard(card);
    }
    public void atEndOfTurn(boolean isPlayer) {
        for(final AbstractCard card : AbstractDungeon.player.hand.group) {
            this.onCardDraw(card);
        }
    }

    @Override
    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
        if(board.cards.containsKey(card.cardID)) {
            board.move(AbstractDungeon.player, board.cards.get(card.cardID));
        } else {
            board.move(AbstractDungeon.player, 1);
            board.processCard(card);
        }
    }
}
