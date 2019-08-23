package paleoftheancients.theshowman.bosscards;

import paleoftheancients.PaleMod;
import paleoftheancients.theshowman.misc.DummyCard;
import paleoftheancients.theshowman.monsters.TheShowmanBoss;
import com.megacrit.cardcrawl.actions.utility.ShowCardAndPoofAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;

public class Showstopper extends AbstractShowmanCard {
    public static final String ID = PaleMod.makeID("Showstopper");
    public static final String NAME;
    public static final String DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "bosscards/Showstopper.png";

    private static final CardStrings cardStrings;

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;

    private static final int COST = 3;

    public Showstopper(TheShowmanBoss owner) {
        super(ID, NAME, assetPath(IMG_PATH), COST, DESCRIPTION, TYPE, RARITY, TARGET, owner, AbstractMonster.Intent.MAGIC);
        this.exhaust = true;
    }

    @Override
    public void use(ArrayList<AbstractCard> availableExhaustCards, AbstractPlayer p, AbstractMonster m) {
        for(final AbstractShowmanCard card : this.getPlayableCards()) {
            AbstractDungeon.actionManager.addToBottom(new ShowCardAndPoofAction(new DummyCard(card)));
            card.use(availableExhaustCards, p, m);
        }
    }

    @Override
    public void applyPowers() {
        this.damage = 0;
        for(final AbstractShowmanCard card : this.getPlayableCards()) {
            card.applyPowers();
            this.damage += card.getPriority(new ArrayList<>(), 0, 0);
        }
    }

    private ArrayList<AbstractShowmanCard> getPlayableCards() {
        ArrayList<AbstractShowmanCard> cards = new ArrayList<>();
        for(int i = this.owner.exhaustpile.size() - 1; i >= 0; i--) {
            AbstractCard card = this.owner.exhaustpile.group.get(i);
            if(card.cost > -2 && card.cardID != this.cardID) {
                cards.add((AbstractShowmanCard) card);
            }
        }
        return cards;
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
        return new Showstopper(this.owner);
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        NAME = cardStrings.NAME;
        DESCRIPTION = cardStrings.DESCRIPTION;
        UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    }
}
