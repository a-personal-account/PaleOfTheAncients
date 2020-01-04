package paleoftheancients.slimeboss.monsters;

import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.actions.animations.AnimateShakeAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.unique.CanLoseAction;
import com.megacrit.cardcrawl.actions.unique.CannotLoseAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.exordium.SlimeBoss;
import com.megacrit.cardcrawl.powers.InvinciblePower;
import paleoftheancients.PaleMod;
import paleoftheancients.slimeboss.powers.SlimeSplitPower;
import paleoftheancients.thevixen.actions.ShuffleBossBurnsAction;

public class SlimeBossest extends SlimeBoss implements WeirdSlimeThing {
    public static String ID = PaleMod.makeID("SlimeBossest");
    public static int SPLIT_AMOUNT = 2;
    private boolean suicided;
    private float height;

    public SlimeBossest() {
        super();
        this.setHp(this.maxHealth * 2);
        this.drawX -= 100 * Settings.scale;
        this.suicided = false;
        this.height = this.hb.height;
    }

    @Override
    public void usePreBattleAction() {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new SlimeSplitPower(this, SPLIT_AMOUNT), SPLIT_AMOUNT));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new InvinciblePower(this, this.maxHealth), this.maxHealth));
    }

    @Override
    public void takeTurn() {
        switch(this.nextMove) {
            case 3:
                AbstractDungeon.actionManager.addToBottom(new CannotLoseAction());
                AbstractDungeon.actionManager.addToBottom(new AnimateShakeAction(this, 1.0F, 0.1F));
                SlimeHelper.splitShake(this);
                SlimeHelper.spawnEnemies(this,
                        new SpikeSlimest_L(-385.0F, 20.0F, 0, this.currentHealth),
                        new AcidSlimest_L(120.0F, -8.0F, 0, this.currentHealth));
                AbstractDungeon.actionManager.addToBottom(new CanLoseAction());
                this.suicided = true;
                ReflectionHacks.setPrivate(this, SlimeBoss.class, "firstTurn", true);
                break;

            case 4:
                AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new SFXAction("MONSTER_SLIME_ATTACK"));
                AbstractDungeon.actionManager.addToBottom(new ShuffleBossBurnsAction(1, 3));

                this.setMove((byte)2, Intent.UNKNOWN);
                break;

            default:
                super.takeTurn();
        }
        SlimeHelper.zombieCheck(this);
    }

    @Override
    public void die() {
        this.die(true);
    }
    @Override
    public void die(boolean triggerRelics) {
        if(SlimeHelper.reform(this)) {
            super.die(triggerRelics);
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
