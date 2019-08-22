package paleoftheancients.thevixen.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class RefreshEffect extends AbstractGameEffect {
    private static final float PHASEINCREASE = (float)Math.toRadians(120);
    private static final float HEIGHTINCREASE = 150 * Settings.scale;

    private float phase;
    private AbstractCreature ac;
    private int sparkles;
    private float centerY;

    float[] delay;

    public RefreshEffect(AbstractCreature ac, int sparkles) {
        this.phase = MathUtils.random((float)Math.PI);
        this.ac = ac;
        this.sparkles = sparkles;
        this.delay = new float[sparkles];
        this.centerY = ac.hb.y;
    }

    public void update() {
        for(int j = 0; j < sparkles; j++) {
            this.delay[j] -= Gdx.graphics.getDeltaTime();
            if(this.delay[j] <= 0) {
                final float x = (float) Math.sin(phase + Math.PI * 2 * j / sparkles) * ac.hb.width * 2 / 4;
                final float y = (float) Math.cos(phase + Math.PI * 2 * j / sparkles) * ac.hb.width / 4;
                SparkleEffect fbpe = new SparkleEffect(ac.hb.cX + x, this.centerY + y);
                fbpe.renderBehind = y > 0;
                AbstractDungeon.effectsQueue.add(fbpe);
                this.delay[j] = MathUtils.random(0.02F, 0.05F);
            }
        }
        this.centerY += HEIGHTINCREASE * Gdx.graphics.getDeltaTime();
        this.phase = (this.phase - PHASEINCREASE * Gdx.graphics.getDeltaTime()) % ((float)Math.PI * 2);

        if(this.centerY - ac.hb.y > HEIGHTINCREASE * 2) {
            this.isDone = true;
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
