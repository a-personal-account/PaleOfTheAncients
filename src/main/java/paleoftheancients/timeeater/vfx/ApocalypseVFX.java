package paleoftheancients.timeeater.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import paleoftheancients.PaleMod;

import java.util.ArrayList;
import java.util.List;

public class ApocalypseVFX extends AbstractSignalEffect {
    private static final float speed = 2000F * Settings.scale;
    private static final float lerpspeed = 3F;

    private Texture spark = null;

    private AbstractCreature target, source;
    private AbstractGameAction followUp;
    private ApocalypseOrb[] orbs;
    private List<BurstParticle> particles;
    private boolean firstImpact = true;

    public ApocalypseVFX(AbstractCreature target, AbstractCreature source, int amount, AbstractGameAction damageActions) {
        spark = ImageMaster.loadImage(PaleMod.assetPath("images/vfx/spark.png"));

        this.target = target;
        this.source = source;
        this.followUp = damageActions;

        orbs = new ApocalypseOrb[amount];
        for(int i = 0; i < orbs.length; i++) {
            Vector2 velocity = new Vector2(target.hb.cX - source.hb.cX, target.hb.cY - source.hb.cY + MathUtils.random(-2000, 2000) * Settings.scale * (i + 2F) / 3F);
            velocity.nor();
            velocity.x *= speed;
            velocity.y *= speed;
            orbs[i] = new ApocalypseOrb(new Vector2(source.hb.cX, source.hb.cY), velocity, (orbs.length - i) * 0.25F);
        }
        this.scale = Settings.scale;
        this.particles = new ArrayList<>();
    }

    @Override
    public void update() {
        this.isDone = true;
        for(ApocalypseOrb orb : orbs) {
            if(orb.update(target)) {
                if(firstImpact) {
                    AbstractDungeon.actionManager.addToTop(followUp);
                    firstImpact = false;
                    end();
                }
                CardCrawlGame.sound.play("ORB_LIGHTNING_EVOKE", 0.3F);
                CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.MED, ScreenShake.ShakeDur.SHORT, false);
                for(int i = MathUtils.random(200, 250); i > 0; i--) {
                    particles.add(new BurstParticle(orb.leader.position));
                }
            }
            this.isDone &= orb.isDone;
        }
        for(BurstParticle par : particles) {
            this.isDone = par.update() && this.isDone;
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        for(ApocalypseOrb orb : orbs) {
            orb.render(sb, spark, scale);
        }
        for(BurstParticle par : particles) {
            par.render(sb, spark, scale / 5F);
        }
    }

    @Override
    public void dispose() {
        spark.dispose();
    }

    private static class ApocalypseOrb {
        private List<ActualOrb> trail;
        public ActualOrb leader;
        private Vector2 velocity;
        private boolean homing = true;
        private float delay;
        private boolean active = false;
        public boolean isDone = false;

        public ApocalypseOrb(Vector2 position, Vector2 velocity, float delay) {
            leader = new ActualOrb(position);
            this.velocity = velocity;
            this.delay = delay;
            this.trail = new ArrayList<>();
        }

        public boolean update(AbstractCreature target) {
            boolean returnVal = false;
            if(delay > 0F) {
                delay -= Gdx.graphics.getRawDeltaTime();
                if(delay <= 0F) {
                    active = true;
                    CardCrawlGame.sound.play("ATTACK_FIRE");
                }
            } else {
                ActualOrb tmp;
                for (int i = trail.size() - 1; i >= 0; i--) {
                    tmp = trail.get(i);
                    tmp.alpha -= Gdx.graphics.getRawDeltaTime();
                    if (tmp.alpha < 0F) {
                        trail.remove(i);
                    }
                }
                if(active) {
                    if (leader.position.x < -Settings.WIDTH / 2F || leader.position.x > Settings.WIDTH * 1.5F || leader.position.y > Settings.HEIGHT * 1.5 || leader.position.y < -Settings.HEIGHT / 2F) {
                        active = false;
                    } else {
                        if (homing) {
                            if (leader.position.x > target.hb.x + target.hb.width / 4 && leader.position.x < target.hb.x + target.hb.width * 3 / 4
                                    && leader.position.y > target.hb.y && leader.position.y < target.hb.y + target.hb.height) {
                                homing = false;
                                returnVal = true;
                            } else {
                                Vector2 ideal = new Vector2(target.hb.cX - leader.position.x, target.hb.cY - leader.position.y);
                                ideal.nor();
                                ideal.x *= speed;
                                ideal.y *= speed;
                                velocity.x = MathUtils.lerp(velocity.x, ideal.x, Gdx.graphics.getRawDeltaTime() * lerpspeed);
                                velocity.y = MathUtils.lerp(velocity.y, ideal.y, Gdx.graphics.getRawDeltaTime() * lerpspeed);
                            }
                        }
                        trail.add(new ActualOrb(new Vector2(leader.position)));
                        leader.position.x += velocity.x * Gdx.graphics.getRawDeltaTime();
                        leader.position.y += velocity.y * Gdx.graphics.getRawDeltaTime();
                    }
                } else if(trail.isEmpty()) {
                    this.isDone = true;
                }
            }
            return returnVal;
        }

        public void render(SpriteBatch sb, Texture spark, float scale) {
            for(ActualOrb orb : trail) {
                orb.render(sb, spark, scale);
            }
            if(active) {
                leader.render(sb, spark, scale);
            }
        }

        private static class ActualOrb {
            public Vector2 position;
            protected float alpha = 1F;
            public ActualOrb(Vector2 position) {
                this.position = position.cpy();
            }

            public void render(SpriteBatch sb, Texture spark, float scale) {
                sb.setColor(new Color(1F, 1F, 1F, alpha));
                sb.draw(spark, position.x - (spark.getWidth() / 2F), position.y - (spark.getHeight() / 2F), spark.getWidth() / 2F, spark.getHeight() / 2F, spark.getWidth(), spark.getHeight(), scale, scale, 0, 0, 0, spark.getWidth(), spark.getHeight(), false, false);
            }
        }
    }
    private static class BurstParticle extends ApocalypseOrb.ActualOrb {
        private Vector2 velocity;

        public BurstParticle(Vector2 position) {
            super(position);
            float rand = MathUtils.random(MathUtils.PI2);
            this.velocity = new Vector2((float)Math.sin(rand), (float)Math.cos(rand));
            this.velocity.nor();
            rand = MathUtils.random(300F);
            this.velocity.x *= rand;
            this.velocity.y *= rand;
        }

        public boolean update() {
            alpha -= Gdx.graphics.getRawDeltaTime();
            position.x += velocity.x * Gdx.graphics.getRawDeltaTime();
            position.y += velocity.y * Gdx.graphics.getRawDeltaTime();
            return alpha <= 0F;
        }
    }
}
