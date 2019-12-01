package paleoftheancients.theshowman.powers;

import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import paleoftheancients.PaleMod;


public class VolunteeringPower extends AbstractShowmanPower {
    public static final String POWER_ID = PaleMod.makeID("VolunteeringPower");
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;

    public static PowerType POWER_TYPE = PowerType.DEBUFF;
    public static final String IMG = "volunteering.png";

    public VolunteeringPower(AbstractCreature owner, int amount) {
        super(IMG);
        this.owner = owner;

        this.name = NAME;
        this.ID = POWER_ID;
        this.type = POWER_TYPE;
        this.amount = amount;

        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
        if(this.amount > 0) {
            this.description += DESCRIPTIONS[2];
        } else {
            this.description += DESCRIPTIONS[1];
        }
    }

    @Override
    public int onAttackToChangeDamage(DamageInfo info, int damageAmount) {
        if(info.type == DamageInfo.DamageType.NORMAL) {
            if (this.amount > 1) {
                AbstractDungeon.actionManager.addToTop(new ReducePowerAction(this.owner, this.owner, this, 1));
            } else {
                AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, this));
            }
            AbstractDungeon.actionManager.addToTop(new DamageAction(this.owner, info));
            return 0;
        }
        return damageAmount;
    }
    @Override
    public void atEndOfTurn(boolean isPlayer) {
        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, this));
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    }
}
