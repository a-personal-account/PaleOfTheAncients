package paleoftheancients.thevixen.vfx;

import paleoftheancients.thevixen.TheVixenMod;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

import java.util.ArrayList;

public class SongEffect extends AbstractGameEffect {
    private static float PARTICLE_INTERVAL = 0.6F;
    private static Texture[] imgs;
    private static float deviation = 0.3F;
    private static float mean = (float)Math.PI * 0 / 2;
    public static float speed = 1F;

    private static float gravity = 296.68F * Settings.scale;
    private static float friction = 0.02F;


    private AbstractCreature source;

    private Color color;

    private float global;
    private float spenttime;

    private Note[] notes;

    protected float centerX;
    protected float centerY;

    public SongEffect(AbstractCreature source) {
        this.source = source;

        if(imgs == null) {
            imgs = new Texture[5];
            imgs[0] = ImageMaster.loadImage(TheVixenMod.getResourcePath("vfx/notes/quarter.png"));
            imgs[1] = ImageMaster.loadImage(TheVixenMod.getResourcePath("vfx/notes/eigth.png"));
            imgs[2] = ImageMaster.loadImage(TheVixenMod.getResourcePath("vfx/notes/linked.png"));
            imgs[3] = ImageMaster.loadImage(TheVixenMod.getResourcePath("vfx/notes/trebblecleff.png"));
            imgs[4] = ImageMaster.loadImage(TheVixenMod.getResourcePath("vfx/notes/basscleff.png"));
        }

        this.scale = 1F;
        this.scale *= Settings.scale;

        color = Color.PURPLE.cpy();
        notes = new Note[12];

        this.global = MathUtils.random();
        this.spenttime = 0;
        for(int j = 0; j < notes.length; j++) {
            notes[j] = new Note();
        }

        this.renderBehind = true;
    }

    public void update() {
        float startY = source.hb.cY + source.hb.height / 4 + Settings.HEIGHT;
        this.centerX = source.hb.cX;
        this.centerY = startY + (source.hb.cY - startY) * (1 - (float) Math.pow((speed - Math.min(speed, this.spenttime)) / speed, 2));
        this.spenttime += Gdx.graphics.getDeltaTime();

        if(this.spenttime < speed * 1.5F) {
            //Spinny speed.
            this.global += Gdx.graphics.getDeltaTime();

            float pi = (float)Math.PI;

            for (int j = 0; j < notes.length; j++) {
                float phase = global + j / (float) notes.length * (float) Math.PI * 2;
                phase += pi;
                phase %= pi * 2;
                phase -= pi;
                notes[j].update(phase, (float) Math.sin(phase));
            }

        } else {
            this.color = Color.RED.cpy();
            boolean finashed = true;
            for (int j = 0; j < notes.length; j++) {
                notes[j].updatetwo();
                finashed &= notes[j].alpha <= 0F;
            }
            if(finashed) {
                this.isDone = true;
            }
        }
    }

    public void render(SpriteBatch sb) {
        for(final Note n : notes) {
            if(this.renderBehind == n.renderBehind) {
                sb.setColor(this.color.cpy());
                n.render(sb);
            }
        }
        sb.setColor(Color.WHITE.cpy());
        this.renderBehind = !this.renderBehind;
    }

    public void dispose() {

    }

    private class Note {
        private Texture img;
        private float rotation;
        private float rotationPhase;
        private float speed;
        private float scale;
        private float alpha;

        private float x;
        private float y;
        private float vX;
        private float vY;

        private float width;
        private float height;

        private ArrayList<NoteParticleEffect> sparklies = new ArrayList<>();
        private float particleInterval;

        public boolean renderBehind;

