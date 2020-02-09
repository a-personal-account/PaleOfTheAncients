package paleoftheancients.watcher.stances;

import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.StanceStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.stances.CalmStance;
import com.megacrit.cardcrawl.stances.NeutralStance;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.stance.CalmParticleEffect;
import com.megacrit.cardcrawl.vfx.stance.StanceAuraEffect;

public class CalmEnemyStance extends AbstractEnemyStance {
    public CalmEnemyStance(AbstractMonster owner) {
        super(owner);
        this.ID = CalmStance.STANCE_ID;
        this.name = AbstractEnemyStance.getStanceString(CalmStance.class).NAME;
        updateDescription();
    }

    private static long sfxId = -1L;

    public void updateDescription() {
        this.description =  AbstractEnemyStance.getStanceString(CalmStance.class).DESCRIPTION[0];
    }

    public void updateAnimation() {
        if (!Settings.DISABLE_EFFECTS) {
            this.particleTimer -= Gdx.graphics.getDeltaTime();
            if (this.particleTimer < 0.0F) {
                this.particleTimer = 0.04F;
                CalmParticleEffect cpe = new CalmParticleEffect();
                ReflectionHacks.setPrivate(cpe, CalmParticleEffect.class, "x", owner.hb.cX - MathUtils.random(100.0F, 160.0F) * Settings.scale - 32.0F);
                ReflectionHacks.setPrivate(cpe, CalmParticleEffect.class, "y", owner.hb.cY + MathUtils.random(-50.0F, 220.0F) * Settings.scale - 32.0F);
                ReflectionHacks.setPrivate(cpe, CalmParticleEffect.class, "vX", -(float)ReflectionHacks.getPrivate(cpe, CalmParticleEffect.class, "vX"));
                ReflectionHacks.setPrivate(cpe, CalmParticleEffect.class, "dvx", -(float)ReflectionHacks.getPrivate(cpe, CalmParticleEffect.class, "dvx"));
                AbstractDungeon.effectsQueue.add(cpe);
            }
        }
        this.particleTimer2 -= Gdx.graphics.getDeltaTime();
        if (this.particleTimer2 < 0.0F) {
            this.particleTimer2 = MathUtils.random(0.45F, 0.55F);
            StanceAuraEffect sae = new StanceAuraEffect(CalmStance.STANCE_ID);
            AdjustStanceAuraEffect(sae);
            AbstractDungeon.effectsQueue.add(sae);
        }
    }

    public void onEnterStance() {
        if (sfxId != -1L)
            stopIdleSfx();
        CardCrawlGame.sound.play("STANCE_ENTER_CALM");
        sfxId = CardCrawlGame.sound.playAndLoop("STANCE_LOOP_CALM");
        AbstractDungeon.effectsQueue.add(new BorderFlashEffect(Color.SKY, true));
    }

    public void onExitStance() {
        stopIdleSfx();
    }

    public void stopIdleSfx() {
        if (sfxId != -1L) {
            CardCrawlGame.sound.stop("STANCE_LOOP_CALM", sfxId);
            sfxId = -1L;
        }
    }
}
