package paleoftheancients.theshowman.misc;

import paleoftheancients.PaleMod;
import paleoftheancients.theshowman.bosscards.AbstractShowmanCard;
import paleoftheancients.theshowman.monsters.TheShowmanBoss;
import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class ShowmanDazed extends AbstractShowmanCard {
    public static final String ID = PaleMod.makeID("ShowmanDazed");

    public ShowmanDazed(TheShowmanBoss owner) {
        super(ID, Dazed.NAME, (String)null, -2, Dazed.DESCRIPTION, CardType.STATUS, CardRarity.COMMON, CardTarget.NONE, owner, AbstractMonster.Intent.NONE);
        this.color = CardColor.COLORLESS;
        this.isEthereal = true;

        ReflectionHacks.setPrivate(this, AbstractCard.class, "portrait", ((TextureAtlas) ReflectionHacks.getPrivateStatic(AbstractCard.class, "cardAtlas")).findRegion("status/dazed"));
    }

    @Override
    public void upgrade() {
        if(!this.upgraded) {
            this.upgradeName();
        }
    }

    @Override
    public void applyPowers() {
        this.exhaustPriority = Integer.MIN_VALUE / 2;
    }

    @Override
    public AbstractCard makeCopy() {
        return new ShowmanDazed(this.owner);
    }
}
