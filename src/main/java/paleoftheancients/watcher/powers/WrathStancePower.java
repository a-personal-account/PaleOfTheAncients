package paleoftheancients.watcher.powers;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.stances.WrathStance;

public class WrathStancePower extends AbstractStancePower {

    public WrathStancePower(AbstractCreature owner) {
        super(owner);
        this.name = WrathStance.STANCE_ID;
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
