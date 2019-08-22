package paleoftheancients.theshowman.powers;

import paleoftheancients.PaleMod;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;


public class DummyColumbifyPower extends AbstractShowmanPower {
    public static final String POWER_ID = PaleMod.makeID("ColumbifyPower");
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;

    public static PowerType POWER_TYPE = PowerType.DEBUFF;
    public static final String IMG = "columbify.png";

    public DummyColumbifyPower(AbstractCreature owner) {
        super(IMG);
        this.owner = owner;

        this.name = NAME;
        this.ID = POWER_ID;
        this.type = POWER_TYPE;

        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];// + DESCRIPTIONS[1] + this.amount + DESCRIPTIONS[this.amount > 1 ? 3 : 2];
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    }
}
