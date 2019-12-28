package paleoftheancients.reimu.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class Needle extends AbstractGameEffect {
    private Texture needle;
    private int width, height;
    public float x, y;
    private float vX, vY, targetX;

    public Needle(Texture tex, Hitbox target, float x, float y) {
        this(tex, target, x, y, MathUtils.random(0.3F));
    }
    public Needle(Texture tex, Hitbox target, float x, float y, float delay) {
        this.needle = tex;

        this.width = needle.getWidth();
        this.height = needle.getHeight();

        float offsetX = MathUtils.random(-target.width, target.width) / 6F;
        float offsetY = MathUtils.random(-target.height, target.height) / 6F;

        targetX = target.cX + offsetX;
        this.x = x + offsetX;
        this.y = y + offsetY;

        rotation = (float)Math.atan((target.cX - x) / (target.cY - y));
        if(target.cY < y) {
            rotation += Math.PI;
        }
        vX = (float)Math.sin(rotation) * 2000 * Settings.scale;
        vY = (float)Math.cos(rotation) * 2000 * Settings.scale;

        rotation = 360F - (float)Math.toDegrees(rotation);

        this.duration = delay;
    }

    @Override
    public void update() {
        if(this.duration >= 0) {
            this.duration -= Gdx.graphics.getDeltaTime();
        } else {
            x += vX * Gdx.graphics.getDeltaTime();
            y += vY * Gdx.graphics.getDeltaTime();

            try {
                if (vX > 0) {
                    if (x > targetX) {
                        throw new ArithmeticException();
                    }
                } else if (x < targetX) {
                    throw new ArithmeticException();
                }
            } catch (ArithmeticException ex) {
                this.isDone = true;
            }
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        if(this.duration < 0) {
            sb.setColor(Color.WHITE);
            sb.draw(needle, x - width / 2F, y - height / 2F, width * 0.8F, height / 2F, width, height, scale, scale, rotation, 0, 0, width, height, false, false);
        }
    }

    @Override
    public void dispose() {

    }
}
