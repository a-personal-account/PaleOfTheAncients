package paleoftheancients.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.ChokePower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import paleoftheancients.PaleMod;
import paleoftheancients.helpers.AssetLoader;

public class SoulOfTheSilent extends PaleRelic {
    public static final String ID = PaleMod.makeID("SoulOfTheSilent");

    private static final RelicTier TIER = RelicTier.SPECIAL;
    private static final LandingSound SOUND = LandingSound.MAGICAL;

    public SoulOfTheSilent() {
        super(ID, AssetLoader.loadImage(PaleMod.assetPath("images/relics/silent.png")), AssetLoader.loadImage(PaleMod.assetPath("images/relics/outline/silent.png")), TIER, SOUND);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new SoulOfTheSilent();
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if(info.type == DamageInfo.DamageType.NORMAL) {
            AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(target, AbstractDungeon.player, new ChokePower(target, 2), 2));
        }
    }
}
