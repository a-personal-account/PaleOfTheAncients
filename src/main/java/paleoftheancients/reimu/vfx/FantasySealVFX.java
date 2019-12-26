package paleoftheancients.reimu.vfx;

import basemod.BaseMod;
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
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import paleoftheancients.PaleMod;
import paleoftheancients.helpers.AssetLoader;
import paleoftheancients.reimu.actions.DelayActionAction;
import paleoftheancients.reimu.monsters.Reimu;

import java.util.ArrayList;

public class FantasySealVFX extends AbstractGameEffect {
    private static final String path = "images/reimu/vfx/fantasyseal.png";
    private Texture orb;
    private int orbwidth, orbheight;
    private float targetScale;

    private Reimu source;
    private AbstractCreature target;
    private DamageInfo info;
    private int num;
    private DelayActionAction delay = null;
    private float timer = 4F;

    private ArrayList<Seal> seals = new ArrayList<>();

    public FantasySealVFX(AbstractCreature target, Reimu source, DamageInfo info, int num) {
        this.orb = AssetLoader.loadImage(PaleMod.assetPath(path));
        this.orbwidth = orb.getWidth();
        this.orbheight = orb.getHeight();

        this.target = target;
        this.source = source;
        this.info = info;
        this.num = num;

        this.rotation = MathUtils.random(6.3F);

        this.duration = 0F;
        this.targetScale = scale * 2F;

        this.setDelay();
    }

    @Override
    public void update() {
        this.duration -= Gdx.graphics.getDeltaTime();
        if(duration <= 0F) {
            duration = timer;
            timer -= 0.2F;
            if(num-- > 0) {
                seals.add(new Seal());
            }
        }
        for(int i = seals.size() - 1; i >= 0; i--) {
            Seal s = seals.get(i);
            if(seals.get(i).update()) {
                seals.remove(i);
                if(seals.isEmpty() && num <= 0) {
                    this.isDone = true;
                    delay.end();
                } else {
                    setDelay();
                }
                AbstractDungeon.actionManager.addToTop(new DamageAction(target, info, AbstractGameAction.AttackEffect.NONE));
                AbstractGameEffect age = new FlashAtkImgEffect(s.calcX() - orbwidth / 2F, s.calcY() - orbheight / 2F, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
                ReflectionHacks.setPrivate(age, AbstractGameEffect.class, "color", s.color);
                AbstractDungeon.effectsQueue.add(age);
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
        private float scale, phase, curvedistance, startingrads, endrads;
        public float x, y;
        private float rotation;
        public Color color;

        public Seal() {
            this.scale = 0F;
            this.rotation = MathUtils.random(360F);
            curvedistance = MathUtils.random(source.hb.height * 0.5F, source.hb.height * 5F);
            if(MathUtils.randomBoolean()) { curvedistance *= -1; }

            float targetX = AbstractDungeon.player.hb.cX + MathUtils.random(-AbstractDungeon.player.hb.width, AbstractDungeon.player.hb.width) / 2F;
            float targetY = AbstractDungeon.player.hb.y + MathUtils.random(AbstractDungeon.player.hb.height);
            float factor = (float)Math.sqrt(Math.pow(targetX - source.hb.cX, 2) + Math.pow(targetY - source.hb.cY, 2));

            x = (targetX + source.hb.cX) / 2F + (targetY - source.hb.cY) * curvedistance / factor;
            y = (targetY + source.hb.cY) / 2F - (targetX - source.hb.cX) * curvedistance / factor;

            curvedistance = (float)Math.sqrt(Math.pow(x - source.hb.cX, 2) + Math.pow(y - source.hb.cY, 2));

            endrads = -(float)Math.PI / 2 - (float)Math.atan((y - targetY) / (x - targetX));
            phase = (float)Math.PI / 2 - (float)Math.atan((y - source.hb.cY) / (x - source.hb.cX));
            if(y > source.hb.cY) {
                endrads += Math.PI * 2;
            }

            this.color = new Color(MathUtils.random(0.7F, 1F), MathUtils.random(0.7F, 1F), MathUtils.random(0.8F, 1F), 0.9F);
        }

        public boolean update() {
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
            }

            this.scale += Gdx.graphics.getDeltaTime() * targetScale;
            return false;
        }

        public void render(SpriteBatch sb) {
            sb.setColor(this.color);

            sb.draw(orb, calcX(), calcY(), orbwidth / 2F, orbheight / 2F, orbwidth, orbheight, this.scale, this.scale, this.rotation, 0, 0, orbwidth, orbheight, false, false);

            sb.setColor(Color.WHITE);
        }

        public float calcX() {
            return x - orbwidth / 2F + (float)Math.sin(phase) * curvedistance;
        }
        public float calcY() {
            return y - orbheight / 2F + (float)Math.cos(phase) * curvedistance;
        }
    }


    public static void disposeAll() {
        try {
            AssetLoader.unLoad(PaleMod.assetPath(path));
        } catch (GdxRuntimeException ex) {}
    }

    private void setDelay() {
        if(this.delay != null) {
            this.delay.end();
        }
        this.delay = new DelayActionAction();
        AbstractDungeon.actionManager.addToTop(this.delay);
    }
}
