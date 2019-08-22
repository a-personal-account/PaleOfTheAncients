package paleoftheancients.thevixen.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class SparkleEffect extends AbstractGameEffect {
    private float x;
    private float y;
    private TextureAtlas.AtlasRegion img;
    private TextureAtlas.AtlasRegion img2;
    private float delay;

    public SparkleEffect(float x, float y) {
        this(x, y, 0.3F, 0.0F);
    }
    public SparkleEffect(float x, float y, float duration, float delay) {
        this.img = ImageMaster.STRIKE_LINE;
        this.img2 = ImageMaster.STRIKE_LINE_2;
        this.duration = duration;
        this.startingDuration = this.duration;
        this.x = x;
        this.y = y;
        this.rotation = 0;
        this.delay = delay;

        this.color = new Color(1.0F, 1.0F, 1.0F, 0.0F);

        this.renderBehind = MathUtils.randomBoolean(0.3F);
    }

    public void update() {
        if(this.delay > 0) {
            this.delay -= Gdx.graphics.getDeltaTime();
            return;
        }


        this.scale = Settings.scale * this.duration / this.startingDuration;
        this.duration -= Gdx.graphics.getDeltaTime();
        Color var10000;
        if (this.duration / this.startingDuration > 0.5F) {
            this.color.a = 1.0F - this.duration / this.startingDuration;
            var10000 = this.color;
            var10000.a += MathUtils.random(0.0F, 0.2F);
        } else {
            this.color.a = this.duration / this.startingDuration;
            var10000 = this.color;
            var10000.a += MathUtils.random(0.0F, 0.2F);
        }

        if (this.duration < 0.0F) {
            this.isDone = true;
            this.color.a = 0.0F;
        }
    }

    public void render(SpriteBatch sb) {
        sb.setBlendFunction(770, 1);
        sb.setColor(this.color);

        float phaseoffset = MathUtils.random(-2.0F, 2.0F);
        sb.draw(this.img, this.x - (float)this.img.packedWidth / 2.0F, this.y - (float)this.img.packedHeight / 2.0F, (float)this.img.packedWidth / 2.0F, (float)this.img.packedHeight / 2.0F, (float)this.img.packedWidth, (float)this.img.packedHeight, this.scale * MathUtils.random(0.35F, 1.0F), this.scale * 0.4F, this.rotation + phaseoffset);
        sb.draw(this.img, this.x - (float)this.img.packedWidth / 2.0F, this.y - (float)this.img.packedHeight / 2.0F, (float)this.img.packedWidth / 2.0F, (float)this.img.packedHeight / 2.0F, (float)this.img.packedWidth, (float)this.img.packedHeight, this.scale * MathUtils.random(0.35F, 1.0F), this.scale * 0.4F, this.rotation + phaseoffset + 90);
        sb.draw(this.img, this.x - (float)this.img.packedWidth / 2.0F, this.y - (float)this.img.packedHeight / 2.0F, (float)this.img.packedWidth / 2.0F, (float)this.img.packedHeight / 2.0F, (float)this.img.packedWidth, (float)this.img.packedHeight, this.scale * MathUtils.random(0.35F, 1.0F), this.scale * 0.4F, this.rotation + phaseoffset + 180);
        sb.draw(this.img, this.x - (float)this.img.packedWidth / 2.0F, this.y - (float)this.img.packedHeight / 2.0F, (float)this.img.packedWidth / 2.0F, (float)this.img.packedHeight / 2.0F, (float)this.img.packedWidth, (float)this.img.packedHeight, this.scale * MathUtils.random(0.35F, 1.0F), this.scale * 0.4F, this.rotation + phaseoffset + 270);
        sb.setColor(new Color(1.0F, 1.0F, 1.0F, this.color.a));
        sb.draw(this.img2, this.x - (float)this.img2.packedWidth / 2.0F, this.y - (float)this.img2.packedHeight / 2.0F, (float)this.img2.packedWidth / 2.0F, (float)this.img2.packedHeight / 2.0F, (float)this.img2.packedWidth, (float)this.img2.packedHeight, this.scale * 0.75F, this.scale * MathUtils.random(0.6F, 1.2F), this.rotation + phaseoffset);
        sb.draw(this.img2, this.x - (float)this.img2.packedWidth / 2.0F, this.y - (float)this.img2.packedHeight / 2.0F, (float)this.img2.packedWidth / 2.0F, (float)this.img2.packedHeight / 2.0F, (float)this.img2.packedWidth, (float)this.img2.packedHeight, this.scale * 0.75F, this.scale * MathUtils.random(0.6F, 1.2F), this.rotation + phaseoffset);
        sb.draw(this.img2, this.x - (float)this.img2.packedWidth / 2.0F, this.y - (float)this.img2.packedHeight / 2.0F, (float)this.img2.packedWidth / 2.0F, (float)this.img2.packedHeight / 2.0F, (float)this.img2.packedWidth, (float)this.img2.packedHeight, this.scale * 0.75F, this.scale * MathUtils.random(0.6F, 1.2F), this.rotation + phaseoffset + 90);
        sb.draw(this.img2, this.x - (float)this.img2.packedWidth / 2.0F, this.y - (float)this.img2.packedHeight / 2.0F, (float)this.img2.packedWidth / 2.0F, (float)this.img2.packedHeight / 2.0F, (float)this.img2.packedWidth, (float)this.img2.packedHeight, this.scale * 0.75F, this.scale * MathUtils.random(0.6F, 1.2F), this.rotation + phaseoffset + 90);
        sb.draw(this.img2, this.x - (float)this.img2.packedWidth / 2.0F, this.y - (float)this.img2.packedHeight / 2.0F, (float)this.img2.packedWidth / 2.0F, (float)this.img2.packedHeight / 2.0F, (float)this.img2.packedWidth, (float)this.img2.packedHeight, this.scale * 0.75F, this.scale * MathUtils.random(0.6F, 1.2F), this.rotation + phaseoffset + 180);
        sb.draw(this.img2, this.x - (float)this.img2.packedWidth / 2.0F, this.y - (float)this.img2.packedHeight / 2.0F, (float)this.img2.packedWidth / 2.0F, (float)this.img2.packedHeight / 2.0F, (float)this.img2.packedWidth, (float)this.img2.packedHeight, this.scale * 0.75F, this.scale * MathUtils.random(0.6F, 1.2F), this.rotation + phaseoffset + 180);
        sb.draw(this.img2, this.x - (float)this.img2.packedWidth / 2.0F, this.y - (float)this.img2.packedHeight / 2.0F, (float)this.img2.packedWidth / 2.0F, (float)this.img2.packedHeight / 2.0F, (float)this.img2.packedWidth, (float)this.img2.packedHeight, this.scale * 0.75F, this.scale * MathUtils.random(0.6F, 1.2F), this.rotation + phaseoffset + 270);
        sb.draw(this.img2, this.x - (float)this.img2.packedWidth / 2.0F, this.y - (float)this.img2.packedHeight / 2.0F, (float)this.img2.packedWidth / 2.0F, (float)this.img2.packedHeight / 2.0F, (float)this.img2.packedWidth, (float)this.img2.packedHeight, this.scale * 0.75F, this.scale * MathUtils.random(0.6F, 1.2F), this.rotation + phaseoffset + 270);
        sb.setBlendFunction(770, 771);
    }

    public void dispose() {
    }
}
