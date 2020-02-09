package paleoftheancients.watcher.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import paleoftheancients.PaleMod;
import paleoftheancients.helpers.AssetLoader;

public class AbstractStancePower extends AbstractPower implements InvisiblePower {
    public static final String POWER_ID = PaleMod.makeID("AbstractStancePower");

    public AbstractStancePower(AbstractCreature owner) {
        this.name = "";
        this.ID = POWER_ID;
        this.owner = owner;
        this.updateDescription();
        region48 = new TextureAtlas.AtlasRegion(AssetLoader.emptyPixel(), 0, 0, 1, 1);
        region128 = new TextureAtlas.AtlasRegion(AssetLoader.emptyPixel(), 0, 0, 1, 1);
        this.type = PowerType.BUFF;
    }

    @Override
    public void updateDescription() {
        this.description = "";
    }
}
