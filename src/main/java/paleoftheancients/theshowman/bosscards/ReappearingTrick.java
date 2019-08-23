package paleoftheancients.theshowman.bosscards;

import paleoftheancients.PaleMod;
import paleoftheancients.theshowman.monsters.TheShowmanBoss;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;

public class ReappearingTrick extends AbstractShowmanCard {
    public static final String ID = PaleMod.makeID("ReappearingTrick");
    public static final String NAME;
    public static final String DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "bosscards/ReappearingAct.png";

    private static final CardStrings cardStrings;

    private static final CardType TYPE = CardType.ATTACK;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    private static final int COST = 1;

    public ReappearingTrick(TheShowmanBoss owner) {
        super(ID, NAME, assetPath(IMG_PATH), COST, DESCRIPTION, TYPE, RARITY, TARGET, owner, AbstractMonster.Intent.ATTACK_BUFF);
        this.baseDamage = 20;
    }

    @Override
    public void use(ArrayList<AbstractCard> availableExhaustCards, AbstractPlayer p, AbstractMonster m) {
        super.use(availableExhaustCards, p, m);
        for(int i = this.owner.exhaustpile.size() - 1; i >= 0; i--) {
            AbstractCard card = this.owner.exhaustpile.group.get(i);
            if(card.cost > -2) {
                this.owner.exhaustpile.removeCard(card);
                this.owner.soulGroup.shuffleNewlyCreatedCard(card);
            }
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new ReappearingTrick(this.owner);
    }

    @Override
    public int getPriority(ArrayList<AbstractCard> availableExhaustCards, int availableEnergy, int byrdHits) {
        return super.getPriority(availableExhaustCards, availableEnergy, byrdHits) + this.owner.exhaustpile.size() * 10;
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        NAME = cardStrings.NAME;
        DESCRIPTION = cardStrings.DESCRIPTION;
        UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    }
}
