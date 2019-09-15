package paleoftheancients.thevixen.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.powers.AbstractPower;
import paleoftheancients.PaleMod;
import paleoftheancients.helpers.AssetLoader;

public class AbstractTheVixenPower extends AbstractPower {
    private static final String BASE_DIR = PaleMod.assetPath("images/TheVixen/powers/");

    public AbstractTheVixenPower(String imgName) {
        this.region128 =
                new TextureAtlas.AtlasRegion(
                        AssetLoader.loadImage(BASE_DIR + "128/" + imgName), 0, 0, 128, 128);
        this.region48 =
                new TextureAtlas.AtlasRegion(
                        AssetLoader.loadImage(BASE_DIR + "48/" + imgName), 0, 0, 48, 48);
    }
}
