package paleoftheancients.reimu.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import paleoftheancients.PaleMod;
import paleoftheancients.helpers.AssetLoader;


public class FreeControlPower extends AbstractPower {
    public static final String POWER_ID = PaleMod.makeID("FreeControlPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public FreeControlPower(AbstractCreature owner) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;

        type = PowerType.BUFF;
        isTurnBased = false;

        this.region128 = new TextureAtlas.AtlasRegion(AssetLoader.loadImage(PaleMod.assetPath("images/reimu/powers/SpellCard84.png")), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(AssetLoader.loadImage(PaleMod.assetPath("images/reimu/powers/SpellCard32.png")), 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0];
    }
}