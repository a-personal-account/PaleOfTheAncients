package paleoftheancients.relics;

import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import paleoftheancients.PaleMod;
import paleoftheancients.helpers.AssetLoader;

public class SoulOfTheShowman extends PaleRelic {
    public static final String ID = PaleMod.makeID("SoulOfTheShowman");

    private static final RelicTier TIER = RelicTier.SPECIAL;
    private static final LandingSound SOUND = LandingSound.MAGICAL;

    public SoulOfTheShowman() {
        super(ID, AssetLoader.loadImage(PaleMod.assetPath("images/relics/showman.png")), AssetLoader.loadImage(PaleMod.assetPath("images/relics/outline/showman.png")), TIER, SOUND);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new SoulOfTheShowman();
    }

    @Override
    public void onExhaust(AbstractCard card) {
        switch(card.type) {
            case CURSE:
            case STATUS:
                break;

            default:
                if(card.color == AbstractCard.CardColor.CURSE) {
                    return;
                }
                this.flash();
                GameActionManager.queueExtraCard(card, AbstractDungeon.getCurrRoom().monsters.getRandomMonster(true));
                break;
        }
    }
}
