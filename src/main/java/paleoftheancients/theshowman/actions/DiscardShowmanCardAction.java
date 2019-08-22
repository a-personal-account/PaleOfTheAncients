package paleoftheancients.theshowman.actions;

import paleoftheancients.theshowman.monsters.TheShowmanBoss;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class DiscardShowmanCardAction extends AbstractGameAction {
    private TheShowmanBoss boss;
    private AbstractCard card;

    public DiscardShowmanCardAction(TheShowmanBoss boss, AbstractCard card) {
        this.boss = boss;
        this.card = card;
    }

    @Override
    public void update() {
        this.boss.discardCard(card);
        this.isDone = true;
    }
}
