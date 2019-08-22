package paleoftheancients.ironcluck.powers;

import paleoftheancients.PaleMod;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.FlameBarrierPower;

public class ChickenBarrierPower extends FlameBarrierPower {
    public static final String POWER_ID = PaleMod.makeID("ChickenBarrierPower");

    public ChickenBarrierPower(AbstractCreature owner, int amount) {
        super(owner, amount);
        this.ID = POWER_ID;
    }

    @Override
    public void atStartOfTurn() {
        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, this));
    }
}
