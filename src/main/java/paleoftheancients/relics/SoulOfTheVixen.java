package paleoftheancients.relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import paleoftheancients.PaleMod;
import paleoftheancients.helpers.AssetLoader;
import paleoftheancients.thevixen.powers.BurnPower;

public class SoulOfTheVixen extends CustomRelic {
    public static final String ID = PaleMod.makeID("SoulOfTheVixen");

    private static final RelicTier TIER = RelicTier.SPECIAL;
    private static final LandingSound SOUND = LandingSound.MAGICAL;

    public SoulOfTheVixen() {
        super(ID, AssetLoader.loadImage(PaleMod.assetPath("images/relics/vixen.png")), AssetLoader.loadImage(PaleMod.assetPath("images/relics/outline/vixen.png")), TIER, SOUND);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new SoulOfTheVixen();
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(target, AbstractDungeon.player, new BurnPower(target, 1), 1));
    }
}
