package thevixen.vfx;

import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Dark;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.DarkOrbPassiveEffect;

public class ShadowBallEffect extends AbstractGameEffect {
    public static final float flighttime = 0.5F;
    private float spenttime;

    private AbstractOrb orb;

    private AbstractCreature source;
    private Hitbox target;

    private Color color;
    private float rotationVelocity;
    private Hitbox hb;

    private float vX;
    private float vY;
    private float interval;
    private float vfxTimer;

    public ShadowBallEffect(AbstractCreature source, AbstractCreature target) {
        this.source = source;
        this.target = new Hitbox(target.hb.cX - target.hb.width / 4, target.hb.cY - target.hb.height / 4, target.hb.width / 2, target.hb.height / 2);

        color = Color.WHITE.cpy();

        this.rotation = MathUtils.random(-10.0F, 10.0F);
        this.rotationVelocity = MathUtils.random(-30.0F, 30.0F);
        this.scale = 1F;
        this.scale *= Settings.scale;

        this.hb = new Hitbox(source.hb.cX + source.hb.width / 2, source.hb.cY, 20 * this.scale, 20 * this.scale);

        this.vX = (this.target.cX - this.hb.cX) / flighttime;
        this.vY = (this.target.cY - this.hb.cY) / flighttime;

        this.interval = 0;

        this.spenttime = 0F;

        this.orb = new Dark() {
            @Override
            protected void renderText(SpriteBatch sb) {}

            @Override
            public void updateAnimation() {
                float vfxtimer = (float)ReflectionHacks.getPrivate(this, Dark.class, "vfxTimer") - Gdx.graphics.getDeltaTime();
                if (vfxTimer < 0.0F) {
                    AbstractDungeon.effectsQueue.add(new DarkOrbPassiveEffect(this.cX, this.cY));
                    vfxTimer = 0.25F;
                }
                ReflectionHacks.setPrivate(this, Dark.class, "vfxTimer", vfxtimer);
                this.cX = MathHelper.orbLerpSnap(this.cX, this.tX);
                this.cY = MathHelper.orbLerpSnap(this.cY, this.tY);
                if (this.channelAnimTimer != 0.0F) {
                    this.channelAnimTimer -= Gdx.graphics.getDeltaTime();
                    if (this.channelAnimTimer < 0.0F) {
                        this.channelAnimTimer = 0.0F;
                    }
                }

                this.c.a = Interpolation.pow2In.apply(1.0F, 0.01F, this.channelAnimTimer / 0.5F);
                this.scale = Interpolation.swingIn.apply(Settings.scale, 0.01F, this.channelAnimTimer / 0.5F);
            }
        };
        this.orb.setSlot(0, 0);
        this.orb.cX = this.orb.tX;
        this.orb.cY = this.orb.tY;
        this.orb.tX = target.hb.cX;
        this.orb.tY = target.hb.cY;
    }

    public void update() {
        this.orb.update();
        this.orb.updateAnimation();
        if(Math.abs(this.orb.cX - this.orb.tX) < 10F * Settings.scale) {
            this.isDone = true;
        }
    }

    public void render(SpriteBatch sb) {
        sb.setColor(this.color);
        renderImg(sb);
        sb.setColor(Color.WHITE);

        this.orb.render(sb);
    }

    public void dispose() {

    }

    private void renderImg(SpriteBatch sb) {
        //sb.setBlendFunction(770, 1);
        //sb.draw(img, this.hb.x, this.hb.y, img.getWidth() * this.scale / 2, img.getHeight() * this.scale / 2, img.getWidth() * this.scale, img.getHeight() * this.scale, this.scale, this.scale, this.rotation, 0, 0, img.getWidth(), img.getHeight(), false, false);
        //sb.setBlendFunction(770, 771);
    }
}
