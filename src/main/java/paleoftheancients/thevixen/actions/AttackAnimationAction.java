package thevixen.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class AttackAnimationAction extends AbstractGameAction {

    public AttackAnimationAction(AbstractCreature target) {
        this.target = target;

        this.duration = Settings.ACTION_DUR_FAST;
        this.startDuration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if(this.duration == this.startDuration) {
            target.useFastAttackAnimation();
            this.isDone = true;
        }

        this.tickDuration();
    }
}
