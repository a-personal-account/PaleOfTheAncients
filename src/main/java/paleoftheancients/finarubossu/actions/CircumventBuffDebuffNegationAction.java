package paleoftheancients.finarubossu.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.Collections;

public class CircumventBuffDebuffNegationAction extends AbstractGameAction {
    private AbstractPower power;

    public CircumventBuffDebuffNegationAction(AbstractCreature target, AbstractCreature source, AbstractPower power) {
        this.target = target;
        this.source = source;
        this.power = power;
    }

    @Override
    public void update() {
        if(!target.hasPower(power.ID)) {
            this.target.powers.add(this.power);
            this.power.onInitialApplication();
            Collections.sort(this.target.powers);
        }
        this.isDone = true;
    }
}
