package paleoftheancients.thedefect.actions;

import paleoftheancients.thedefect.monsters.TheDefectBoss;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.SuicideAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class MultiCastAction extends AbstractGameAction {

    private TheDefectBoss tdb;
    private int amount;

    public MultiCastAction(TheDefectBoss tdb, int amount) {
        this.tdb = tdb;
        this.amount = amount;
    }

    public void update() {
        this.isDone = true;

        for(int i = 0; i < amount - 1; i++) {
            this.tdb.orbs.get(0).evoke(AbstractDungeon.player);
        }
        AbstractDungeon.actionManager.addToTop(new SuicideAction(this.tdb.orbs.get(0)));
    }
}
