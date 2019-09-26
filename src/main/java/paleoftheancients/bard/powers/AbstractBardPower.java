package paleoftheancients.bard.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.powers.AbstractPower;
import paleoftheancients.PaleMod;
import paleoftheancients.helpers.AssetLoader;

public class AbstractBardPower extends AbstractPower {
    private TextureAtlas powerAtlas;

    public AbstractBardPower() {
        this.powerAtlas = AssetLoader.loadAtlas(PaleMod.assetPath("images/bard/powers/powers.atlas"));
    }

    protected void loadRegion(String fileName) {
        this.region48 = this.powerAtlas.findRegion("48/" + fileName);
        this.region128 = this.powerAtlas.findRegion("128/" + fileName);
        if (this.region48 == null && this.region128 == null) {
            super.loadRegion(fileName);
        }

    }
}
