package paleoftheancients.bard.powers;

import paleoftheancients.PaleMod;
import paleoftheancients.bard.helpers.MelodyManager;
import paleoftheancients.bard.melodies.AbstractMelody;
import paleoftheancients.bard.monsters.BardBoss;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

public class MelodyPotencyPower extends AbstractBardPower {
    public static final String POWER_ID = PaleMod.makeID("MelodyPotencyPower");
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;

    public MelodyPotencyPower(BardBoss owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.amount = amount;
        this.owner = owner;
        this.type = PowerType.BUFF;
        this.updateDescription();
        this.loadRegion("harmoniousVoice");
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + (this.amount) + DESCRIPTIONS[1];
    }

    @Override
    public void stackPower(int amount) {
        super.stackPower(amount);
        for(final AbstractMelody melody : MelodyManager.getAllMelodies()) {
            melody.applyPowers();
        }
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    }
}
