package paleoftheancients.slimeboss.powers;

import paleoftheancients.PaleMod;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;


public class SlimeSplitPower extends AbstractPower {
    public static final String POWER_ID = PaleMod.makeID("SlimeSplitPower");
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;

    public static PowerType POWER_TYPE = PowerType.BUFF;

    public SlimeSplitPower(AbstractCreature owner, int amount) {
        super();
        this.owner = owner;
        this.amount = amount;

        this.loadRegion("loop");

        this.name = NAME;
        this.ID = POWER_ID;
        this.type = POWER_TYPE;

        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
        if(this.amount == 1) {
            this.description += DESCRIPTIONS[1];
        } else {
            this.description += this.amount + DESCRIPTIONS[2];
        }
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    }
}
