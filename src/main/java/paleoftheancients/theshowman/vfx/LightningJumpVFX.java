package paleoftheancients.theshowman.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.ImpactSparkEffect;

public class LightningJumpVFX extends AbstractGameEffect {
    private float x;
    private float y;
    private float startX;
    private float startY;
    private float lengthX;
    private float lengthY;
    private float heightScale;
    private TextureAtlas.AtlasRegion img = null;

    public LightningJumpVFX(float startDrawX, float startDrawY, float x, float y, boolean flip) {
        if (this.img == null) {
            this.img = ImageMaster.vfxAtlas.findRegion("combat/lightning");
        }

        this.startX = startDrawX;
        this.startY = startDrawY;
        this.lengthX = startDrawX - x;
        this.lengthY = startDrawY - y;
        this.heightScale = (float)Math.sqrt((double)(this.lengthX * this.lengthX + this.lengthY * this.lengthY)) / (float)this.img.packedHeight;
        if (flip) {
            this.rotation = (float)Math.toDegrees(Math.atan((double)(this.lengthY / this.lengthX))) - 90.0F;
        } else {
            this.rotation = (float)Math.toDegrees(Math.atan((double)(this.lengthY / this.lengthX))) + 90.0F;
        }

        this.x = x - (float)this.img.packedWidth / 2.0F;
        this.y = y;
        this.color = Color.WHITE.cpy();
        this.duration = 0.5F;
        this.startingDuration = 0.5F;
    }

    public void update() {
        if (this.duration == this.startingDuration) {
            CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.LOW, ScreenShake.ShakeDur.MED, false);
            AbstractDungeon.actionManager.addToTop(new SFXAction("ORB_LIGHTNING_EVOKE"));

            for(int i = 0; i < 15; ++i) {
                AbstractDungeon.topLevelEffectsQueue.add(new ImpactSparkEffect(this.x + MathUtils.random(-20.0F, 20.0F) * Settings.scale + 150.0F * Settings.scale, this.y + MathUtils.random(-20.0F, 20.0F) * Settings.scale));
            }
        }

        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0.0F) {
            this.isDone = true;
        }

        this.color.a = Interpolation.bounceIn.apply(this.duration * 2.0F);
    }

    public void render(SpriteBatch sb) {
        sb.setBlendFunction(770, 1);
        sb.setColor(this.color);
        sb.draw(this.img, this.x, this.y, (float)this.img.packedWidth / 2.0F, 0.0F, (float)this.img.packedWidth, (float)this.img.packedHeight, this.scale, this.heightScale, this.rotation);
        sb.setBlendFunction(770, 771);
    }

    public void dispose() {
    }
}
