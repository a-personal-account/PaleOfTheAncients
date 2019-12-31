package paleoftheancients.reimu.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.unique.VampireDamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import paleoftheancients.PaleMod;
import paleoftheancients.helpers.AssetLoader;
import paleoftheancients.reimu.monsters.Reimu;

import java.util.ArrayList;

public class HakureiBarrierVFX extends AbstractDamagingVFX {
    private static final String path = "images/reimu/vfx/arrowhead.png";
    private static final String borderpath = "images/reimu/vfx/raiko";
    private Texture bullet;
    private Texture[] raiko;
    private int bulletwidth, bulletheight;
    private int raikowidth, raikoheight;

    private float rotationdifference, bullettimer;
    private float strands;
    private float sourcescale, targetscale, enterscale;
    private float alpha;
    private boolean ending, moreBullets;
    private int soundDelay;

    private Reimu source;

    private ArrayList<Bullet> bullets = new ArrayList<>();


    public HakureiBarrierVFX(AbstractCreature target, Reimu source, DamageInfo info, int num) {
        this(target, source, info, num, 4);
    }
    public HakureiBarrierVFX(AbstractCreature target, Reimu source, DamageInfo info, int num, float strands) {
        super(target, info, num);
        this.bullet = AssetLoader.loadImage(PaleMod.assetPath(path));
        this.raiko = new Texture[4];
        for(int i = 1; i <= raiko.length; i++) {
            this.raiko[i - 1] = AssetLoader.loadImage(PaleMod.assetPath(borderpath + i + ".png"));
        }

        this.bulletwidth = bullet.getWidth();
        this.bulletheight = bullet.getHeight();
        this.raikowidth = raiko[0].getWidth();
        this.raikoheight = raiko[0].getHeight();

        sourcescale = source.hb.width / raikowidth * 2.1F;
        targetscale = target.hb.width / raikowidth * 2.1F;
        enterscale = 0;
        alpha = 1F;
        ending = false;
        moreBullets = true;
        soundDelay = 0;

        this.source = source;

        this.rotation = MathUtils.random((float)Math.PI * 2);
        this.rotationdifference = MathUtils.random((float)Math.PI * 2);

        this.duration = 3F;
        this.bullettimer = 0F;
        this.strands = strands;

        this.setDelay();
    }

