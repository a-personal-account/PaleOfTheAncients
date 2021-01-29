package paleoftheancients.relics;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import paleoftheancients.PaleMod;
import paleoftheancients.helpers.AssetLoader;

public class SoulOfTheTimeEater extends PaleRelic {
    public static final String ID = PaleMod.makeID("SoulOfTheTimeEater");

    private static final RelicTier TIER = RelicTier.SPECIAL;
    private static final LandingSound SOUND = LandingSound.CLINK;

    private AbstractCard cardSelected;
    private boolean shuffleIntoDrawpile;
    private boolean returnToHand;

    public SoulOfTheTimeEater() {
        super(ID, AssetLoader.loadImage(PaleMod.assetPath("images/relics/timeeater.png")), AssetLoader.loadImage(PaleMod.assetPath("images/relics/outline/timeeater.png")), TIER, SOUND);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new SoulOfTheTimeEater();
    }

    @Override
    public void onUseCard(AbstractCard targetCard, UseCardAction useCardAction) {
        if(!targetCard.purgeOnUse && targetCard.type != AbstractCard.CardType.POWER && !useCardAction.exhaustCard && (cardSelected == null || cardSelected == targetCard)) {
            if(cardSelected == null && targetCard.costForTurn >= 0) {
                int prevcost = targetCard.costForTurn;
                targetCard.costForTurn--;
                if(targetCard.costForTurn < 1) {
                    targetCard.costForTurn = 1;
                }
                if(prevcost != targetCard.costForTurn) {
                    targetCard.isCostModifiedForTurn = true;
                }
            }
            returnToHand = targetCard.returnToHand;
            shuffleIntoDrawpile = targetCard.shuffleBackIntoDrawPile;
            targetCard.glowColor = Color.LIGHT_GRAY;
            cardSelected = targetCard;
            stopPulse();
            flash();
            useCardAction.reboundCard = false;
            targetCard.returnToHand = true;
            targetCard.shuffleBackIntoDrawPile = false;
        }
    }

    @Override
    public void atTurnStart() {
        if(cardSelected != null) {
            cardSelected.returnToHand = returnToHand;
            cardSelected.shuffleBackIntoDrawPile = shuffleIntoDrawpile;
            cardSelected = null;
        }
        this.beginLongPulse();
    }
}
