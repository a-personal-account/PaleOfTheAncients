package paleoftheancients.thevixen.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

import java.util.ArrayList;

public class PsycrackerFireworks extends AbstractGameEffect {
    private static float VELOCITY = 200 * Settings.scale;
    private static float GRAVITY = 100 * Settings.scale;
    private static float FRICTION = 0.998F;
    private static float DURATION = 3F;

    private Color color;
    private ArrayList<FireworkData> data;
    private float time;
    private float particleTimer;

    public PsycrackerFireworks(Color color, float x, float y) {
        this.color = color;
        this.data = new ArrayList<>();

        float randomPhase = MathUtils.random((float)Math.PI / 4);

        for(int i = 0; i < 8; i++) {
            FireworkData fd = new FireworkData();
            fd.x = x;
            fd.y = y;
            fd.vX = (float)Math.sin(i * Math.PI / 4.0 + randomPhase) * VELOCITY;
            fd.vY = (float)Math.cos(i * Math.PI / 4.0 + randomPhase) * VELOCITY;
            this.data.add(fd);
        }

        this.time = DURATION;
        this.particleTimer = 0F;
    }

    public void update() {
        for(final FireworkData fd : data) {
            fd.update();
            this.particleTimer -= Gdx.graphics.getDeltaTime();
            if(this.particleTimer < 0F) {
                this.particleTimer = 0.1F;
                AbstractDungeon.effectsQueue.add(new FireBurstParticleEffectCopyPaste(color, fd.x, fd.y, -90F));
            }
        }
        this.time -= Gdx.graphics.getDeltaTime();
        if (this.time < 0F) {
            this.isDone = true;
        }
    }

    public void render(SpriteBatch sb) {

    }

    public void dispose() {

    }


    private class FireworkData {
        private float x;
        private float y;
        private float vX;
        private float vY;

        public void update() {
            x += vX * Gdx.graphics.getDeltaTime();
            y += vY * Gdx.graphics.getDeltaTime();

            vX *= FRICTION;
            vY -= GRAVITY * Gdx.graphics.getDeltaTime();
            vY *= FRICTION;
        }
    }
}
