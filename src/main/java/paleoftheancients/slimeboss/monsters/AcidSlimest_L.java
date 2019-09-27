package paleoftheancients.slimeboss.monsters;

import basemod.ReflectionHacks;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.actions.unique.CanLoseAction;
import com.megacrit.cardcrawl.actions.unique.CannotLoseAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.exordium.AcidSlime_L;
import com.megacrit.cardcrawl.monsters.exordium.AcidSlime_M;
import paleoftheancients.PaleMod;
import paleoftheancients.slimeboss.actions.ReallocateSlimeUnityAction;
import paleoftheancients.thevixen.cards.status.BossBurn;

public class AcidSlimest_L extends AcidSlime_L implements WeirdSlimeThing {
    public static String ID = PaleMod.makeID("AcidSlimest_L");
    private boolean suicided;
    private float height;
    public AcidSlimest_L(float x, float y, int poisonAmount, int newHealth) {
        super(x, y, poisonAmount, newHealth);
        this.id = ID;
        this.suicided = false;
        this.height = this.hb.height;
        this.usePreBattleAction();
    }

    public void usePreBattleAction() {
        SlimeHelper.usePreBattleAction(this);
    }

    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case 1:
                AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new SFXAction("MONSTER_SLIME_ATTACK"));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, (DamageInfo)this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(new BossBurn(), 2));
                AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
                break;

            case 3:
                float x = (float)ReflectionHacks.getPrivate(this, AcidSlime_L.class, "saveX");
                float y = (float)ReflectionHacks.getPrivate(this, AcidSlime_L.class, "saveY");
                AbstractDungeon.actionManager.addToBottom(new CannotLoseAction());
                SlimeHelper.splitShake(this);
                SlimeHelper.spawnEnemies(this,
                        new AcidSlime_M(x - 134.0F, y + MathUtils.random(-4.0F, 4.0F), 0, this.currentHealth),
                        new AcidSlime_M(x + 134.0F, y + MathUtils.random(-4.0F, 4.0F), 0, this.currentHealth));
                AbstractDungeon.actionManager.addToBottom(new CanLoseAction());
                this.suicided = true;
                break;

            default:
                super.takeTurn();
        }
        SlimeHelper.zombieCheck(this);
    }

    @Override
    public void die() {
        die(true);
    }
    @Override
    public void die(boolean triggerRelics) {
        if(SlimeHelper.reform(this)) {
            super.die(triggerRelics);

            AbstractDungeon.actionManager.addToBottom(new ReallocateSlimeUnityAction(this));
        } else {
            ReflectionHacks.setPrivate(this, AcidSlime_L.class, "splitTriggered", false);
        }
    }

    @Override
    public boolean getSuicided() {
        return suicided;
    }
    @Override
    public void resetSuicided() {
        this.suicided = false;
    }
    @Override
    public float getHeight() {
        return height;
    }
}
