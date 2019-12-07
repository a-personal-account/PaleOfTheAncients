package paleoftheancients.vfx.BlurryLens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class BlurryCharacterSlide extends BlurryCharacterVFX {

    private float drawX, drawY, x, y, phase, sinewobble, origDrawX, origDrawY;

    public BlurryCharacterSlide(AbstractCreature ac) {
        super(ac);

        drawX = this.ac.drawX;
        drawY = this.ac.drawY;
        origDrawX = this.ac.drawX;
        origDrawY = this.ac.drawY;

        x = MathUtils.random(40, 60);
        if(MathUtils.randomBoolean()) {
            x *= -1;
        }
        y = MathUtils.random(-6, 6);

        sinewobble = MathUtils.random(200F) * Settings.scale;
        phase = MathUtils.random((float)Math.PI * 2);
    }

    @Override
    public void update() {
        drawX += x * Gdx.graphics.getDeltaTime();
        drawY += y * Gdx.graphics.getDeltaTime();

        if(x > 0 && drawX > Settings.WIDTH + ac.hb.width / 2) {
            drawX -= Settings.WIDTH + ac.hb.width;
        } else if(x < 0 && drawX < -ac.hb.width / 2) {
            drawX += Settings.WIDTH + ac.hb.width;
        }

        if(y > 0 && drawY > Settings.HEIGHT + ac.hb.height / 2) {
            drawY -= Settings.HEIGHT + ac.hb.height;
        } else if(y < 0 && drawY < -ac.hb.height / 2) {
            drawY += Settings.HEIGHT + ac.hb.height;
        }

        phase += Gdx.graphics.getDeltaTime();
        ac.drawX = drawX + sinewobble * (float)Math.sin(phase);
        ac.drawY = drawY + sinewobble * (float)Math.cos(phase);
    }

    @Override
    public void dispose() {
        ac.drawX = origDrawX;
        ac.drawY = origDrawY;
    }
}
