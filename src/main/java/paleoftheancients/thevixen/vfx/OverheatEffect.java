package paleoftheancients.thevixen.vfx;

import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.FireBurstParticleEffect;

public class OverheatEffect extends AbstractGameEffect {
    private static final float GRAVITY = 440F;
    private static final float FRICTION = 0.17F;
    private static final float DURATION = 5F;

    private AbstractCreature ac;

    private Flame[] flames;
    private float timeElapsed;

    public OverheatEffect(AbstractCreature ac) {
        this.ac = ac;
        this.flames = new Flame[MathUtils.random(5, 8)];

        for(int i = 0; i < this.flames.length; i++) {
            this.flames[i] = new Flame(MathUtils.random(0.3F) + i * Math.PI * 2 / this.flames.length);
        }

        this.timeElapsed = DURATION;
    }

    public void update() {
        for(final Flame f : this.flames) {
            f.update();
        }
        this.timeElapsed -= Gdx.graphics.getDeltaTime();
        if(this.timeElapsed <= 0F) {
            this.isDone = true;
        }
    }

    public void render(SpriteBatch sb) {

    }

    public void dispose() {

    }

    private static float velocity = 250;
    class Flame {
        private float x, y, vX, vY;

        private boolean renderBehind;
        private float bounceplane;
        public Flame(double angle) {
            this.vX = (float)Math.sin(angle) * Settings.scale * MathUtils.random(0.85F, 1F) * velocity;
            this.vY = (float)Math.cos(angle) * Settings.scale * MathUtils.random(0.85F, 1F) * velocity * 0.5F;
            this.x = ac.hb.cX;
            this.y = ac.hb.cY;

            this.bounceplane = (float)Math.cos(angle) * ac.hb.width / 3 + ac.hb.y;

            this.renderBehind = Math.cos(angle) > 0;
        }

        public void update() {
            if(this.y < this.bounceplane) {
                this.vX *= 1 - FRICTION;
                this.vY *= -1 + FRICTION;
            }
            this.x += this.vX * Gdx.graphics.getDeltaTime();
            this.y += this.vY * Gdx.graphics.getDeltaTime();
            this.vY -= GRAVITY * Gdx.graphics.getDeltaTime();

            AbstractGameEffect gie = new FireBurstParticleEffect(x, y);
            gie.renderBehind = this.renderBehind;
            ReflectionHacks.setPrivate(gie, AbstractGameEffect.class, "color", Color.ORANGE.cpy());
            AbstractDungeon.effectsQueue.add(gie);
        }
    }
}
