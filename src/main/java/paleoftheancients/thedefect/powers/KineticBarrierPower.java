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


public class KineticBarrierPower extends AbstractPower {
    public static final String POWER_ID = PaleMod.makeID("KineticBarrierPower");
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;

    public static PowerType POWER_TYPE = PowerType.BUFF;

    public KineticBarrierPower(AbstractCreature owner, int amount) {
        this.owner = owner;
        this.amount = amount;
        this.isTurnBased = false;

        this.name = NAME;
        this.ID = POWER_ID;
        this.type = POWER_TYPE;

        this.loadRegion("buffer");

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
        switch (this.amount) {
            case 1:
                return 2F / 3F;
            case 2:
                return 1F / 3F;
            case 3:
                return 0.1F;
        }
        return 1F;
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + (int)((1 - this.mutliplyer()) * 100) + DESCRIPTIONS[1];
    }

    @Override
    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
        if(this.owner.hasPower(DivertingPowerPower.POWER_ID)) {
            this.amount -= this.owner.getPower(DivertingPowerPower.POWER_ID).amount;
        }
        if (this.amount <= 0) {
            AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
            if(this.amount < 0) {
                AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(this.owner, this.owner, new DivertingPowerPower(this.owner, -this.amount), -this.amount));
            }
        }
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    }
}
