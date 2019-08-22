package paleoftheancients.theshowman.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.ExhaustCardEffect;

public abstract class ByrdCard extends CustomCard {
    public ByrdCard(
            String id, String name, String imagePath, int COST, String description, CardType TYPE, CardColor COLOR, CardRarity rarity,
            CardTarget target) {
        super(id, name, imagePath, COST, description, TYPE, COLOR, rarity, target);
        this.purgeOnUse = true;
    }

    @Override
    public void triggerOnEndOfPlayerTurn() {
        super.triggerOnEndOfPlayerTurn();
        AbstractDungeon.player.hand.removeCard(this);
        AbstractDungeon.effectList.add(new ExhaustCardEffect(this));
    }
}
