package paleoftheancients.thevixen.vfx;

import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.FireBurstParticleEffect;
import com.megacrit.cardcrawl.vfx.combat.GhostIgniteEffect;

public class DefiantFlameVFX extends AbstractGameEffect {
    private static final float PHASEINCREASE = (float)Math.toRadians(30);

    private float phase;
    private float distance;
    private AbstractCreature ac;

    private int amnt;
    private boolean isEnding;

    public DefiantFlameVFX(AbstractCreature ac) {
        this.phase = MathUtils.random((float)Math.PI);
        this.ac = ac;
        this.distance = 1.0F;

        this.amnt = 1;
        this.isEnding = false;
    }

    public void increaseAmount() {
        this.amnt++;
    }

    public void update() {
        this.phase = (this.phase + PHASEINCREASE * this.amnt * Gdx.graphics.getDeltaTime()) % (float)(Math.PI * 2);
        for(int j = 0; j < this.amnt; j++) {
            final float tmpphase = this.phase + (float)(Math.PI * 2 * j / this.amnt);
            final float x = (float)Math.sin(tmpphase + Math.PI / 2) * ac.hb.width * 3 / 4 * distance;
            final float y = (float)Math.cos(tmpphase + Math.PI / 2) * ac.hb.width / 3 * distance;
            for(int k = 0; k < 2; k++) {
                FireBurstParticleEffect fbpe = new FireBurstParticleEffect(ac.hb.cX + x, ac.hb.y + y);
                fbpe.renderBehind = y > 0;
                ReflectionHacks.setPrivate(fbpe, AbstractGameEffect.class, "color", Color.RED.cpy());
                AbstractDungeon.effectsQueue.add(fbpe);
            }
        }
        if(this.isEnding) {
            this.distance -= Gdx.graphics.getDeltaTime() * 2F;
            if(this.distance <= 0F) {
                AbstractDungeon.effectsQueue.add(new GhostIgniteEffect(ac.hb.cX, ac.hb.y));
                this.isDone = true;
            }
        }
    }

    public void end() {
        this.isEnding = true;
    }

    public void render(SpriteBatch sb) {

    }

    public void dispose() {

    }
}
