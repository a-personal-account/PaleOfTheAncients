package paleoftheancients.thevixen.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class RingClosingEffect extends AbstractGameEffect {
    private Texture img;
    private float x;
    private float y;

    public RingClosingEffect(Texture img, Color color, float x, float y) {
        this.img = img;
        this.x = x;
        this.y = y;

        this.scale = 10F * Settings.scale;
        this.color = color;
    }

    public void update() {
        this.scale -= Gdx.graphics.getDeltaTime() * 10 * Settings.scale;
        if(this.scale < 0) {
            this.isDone = true;
        }
    }

    public void render(SpriteBatch sb) {
        final float width = img.getWidth() * this.scale;
        final float height = img.getHeight() * this.scale;

        sb.setBlendFunction(770, 1);
        sb.setColor(color);
        sb.draw(img, this.x - width / 2, this.y - height / 2, width / 2, height / 2, width, height, this.scale, this.scale, this.rotation, 0, 0, img.getWidth(), img.getHeight(), false, false);
        sb.setColor(Color.WHITE.cpy());
        sb.setBlendFunction(770, 771);
    }

    public void dispose() {

    }
}
