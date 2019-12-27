package paleoftheancients.thevixen.vfx;

import paleoftheancients.thevixen.vfx.SparkleEffect;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class ShinyEffect extends AbstractGameEffect {
    private static final float X_JITTER;
    private static final float Y_JITTER;

    private static final float SHINYTHINGS = 6;

    float x;
    float y;
    float phaseoffset;
    float offsetY;

    int progress;
    float time;

    public ShinyEffect(float x, float y, boolean offset) {
        progress = 0;
        time = 0.0F;

        phaseoffset = MathUtils.random((float)Math.PI * 2 / SHINYTHINGS);

        if(offset) {
            offsetY = -50F * Settings.scale;
        } else {
            offsetY = 0F;
        }

        this.x = x;
        this.y = y;
    }

    public void update() {
        time -= Gdx.graphics.getDeltaTime();
        if(time > 0.0F) {
            return;
        }

        if(progress == 15) {
            for(int i = 0; i < 18; ++i) {
                AbstractDungeon.effectsQueue.add(new SparkleEffect(x + MathUtils.random(-X_JITTER, X_JITTER), y + offsetY + MathUtils.random(-Y_JITTER, Y_JITTER), 0.7F, MathUtils.random(1F)));
            }
            this.isDone = true;
            return;
        }

        time = 0.04F;
        phaseoffset += (float)Math.PI * 2F / 22F;
        float distance = Settings.scale;
        distance *= 30 + progress * 14;

        for(int i = 0; i < SHINYTHINGS; ++i) {
            float angle = i * (float)Math.PI * 2F / SHINYTHINGS + phaseoffset;
            AbstractDungeon.effectsQueue.add(new SparkleEffect(x + (float)Math.sin(angle) * distance, y + offsetY + (float)Math.cos(angle) * distance));
        }
        progress++;
    }

    public void render(SpriteBatch sb) {
    }

    public void dispose() {
    }

    static {
        X_JITTER = 160.0F * Settings.scale;
        Y_JITTER = 160.0F * Settings.scale;
    }
}
