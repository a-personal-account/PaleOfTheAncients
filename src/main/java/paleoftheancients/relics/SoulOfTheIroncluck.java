package paleoftheancients.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import paleoftheancients.PaleMod;
import paleoftheancients.helpers.AssetLoader;
import paleoftheancients.ironcluck.monsters.IronCluck;

public class SoulOfTheIroncluck extends CustomRelic {
    public static final String ID = PaleMod.makeID("SoulOfTheIroncluck");

    private static final RelicTier TIER = RelicTier.SPECIAL;
    private static final LandingSound SOUND = LandingSound.MAGICAL;

    public SoulOfTheIroncluck() {
        super(ID, AssetLoader.loadImage(PaleMod.assetPath("images/relics/ironcluck" + suffix())), AssetLoader.loadImage(PaleMod.assetPath("images/relics/outline/ironcluck" + suffix())), TIER, SOUND);
    }
    private static String suffix() {
        return (IronCluck.cowboyClad() ? "-cowboy" : "") + ".png";
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new SoulOfTheIroncluck();
    }

    @Override
    public int onPlayerGainedBlock(float blockAmount) {
        AbstractDungeon.actionManager.addToBottom(new DrawCardAction(AbstractDungeon.player, 1));
        return MathUtils.floor(blockAmount);
    }

    @Override
    public void onCardDraw(AbstractCard card) {
        if(card.type.equals(AbstractCard.CardType.STATUS)) {
            AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(1));
        }
    }
}
