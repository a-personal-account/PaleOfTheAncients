package paleoftheancients.bard.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.NonStackablePower;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import paleoftheancients.PaleMod;

public class InspirationPower extends AbstractBardTwoAmountPower implements NonStackablePower, CloneablePowerInterface {
    public static final String POWER_ID = PaleMod.makeID("Inspiration");
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;
    private boolean reduceThisTurn = false;
    private boolean justApplied = true;

    public InspirationPower(AbstractCreature owner, int cards, int percent) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = PowerType.BUFF;
        this.amount = cards;
        this.amount2 = percent;
        this.priority = 7;
        this.updateDescription();
        this.loadRegion("inspiration");
    }

    public void updateDescription() {
        this.name = NAME + " " + this.amount2;
        if (this.amount == 1) {
            this.description = DESCRIPTIONS[0] + DESCRIPTIONS[2] + DESCRIPTIONS[4]  + DESCRIPTIONS[5] + DESCRIPTIONS[7] + this.amount2 + DESCRIPTIONS[8];
        } else {
            this.description = DESCRIPTIONS[1] + this.amount + DESCRIPTIONS[3] + DESCRIPTIONS[4] + DESCRIPTIONS[6] + DESCRIPTIONS[7]  + this.amount2 + DESCRIPTIONS[8];
        }

    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type) {
        if (!this.justApplied && type == DamageInfo.DamageType.NORMAL) {
            return damage * (1 + this.amount2 / 100F);
        } else {
            return damage;
        }
    }
    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (info.type == DamageInfo.DamageType.NORMAL) {
            this.reduceThisTurn = true;
        }
    }
    @Override
    public void atEndOfRound() {
        if(!this.justApplied && this.reduceThisTurn) {
            if(this.amount <= 1) {
                AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, this));
            } else {
                AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(this.owner, this.owner, this, 1));
            }
        } else {
            this.justApplied = false;
        }
        this.reduceThisTurn = false;
    }

    public boolean isStackable(AbstractPower power) {
        return power instanceof InspirationPower && this.amount2 == ((InspirationPower)power).amount2;
    }

    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
    }

    public AbstractPower makeCopy() {
        return new InspirationPower(this.owner, this.amount, this.amount2);
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    }
}