    @Override
    public void update() {
        this.rotation += Gdx.graphics.getDeltaTime();

        if(enterscale < sourcescale) {
            enterscale += Gdx.graphics.getDeltaTime() * sourcescale;
        } else if(!ending) {
            enterscale = sourcescale;

            if(moreBullets) {
                this.bullettimer -= Gdx.graphics.getDeltaTime();
                if (bullettimer <= 0F) {
                    if(soundDelay-- <= 0) {
                        CardCrawlGame.sound.playV(PaleMod.makeID("touhou_shot"), 0.15F);
                        soundDelay = 3;
                    }
                    bullettimer = 0.05F;
                    for (int i = 0; i < strands; i++) {
                        float tmprotation = rotation + ((float)Math.PI * 2 * i / strands);
                        bullets.add(new Bullet(target, source, tmprotation, Color.WHITE));
                        bullets.add(new Bullet(target, source, -tmprotation + rotationdifference, Color.RED));
                    }
                }
            }
            for(int i = bullets.size() - 1; i >= 0; i--) {
                if(bullets.get(i).update()) {
                    bullets.remove(i);
                    if(bullets.isEmpty()) {
                        ending = true;
                    }
                    if(num > 0) {
                        endDelay();

                        boolean stillActive = true;
                        for(; num > 0; num--) {
                            AbstractDungeon.actionManager.addToTop(new VampireDamageAction(target, info, AbstractGameAction.AttackEffect.NONE));
                            if(stillActive) {
                                stillActive = false;
                                AbstractDungeon.actionManager.addToTop(new AbstractGameAction() {
                                    @Override
                                    public void update() {
                                        this.isDone = true;
                                        moreBullets = false;
                                    }
                                });
                            }
                        }
                    }
                }
            }
        } else {
            alpha -= Gdx.graphics.getDeltaTime();
            if(alpha <= 0F) {
                this.isDone = true;
            }
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        for(final Bullet s : bullets) {
            s.render(sb, bullet, bulletwidth, bulletheight, scale * 2);
        }

        float distance = source.hb.width * (enterscale / sourcescale);
        sb.setColor(new Color(0, 0, 0.8F, alpha));
        sb.setBlendFunction(770, 1);
        sb.draw(raiko[MathUtils.random(raiko.length - 1)], source.hb.cX - raikowidth / 2F, source.hb.cY - distance - raikoheight / 2F, raikowidth / 2F, raikoheight / 2F, raikowidth, raikoheight, enterscale, enterscale, 0, 0, 0, raikowidth, raikoheight, false, false);
        sb.draw(raiko[MathUtils.random(raiko.length - 1)], source.hb.cX - raikowidth / 2F, source.hb.cY + distance - raikoheight / 2F, raikowidth / 2F, raikoheight / 2F, raikowidth, raikoheight, enterscale, enterscale, 180, 0, 0, raikowidth, raikoheight, false, false);

        sb.draw(raiko[MathUtils.random(raiko.length - 1)], source.hb.cX - distance - raikowidth / 2F, source.hb.cY - raikoheight / 2F, raikowidth / 2F, raikoheight / 2F, raikowidth, raikoheight, enterscale, enterscale, 90, 0, 0, raikowidth, raikoheight, false, false);
        sb.draw(raiko[MathUtils.random(raiko.length - 1)], source.hb.cX + distance - raikowidth / 2F, source.hb.cY - raikoheight / 2F, raikowidth / 2F, raikoheight / 2F, raikowidth, raikoheight, enterscale, enterscale, 270, 0, 0, raikowidth, raikoheight, false, false);

        float tscale = targetscale * enterscale / sourcescale;
        distance = target.hb.width / 2 * tscale;

        sb.setColor(new Color(0.8F, 0, 0, alpha));
        sb.draw(raiko[MathUtils.random(raiko.length - 1)], target.hb.cX - raikowidth / 2F, target.hb.cY - distance - raikoheight / 2F, raikowidth / 2F, raikoheight / 2F, raikowidth, raikoheight, tscale, tscale, 0, 0, 0, raikowidth, raikoheight, false, false);
        sb.draw(raiko[MathUtils.random(raiko.length - 1)], target.hb.cX - raikowidth / 2F, target.hb.cY + distance - raikoheight / 2F, raikowidth / 2F, raikoheight / 2F, raikowidth, raikoheight, tscale, tscale, 180, 0, 0, raikowidth, raikoheight, false, false);

        sb.draw(raiko[MathUtils.random(raiko.length - 1)], target.hb.cX - distance - raikowidth / 2F, target.hb.cY - raikoheight / 2F, raikowidth / 2F, raikoheight / 2F, raikowidth, raikoheight, tscale, tscale, 90, 0, 0, raikowidth, raikoheight, false, false);
        sb.draw(raiko[MathUtils.random(raiko.length - 1)], target.hb.cX + distance - raikowidth / 2F, target.hb.cY - raikoheight / 2F, raikowidth / 2F, raikoheight / 2F, raikowidth, raikoheight, tscale, tscale, 270, 0, 0, raikowidth, raikoheight, false, false);
        sb.setBlendFunction(770, 771);

        sb.setColor(Color.WHITE);
    }

    @Override
    public void dispose() {

    }


    public static void disposeAll() {
        try {
            AssetLoader.unLoad(PaleMod.assetPath(path));
            for(int i = 1; i <= 4; i++) {
                AssetLoader.unLoad(PaleMod.assetPath(PaleMod.assetPath(borderpath + i + ".png")));
            }
        } catch (GdxRuntimeException ex) {}
    }
}
