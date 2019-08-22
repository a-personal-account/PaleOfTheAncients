package paleoftheancients.thedefect.actions;

import paleoftheancients.thedefect.monsters.orbs.AbstractBossOrb;
import paleoftheancients.thedefect.monsters.orbs.EmptyOrbSlot;
import com.megacrit.cardcrawl.actions.AbstractGameAction;

public class DefectRemoveOrbAction extends AbstractGameAction {

    AbstractBossOrb orb;

    public DefectRemoveOrbAction(AbstractBossOrb orb) {
        this.orb = orb;
    }

    public void update() {

        this.orb.owner.orbs.remove(orb);
        this.orb.owner.orbs.add(new EmptyOrbSlot(this.orb.owner));

        for(int i = 0; i < this.orb.owner.orbs.size(); i++) {
            this.orb.owner.orbs.get(i).setSlot(i, this.orb.owner.maxOrbs);
        }
        this.isDone = true;
    }
}
