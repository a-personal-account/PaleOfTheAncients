package paleoftheancients.thorton.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import paleoftheancients.PaleMod;
import paleoftheancients.bard.powers.SilencedPower;

public class DemotionPower extends SilencedPower {
    public static final String POWER_ID = PaleMod.makeID("DemotionPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public DemotionPower(AbstractCreature owner) {
        super(owner, -1, false);
        name = NAME;
        ID = POWER_ID;
        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(PaleMod.assetPath("images/thorton/powers/LoseFocus_84.png")), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(PaleMod.assetPath("images/thorton/powers/LoseFocus_32.png")), 0, 0, 32, 32);
    }


    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0];
    }

    @Override
    public void atEndOfRound() {}
}
