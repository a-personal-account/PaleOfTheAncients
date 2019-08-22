package paleoftheancients.thevixen.vfx;

import paleoftheancients.thevixen.TheVixenMod;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.RoomTintEffect;

public class WishEffect extends AbstractGameEffect {
    private static float GRAVITY = 200F;
    private Texture img;
    private Hitbox hb;

    private boolean firstFrame;

    private float x;
    private float y;
    private float vX;
    private float vY;

    private float timer;
    private float alpha;

    private float width;
    private float height;

    public WishEffect(boolean fromRight) {
        this.img = ImageMaster.loadImage(TheVixenMod.getResourcePath("vfx/psycracker/star.png"));
        this.firstFrame = true;

        this.scale = 0.4F * Settings.scale;

        this.vX = 900F * Settings.scale;
        if(fromRight) {
            this.x = Settings.WIDTH + 200 * Settings.scale;
            this.vX *= -1;
        } else {
            this.x = -200F * Settings.scale;
        }
        this.y = Settings.HEIGHT * 3F / 4F;
        this.vY = -80 * Settings.scale;
        this.alpha = 1.5F;

        this.width = this.img.getWidth() * this.scale;
        this.height = this.img.getHeight() * this.scale;
    }

    public void update() {
        if(firstFrame) {
            AbstractDungeon.effectsQueue.add(new RoomTintEffect(Color.BLACK.cpy(), 0.8F));
            this.firstFrame = false;
        }
        this.alpha -= Gdx.graphics.getDeltaTime();
        if(this.alpha <= 0F) {
            this.isDone = true;
        }
        this.rotation += Gdx.graphics.getDeltaTime();


        this.vY -= GRAVITY * Gdx.graphics.getDeltaTime();
        this.x += this.vX * Gdx.graphics.getDeltaTime();
        this.y += this.vY * Gdx.graphics.getDeltaTime();

        this.timer -= Gdx.graphics.getDeltaTime();
        if(timer <= 0F) {
            timer = MathUtils.random(0.03F, 0.06F);
            AbstractDungeon.effectsQueue.add(new SparkleEffect(this.x - this.vX / 10F + MathUtils.random(-100F, 100F) * Settings.scale + this.width / 2, this.y - this.vY / 10F + MathUtils.random(-100F, 100F) * Settings.scale + this.height / 2));
        }
    }

    public void render(SpriteBatch sb) {
        sb.setBlendFunction(770, 1);
        sb.setColor(Color.WHITE.cpy().mul(1F, 1F, 1F, Math.min(1, this.alpha)));
        sb.draw(img, this.x, this.y, this.width / 2, this.height / 2, this.width, this.height, this.scale, this.scale, this.rotation, 0, 0, img.getWidth(), img.getHeight(), false, false);
        sb.setColor(Color.WHITE.cpy());
        sb.setBlendFunction(770, 771);
    }

    public void dispose() {
        this.img.dispose();
    }
}
