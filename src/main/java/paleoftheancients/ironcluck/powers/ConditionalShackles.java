package paleoftheancients.ironcluck.powers;

import paleoftheancients.PaleMod;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.ShiftingPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class ConditionalShackles extends ShiftingPower {
    public static final String POWER_ID = PaleMod.makeID("ConditionalShackles");
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;

    public ConditionalShackles(AbstractCreature owner) {
        super(owner);
        this.ID = POWER_ID;
    }

    public int onAttacked(DamageInfo info, int damageAmount) {
        if (damageAmount > 0 && this.owner.hasPower(StrengthPower.POWER_ID)) {
            int reduceBy = Math.min(this.owner.getPower(StrengthPower.POWER_ID).amount, damageAmount);
            super.onAttacked(info, reduceBy);
        }

        return damageAmount;
    }

    public void updateDescription() {
        this.description = ShiftingPower.DESCRIPTIONS[1] + DESCRIPTIONS[0];
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    }
}
