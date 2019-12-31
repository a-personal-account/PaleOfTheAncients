package paleoftheancients.reimu.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import paleoftheancients.PaleMod;

public class ShotTypeBasePower extends ShotTypePower {
    public static final String POWER_ID = PaleMod.makeID("ShotTypeBasePower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public ShotTypeBasePower(AbstractCreature owner) {
        super(owner, POWER_ID, NAME);

        if(owner != null) {
            this.loadRegion("bias");
        }

        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }

    @Override
    public ShotTypePower makeCopy(AbstractMonster mo) {
        return new ShotTypeBasePower(mo);
    }
}