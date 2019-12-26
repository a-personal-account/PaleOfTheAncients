package paleoftheancients.reimu.vfx;

import basemod.BaseMod;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import paleoftheancients.PaleMod;
import paleoftheancients.helpers.AssetLoader;

public class SpellCardBackgroundVFX extends AbstractGameEffect {
    public static String staticpath = "images/reimu/vfx/spellbackground_static.png";
    public static String rotatingpath = "images/reimu/vfx/spellbackground_rotating.png";

    private Texture staticimage, rotatingimage;

    private int staticwidth, staticheight, rotatingwidth, rotatingheight;

    private boolean ending;

    public SpellCardBackgroundVFX() {
        staticimage = AssetLoader.loadImage(PaleMod.assetPath(staticpath));
        rotatingimage = AssetLoader.loadImage(PaleMod.assetPath(rotatingpath));

        this.scale = (float)Settings.WIDTH / (float)staticimage.getWidth();

        staticwidth = staticimage.getWidth();
        staticheight = staticimage.getHeight();
        rotatingwidth = rotatingimage.getWidth();
        rotatingheight = rotatingimage.getHeight();

        this.color = Color.WHITE.cpy();
        this.color.a = 0F;

        this.renderBehind = true;
        this.ending = false;
        this.rotation = MathUtils.random(360F);
    }

    @Override
    public void update() {
        this.rotation += Gdx.graphics.getDeltaTime() * 50;

        if(!this.ending) {
            if (this.color.a < 1F) {
                this.color.a += Gdx.graphics.getDeltaTime();
            } else {
                this.color.a = 1F;
            }
        } else {
            if (this.color.a > 0F) {
                this.color.a -= Gdx.graphics.getDeltaTime();
            } else {
                this.isDone = true;
            }
        }
    }

    public void end() {
        this.ending = true;
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setColor(color);
        sb.draw(staticimage, Settings.WIDTH / 2F - staticwidth / 2F, Settings.HEIGHT / 2F - staticheight / 2F, staticwidth / 2F, staticheight / 2F, staticwidth, staticheight, scale, scale, 0, 0, 0, staticwidth, staticheight, false, false);
        sb.draw(rotatingimage, Settings.WIDTH / 2F - rotatingwidth / 2F, Settings.HEIGHT / 2F - rotatingheight / 2F, rotatingwidth / 2F, rotatingheight / 2F, rotatingwidth, rotatingheight, scale, scale, this.rotation, 0, 0, rotatingwidth, rotatingheight, false, false);
        sb.setColor(Color.WHITE);
    }

    @Override
    public void dispose() {}

    public static void disposeAll() {
        try {
            AssetLoader.unLoad(PaleMod.assetPath(rotatingpath));
        } catch (GdxRuntimeException ex) {}
        try {
            AssetLoader.unLoad(PaleMod.assetPath(staticpath));
        } catch (GdxRuntimeException ex) {}
    }
}
