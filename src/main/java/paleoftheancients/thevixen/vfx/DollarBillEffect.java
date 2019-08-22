package paleoftheancients.thevixen.vfx;

import paleoftheancients.thevixen.TheVixenMod;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class DollarBillEffect extends AbstractGameEffect {
    private float x;
    private float y;
    private float vY;
    private float vX;
    private float scaleY;
    private static Texture[] imgs = null;
    private int frame = 0;
    private float animTimer = 0.05F;
    private static final int W = 32;

    private boolean flipX;
    private boolean flipY;

    public DollarBillEffect(AbstractCreature ac) {
        this.x = MathUtils.random(ac.drawX - 100.0F * Settings.scale, ac.drawX + 100.0F * Settings.scale);
        this.y = (float)Settings.HEIGHT + MathUtils.random(20.0F, 300.0F) * Settings.scale;
        if (imgs == null) {
            imgs = new Texture[8];
            imgs[0] = ImageMaster.loadImage(TheVixenMod.getResourcePath("vfx/bill/front_1.png"));
            imgs[1] = ImageMaster.loadImage(TheVixenMod.getResourcePath("vfx/bill/front_2.png"));
            imgs[2] = ImageMaster.loadImage(TheVixenMod.getResourcePath("vfx/bill/front_3.png"));
            imgs[3] = ImageMaster.loadImage(TheVixenMod.getResourcePath("vfx/bill/front_4.png"));
            imgs[4] = ImageMaster.loadImage(TheVixenMod.getResourcePath("vfx/bill/back_1.png"));
            imgs[5] = ImageMaster.loadImage(TheVixenMod.getResourcePath("vfx/bill/back_2.png"));
            imgs[6] = ImageMaster.loadImage(TheVixenMod.getResourcePath("vfx/bill/back_3.png"));
            imgs[7] = ImageMaster.loadImage(TheVixenMod.getResourcePath("vfx/bill/back_4.png"));
        }

        flipX = MathUtils.randomBoolean();
        flipY = MathUtils.randomBoolean();

        this.frame = MathUtils.random(8);
        this.rotation = MathUtils.random(-10.0F, 10.0F);
        this.scale = MathUtils.random(1.0F, 2.5F);
        this.scaleY = MathUtils.random(1.0F, 1.2F);
        if (this.scale < 1.5F) {
            this.renderBehind = true;
        }

        this.vY = MathUtils.random(200.0F, 300.0F) * this.scale * Settings.scale;
        this.vX = MathUtils.random(-100.0F, 100.0F) * this.scale * Settings.scale;
        this.scale *= Settings.scale;
        if (MathUtils.randomBoolean()) {
            this.rotation += 180.0F;
        }

        this.color = new Color(MathUtils.random(0.7F, 0.9F), 1, MathUtils.random(0.7F, 0.9F), 1.0F);
        this.duration = 4.0F;
    }

    public void update() {
        this.y -= this.vY * Gdx.graphics.getDeltaTime();
        this.x += this.vX * Gdx.graphics.getDeltaTime();
        this.animTimer -= Gdx.graphics.getDeltaTime() / this.scale;
        if (this.animTimer < 0.0F) {
            this.animTimer += 0.05F;
            ++this.frame;
            if (this.frame > 11) {
                this.frame = 0;
            }
        }

        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0.0F) {
            this.isDone = true;
        } else if (this.duration < 1.0F) {
            this.color.a = this.duration;
        }

    }

    public void render(SpriteBatch sb) {
        sb.setColor(this.color);
        switch(this.frame) {
            case 0:
                this.renderImg(sb, imgs[0], flipX, flipY);
                break;
            case 1:
                this.renderImg(sb, imgs[1], flipX, flipY);
                break;
            case 2:
                this.renderImg(sb, imgs[2], flipX, flipY);
                break;
            case 3:
                this.renderImg(sb, imgs[3], flipX, flipY);
                break;
            case 4:
                this.renderImg(sb, imgs[6], !flipX, !flipY);
                break;
            case 5:
                this.renderImg(sb, imgs[5], !flipX, !flipY);
                break;
            case 6:
                this.renderImg(sb, imgs[4], !flipX, !flipY);
                break;
            case 7:
                this.renderImg(sb, imgs[5], !flipX, !flipY);
                break;
            case 8:
                this.renderImg(sb, imgs[6], !flipX, !flipY);
                break;
            case 9:
                this.renderImg(sb, imgs[7], !flipX, !flipY);
                break;
            case 10:
                this.renderImg(sb, imgs[2], flipX, flipY);
                break;
            case 11:
                this.renderImg(sb, imgs[1], flipX, flipY);
        }

    }

    public void dispose() {

    }

    private void renderImg(SpriteBatch sb, Texture img, boolean flipH, boolean flipV) {
        //sb.setBlendFunction(770, 1);
        sb.draw(img, this.x, this.y, 19.5F, 8.5F, 39.0F, 17.0F, this.scale, this.scale * this.scaleY, this.rotation, 0, 0, 39, 17, flipH, flipV);
        //sb.setBlendFunction(770, 771);
    }
}
