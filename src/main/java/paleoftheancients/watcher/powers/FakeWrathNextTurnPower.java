package paleoftheancients.watcher.powers;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.watcher.WrathNextTurnPower;

public class FakeWrathNextTurnPower extends WrathNextTurnPower {
    private boolean sameturn = true;
    public FakeWrathNextTurnPower(AbstractCreature owner) {
        super(owner);
    }

    @Override
    public void atStartOfTurn() {}

    @Override
    public float atDamageFinalGive(float damage, DamageInfo.DamageType type) {
        return damage * 2F;
    }
}
