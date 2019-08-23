package paleoftheancients.theshowman.bosscards;

import paleoftheancients.PaleMod;
import paleoftheancients.theshowman.monsters.TheShowmanBoss;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class IsThisYourCard extends AbstractShowmanCard {
    public static final String ID = PaleMod.makeID("IsThisYourCard");
    public static final String NAME;
    public static final String DESCRIPTION;
    public static final String IMG_PATH = "bosscards/IsThisYourCard.png";

    private static final CardStrings cardStrings;

    private static final CardType TYPE = CardType.ATTACK;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;

    private static final int COST = 2;

    public IsThisYourCard(TheShowmanBoss owner) {
        super(ID, NAME, assetPath(IMG_PATH), COST, DESCRIPTION, TYPE, RARITY, TARGET, owner, AbstractMonster.Intent.ATTACK);
        this.baseDamage = 3;
    }

    @Override
    public void applyPowers() {
        this.multiplier = this.owner.exhaustpile.size();
        super.applyPowers();
        this.magicNumber = this.multiplier;
    }

    @Override
    public AbstractCard makeCopy() {
        return new IsThisYourCard(this.owner);
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        NAME = cardStrings.NAME;
        DESCRIPTION = cardStrings.DESCRIPTION;
    }
}
