package paleoftheancients.theshowman.actions;

import paleoftheancients.theshowman.monsters.TheShowmanBoss;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class DiscardShowmanHandAction extends AbstractGameAction {
    private TheShowmanBoss boss;

    public DiscardShowmanHandAction(TheShowmanBoss boss) {
        this.boss = boss;
    }

    @Override
    public void update() {
        this.boss.discardHand();
        AbstractDungeon.actionManager.addToTop(new ResolveCardsToHandAction(this.boss));
        this.isDone = true;
    }
}
