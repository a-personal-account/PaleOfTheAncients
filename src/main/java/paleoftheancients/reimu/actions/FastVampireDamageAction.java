package paleoftheancients.reimu.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import com.megacrit.cardcrawl.vfx.combat.HealEffect;

public class FastVampireDamageAction extends AbstractGameAction {
    private DamageInfo info;
    private int multiplier;

    public FastVampireDamageAction(AbstractCreature target, DamageInfo info, AttackEffect effect) {
        this(target, info, effect, 3);
    }
    public FastVampireDamageAction(AbstractCreature target, DamageInfo info, AttackEffect effect, int multiplier) {
        this.info = info;
        this.setValues(target, info);
        this.actionType = ActionType.DAMAGE;
        this.attackEffect = effect;
        this.duration = 0.1F;
        this.multiplier = multiplier;
    }

    public void update() {
        if (this.duration == 0.1F) {
            AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, this.attackEffect));
        }

        this.tickDuration();
        if (this.isDone) {
            this.target.damage(this.info);
            if (this.target.lastDamageTaken > 0) {
                this.source.heal(this.target.lastDamageTaken * multiplier, false);
                AbstractDungeon.effectsQueue.add(new HealEffect(this.source.hb.cX - this.source.animX, this.source.hb.cY, this.target.lastDamageTaken * multiplier));
            }

            if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                AbstractDungeon.actionManager.clearPostCombatActions();
            }
        }

    }
}