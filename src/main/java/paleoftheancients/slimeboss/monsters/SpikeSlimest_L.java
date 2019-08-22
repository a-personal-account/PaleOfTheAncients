package paleoftheancients.slimeboss.monsters;

import paleoftheancients.PaleMod;
import paleoftheancients.slimeboss.actions.ReallocateSlimeUnityAction;
import paleoftheancients.slimeboss.powers.SlimeSplitPower;
import paleoftheancients.slimeboss.powers.SlimeUnityPower;
import paleoftheancients.thevixen.cards.status.BossBurn;
import basemod.ReflectionHacks;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateShakeAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.unique.CanLoseAction;
import com.megacrit.cardcrawl.actions.unique.CannotLoseAction;
import com.megacrit.cardcrawl.actions.utility.HideHealthBarAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.exordium.SpikeSlime_L;
import com.megacrit.cardcrawl.monsters.exordium.SpikeSlime_M;

public class SpikeSlimest_L extends SpikeSlime_L implements WeirdSlimeThing {
    public static String ID = PaleMod.makeID("SpikeSlimest_L");
    private boolean suicided;
    private float height;
    public SpikeSlimest_L(float x, float y, int poisonAmount, int newHealth) {
        super(x, y, poisonAmount, newHealth);
        this.id = ID;
        this.suicided = false;
        this.height = this.hb.height;
        SlimeBossest.applyInvincible(this);
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new SlimeSplitPower(this, SlimeBossest.SPLIT_AMOUNT / 2), SlimeBossest.SPLIT_AMOUNT / 2));
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
                AbstractMonster mo;
                float x = (float)ReflectionHacks.getPrivate(this, SpikeSlime_L.class, "saveX");
                float y = (float)ReflectionHacks.getPrivate(this, SpikeSlime_L.class, "saveY");
                AbstractDungeon.actionManager.addToBottom(new CannotLoseAction());
                AbstractDungeon.actionManager.addToBottom(new AnimateShakeAction(this, 1.0F, 0.1F));
                AbstractDungeon.actionManager.addToBottom(new HideHealthBarAction(this));
                AbstractDungeon.actionManager.addToBottom(new SuicideAction(this, false));
                AbstractDungeon.actionManager.addToBottom(new WaitAction(1.0F));
                AbstractDungeon.actionManager.addToBottom(new SFXAction("SLIME_SPLIT"));
                AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(mo = new SpikeSlime_M(x - 134.0F, y + MathUtils.random(-4.0F, 4.0F), 0, this.currentHealth), false, AbstractDungeon.getCurrRoom().monsters.monsters.size()));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(mo, this, new SlimeUnityPower(mo, this)));
                AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(mo = new SpikeSlime_M(x + 134.0F, y + MathUtils.random(-4.0F, 4.0F), 0, this.currentHealth), false, AbstractDungeon.getCurrRoom().monsters.monsters.size()));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(mo, this, new SlimeUnityPower(mo, this)));
                AbstractDungeon.actionManager.addToBottom(new CanLoseAction());
                this.suicided = true;
                break;

            default:
                super.takeTurn();
        }
    }

    @Override
    public void die(boolean triggerRelics) {
        if(SlimeBossest.reform(this)) {
            super.die(triggerRelics);

            AbstractDungeon.actionManager.addToBottom(new ReallocateSlimeUnityAction(this));
        } else {
            ReflectionHacks.setPrivate(this, SpikeSlime_L.class, "splitTriggered", false);
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
