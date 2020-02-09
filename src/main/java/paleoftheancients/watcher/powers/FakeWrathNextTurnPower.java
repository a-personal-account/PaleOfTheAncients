package paleoftheancients.watcher.powers;

import com.megacrit.cardcrawl.actions.common.ChangeStateAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.watcher.WrathNextTurnPower;
import com.megacrit.cardcrawl.stances.WrathStance;

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

    @Override
    public void atEndOfRound() {
        if(sameturn) {
            sameturn = false;
        } else {
            this.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, this));
            this.addToTop(new ChangeStateAction((AbstractMonster) this.owner, WrathStance.STANCE_ID));
        }
    }
}
