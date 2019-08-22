package paleoftheancients.bard.relics;

import paleoftheancients.PaleMod;
import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class MagicTuningFork extends CustomRelic {
    public static final String ID = PaleMod.makeID("MagicTuningFork");

    public MagicTuningFork() {
        super(ID, ImageMaster.loadImage(PaleMod.assetPath("images/bard/relics/magicTuningFork.png")), AbstractRelic.RelicTier.SPECIAL, AbstractRelic.LandingSound.MAGICAL);
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
