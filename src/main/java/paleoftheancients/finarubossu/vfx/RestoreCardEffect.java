package paleoftheancients.finarubossu.vfx;

import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

public class RestoreCardEffect extends ShowCardAndObtainEffect {
    private AbstractCard thecard;

    public RestoreCardEffect(AbstractCard card, float x, float y, AbstractCard.CardRarity originalRarity) {
        this(card, x, y, true, originalRarity);
    }
    public RestoreCardEffect(AbstractCard card, float x, float y, boolean convergeCards, AbstractCard.CardRarity originalRarity) {
        super(card, x, y, convergeCards);
        card.rarity = originalRarity;
        thecard = card;
    }

    @Override
    public void update() {
        this.duration -= Gdx.graphics.getDeltaTime();
        thecard.update();
        if (this.duration < 0.0F) {

            this.isDone = true;
            thecard.shrink();
            AbstractDungeon.getCurrRoom().souls.obtain(thecard, true);
        }
    }
}
