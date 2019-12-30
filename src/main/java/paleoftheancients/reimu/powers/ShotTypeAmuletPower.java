package paleoftheancients.reimu.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import paleoftheancients.PaleMod;

public class ShotTypeAmuletPower extends AbstractPower {
    public static final String POWER_ID = PaleMod.makeID("ShotTypeAmuletPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public boolean homing;

    public ShotTypeAmuletPower(AbstractCreature owner) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;

        type = PowerType.BUFF;
        isTurnBased = false;

        this.loadRegion("ai");

        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        homing = false;
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }
}