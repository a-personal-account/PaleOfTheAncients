package paleoftheancients.bard.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

public class LifeDrainAction extends AbstractGameAction {
    private int damage;

    public LifeDrainAction(AbstractCreature target, AbstractCreature source, int amount, DamageInfo.DamageType type, AttackEffect effect) {
        this.setValues(target, source, amount);
        this.damage = amount;
        this.actionType = ActionType.DAMAGE;
        this.damageType = type;
        this.attackEffect = effect;
    }

    public void update() {
        if (this.duration == 0.5F) {
            AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, this.attackEffect));
        }

        this.tickDuration();
        if (this.isDone) {
            this.tempHp();
            this.target.damage(new DamageInfo(this.source, this.damage, this.damageType));
            if (AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
                AbstractDungeon.actionManager.clearPostCombatActions();
            }

            AbstractDungeon.actionManager.addToTop(new WaitAction(0.1F));
        }

    }

    private void tempHp() {
        int tmp = this.damage;
        tmp -= this.target.currentBlock;
        if (tmp > this.target.currentHealth) {
            tmp = this.target.currentHealth;
        }

        tmp /= 2;
        if (tmp > 0) {
            if(this.source.maxHealth < this.source.currentHealth + tmp) {
                this.source.maxHealth = this.source.currentHealth + tmp;
            }
            AbstractDungeon.actionManager.addToBottom(new HealAction(this.source, this.source, tmp));
        }

    }
}
