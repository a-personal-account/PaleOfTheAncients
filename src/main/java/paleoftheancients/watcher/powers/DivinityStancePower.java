package paleoftheancients.watcher.powers;

import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.localization.StanceStrings;
import com.megacrit.cardcrawl.stances.DivinityStance;

public class DivinityStancePower extends AbstractStancePower {
    private static final StanceStrings stanceString = (StanceStrings) ReflectionHacks.getPrivateStatic(DivinityStance.class, "stanceString");

    public DivinityStancePower(AbstractCreature owner) {
        super(owner, stanceString);
    }

    @Override
    public float atDamageFinalGive(float damage, DamageInfo.DamageType type) {
        return damage * 3F;
    }
}