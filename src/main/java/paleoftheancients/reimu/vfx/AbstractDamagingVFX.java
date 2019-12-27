package paleoftheancients.reimu.vfx;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import paleoftheancients.reimu.actions.DelayActionAction;

public abstract class AbstractDamagingVFX extends AbstractGameEffect {
    protected AbstractCreature target;
    protected DamageInfo info;
    protected int num;
    protected DelayActionAction delay;


    public AbstractDamagingVFX(AbstractCreature target, DamageInfo info, int num) {
        this.target = target;
        this.info = info;
        this.num = num;

        this.delay = null;
    }

    protected void setDelay() {
        if(this.delay != null) {
            this.delay.end();
        }
        this.delay = new DelayActionAction();
        AbstractDungeon.actionManager.addToTop(this.delay);
    }
    protected void endDelay() {
        this.delay.end();
    }
}
