package paleoftheancients.relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import paleoftheancients.PaleMod;
import paleoftheancients.helpers.AssetLoader;

public class SoulOfTheDefect extends CustomRelic {
    public static final String ID = PaleMod.makeID("SoulOfTheDefect");

    private static final RelicTier TIER = RelicTier.SPECIAL;
    private static final LandingSound SOUND = LandingSound.MAGICAL;

    public SoulOfTheDefect() {
        super(ID, AssetLoader.loadImage(PaleMod.assetPath("images/relics/defect.png")), AssetLoader.loadImage(PaleMod.assetPath("images/relics/outline/defect.png")), TIER, SOUND);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new SoulOfTheDefect();
    }

    public void onUseCard(AbstractCard card, UseCardAction action) {
        if(card.type == AbstractCard.CardType.POWER && this.counter > 0) {
            this.counter--;

            for(int i = 0; i < 2; i++) {
                GameActionManager.queueExtraCard(card, null);
            }
        }
    }

    public void atBattleStart() {
        this.counter = 2;
    }

    public void onVictory() {
        this.counter = -1;
    }
}
