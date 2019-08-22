package paleoftheancients.thevixen.vfx;

import paleoftheancients.thevixen.TheVixenMod;
import paleoftheancients.thevixen.helpers.RainbowColor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

import java.util.ArrayList;

public class PsycrackerOrb extends AbstractGameEffect {
    private static Texture[] imgs;
    private static float ORBSCALE = 0.3F * Settings.scale;

    private float rotation;
    private float phase;
    private float distance;

    private float radX;
    private float radY;

    private float x;
    private float y;

    private int middle;

    private float targetX;
    private float targetY;

    private int step;
    private RingClosingEffect rce;
    private float secondaryScale;
    private ArrayList<AfterImage> afterimages;
    private float clearAfterImages;

    public static Texture getCircleTexture() {
        if(imgs == null) {
            imgs = new Texture[6];
            imgs[0] = ImageMaster.loadImage(TheVixenMod.getResourcePath("vfx/psycracker/circle.png"));
            imgs[1] = ImageMaster.loadImage(TheVixenMod.getResourcePath("vfx/psycracker/orb.png"));
            imgs[2] = ImageMaster.loadImage(TheVixenMod.getResourcePath("vfx/psycracker/note.png"));
            imgs[3] = ImageMaster.loadImage(TheVixenMod.getResourcePath("vfx/psycracker/doublenote.png"));
            imgs[4] = ImageMaster.loadImage(TheVixenMod.getResourcePath("vfx/psycracker/star.png"));
            imgs[5] = ImageMaster.loadImage(TheVixenMod.getResourcePath("vfx/psycracker/heart.png"));
        }
        return imgs[0];
    }
    public PsycrackerOrb(AbstractCreature source, int index) {
        getCircleTexture();
        this.rotation = MathUtils.random(-15, 15);
        this.scale = 0F;

        this.middle = MathUtils.random(2, 5);


        this.distance = source.hb.width * 1.5F;
        this.radX = source.hb.cX;
        this.radY = source.hb.y + source.hb.height;
        switch(index) {
            case 0:
                this.phase = (float)Math.PI / 2F;
                break;

            case 1:
                this.phase = (float)Math.PI * 3F / 2F;
                break;

            default:
                this.distance = MathUtils.random(source.hb.width) + source.hb.width / 2;
                this.radY += MathUtils.random(150 * Settings.scale);
                this.phase = MathUtils.random((float)Math.PI * 2);
                break;
        }
        this.targetX = (float)Math.sin(this.phase) * this.distance + this.radX;
        this.targetY = (float)Math.cos(this.phase) * this.distance / 3 + this.radY;

        this.x = source.hb.cX;
        this.y = source.hb.cY;

        this.step = 0;

        switch(MathUtils.random(5)) {
            case 0:
                this.color = new RainbowColor(1F, 0F, 0F);
                break;
            case 1:
                this.color = new RainbowColor(1F, 1F, 0F);
                break;
            case 2:
                this.color = new RainbowColor(0F, 1F, 0F);
                break;
            case 3:
                this.color = new RainbowColor(0F, 1F, 1F);
                break;
            case 4:
                this.color = new RainbowColor(0F, 0F, 1F);
                break;
            case 5:
                this.color = new RainbowColor(1F, 0F, 1F);
                break;
        }

        this.secondaryScale = 1.0F;
        this.afterimages = new ArrayList<>();
        this.clearAfterImages = 0F;
    }

