package paleoftheancients.relics;

import com.megacrit.cardcrawl.relics.AbstractRelic;
import paleoftheancients.PaleMod;
import paleoftheancients.helpers.AssetLoader;

public class SoulOfTheBandit extends PaleRelic {
    public static final String ID = PaleMod.makeID("SoulOfTheBandit");

    private static final RelicTier TIER = RelicTier.SPECIAL;
    private static final LandingSound SOUND = LandingSound.CLINK;

    public SoulOfTheBandit() {
        super(ID, AssetLoader.loadImage(PaleMod.assetPath("images/relics/bandit.png")), AssetLoader.loadImage(PaleMod.assetPath("images/relics/outline/bandit.png")), TIER, SOUND);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new SoulOfTheBandit();
    }
}