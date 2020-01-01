package paleoftheancients.reimu.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import paleoftheancients.PaleMod;
import paleoftheancients.helpers.AssetLoader;
import paleoftheancients.reimu.monsters.Reimu;

public class EvilSealingCircleVFX extends AbstractDamagingVFX {
    private static final String path = "images/reimu/vfx/border.png";

    private Texture border;
    private int borderwidth, borderheight;

    private float targetscale;
    private float alpha;

    private Reimu source;
    private boolean ending = false;

    public EvilSealingCircleVFX(AbstractCreature target, Reimu source, DamageInfo info, int num) {
        super(target, info, num);
        this.border = AssetLoader.loadImage(PaleMod.assetPath(path));

        this.borderwidth = border.getWidth();
        this.borderheight = border.getHeight();

        targetscale = Math.abs(target.hb.x - source.hb.cX) / borderwidth;
        alpha = 0F;
        this.source = source;

        this.setDelay();
    }

    @Override
    public void update() {
        if(ending) {
            alpha -= Gdx.graphics.getDeltaTime();
            if(alpha <= 0F) {
                this.isDone = true;
            }
        } else if(alpha < 1F) {
            alpha += Gdx.graphics.getDeltaTime();
            if(alpha >= 1F) {
                alpha = 1F;
                CardCrawlGame.sound.playV(PaleMod.makeID("touhou_border"), 0.35F);
            }
        } else {
            scale = MathUtils.lerp(scale, targetscale, Gdx.graphics.getDeltaTime() * 3);

            if(num > 0 && targetscale - scale < 4 * Settings.scale) {
                endDelay();
                boolean stillActive = true;
                for(; num > 0; num--) {
                    AbstractDungeon.actionManager.addToTop(new FastVampireDamageAction(target, info, AbstractGameAction.AttackEffect.NONE));
                    if(stillActive) {
                        stillActive = false;
                        AbstractDungeon.actionManager.addToTop(new AbstractGameAction() {
                            @Override
                            public void update() {
                                this.isDone = true;
                                ending = true;
                            }
                        });
                    }
                }
            }
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setBlendFunction(770, 1);
        final float factor = 0.5F;

        final float leftX = source.hb.cX - borderwidth / 2F - source.hb.width * factor;
        final float rightX = source.hb.cX - borderwidth / 2F + source.hb.width * factor;
        final float horizontalY = source.hb.cY - borderheight / 2F;

        final float bottomY = source.hb.cY - borderheight / 2F - source.hb.width * factor;
        final float topY = source.hb.cY - borderheight / 2F + source.hb.width * factor;
        final float verticalX = source.hb.cX - borderwidth / 2F;

        sb.setColor(new Color(1F, 0.2F, 0.2F, alpha));
        int start = (int)(-verticalX / borderwidth * scale) - 1;
        int end = (int)(Settings.WIDTH / verticalX * borderwidth * scale);
        for(int i = start; i < end; i++) {
            sb.draw(border, verticalX + i * borderwidth * scale, bottomY, borderwidth * 0.8F, borderheight / 2F, borderwidth, borderheight, scale, scale, 90, 0, 0, borderwidth, borderheight, false, false);
            sb.draw(border, verticalX + i * borderwidth * scale, topY, borderwidth * 0.8F, borderheight / 2F, borderwidth, borderheight, scale, scale, 270, 0, 0, borderwidth, borderheight, false, false);
        }

        sb.setColor(new Color(0.2F, 0.2F, 1F, alpha));
        start = (int)(-horizontalY / borderwidth * scale) - 1;
        end = (int)(Settings.HEIGHT / horizontalY * borderwidth * scale);
        for(int i = start; i < end; i++) {
            sb.draw(border, rightX, horizontalY + i * borderwidth * scale, borderwidth * 0.8F, borderheight / 2F, borderwidth, borderheight, scale, scale, 180, 0, 0, borderwidth, borderheight, false, false);
            sb.draw(border, leftX, horizontalY + i * borderwidth * scale, borderwidth * 0.8F, borderheight / 2F, borderwidth, borderheight, scale, scale, 0, 0, 0, borderwidth, borderheight, false, false);
        }



        sb.setBlendFunction(770, 771);
        sb.setColor(Color.WHITE);
    }

    @Override
    public void dispose() {

    }


    public static void disposeAll() {
        AbstractDamagingVFX.disposeTry(PaleMod.assetPath(path));
    }
}
