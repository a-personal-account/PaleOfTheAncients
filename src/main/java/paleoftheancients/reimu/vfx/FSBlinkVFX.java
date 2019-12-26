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
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import paleoftheancients.PaleMod;
import paleoftheancients.helpers.AssetLoader;
import paleoftheancients.reimu.actions.DelayActionAction;
import paleoftheancients.reimu.monsters.Reimu;

public class FSBlinkVFX extends AbstractGameEffect {
    private static final String path = "images/reimu/vfx/fantasyorb.png";
    private Texture orb;
    private int orbwidth, orbheight;

    private Reimu source;
    private AbstractCreature target;
    private DamageInfo info;
    private int num;
    private DelayActionAction delay = null;
    private float distance;
    private float x, y;

    public FSBlinkVFX(AbstractCreature target, Reimu source, DamageInfo info, int num) {
        this.orb = AssetLoader.loadImage(PaleMod.assetPath(path));
        this.orbwidth = orb.getWidth();
        this.orbheight = orb.getHeight();

        this.target = target;
        this.source = source;
        this.info = info;
        this.num = num;

        this.rotation = MathUtils.random(6.3F);

        this.duration = 0F;

        x = source.hb.cX - orbwidth / 2F;
        y = source.hb.cY - orbheight / 2F;

        this.setDelay();

        this.color = Color.PURPLE.cpy();
    }

    @Override
    public void update() {
        this.rotation += Gdx.graphics.getDeltaTime() * 2F;
        this.scale = Settings.scale * (1.5F + (float)Math.sin(this.rotation * 3F) * 0.5F);

        if(this.color.a == 1F) {
            if (distance < orbwidth) {
                distance += Gdx.graphics.getDeltaTime() * 200F;
            } else {
                this.duration -= Gdx.graphics.getDeltaTime();
                if (this.duration <= 0F) {
                    this.duration = 0.3F;
                    if (num-- > 0) {
                        setDelay();
                    } else {
                        delay.end();
                        this.color.a -= Gdx.graphics.getDeltaTime();
                    }
                    AbstractDungeon.actionManager.addToTop(new DamageAction(target, info, AbstractGameAction.AttackEffect.NONE));
                    AbstractGameEffect age = new FlashAtkImgEffect(target.hb.cX, target.hb.cY, AbstractGameAction.AttackEffect.BLUNT_HEAVY);
                    ReflectionHacks.setPrivate(age, AbstractGameEffect.class, "color", new Color(MathUtils.random(1F), MathUtils.random(1F), MathUtils.random(1F), 1F));
                    AbstractDungeon.effectsQueue.add(age);
                }
            }
        } else {
            this.color.a -= Gdx.graphics.getDeltaTime();
            if(this.color.a <= 0F) {
                this.isDone = true;
            }
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setColor(this.color);
        sb.setBlendFunction(770, 1);

        float rotX, rotY;
        for(int i = 0; i < 10; i++) {
            rotX = (float)Math.sin(rotation + i * Math.PI * 2 / 10);
            rotY = (float)Math.cos(rotation + i * Math.PI * 2 / 10);
            sb.draw(orb, x + rotX * distance, y + rotY * distance, orbwidth / 2F, orbheight / 2F, orbwidth, orbheight, this.scale, this.scale, 0, 0, 0, orbwidth, orbheight, false, false);
            rotX = (float)Math.sin(-rotation + i * Math.PI * 2 / 10);
            rotY = (float)Math.cos(-rotation + i * Math.PI * 2 / 10);
            sb.draw(orb, x + rotX * distance * 2, y + rotY * distance * 2, orbwidth / 2F, orbheight / 2F, orbwidth, orbheight, this.scale, this.scale, 0, 0, 0, orbwidth, orbheight, false, false);
        }

        sb.setColor(Color.WHITE);
        sb.setBlendFunction(770, 771);
    }

    @Override
    public void dispose() {

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
