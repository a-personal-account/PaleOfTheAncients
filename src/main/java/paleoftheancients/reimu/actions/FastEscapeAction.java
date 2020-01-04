package paleoftheancients.reimu.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class FastEscapeAction extends AbstractGameAction {
    private AbstractMonster m;

    public FastEscapeAction(AbstractMonster target) {
        this.duration = 0.0F;
        this.actionType = ActionType.DAMAGE;
        this.m = target;
    }

    public void update() {
        if (this.duration == 0.0F) {
            this.m.escapeTimer = -1F;
        }

        this.tickDuration();
    }
}
