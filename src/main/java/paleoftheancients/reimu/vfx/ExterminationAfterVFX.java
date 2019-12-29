package paleoftheancients.reimu.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import paleoftheancients.PaleMod;
import paleoftheancients.helpers.AssetLoader;

import java.util.ArrayList;

public class ExterminationAfterVFX extends AbstractGameEffect {
    private Texture tex;
    private int width, height;

    ArrayList<Particle> particles;

    public ExterminationAfterVFX(float x, float y, float rotation) {
        tex = AssetLoader.loadImage(PaleMod.assetPath(FantasySealVFX.path));
        width = tex.getWidth();
        height = tex.getHeight();

        color = new Color(1F, 0.4F, 0.4F, 1F);

        this.rotation = (float)Math.toRadians(rotation);
        particles = new ArrayList<>();

        float end = (float)Math.PI * 3F / 4F;
        float step = end / 49F;
        for(float i = -end; i < end; i += step) {
            float factor = (float)Math.pow(Math.abs(i), 2) + 3F;
            float rotX = (float)Math.sin(this.rotation + i) * factor;
            float rotY = (float)Math.cos(this.rotation + i) * factor;
            float angle = this.rotation + i;
            particles.add(new Particle(x + rotX, y + rotY, rotX, rotY, angle));
            particles.add(new Particle(x + rotX, y + rotY, rotX * 1.5F, rotY * 1.5F, angle));
        }

        this.scale = 0.03F * Settings.scale;
    }

    @Override
    public void update() {
        for(final Particle p : particles) {
            p.x += p.vX * Gdx.graphics.getDeltaTime() * 30F;
            p.y += p.vY * Gdx.graphics.getDeltaTime() * 12F;

            p.angle = MathUtils.lerp(p.angle, rotation, Gdx.graphics.getDeltaTime() / 4);
            p.vX = (float)Math.sin(p.angle) * p.speed;
            p.vY = (float)Math.cos(p.angle) * p.speed;
        }
        this.color.a -= Gdx.graphics.getDeltaTime() / 2;
        if(this.color.a <= 0F) {
            this.isDone = true;
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setColor(color);
        sb.setBlendFunction(770, 1);
        for(final Particle p : particles) {
            sb.draw(tex, p.x - width / 2F, p.y - height / 2F, width / 2F, height / 2F, width, height, this.scale, this.scale, p.rotation, 0, 0, width, height, false, false);
        }
        sb.setBlendFunction(770, 771);
        sb.setColor(Color.WHITE);
    }

    @Override
    public void dispose() {

    }

    class Particle {
        float x, y, vX, vY, rotation, angle, speed;

        public Particle(float x, float y, float vX, float vY, float angle) {
            this.x = x;
            this.y = y;
            this.vX = vX;
            this.vY = vY;

            this.angle = angle;

            speed = (float)Math.sqrt(Math.pow(vX, 2) + Math.pow(vY, 2));

            rotation = MathUtils.random(360F);
        }
    }
}
