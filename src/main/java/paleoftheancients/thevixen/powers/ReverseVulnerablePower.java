package paleoftheancients.thevixen.powers;

import paleoftheancients.PaleMod;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.VulnerablePower;


public class ReverseVulnerablePower extends AbstractTheVixenPower {
    public static final String POWER_ID = PaleMod.makeID("ReverseVulnerablePower");
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;

    public static PowerType POWER_TYPE = PowerType.BUFF;
    public static final String IMG = "trickroom.png";

    private boolean attacked;

    public ReverseVulnerablePower(AbstractCreature owner, int amount) {
        super(IMG);
        this.owner = owner;
        this.amount = amount;
        this.isTurnBased = true;

        this.name = NAME;
        this.ID = POWER_ID;
        this.type = POWER_TYPE;

        this.attacked = false;

        updateDescription();
    }

    @Override
    public float atDamageReceive(float damage, DamageInfo.DamageType type) {
        if (type == DamageInfo.DamageType.NORMAL && this.owner.hasPower(VulnerablePower.POWER_ID)) {
            return (damage / 1.5F) * (2F / 3F);
        } else {
            return damage;
        }
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if(this.owner.hasPower(VulnerablePower.POWER_ID)) {
            this.attacked = true;
            this.flash();
        }
        return damageAmount;
    }

    @Override
    public void atEndOfRound() {
        if(this.attacked) {
            if (this.amount == 0) {
                AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, this.POWER_ID));
            } else if(this.amount > 0) {
                AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(this.owner, this.owner, this.POWER_ID, 1));
            }
        }
        this.attacked = false;
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
        if(this.amount == 0) {
            this.description += DESCRIPTIONS[1];
        } else if(this.amount > 0) {
            this.description += DESCRIPTIONS[2] + this.amount + DESCRIPTIONS[3];
        } else {
            this.description += DESCRIPTIONS[4];
        }
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    }
}
