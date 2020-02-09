package paleoftheancients.watcher.stances;

import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.stances.DivinityStance;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.stance.DivinityParticleEffect;
import com.megacrit.cardcrawl.vfx.stance.StanceAuraEffect;
import com.megacrit.cardcrawl.vfx.stance.StanceChangeParticleGenerator;

public class DivinityEnemyStance extends AbstractEnemyStance {
    public DivinityEnemyStance(AbstractMonster owner) {
        super(owner);
        this.ID = DivinityStance.STANCE_ID;
        this.name = AbstractEnemyStance.getStanceString(DivinityStance.class).NAME;
        updateDescription();
    }

    private static long sfxId = -1L;

    public void updateDescription() {
        this.description =  AbstractEnemyStance.getStanceString(DivinityStance.class).DESCRIPTION[0];
    }

    public void updateAnimation() {
        if (!Settings.DISABLE_EFFECTS) {
            this.particleTimer -= Gdx.graphics.getDeltaTime();
            if (this.particleTimer < 0.0F) {
                this.particleTimer = 0.2F;
                DivinityParticleEffect cpe = new DivinityParticleEffect();

                ReflectionHacks.setPrivate(cpe, DivinityParticleEffect.class, "x", owner.hb.cX + MathUtils.random(-owner.hb.width / 2.0F - 50.0F * Settings.scale, owner.hb.width / 2.0F + 50.0F * Settings.scale));
                ReflectionHacks.setPrivate(cpe, DivinityParticleEffect.class, "y", owner.hb.cY + MathUtils.random(-owner.hb.height / 2.0F + 10.0F * Settings.scale, owner.hb.height / 2.0F - 20.0F * Settings.scale));
                AbstractDungeon.effectsQueue.add(cpe);
            }
        }
        this.particleTimer2 -= Gdx.graphics.getDeltaTime();
        if (this.particleTimer2 < 0.0F) {
            this.particleTimer2 = MathUtils.random(0.3F, 0.4F);
            StanceAuraEffect sae = new StanceAuraEffect(DivinityStance.STANCE_ID);
            AdjustStanceAuraEffect(sae);
            AbstractDungeon.effectsQueue.add(sae);
        }
    }

    public void onEnterStance() {
        if (sfxId != -1L)
            stopIdleSfx();
        CardCrawlGame.sound.play("STANCE_ENTER_DIVINITY");
        sfxId = CardCrawlGame.sound.playAndLoop("STANCE_LOOP_DIVINITY");
        AbstractDungeon.effectsQueue.add(new BorderFlashEffect(Color.PINK, true));
        AbstractDungeon.effectsQueue.add(new StanceChangeParticleGenerator(owner.hb.cX, owner.hb.cY, DivinityStance.STANCE_ID));
    }

    public void onExitStance() {
        stopIdleSfx();
    }

    public void stopIdleSfx() {
        if (sfxId != -1L) {
            CardCrawlGame.sound.stop("STANCE_LOOP_DIVINITY", sfxId);
            sfxId = -1L;
        }
    }
}
