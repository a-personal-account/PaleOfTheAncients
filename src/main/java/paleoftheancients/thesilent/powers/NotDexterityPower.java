package paleoftheancients.thesilent.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import paleoftheancients.PaleMod;


public class NotDexterityPower extends AbstractSilentPower {
    public static final String POWER_ID = PaleMod.makeID("NotDexterityPower");
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;

    public static PowerType POWER_TYPE = PowerType.DEBUFF;

    public NotDexterityPower(AbstractCreature owner, int amount) {
        super();
        this.owner = owner;
        this.amount = amount;
        this.isTurnBased = false;

        this.name = NAME;
        this.ID = POWER_ID;
        this.type = POWER_TYPE;
        this.canGoNegative = true;

        this.loadRegion("dexterity");
        updateDescription();
    }


    public void reducePower(int reduceAmount) {
        this.fontScale = 8.0F;
        this.amount -= reduceAmount;

        if (this.amount <= -999) {
            this.amount = -999;
        }

    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    }
}
