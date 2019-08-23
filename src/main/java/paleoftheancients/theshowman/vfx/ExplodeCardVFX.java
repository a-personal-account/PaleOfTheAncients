package paleoftheancients.theshowman.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import paleoftheancients.PaleMod;

public class ExplodeCardVFX extends AbstractGameEffect {
    private static final float GRAVITY;
    private static final float START_VY;
    private static final float START_VY_JITTER;
    private static final float START_VX;
    private static final float START_VX_JITTER;
    private float rotation;
    private float startingRotation;
    private float vX;
    private float vY;
    private float targetX;
    private float targetY;
    private float alpha;
    public static final String IMG = PaleMod.assetPath("images/TheShowman/vfx/card.png");
    private TextureRegion img = null;
    private Texture texture;
    private float x;
    private float y;

    public ExplodeCardVFX(float x, float y, TextureRegion texture) {
        this.texture = new Texture(IMG);
        if (this.img == null) {
            this.img = texture;
        }

        this.x = x;
        this.y = y;
        this.x = x - (float)this.img.getRegionWidth() / 2.0F;
        this.y = y - (float)this.img.getRegionHeight() / 2.0F;
        this.vX = MathUtils.random(START_VX - 50.0F * Settings.scale, START_VX_JITTER);
        this.rotation = this.startingRotation = (float)MathUtils.random(0, 360);
        if (MathUtils.randomBoolean()) {
            this.vX = -this.vX;
        }

        this.vY = MathUtils.random(START_VY, START_VY_JITTER);
        this.scale = 1.2F * Settings.scale;
        this.color = Color.WHITE.cpy();
        this.duration = this.startingDuration = 1.5F;
        this.alpha = 1.0F;
    }

    public void update() {
        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration <= 0.3F) {
            this.alpha *= 0.5F;
            this.color.a = this.alpha;
        }

        if (this.duration < 0.0F) {
            this.isDone = true;
        }

        this.rotation = MathUtils.lerpAngleDeg(this.startingRotation, 180.0F, Gdx.graphics.getDeltaTime() * 5.0F);
        this.x += Gdx.graphics.getDeltaTime() * this.vX;
        this.y += Gdx.graphics.getDeltaTime() * this.vY;
        this.vY -= Gdx.graphics.getDeltaTime() * GRAVITY;
    }

    public void render(SpriteBatch sb) {
        sb.setColor(this.color);
        sb.draw(this.img, this.x, this.y, (float)this.img.getRegionWidth() / 2.0F, (float)this.img.getRegionHeight() / 2.0F, (float)this.img.getRegionWidth(), (float)this.img.getRegionHeight(), this.scale, this.scale, this.rotation);
    }

    public void dispose() {
        texture.dispose();
    }

    static {
        GRAVITY = 2000.0F * Settings.scale;
        START_VY = 800.0F * Settings.scale;
        START_VY_JITTER = 400.0F * Settings.scale;
        START_VX = 1000.0F * Settings.scale;
        START_VX_JITTER = 400.0F * Settings.scale;
    }
}
