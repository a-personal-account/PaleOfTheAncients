package paleoftheancients.reimu.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;

public abstract class AbstractDamagingAction extends AbstractGameAction {
    protected AbstractCreature target, source;
    protected DamageInfo info;
    protected int num;
    public AbstractDamagingAction(AbstractCreature target, AbstractCreature source, DamageInfo info, int num) {
        this.target = target;
        this.source = source;
        this.info = info;
        this.num = num;
    }
}