        public Note() {
            int rand = (int)MathUtils.random(imgs.length * 1.6F);
            if(rand > 2) {
                //Double the weight of the actual note images.
                rand -= 3;
            }
            this.particleInterval = 0F;
            this.rotation = 0;
            this.img = imgs[rand];
            this.rotationPhase = MathUtils.random((float)Math.PI * 2);
            this.speed = MathUtils.random(0.8F, 1.2F);
            this.scale = MathUtils.random(0.8F, 1.2F);

            this.vX = MathUtils.random(-250, 250) * Settings.scale;
            this.vY = MathUtils.random(180, 320) * Settings.scale;

            this.alpha = 1F;

            this.width = img.getWidth() * this.scale;
            this.height = img.getHeight() * this.scale;
        }

        public void update(float phase, float offsetY) {
            this.rotationPhase += Gdx.graphics.getDeltaTime() * speed;
            this.rotationPhase = this.rotationPhase % ((float)Math.PI * 2);

            this.rotation = (float)Math.sin(this.rotationPhase) * 20;

            this.x = centerX + source.hb.width * (float)Math.sin(phase) * 3 / 4;
            this.y = centerY + source.hb.height / 4 * (float)Math.cos(phase) + offsetY;
            this.renderBehind = Math.cos(phase) > 0;

            updateSparklies();
        }

        public void updatetwo() {
            this.alpha -= 0.3F * Gdx.graphics.getDeltaTime();
            if(this.alpha > 0F) {
                this.rotation += this.rotationPhase * Gdx.graphics.getDeltaTime();

                this.x += this.vX * Gdx.graphics.getDeltaTime();
                this.y += this.vY * Gdx.graphics.getDeltaTime();

                this.vX *= 1 - friction;
                this.vY -= gravity * Gdx.graphics.getDeltaTime();
            } else {
                this.alpha = 0F;
            }
            updateSparklies();
        }

        private void updateSparklies() {
            this.particleInterval -= Gdx.graphics.getDeltaTime();
            if(particleInterval <= 0F) {
                this.particleInterval = PARTICLE_INTERVAL;
                sparklies.add(new NoteParticleEffect(scale, this.rotation));
                if(sparklies.get(0).duration <= 0) {
                    sparklies.remove(0);
                }
            }
            for(final NoteParticleEffect npe : sparklies) {
                npe.update();
            }
        }

        public void render(SpriteBatch sb) {
            if(this.alpha > 0F) {
                Color c = color.cpy();
                c.a = this.alpha;

                for(final NoteParticleEffect npe : sparklies) {
                    npe.render(sb, c.cpy());
                }
                sb.setColor(c);

                sb.draw(this.img, this.x - width / 2, this.y - height / 2, width / 2, height / 2, width, height, this.scale, this.scale, rotation, 0, 0, img.getWidth(), img.getHeight(), false, false);
            }
        }


        private class NoteParticleEffect {

            private static final float DURATION = 2.0F;
            private float scale;

            private float alpha;
            private float duration;

            private float rotation;

            private float width;
            private float height;

            public NoteParticleEffect(float scale, float rotation) {
                this.scale = scale * 4 / 5;
                this.rotation = rotation;
                this.duration = DURATION;
                this.alpha = 1.0F;
            }

            public void update() {
                this.scale += Gdx.graphics.getDeltaTime() * Settings.scale * 0.4F;
                if (this.duration > 1.0F) {
                    this.alpha = Interpolation.fade.apply(0.0F, 0.3F, 1.0F - (this.duration - 1.0F));
                } else {
                    this.alpha = Interpolation.fade.apply(0.3F, 0.0F, 1.0F - this.duration);
                }

                this.width = img.getWidth() * this.scale;
                this.height = img.getHeight() * this.scale;

                this.duration -= Gdx.graphics.getDeltaTime();
            }

            public void render(SpriteBatch sb, Color c) {
                sb.setBlendFunction(770, 1);
                sb.setColor(c.mul(1, 1, 1, alpha));
                sb.draw(img, x - this.width / 2, y - this.height / 2, this.width, this.height / 2, this.width, this.height, this.scale, this.scale, this.rotation, 0, 0, img.getWidth(), img.getHeight(), false, false);
                sb.setBlendFunction(770, 771);
            }
        }
    }
}