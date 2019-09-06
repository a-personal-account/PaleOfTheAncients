package paleoftheancients.thevixen.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import paleoftheancients.PaleMod;
import paleoftheancients.thevixen.actions.ApplyTempGainStrengthPowerAction;


public class SunnyDayPower extends AbstractTheVixenPower {
    public static final String POWER_ID = PaleMod.makeID("SunnyDayPower");
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;

    public static int totalAmount = 0;

    public static PowerType POWER_TYPE = PowerType.BUFF;
    public static final String IMG = "sunnyday.png";

    public SunnyDayPower(AbstractCreature owner, int amount) {
        super(IMG);
        this.owner = owner;
        this.amount = amount;
        this.isTurnBased = false;

        this.name = NAME;
        this.ID = POWER_ID;
        this.type = POWER_TYPE;

        updateDescription();
        this.priority = -98;
    }

    @Override
    public void updateDescription() {
        if(this.owner == AbstractDungeon.player) {
            if (this.amount == 1) {
                this.description = DESCRIPTIONS[0];
            } else {
                this.description = DESCRIPTIONS[1] + this.amount + DESCRIPTIONS[2];
            }
        } else {
            this.description = DESCRIPTIONS[3];
        }
    }

    @Override
    public void atEndOfRound() {
        this.flash();
        AbstractDungeon.actionManager.addToBottom(new ApplyTempGainStrengthPowerAction(this.owner, this.owner, this.owner.getPower(SunnyDayPower.POWER_ID).amount));
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    }
}
