//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package paleoftheancients.thevixen.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class GhostlyFireEffect extends AbstractGameEffect {
    private AtlasRegion img = this.getImg();
    private float x;
    private float y;
    private float vX;
    private float vY;
    private static final float DUR = 1.0F;

    public GhostlyFireEffect(float x, float y, Color color, float scale) {
        this.x = x + MathUtils.random(-2.0F, 2.0F) * Settings.scale - (float)this.img.packedWidth / 2.0F;
        this.y = y + (MathUtils.random(-2.0F, 2.0F) + scale * 1.5F) * Settings.scale - (float)this.img.packedHeight / 2.0F;
        this.vX = MathUtils.random(-10.0F, 10.0F) * Settings.scale;
        this.vY = MathUtils.random(20.0F, 100.0F) * Settings.scale;
        this.duration = 1.0F;
        this.color = color;
        this.color.a = 0.0F;
        this.scale = Settings.scale * MathUtils.random(5 + scale / 4F, 6 +  + scale / 4F);
    }
    public GhostlyFireEffect(float x, float y) {
        this(x, y, Color.CHARTREUSE.cpy(), 1F);
    }

    private AtlasRegion getImg() {
        switch(MathUtils.random(0, 2)) {
            case 0:
                return ImageMaster.TORCH_FIRE_1;
            case 1:
                return ImageMaster.TORCH_FIRE_2;
            default:
                return ImageMaster.TORCH_FIRE_3;
        }
    }

    public void update() {
        this.x += this.vX * Gdx.graphics.getDeltaTime();
        this.y += this.vY * Gdx.graphics.getDeltaTime();
        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0.0F) {
            this.isDone = true;
        }

        if (this.scale > 0.1F) {
            this.scale -= Gdx.graphics.getDeltaTime() / 4.0F;
        }

        this.color.a = this.duration / 2.0F;
    }

    public void render(SpriteBatch sb) {
        sb.setBlendFunction(770, 1);
        sb.setColor(this.color);
        sb.draw(this.img, this.x, this.y, (float)this.img.packedWidth / 2.0F, (float)this.img.packedHeight / 2.0F, (float)this.img.packedWidth, (float)this.img.packedHeight, this.scale * MathUtils.random(0.95F, 1.05F), this.scale * MathUtils.random(0.95F, 1.05F), this.rotation);
        sb.setBlendFunction(770, 771);
    }

    public void dispose() {
    }
}
