package paleoftheancients.theshowman.misc;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class DummyCard extends CustomCard {

    public DummyCard(AbstractCard toCopy) {
        super(toCopy.cardID, toCopy.name, (String)null, toCopy.cost, toCopy.rawDescription, toCopy.type, toCopy.color, toCopy.rarity, toCopy.target);
        this.portrait = toCopy.portrait;

        for(int i = 0; i < toCopy.timesUpgraded; ++i) {
            this.upgrade();
        }

        this.target = toCopy.target;
        this.upgraded = toCopy.upgraded;
        this.timesUpgraded = toCopy.timesUpgraded;
        this.baseDamage = toCopy.baseDamage;
        this.baseBlock = toCopy.baseBlock;
        this.baseMagicNumber = toCopy.baseMagicNumber;
        this.costForTurn = toCopy.costForTurn;
        this.isCostModified = toCopy.isCostModified;
        this.isCostModifiedForTurn = toCopy.isCostModifiedForTurn;
        this.misc = toCopy.misc;
        this.freeToPlayOnce = toCopy.freeToPlayOnce;

        this.applyPowers();
    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {

    }

    @Override
    public void upgrade() {

    }
}
