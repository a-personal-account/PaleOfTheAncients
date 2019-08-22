package paleoftheancients.theshowman.actions;

import paleoftheancients.theshowman.monsters.TheShowmanBoss;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class ExhaustShowmanCardAction extends AbstractGameAction {
    private TheShowmanBoss boss;
    private AbstractCard card;

    public ExhaustShowmanCardAction(TheShowmanBoss boss, AbstractCard card) {
        this.boss = boss;
        this.card = card;
    }

    @Override
    public void update() {
        this.boss.exhaustCard(card);
        this.isDone = true;
    }
}
