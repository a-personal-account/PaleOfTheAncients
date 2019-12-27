package paleoftheancients.reimu.vfx;

import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import paleoftheancients.PaleMod;
import paleoftheancients.helpers.AssetLoader;
import paleoftheancients.reimu.monsters.Reimu;

import java.util.ArrayList;

public class FantasySealVFX extends AbstractDamagingVFX {
    private static final String path = "images/reimu/vfx/fantasyseal.png";
    private Texture orb;
    private int orbwidth, orbheight;
    private float targetScale;

    private Reimu source;
    private float timer;
    private boolean explode;
    private int sealcount;

    private ArrayList<Seal> seals = new ArrayList<>();

    public FantasySealVFX(AbstractCreature target, Reimu source, DamageInfo info, int num) {
        this(target, source, info, num, 8);
    }
    public FantasySealVFX(AbstractCreature target, Reimu source, DamageInfo info, int num, int sealcount) {
        super(target, info, num);
        this.orb = AssetLoader.loadImage(PaleMod.assetPath(path));
        this.orbwidth = orb.getWidth();
        this.orbheight = orb.getHeight();

        this.source = source;

        this.rotation = MathUtils.random(6.3F);

        this.duration = 0F;
        this.timer = 0.3F;
        this.targetScale = scale * 1.4F;

        this.setDelay();
        this.explode = false;
        this.sealcount = sealcount;
    }

    @Override
    public void update() {
        this.duration -= Gdx.graphics.getDeltaTime();
        if(duration <= 0F) {
            duration = timer;
            if(sealcount-- > 0) {
                CardCrawlGame.sound.playV(PaleMod.makeID("touhou_seal"), 0.15F);
                seals.add(new Seal());
            }
        }
        for(int i = seals.size() - 1; i >= 0; i--) {
            Seal s = seals.get(i);
            if(seals.get(i).update()) {
                seals.remove(i);
                if(seals.isEmpty() && num <= 0) {
                    this.isDone = true;
                    endDelay();
                }
            }
        }
        if(!explode && !seals.isEmpty()) {
            Seal s = seals.get(seals.size() - 1);
            if(s.atDestination()) {
                explode = true;
                CardCrawlGame.sound.playV(PaleMod.makeID("touhou_shot"), 0.25F);

                endDelay();
                for(int i = 0; i < num; i++) {
                    AbstractDungeon.actionManager.addToTop(new DamageAction(target, info, AbstractGameAction.AttackEffect.NONE));
                }
            }
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setBlendFunction(770, 1);
        for(final Seal s : seals) {
            s.render(sb);
        }
        sb.setBlendFunction(770, 771);
    }

    @Override
    public void dispose() {

    }

    class Seal {
        private float scale, phase, curvedistance, targetX, targetY, startingrads, endrads;
        public float x, y;
        private float[] rotation;
        public Color color;
        private boolean shrinking;

        public Seal() {
            this.scale = 0F;
            this.rotation = new float[3];
            for(int i = 0; i < this.rotation.length; i++) { MathUtils.random(360F); }

            curvedistance = MathUtils.random(source.hb.height * 0.5F, source.hb.height * 5F);
            if(MathUtils.randomBoolean()) { curvedistance *= -1; }

            targetX = AbstractDungeon.player.hb.cX + MathUtils.random(-AbstractDungeon.player.hb.width, AbstractDungeon.player.hb.width) * 1.2F;
            targetY = AbstractDungeon.player.hb.cY + MathUtils.random(-AbstractDungeon.player.hb.height, AbstractDungeon.player.hb.height) * 1.2F;
            float factor = (float)Math.sqrt(Math.pow(targetX - source.hb.cX, 2) + Math.pow(targetY - source.hb.cY, 2));

            x = (targetX + source.hb.cX) / 2F + (targetY - source.hb.cY) * curvedistance / factor;
            y = (targetY + source.hb.cY) / 2F - (targetX - source.hb.cX) * curvedistance / factor;

            curvedistance = (float)Math.sqrt(Math.pow(x - source.hb.cX, 2) + Math.pow(y - source.hb.cY, 2));

            endrads = -(float)Math.PI / 2 - (float)Math.atan((y - targetY) / (x - targetX));
            phase = (float)Math.PI / 2 - (float)Math.atan((y - source.hb.cY) / (x - source.hb.cX));
            if(y > source.hb.cY) {
                endrads += Math.PI * 2;
            }
            shrinking = false;

            this.color = new Color(MathUtils.random(0.7F, 1F), MathUtils.random(0.7F, 1F), MathUtils.random(0.8F, 1F), 0.9F);
        }

        public boolean update() {
            rotation[0] += Gdx.graphics.getDeltaTime() * 150F;
            rotation[1] -= Gdx.graphics.getDeltaTime() * 150F;
            rotation[2] += Gdx.graphics.getDeltaTime() * 50F;

            phase = MathUtils.lerp(phase, endrads, Gdx.graphics.getDeltaTime() * 4);

            if(explode) {
                if(shrinking) {
                    scale -= Gdx.graphics.getDeltaTime() * targetScale * 20F;
                    if(scale <= 0) {
                        return true;
                    }
                } else {
                    scale += Gdx.graphics.getDeltaTime() * targetScale * 3F;
                    if(scale > targetScale * 1.5F) {
                        shrinking = true;
                        AbstractDungeon.effectsQueue.add(new FantasySealResidueVFX(targetX, targetY));
                    }
                }
            }

            /*
            if(endrads > phase) {
                this.phase += Gdx.graphics.getDeltaTime();
                if (this.phase > endrads) {
                    return true;
                }
            } else {
                this.phase -= Gdx.graphics.getDeltaTime();
                if (this.phase < endrads) {
                    return true;
                }
            }*/

            if(this.scale < targetScale) {
                this.scale += Gdx.graphics.getDeltaTime() * targetScale;
            }
            return false;
        }

        public void render(SpriteBatch sb) {
            sb.setColor(Color.RED);
            float curX = x + (float)Math.sin(phase) * curvedistance - orbwidth / 2F;
            float curY = y + (float)Math.cos(phase) * curvedistance - orbheight / 2F;
            sb.draw(orb, curX, curY, orbwidth / 2F, orbheight / 2F, orbwidth, orbheight, this.scale, this.scale, this.rotation[0], 0, 0, orbwidth, orbheight, false, false);

            sb.setColor(Color.BLUE);
            sb.draw(orb, curX, curY, orbwidth / 2F, orbheight / 2F, orbwidth, orbheight, this.scale, this.scale, this.rotation[1], 0, 0, orbwidth, orbheight, false, false);

            sb.setColor(Color.GREEN);
            sb.draw(orb, curX, curY, orbwidth / 2F, orbheight / 2F, orbwidth, orbheight, this.scale, this.scale, this.rotation[2], 0, 0, orbwidth, orbheight, false, false);

            sb.setColor(Color.WHITE);
        }

        public boolean atDestination() {
            return Math.abs(endrads - phase) < 0.02F;
        }
    }


    public static void disposeAll() {
        try {
            AssetLoader.unLoad(PaleMod.assetPath(path));
        } catch (GdxRuntimeException ex) {}
    }
}
