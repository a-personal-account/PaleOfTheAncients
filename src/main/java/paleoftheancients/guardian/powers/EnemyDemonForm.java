package paleoftheancients.guardian.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.DemonFormPower;
import com.megacrit.cardcrawl.powers.StrengthPower;


public class EnemyDemonForm extends DemonFormPower {

    public EnemyDemonForm(AbstractCreature owner, int amount) {
        super(owner, amount);
    }

    @Override
    public void atEndOfRound() {
        this.flash();
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.owner, this.owner, new StrengthPower(this.owner, this.amount), this.amount));
    }

    public void atStartOfTurnPostDraw() {
    }
}
