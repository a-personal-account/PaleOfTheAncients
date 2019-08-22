package paleoftheancients.thedefect.powers;

import paleoftheancients.PaleMod;
import paleoftheancients.thedefect.monsters.TheDefectBoss;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;


public class DivertingPowerPower extends AbstractPower {
    public static final String POWER_ID = PaleMod.makeID("DivertingPowerPower");
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;

    public static PowerType POWER_TYPE = PowerType.BUFF;

    public DivertingPowerPower(AbstractCreature owner, int amount) {
        this.owner = owner;
        this.amount = amount;
        this.isTurnBased = false;

        this.name = NAME;
        this.ID = POWER_ID;
        this.type = POWER_TYPE;

        this.loadRegion("mastery");

        updateDescription();
    }

    @Override
    public float atDamageReceive(float damage, DamageInfo.DamageType type) {
        if(this.owner instanceof TheDefectBoss) {
            return damage * mutliplyer();
        }
        return damage;
    }

    private float mutliplyer() {
        return (float) Math.pow(1.3, this.amount);
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + (int)((this.mutliplyer() - 1F) * 100) + DESCRIPTIONS[1];
    }

    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
        if(this.owner.hasPower(KineticBarrierPower.POWER_ID)) {
            this.amount -= this.owner.getPower(KineticBarrierPower.POWER_ID).amount;
        }
        if (this.amount <= 0) {
            AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
            if(this.amount < 0) {
                AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(this.owner, this.owner, new KineticBarrierPower(this.owner, -this.amount), -this.amount));
            }
        }
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    }
}
