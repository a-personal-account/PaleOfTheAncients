package paleoftheancients.thedefect.actions;

import paleoftheancients.thedefect.monsters.TheDefectBoss;
import paleoftheancients.thedefect.monsters.orbs.AbstractBossOrb;
import paleoftheancients.thedefect.monsters.orbs.EmptyOrbSlot;
import com.megacrit.cardcrawl.actions.AbstractGameAction;

public class DefectRemoveOrbSlotAction extends AbstractGameAction {

    TheDefectBoss tdb;

    public DefectRemoveOrbSlotAction(TheDefectBoss tdb) {
        this.tdb = tdb;
    }

    public void update() {
        this.tdb.maxOrbs--;
        AbstractBossOrb abo;

        if (!this.tdb.orbs.isEmpty()) {
            abo = this.tdb.orbs.get(this.tdb.maxOrbs);
            if(abo.id != EmptyOrbSlot.ID) {
                abo.removeFromRoom();
            }
            this.tdb.orbs.remove(this.tdb.maxOrbs);
        }

        for(int i = 0; i < this.tdb.maxOrbs; ++i) {
            abo = this.tdb.orbs.get(i);
            abo.setSlot(i, this.tdb.maxOrbs);
            abo.applyFocus();
        }


        this.isDone = true;
    }
}
