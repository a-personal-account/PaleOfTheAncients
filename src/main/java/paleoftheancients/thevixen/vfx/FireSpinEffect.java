package paleoftheancients.thevixen.vfx;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.FireBurstParticleEffect;

public class FireSpinEffect extends AbstractGameEffect {
    private static final int SPINS = 2;
    private static final float PHASEINCREASE = (float)Math.toRadians(15);

    private float phase;
    private AbstractCreature ac;

    public FireSpinEffect(AbstractCreature ac) {
        this.phase = MathUtils.random((float)Math.PI);
        this.ac = ac;
    }

    public void update() {
        final float x = (float)Math.sin(phase + Math.PI / 2) * ac.hb.width * 3 / 4;
        final float y = (float)Math.cos(phase + Math.PI / 2) * ac.hb.width / 3;
        this.phase = (this.phase + PHASEINCREASE) % (float)Math.PI;
        for(int j = 0; j < SPINS; j++) {

            for(int k = 0; k < 2; k++) {
                AbstractDungeon.effectsQueue.add(new FireBurstParticleEffect(ac.hb.cX + x, ac.hb.y + y));

                FireBurstParticleEffect fbpe = new FireBurstParticleEffect(ac.hb.cX - x, ac.hb.y - y);
                fbpe.renderBehind = true;
                AbstractDungeon.effectsQueue.add(fbpe);
            }

        }
    }

    public void end() {
        this.isDone = true;
    }

    public void render(SpriteBatch sb) {

    }

    public void dispose() {

    }
}
