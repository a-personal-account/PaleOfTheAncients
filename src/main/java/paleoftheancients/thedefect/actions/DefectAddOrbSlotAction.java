package paleoftheancients.thedefect.actions;

import paleoftheancients.thedefect.monsters.TheDefectBoss;
import paleoftheancients.thedefect.monsters.orbs.EmptyOrbSlot;
import com.megacrit.cardcrawl.actions.AbstractGameAction;

public class DefectAddOrbSlotAction extends AbstractGameAction {

    TheDefectBoss tdb;

    public DefectAddOrbSlotAction(TheDefectBoss tdb) {
        this.tdb = tdb;
    }

    public void update() {
        this.tdb.maxOrbs++;

        this.tdb.orbs.add(new EmptyOrbSlot(tdb));

        for(int i = 0; i < this.tdb.maxOrbs; ++i) {
            this.tdb.orbs.get(i).setSlot(i, this.tdb.maxOrbs);
        }

        this.isDone = true;
    }
}
