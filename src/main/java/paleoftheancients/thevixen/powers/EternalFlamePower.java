package paleoftheancients.thevixen.powers;

import paleoftheancients.PaleMod;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;


public class EternalFlamePower extends AbstractTheVixenPower {
    public static final String POWER_ID = PaleMod.makeID("EternalFlamePower");
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;

    public static PowerType POWER_TYPE = PowerType.BUFF;
    public static final String IMG = "eternalflame.png";

    public EternalFlamePower(AbstractCreature owner) {
        super(IMG);
        this.owner = owner;
        this.amount = amount;
        this.isTurnBased = false;

        this.name = NAME;
        this.ID = POWER_ID;
        this.type = POWER_TYPE;

        updateDescription();

        this.priority = -99;
    }


    @Override
    public void atEndOfRound() {
        if (this.owner.hasPower(SunnyDayPower.POWER_ID)) {
            this.flash();
            AbstractDungeon.actionManager.addToBottom(new thevixen.actions.ApplyTempGainStrengthPowerAction(this.owner, this.owner, this.owner.getPower(SunnyDayPower.POWER_ID).amount));
        }
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    }
}
