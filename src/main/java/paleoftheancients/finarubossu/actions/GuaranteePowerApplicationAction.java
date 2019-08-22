package paleoftheancients.finarubossu.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ArtifactPower;

public class GuaranteePowerApplicationAction extends AbstractGameAction {
    private AbstractPower power;

    public GuaranteePowerApplicationAction(AbstractCreature target, AbstractCreature source, AbstractPower power) {
        this.target = target;
        this.source = source;
        this.power = power;
    }

    @Override
    public void update() {
        AbstractDungeon.actionManager.addToTop(new CircumventBuffDebuffNegationAction(this.target, this.source, this.power));
        if(this.power.type != AbstractPower.PowerType.DEBUFF || !this.target.hasPower(ArtifactPower.POWER_ID)) {
            AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(this.target, this.source, this.power, this.power.amount));
        }
        this.isDone = true;
    }
}
