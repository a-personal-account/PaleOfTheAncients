package paleoftheancients.theshowman.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import paleoftheancients.theshowman.helpers.Numbers;

public class CrashingLightsVFX extends AbstractGameEffect {
    private static float Y_OFFSET;
    private static float Y_GRAVITY;
    private static float Y_LAUNCH;
    private static float X_LAUNCH;
    private static float X_JITTER;
    private static float Y_JITTER;
    private static float PADDING_X;
    private float drawX;
    private float drawY;
    private float graphicsAnimation;

    private Texture[] numbers;
    private Vector2[] position;
    private Vector2[] trajectory;
    private float elementWidth;
    private float elementHeight;

    private AbstractCreature m;

    public CrashingLightsVFX(AbstractCreature m, int damage) {
        this.scale = 1.5F * Settings.scale;
        this.m = m;

        this.numbers = Numbers.getWholeNumber(damage);
        this.position = new Vector2[this.numbers.length];
        this.trajectory = new Vector2[this.numbers.length];
        this.elementWidth = (this.numbers[0].getWidth() + PADDING_X) * this.scale;
        this.elementHeight = this.numbers[0].getHeight() * this.scale;
        float totalWidth = numbers.length * elementWidth;
        float cX = m.hb.cX;
        float cY = m.hb.cY;
        this.color = Color.WHITE.cpy();
        this.rotation = 0.0F;


        this.drawX = cX - totalWidth / 2.0F;
        this.drawY = cY + Y_OFFSET - elementHeight / 2.0F;
        this.duration = this.startingDuration = 2.5F;
        this.graphicsAnimation = 0.0F;

        float vX, vY;

        for(int i = 0; i < numbers.length; i++) {
            this.position[i] = new Vector2(this.drawX + elementWidth * i, this.drawY);

            vX = MathUtils.random(X_LAUNCH, X_JITTER);
            vY = MathUtils.random(Y_LAUNCH, Y_JITTER);
            if (this.position[i].x + elementWidth / 2.0F < this.m.hb.cX) {
                vX *= -1.0F;
            }

            this.trajectory[i] = new Vector2(vX, vY);
        }
    }

    public void update() {
        this.duration -= Gdx.graphics.getDeltaTime();
        this.graphicsAnimation += Gdx.graphics.getDeltaTime();
        int i;
        if (this.duration > 2.0F) {
            this.rotation = Interpolation.pow4In.apply(0.0F, 135.0F, this.graphicsAnimation / 0.5F);
        }

        if (this.duration < 2.0F && this.duration > 1.5F) {
            this.drawY = Interpolation.pow4In.apply(this.m.hb.cY + Y_OFFSET - elementHeight / 2.0F, this.m.drawY, (this.graphicsAnimation - 0.5F) / 0.5F);
        }

        if (this.duration < 1.5F) {

            for(i = 0; i < numbers.length; i++) {
                position[i].x += trajectory[i].x * Gdx.graphics.getDeltaTime();
                position[i].y += trajectory[i].y * Gdx.graphics.getDeltaTime();
                trajectory[i].y -= Y_GRAVITY * Gdx.graphics.getDeltaTime();
            }
        }

        if (this.duration <= 0.0F) {
            this.isDone = true;
        }

    }

    public void render(SpriteBatch sb) {
        sb.setColor(this.color);

        for(int i = 0; i < numbers.length; i++) {
            sb.draw(numbers[i], position[i].x, position[i].y, elementWidth / 2, elementHeight / 2, elementWidth, elementHeight, this.scale, this.scale, this.rotation, 0, 0, numbers[i].getWidth(), numbers[i].getHeight(), false, false);
        }
    }

    public void dispose() {
    }

    static {
        Y_OFFSET = 200.0F * Settings.scale;
        Y_GRAVITY = 2000.0F * Settings.scale;
        Y_LAUNCH = 800.0F * Settings.scale;
        Y_JITTER = 400.0F * Settings.scale;
        X_LAUNCH = 1000.0F * Settings.scale;
        X_JITTER = 400.0F * Settings.scale;
        PADDING_X = -30.0F * Settings.scale;
    }
}
