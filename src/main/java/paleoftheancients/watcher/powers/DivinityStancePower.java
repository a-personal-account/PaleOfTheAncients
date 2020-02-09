package paleoftheancients.watcher.powers;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;

public class DivinityStancePower extends AbstractStancePower {

    public DivinityStancePower(AbstractCreature owner) {
        super(owner);
    }

    @Override
    public float atDamageFinalGive(float damage, DamageInfo.DamageType type) {
        return damage * 3F;
    }
}
