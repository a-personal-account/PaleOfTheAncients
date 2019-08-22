package paleoftheancients.theshowman.powers;

import paleoftheancients.PaleMod;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.EnergizedBluePower;


public class EnergizedShowmanPower extends AbstractPower {
    public static final String POWER_ID = PaleMod.makeID("EnergizedShowmanPower");

    public EnergizedShowmanPower(AbstractCreature owner, int energyAmt) {
        super();
        this.name = EnergizedBluePower.NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = energyAmt;
        if (this.amount >= 999) {
            this.amount = 999;
        }

        this.updateDescription();
        this.loadRegion("energized_blue");
    }

    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        if (this.amount >= 999) {
            this.amount = 999;
        }

    }

    public void updateDescription() {
        if (this.amount == 1) {
            this.description = EnergizedBluePower.DESCRIPTIONS[0] + this.amount + EnergizedBluePower.DESCRIPTIONS[1];
        } else {
            this.description = EnergizedBluePower.DESCRIPTIONS[0] + this.amount + EnergizedBluePower.DESCRIPTIONS[2];
        }
    }

    @Override
    public void atStartOfTurn() {
        this.flash();
        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, this));
    }
}
