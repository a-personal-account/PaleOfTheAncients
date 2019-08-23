package paleoftheancients.theshowman.bosscards;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import paleoftheancients.PaleMod;
import paleoftheancients.theshowman.monsters.TheShowmanBoss;

import java.util.ArrayList;

public class SetTheStage extends AbstractShowmanCard {
    public static final String ID = PaleMod.makeID("SetTheStage");
    public static final String NAME;
    public static final String DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION;
    public static final String IMG_PATH = "bosscards/SetTheStage" + MathUtils.random(1, 2) + ".png";

    private static final CardStrings cardStrings;

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;

    private static final int COST = 0;

    private CardGroup[] cards;

    public SetTheStage(TheShowmanBoss owner, CardGroup... cardgroups) {
        super(ID, NAME, assetPath(IMG_PATH), COST, DESCRIPTION, TYPE, RARITY, TARGET, owner, AbstractMonster.Intent.UNKNOWN);
        this.purgeOnUse = true;
        cards = new CardGroup[] {
                new CardGroup(cardgroups[0], CardGroup.CardGroupType.UNSPECIFIED),
                new CardGroup(cardgroups[1], CardGroup.CardGroupType.UNSPECIFIED),
                new CardGroup(cardgroups[2], CardGroup.CardGroupType.UNSPECIFIED)
        };
    }

    @Override
    public void use(ArrayList<AbstractCard> availableExhaustCards, AbstractPlayer p, AbstractMonster m) {
        this.owner.stage.maxHealth += 20;
        AbstractDungeon.actionManager.addToBottom(new HealAction(this.owner.stage, this.owner, this.owner.stage.maxHealth));
        this.owner.stage.isDying = false;
        this.owner.stage.setStage();

        this.owner.discardpile.group.addAll(cards[0].group);
        this.owner.drawpile.group.addAll(cards[1].group);
        this.owner.discardpile.group.addAll(cards[2].group);
    }

    @Override
    public int getPriority(ArrayList<AbstractCard> availableExhaustCards, int availableEnergy, int byrdHits) {
        return 1;
    }

    @Override
    public void upgrade() {
        if(!this.upgraded) {
            this.upgradeName();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new SetTheStage(this.owner, cards[0], cards[1], cards[2]);
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        NAME = cardStrings.NAME;
        DESCRIPTION = cardStrings.DESCRIPTION;
        EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    }
}
