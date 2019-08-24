package paleoftheancients.theshowman.bosscards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import paleoftheancients.PaleMod;
import paleoftheancients.theshowman.monsters.TheShowmanBoss;

import java.util.ArrayList;

public class SleeveAces extends AbstractShowmanCard {
    public static final String ID = PaleMod.makeID("SleeveAces");
    public static final String NAME;
    public static final String DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "bosscards/SleeveAces.png";

    private static final CardStrings cardStrings;

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;

    private static final int COST = 1;
    private AbstractCard toCopy;

    public SleeveAces(TheShowmanBoss owner) {
        super(ID, NAME, assetPath(IMG_PATH), COST, DESCRIPTION, TYPE, RARITY, TARGET, owner, AbstractMonster.Intent.UNKNOWN);
        this.exhaust = true;
        this.baseMagicNumber = this.magicNumber = 1;
    }

    @Override
    public void use(ArrayList<AbstractCard> availableExhaustCards, AbstractPlayer p, AbstractMonster m) {
        this.toCopy = null;
        this.getPriority(availableExhaustCards, this.owner.baseenergy, 0);
        if(toCopy != null) {
            for (int i = 0; i < this.magicNumber; i++) {
                this.owner.soulGroup.shuffleNewlyCreatedCard(toCopy.makeStatEquivalentCopy());
                this.owner.soulGroup.discard(toCopy.makeStatEquivalentCopy());
            }
        }
    }

    @Override
    public int getPriority(ArrayList<AbstractCard> availableExhaustCards, int availableEnergy, int byrdHits) {
        ArrayList<AbstractShowmanCard> cards = new ArrayList<>();
        int highestPriority = -1;
        int tmp;
        this.toCopy = null;
        for(final AbstractCard card : this.owner.exhaustpile.group) {
            if(card.cost > -2 && card.cardID != this.cardID) {
                AbstractShowmanCard scard = ((AbstractShowmanCard) card);
                scard.applyPowers();
                if((tmp = scard.getPriority(availableExhaustCards, availableEnergy, byrdHits)) > highestPriority) {
                    highestPriority = tmp;
                    this.toCopy = scard;
                }
            }
        }
        return highestPriority / 2;
    }

    @Override
    public void upgrade() {
        if(!this.upgraded) {
            this.upgradeName();
            this.exhaust = false;
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new SleeveAces(this.owner);
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        NAME = cardStrings.NAME;
        DESCRIPTION = cardStrings.DESCRIPTION;
        UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    }
}
