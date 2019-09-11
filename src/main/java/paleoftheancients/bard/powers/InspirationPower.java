package paleoftheancients.bard.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.NonStackablePower;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import paleoftheancients.PaleMod;

import java.util.Iterator;

public class InspirationPower extends AbstractBardTwoAmountPower implements NonStackablePower, CloneablePowerInterface {
    public static final String POWER_ID = PaleMod.makeID("Inspiration");
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;

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
            this.description = DESCRIPTIONS[0] + DESCRIPTIONS[2] + DESCRIPTIONS[4] + this.amount2 + DESCRIPTIONS[5];
        } else {
            this.description = DESCRIPTIONS[1] + this.amount + DESCRIPTIONS[3] + DESCRIPTIONS[4] + this.amount2 + DESCRIPTIONS[5];
        }

    }

    public int onAttackToChangeDamage(DamageInfo info, int damageAmount) {
        if (this == this.owner.getPower(this.ID) && info.type == DamageInfo.DamageType.NORMAL) {
            float additiveDamage = 0.0F;
            Iterator var4 = this.owner.powers.iterator();

            while(var4.hasNext()) {
                AbstractPower p = (AbstractPower)var4.next();
                if (p instanceof InspirationPower) {
                    additiveDamage += damageAmount * ((float)((InspirationPower)p).amount2 / 100.0F);
                }
            }

            return damageAmount + (int)additiveDamage;
        } else {
            return damageAmount;
        }
    }

    public static int calcBlock(AbstractCreature owner, int baseBlock) {
        float additiveBlock = 0.0F;
        for(final AbstractPower pow : owner.powers) {
            if(pow instanceof InspirationPower) {
                additiveBlock += baseBlock * ((float)((InspirationPower)pow).amount2 / 100.0F);
            }
        }
        return baseBlock + (int)additiveBlock;
    }
    public static void reduceAllStacks(AbstractCreature owner) {
        for(final AbstractPower pow : owner.powers) {
            if(pow instanceof InspirationPower) {
                AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(owner, owner, pow, 1));
            }
        }
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
