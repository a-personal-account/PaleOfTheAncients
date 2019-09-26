package paleoftheancients.bard.relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import paleoftheancients.PaleMod;
import paleoftheancients.helpers.AssetLoader;

public class MagicTuningFork extends CustomRelic {
    public static final String ID = PaleMod.makeID("MagicTuningFork");

    public MagicTuningFork() {
        super(ID, AssetLoader.loadImage(PaleMod.assetPath("images/bard/relics/magicTuningFork.png")), AbstractRelic.RelicTier.SPECIAL, AbstractRelic.LandingSound.MAGICAL);
    }

    @Override
    public String getUpdatedDescription() {
        return "";
    }

    @Override
    public AbstractRelic makeCopy() {
        return new MagicTuningFork();
    }
}
