package paleoftheancients.watcher.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import paleoftheancients.watcher.powers.FakeMarkPower;

public class PressurePointDamageAction extends AbstractGameAction {
    AbstractCreature target, source;
    public PressurePointDamageAction(AbstractCreature target, AbstractCreature source) {
        this.target = target;
        this.source = source;
    }

    @Override
    public void update() {
        int amnt = 0;
        if(target.hasPower(FakeMarkPower.POWER_ID)) {
            amnt = target.getPower(FakeMarkPower.POWER_ID).amount;
        }
        this.addToTop(new DamageAction(target, new DamageInfo(source, amnt, DamageInfo.DamageType.THORNS), AttackEffect.FIRE));
        this.isDone = true;
    }
}