    public void update() {
        ((RainbowColor)this.color).update();
        switch(this.step) {
            case 0: {
                if (this.scale < ORBSCALE) {
                    this.scale += Gdx.graphics.getDeltaTime();
                }
                Vector2 f = new Vector2(this.targetX - this.x, this.targetY - this.y);
                final float velocityFactor = Gdx.graphics.getDeltaTime() * 700F * Settings.scale;
                f = f.nor();
                this.x += f.x * velocityFactor;
                this.y += f.y * velocityFactor;
                if (this.y > this.targetY) {
                    CardCrawlGame.sound.play("RELIC_DROP_MAGICAL");
                    this.step++;
                }
                break;
            }

            case 1:
                if(this.scale < ORBSCALE * 1.5F) {
                    this.scale += Gdx.graphics.getDeltaTime();
                } else {
                    this.step++;
                }
                break;

            case 2:
                if(this.scale > ORBSCALE) {
                    this.scale -= Gdx.graphics.getDeltaTime();
                } else {
                    this.step++;
                }
                break;

            //This is the idle step
            case 3:
                this.phase += Gdx.graphics.getDeltaTime();
                this.x = (float)Math.sin(this.phase) * this.distance + this.radX;
                this.y = (float)Math.cos(this.phase) * this.distance / 3 + this.radY;
                break;

            case 4: {
                if(this.secondaryScale < 1.3F) {
                    this.secondaryScale += Gdx.graphics.getDeltaTime();
                }
                float factor = (this.secondaryScale - 1.0F) / 0.3F;
                final float velocityFactor = Gdx.graphics.getDeltaTime() * 2000F * Settings.scale;

                this.afterimages.add(new AfterImage(this.color, this.x, this.y));

                Vector2 f = new Vector2(this.targetX - this.x, this.targetY - this.y);
                f = f.nor();
                this.x += f.x * velocityFactor * factor;
                this.y += f.y * velocityFactor * factor;
                if ((f.x > 0 && this.x > this.targetX) || (f.x < 0 && this.x < this.targetX)) {
                    this.step++;
                    this.rce = new RingClosingEffect(imgs[0], this.color, this.x, this.y);

                    CardCrawlGame.sound.play("HEAL_" + MathUtils.random(1, 3));
                    AbstractDungeon.effectsQueue.add(this.rce);
                }
                break;
            }

            case 5:
                if(rce.isDone) {
                    this.isDone = true;
                    CardCrawlGame.sound.play("ATTACK_FIRE");
                    AbstractDungeon.effectsQueue.add(new PsycrackerFireworks(this.color, this.x, this.y));
                }
                break;
        }

        if(!afterimages.isEmpty()) {
            if(this.clearAfterImages > 0.3F) {
                afterimages.remove(0);
            } else {
                this.clearAfterImages += Gdx.graphics.getDeltaTime();
            }
        }
    }

    public void shoot(AbstractCreature ac) {
        this.step = 4;
        this.targetX = ac.hb.cX + MathUtils.random(-ac.hb.width, ac.hb.width) / 4F;
        this.targetY = ac.hb.cY + MathUtils.random(-ac.hb.width, ac.hb.width) / 4F;
    }
    public boolean idle() {
        return this.step == 3;
    }

    public void render(SpriteBatch sb) {
        float scale = this.scale;
        float width;
        float height;

        sb.setBlendFunction(770, 1);
        for(int i = 0; i < afterimages.size(); i++) {
            afterimages.get(i).render(sb, (float)i / (float)afterimages.size());
        }
        sb.setBlendFunction(770, 771);


        sb.setColor(Color.WHITE.cpy());
        width = scale * imgs[this.middle].getWidth();
        height = scale * imgs[this.middle].getHeight();
        sb.draw(imgs[this.middle], this.x - width / 2, this.y - height / 2, width / 2, height / 2, width, height, this.scale, this.scale, this.rotation, 0, 0, imgs[this.middle].getWidth(), imgs[this.middle].getHeight(), false, false);


        if (this.step >= 4) {
            sb.setColor(this.color.cpy().mul(1F, 1F, 1F, (this.secondaryScale - 1F) / 0.3F));

            width = scale * imgs[1].getWidth();
            height = scale * imgs[1].getHeight();
            sb.draw(imgs[1], this.x - width / 2, this.y - height / 2, width / 2, height / 2, width, height, this.scale, this.scale, this.rotation, 0, 0, imgs[1].getWidth(), imgs[1].getHeight(), false, false);

            scale *= this.secondaryScale;
        }
        sb.setColor(this.color.cpy());
        width = scale * imgs[0].getWidth();
        height = scale * imgs[0].getHeight();
        sb.draw(imgs[0], this.x - width / 2, this.y - height / 2, width / 2, height / 2, width, height, this.scale, this.scale, this.rotation, 0, 0, imgs[0].getWidth(), imgs[0].getHeight(), false, false);



        sb.setColor(Color.WHITE.cpy());
    }

    public void dispose() {

    }


    private class AfterImage {
        private float x;
        private float y;
        private Color color;

        public AfterImage(Color color, float x, float y) {
            this.color = color.cpy();
            this.x = x;
            this.y = y;
        }

        public void render(SpriteBatch sb, float alpha) {
            sb.setBlendFunction(770, 1);
            this.color.a = alpha;
            sb.setColor(this.color);
            float width = scale * imgs[1].getWidth();
            float height = scale * imgs[1].getHeight();
            sb.draw(imgs[1], this.x - width / 2, this.y - height / 2, width / 2, height / 2, width, height, scale, scale, rotation, 0, 0, imgs[1].getWidth(), imgs[1].getHeight(), false, false);
            sb.setBlendFunction(770, 771);
        }
    }
}
