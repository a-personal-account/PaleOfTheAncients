package paleoftheancients.theshowman.actions;

import paleoftheancients.theshowman.monsters.TheShowmanBoss;
import com.megacrit.cardcrawl.actions.AbstractGameAction;

public class ResolveCardsToHandAction extends AbstractGameAction {
    private TheShowmanBoss boss;

    public ResolveCardsToHandAction(TheShowmanBoss boss) {
        this.boss = boss;
    }

    @Override
    public void update() {

        this.isDone = true;
    }
}
