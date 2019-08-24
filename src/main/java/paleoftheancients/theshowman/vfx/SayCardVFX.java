package paleoftheancients.theshowman.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import paleoftheancients.theshowman.helpers.Cards;

public class SayCardVFX extends AbstractGameEffect {
    private TextureRegion img;
    private AbstractCreature m;
    private float a;
    private float x;
    private float y;
    private float scaleTimer;
    private float scale_x;
    private float scale_y;
    private static float ADJUST_X;
    private static float ADJUST_Y;

    public SayCardVFX(AbstractCreature m, int specifiedCard) {
        this.img = null;
        if (this.img == null) {
            this.img = new TextureRegion(Cards.getCard(specifiedCard));
        }

        this.m = m;
        this.duration = this.startingDuration = 2.0F;
        this.a = 1.0F;
        this.x = m.dialogX + ADJUST_X;
        this.y = m.dialogY + ADJUST_Y;
        this.scaleTimer = 0.3F;
    }

    public void update() {
        this.updateScale();
        if (this.duration <= 0.5F) {
            this.a -= Gdx.graphics.getDeltaTime() * 2.0F;
            if (this.a < 0.0F) {
                this.a = 0.0F;
            }
        }

        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration <= 0.0F) {
            this.isDone = true;
        }

    }

    private void updateScale() {
        this.scaleTimer -= Gdx.graphics.getDeltaTime();
        if (this.scaleTimer < 0.0F) {
            this.scaleTimer = 0.0F;
        }

        this.scale_x = Interpolation.circleIn.apply(Settings.scale, Settings.scale * 0.5F, this.scaleTimer / 0.3F);
        this.scale_y = Interpolation.swingIn.apply(Settings.scale, Settings.scale * 0.8F, this.scaleTimer / 0.3F);
    }

    public void render(SpriteBatch sb) {
        Color whiteMaybe = Color.WHITE.cpy();
        whiteMaybe.a = this.a;
        sb.setColor(whiteMaybe);
        sb.draw(this.img, this.x - (float)this.img.getRegionWidth() / (2.0F * Settings.scale) * Settings.scale, this.y - (float)this.img.getRegionHeight() / (2.0F * Settings.scale) * Settings.scale, (float)this.img.getRegionWidth() / 2.0F, (float)this.img.getRegionHeight() / 2.0F, (float)this.img.getRegionWidth(), (float)this.img.getRegionHeight(), 1.5F * this.scale_x, 1.5F * this.scale_y, 0.0F);
    }

    public void dispose() {
    }

    static {
        ADJUST_X = 170.0F * Settings.scale;
        ADJUST_Y = 116.0F * Settings.scale;
    }
}
