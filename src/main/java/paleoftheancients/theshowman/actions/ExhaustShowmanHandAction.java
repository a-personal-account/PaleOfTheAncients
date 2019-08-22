package paleoftheancients.theshowman.actions;

import paleoftheancients.theshowman.monsters.TheShowmanBoss;
import com.megacrit.cardcrawl.actions.AbstractGameAction;

public class ExhaustShowmanHandAction extends AbstractGameAction {
    private TheShowmanBoss boss;

    public ExhaustShowmanHandAction(TheShowmanBoss boss) {
        this.boss = boss;
    }

    @Override
    public void update() {
        this.boss.exhaustHand();
        this.isDone = true;
    }
}
