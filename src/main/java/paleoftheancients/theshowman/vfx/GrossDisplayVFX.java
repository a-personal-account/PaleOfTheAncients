package paleoftheancients.theshowman.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import paleoftheancients.theshowman.helpers.Numbers;

public class GrossDisplayVFX extends AbstractGameEffect {
    private float drawX;
    private float drawY;
    private float graphicsAnimation;
    private static float PADDING_X;
    private static float Y_OFFSET;

    private Texture[] numbers;
    private float elementWidth;
    private float elementHeight;

    public GrossDisplayVFX(AbstractCreature m, int damage) {
        this.scale = 1.5F * Settings.scale;

        this.numbers = Numbers.getWholeNumber(damage);
        this.elementWidth = (this.numbers[0].getWidth() + PADDING_X) * this.scale;
        this.elementHeight = this.numbers[0].getHeight() * this.scale;
        float totalWidth = numbers.length * elementWidth;
        float cX = m.hb.cX;
        this.drawX = cX - totalWidth / 2.0F;
        this.drawY = m.drawY + m.hb.height + Y_OFFSET - elementHeight / 2.0F;
        this.color = Color.WHITE.cpy();
        this.rotation = 0.0F;
        this.duration = this.startingDuration = 1.5F;
    }

    public void update() {
        this.duration -= Gdx.graphics.getDeltaTime();
        this.graphicsAnimation += Gdx.graphics.getDeltaTime();
        if (this.duration > 1.0F) {
            this.scale = Interpolation.linear.apply(0.0F, 1.5F * Settings.scale, this.graphicsAnimation / 0.5F);
        } else {
            this.color.a = 1.0F;
            if (this.duration > 0.6F && this.duration < 0.8F) {
                this.color.a = 0.0F;
            }

            if (this.duration > 0.2F && this.duration < 0.4F) {
                this.color.a = 0.0F;
            }
        }

        if (this.duration <= 0.0F) {
            this.isDone = true;
        }

    }

    public void render(SpriteBatch sb) {
        sb.setColor(this.color);

        for(int i = 0; i < numbers.length; i++) {
            sb.draw(numbers[i], this.drawX + elementWidth * i, this.drawY, elementWidth / 2, elementHeight / 2, elementWidth, elementHeight, this.scale, this.scale, this.rotation, 0, 0, numbers[i].getWidth(), numbers[i].getHeight(), false, false);
        }

    }

    public void dispose() {
    }

    static {
        PADDING_X = -30.0F * Settings.scale;
        Y_OFFSET = 100.0F * Settings.scale;
    }
}
