package paleoftheancients.thevixen.vfx;

import paleoftheancients.thevixen.TheVixenMod;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class EyeOpeningEffect extends AbstractGameEffect {
    public static float SPEED_APPEAR = 0.5F;
    public static float SPEED_OPEN = 0.25F;
    private static Texture[] imgs;

    private AbstractCreature ac;

    private float width;
    private float height;

    private float rayWidth;
    private float rayHeight;

    private float opened;
    private float alpha;
    private float rayAlpha;

    public EyeOpeningEffect(AbstractCreature ac) {
        if(imgs == null) {
            imgs = new Texture[3];
            imgs[0] = ImageMaster.loadImage(TheVixenMod.getResourcePath("vfx/eye/closed.png"));
            imgs[1] = ImageMaster.loadImage(TheVixenMod.getResourcePath("vfx/eye/iris.png"));
            imgs[2] = ImageMaster.loadImage(TheVixenMod.getResourcePath("vfx/eye/ray.png"));
        }
        this.ac = ac;
        this.scale = ac.hb.width / (float)imgs[0].getWidth();

        this.width = imgs[0].getWidth() * this.scale;
        this.height = imgs[0].getHeight() * this.scale;

        this.rayWidth = imgs[2].getWidth() * this.scale;
        this.rayHeight = imgs[2].getHeight() * this.scale;

        this.opened = -1F;
        this.alpha = 1F;
        this.rayAlpha = 0.5F;
    }

    public void update() {
        if(this.alpha > 0F && this.opened < 1F) {
            this.alpha -= Gdx.graphics.getDeltaTime() / SPEED_APPEAR;
        } else if(this.opened < 1F) {
            this.opened += Gdx.graphics.getDeltaTime() / SPEED_OPEN;
            this.rayAlpha = 0.5F + ((this.opened + 1) / 4);
        } else if(this.rayAlpha > 0.5F) {
            this.rayAlpha -= Gdx.graphics.getDeltaTime();
        } else if(this.alpha < 1F) {
            this.alpha += Gdx.graphics.getDeltaTime() / 2;
        } else {
            this.isDone = true;
        }
    }

    public void render(SpriteBatch sb) {
        final float irisAdjustment = ((this.opened + 1F) / 2F);
        final Color color = Color.PURPLE.cpy();

        sb.setColor(color.sub(0, 0, 0, this.alpha));
        sb.draw(imgs[0], ac.hb.cX - this.width / 2F, ac.hb.cY - this.height / 2F, this.width / 2F, this.height / 2F, this.width, this.height, this.scale, this.scale, this.rotation, 0, 0, imgs[0].getWidth(), imgs[0].getHeight(), false, false);
        sb.draw(imgs[0], ac.hb.cX - this.width / 2F, ac.hb.cY - this.height / 2F, this.width / 2F, this.height / 2F, this.width, this.height, this.scale, this.scale * Math.abs(this.opened), this.rotation, 0, 0, imgs[0].getWidth(), imgs[0].getHeight(), false, this.opened > 0);

        sb.draw(imgs[1], ac.hb.cX - this.width / 2F, ac.hb.cY - this.height / 2F, this.width / 2F, this.height / 2F, this.width, this.height * irisAdjustment, this.scale, this.scale, this.rotation, 0, (int)(imgs[1].getHeight() - imgs[1].getHeight() * irisAdjustment), imgs[1].getWidth(), (int)(imgs[1].getHeight() * irisAdjustment), false, false);

        sb.setBlendFunction(770, 1);
        sb.setColor(color.mul(1F, 1F, 1F, this.rayAlpha));
        sb.draw(imgs[2], ac.hb.cX - this.rayWidth / 2F, ac.hb.cY - this.rayHeight / 2F, this.rayWidth / 2F, this.rayHeight / 2F, this.rayWidth, this.rayHeight, this.scale, this.scale, this.rotation, 0, 0, imgs[2].getWidth(), imgs[2].getHeight(), false, false);
        sb.draw(imgs[2], ac.hb.cX - this.rayWidth / 2F, ac.hb.cY - this.rayHeight / 2F, this.rayWidth / 2F, this.rayHeight / 2F, this.rayWidth, this.rayHeight, this.scale, this.scale * Math.abs(this.opened), this.rotation, 0, 0, imgs[2].getWidth(), imgs[2].getHeight(), false, this.opened > 0);

        sb.setBlendFunction(770, 771);
        sb.setColor(Color.WHITE.cpy());
    }

    public void dispose() {

    }
}
