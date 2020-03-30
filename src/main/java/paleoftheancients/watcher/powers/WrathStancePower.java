package paleoftheancients.watcher.powers;

import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.localization.StanceStrings;
import com.megacrit.cardcrawl.stances.WrathStance;

public class WrathStancePower extends AbstractStancePower {
    private static final StanceStrings stanceString = (StanceStrings) ReflectionHacks.getPrivateStatic(WrathStance.class, "stanceString");

    public WrathStancePower(AbstractCreature owner) {
        super(owner, stanceString);
    }

    @Override
    public float atDamageFinalGive(float damage, DamageInfo.DamageType type) {
        return damage * 2F;
    }

    @Override
    public float atDamageFinalReceive(float damage, DamageInfo.DamageType type) {
        return type == DamageInfo.DamageType.NORMAL ? damage * 2F : damage;
    }
}
