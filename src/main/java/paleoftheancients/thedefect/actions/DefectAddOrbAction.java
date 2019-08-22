package paleoftheancients.thedefect.actions;

import paleoftheancients.thedefect.monsters.orbs.AbstractBossOrb;
import paleoftheancients.thedefect.monsters.orbs.EmptyOrbSlot;
import paleoftheancients.thedefect.powers.DivertingPowerPower;
import paleoftheancients.thedefect.powers.KineticBarrierPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.FrozenCore;

public class DefectAddOrbAction extends AbstractGameAction {

    private AbstractBossOrb orb;
    private boolean replaceOrb;

    public DefectAddOrbAction(AbstractBossOrb orb, boolean replaceOrb) {
        this.orb = orb;
        this.replaceOrb = replaceOrb;
    }
    public DefectAddOrbAction(AbstractBossOrb orb) {
        this(orb, true);
    }

    public void update() {
        this.isDone = true;

        for(int i = 0; i < this.orb.owner.orbs.size(); i++) {
            AbstractBossOrb abo = this.orb.owner.orbs.get(i);
            if(abo instanceof EmptyOrbSlot) {
                this.orb.drawX = abo.drawX;
                this.orb.drawY = abo.drawY;
                this.orb.owner.orbs.remove(i);
                this.orb.owner.orbs.add(i, orb);

                this.orb.setSlot(i, this.orb.owner.maxOrbs);
                this.orb.applyFocus();

                if(this.orb.owner.hasPower(KineticBarrierPower.POWER_ID)) {
                    AbstractDungeon.actionManager.addToTop(new ReducePowerAction(this.orb.owner, this.orb.owner, KineticBarrierPower.POWER_ID, 1));
                } else {
                    AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(this.orb.owner, this.orb.owner, new DivertingPowerPower(this.orb.owner, 1), 1));
                }

                this.orb.addToRoom();

                if(!replaceOrb) {
                    //Because this is the only case where this happens.
                    AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(this.orb.owner, new FrozenCore()));
                }

                return;
            }
        }

        if(replaceOrb) {
            this.orb.owner.orbs.get(0).evoke(AbstractDungeon.player);
            AbstractDungeon.getCurrRoom().monsters.monsters.remove(this.orb.owner.orbs.get(0));
            this.orb.owner.orbs.remove(0);
            this.orb.owner.orbs.add(this.orb);
            this.orb.applyFocus();

            this.orb.addToRoom();

            for (int i = 0; i < this.orb.owner.orbs.size(); i++) {
                this.orb.owner.orbs.get(i).setSlot(i, this.orb.owner.maxOrbs);
            }
        }
    }
}
