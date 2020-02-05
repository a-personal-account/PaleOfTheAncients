package paleoftheancients.relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import paleoftheancients.PaleMod;
import paleoftheancients.bard.actions.RelicWishAction;
import paleoftheancients.helpers.AssetLoader;

public class SoulOfTheBard extends CustomRelic {
    public static final String ID = PaleMod.makeID("SoulOfTheBard");

    private static final RelicTier TIER = RelicTier.SPECIAL;
    private static final LandingSound SOUND = LandingSound.CLINK;

    public SoulOfTheBard() {
        super(ID, AssetLoader.loadImage(PaleMod.assetPath("images/relics/bard.png")), AssetLoader.loadImage(PaleMod.assetPath("images/relics/outline/bard.png")), TIER, SOUND);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new SoulOfTheBard();
    }

    @Override
    public void atBattleStart() {
        AbstractDungeon.actionManager.addToBottom(new RelicWishAction());
    }
}
