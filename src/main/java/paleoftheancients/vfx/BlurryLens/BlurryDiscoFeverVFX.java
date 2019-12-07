package paleoftheancients.vfx.BlurryLens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import paleoftheancients.PaleMod;
import paleoftheancients.helpers.AssetLoader;
import paleoftheancients.thevixen.helpers.RainbowColor;

public class BlurryDiscoFeverVFX extends BlurryLensVFX {
    private RainbowColor color;
    private Texture filledPixel;

    public BlurryDiscoFeverVFX() {
        color = new RainbowColor(Color.WHITE);
        color.a = 0.4F;
        filledPixel = AssetLoader.loadImage(PaleMod.assetPath("images/misc/filledpixel.png"));
    }

    @Override
    public void update() {
        color.update();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setColor(this.color);
        sb.draw(filledPixel, 0F, 0F, Settings.WIDTH / 2F, Settings.HEIGHT / 2F, Settings.WIDTH, Settings.HEIGHT, 1F, 1F, 0, 0, 0, 1, 1, false, false);
        sb.setColor(Color.WHITE);
    }

    @Override
    public void dispose() {

    }
}
