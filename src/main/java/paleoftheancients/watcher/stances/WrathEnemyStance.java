package paleoftheancients.watcher.stances;

import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.stances.WrathStance;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.stance.StanceAuraEffect;
import com.megacrit.cardcrawl.vfx.stance.StanceChangeParticleGenerator;
import com.megacrit.cardcrawl.vfx.stance.WrathParticleEffect;

public class WrathEnemyStance extends AbstractEnemyStance {
    public WrathEnemyStance(AbstractMonster owner) {
        super(owner);
        this.ID = WrathStance.STANCE_ID;
        this.name = AbstractEnemyStance.getStanceString(WrathStance.class).NAME;
        updateDescription();
    }

    private static long sfxId = -1L;

    public void updateDescription() {
        this.description =  AbstractEnemyStance.getStanceString(WrathStance.class).DESCRIPTION[0];
    }

    public void updateAnimation() {
        if (!Settings.DISABLE_EFFECTS) {
            this.particleTimer -= Gdx.graphics.getDeltaTime();
            if (this.particleTimer < 0.0F) {
                this.particleTimer = 0.05F;
                WrathParticleEffect cpe = new WrathParticleEffect();
                ReflectionHacks.setPrivate(cpe, WrathParticleEffect.class, "x", (float)ReflectionHacks.getPrivate(cpe, WrathParticleEffect.class, "x") - AbstractDungeon.player.hb.cX + owner.hb.cX);
                ReflectionHacks.setPrivate(cpe, WrathParticleEffect.class, "y", (float)ReflectionHacks.getPrivate(cpe, WrathParticleEffect.class, "y") - AbstractDungeon.player.hb.cY + owner.hb.cY);
                AbstractDungeon.effectsQueue.add(cpe);
            }
        }
        this.particleTimer2 -= Gdx.graphics.getDeltaTime();
        if (this.particleTimer2 < 0.0F) {
            this.particleTimer2 = MathUtils.random(0.3F, 0.4F);
            StanceAuraEffect sae = new StanceAuraEffect(WrathStance.STANCE_ID);
            AdjustStanceAuraEffect(sae);
            AbstractDungeon.effectsQueue.add(sae);
        }
    }

    public void onEnterStance() {
        if (sfxId != -1L)
            stopIdleSfx();
        CardCrawlGame.sound.play("STANCE_ENTER_WRATH");
        sfxId = CardCrawlGame.sound.playAndLoop("STANCE_LOOP_WRATH");
        AbstractDungeon.effectsQueue.add(new BorderFlashEffect(Color.SCARLET, true));
        AbstractDungeon.effectsQueue.add(new StanceChangeParticleGenerator(owner.hb.cX, owner.hb.cY, WrathStance.STANCE_ID));
    }

    public void onExitStance() {
        stopIdleSfx();
    }

    public void stopIdleSfx() {
        if (sfxId != -1L) {
            CardCrawlGame.sound.stop("STANCE_LOOP_WRATH", sfxId);
            sfxId = -1L;
        }
    }
}
