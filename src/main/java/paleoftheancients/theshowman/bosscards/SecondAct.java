package paleoftheancients.theshowman.bosscards;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
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

public class SecondAct extends AbstractShowmanCard {
    public static final String ID = PaleMod.makeID("SecondAct");
    public static final String NAME;
    public static final String DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION;
    public static final String IMG_PATH = "bosscards/SecondAct.png";

    private static final CardStrings cardStrings;

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;

    private static final int COST = 3;

    public SecondAct(TheShowmanBoss owner) {
        super(ID, NAME, assetPath(IMG_PATH), COST, DESCRIPTION, TYPE, RARITY, TARGET, owner, AbstractMonster.Intent.BUFF);
        this.purgeOnUse = true;
        this.exhaustPriority = -1;
    }

    @Override
    public void use(ArrayList<AbstractCard> availableExhaustCards, AbstractPlayer p, AbstractMonster m) {
        this.upgraded = true;
        this.owner.baseenergy++;
        this.owner.handsize += 2;
        CardGroup[] groups = new CardGroup[]{
                this.owner.hand, this.owner.drawpile, this.owner.discardpile, this.owner.exhaustpile
        };

        this.owner.drawpile.addToRandomSpot(new VanDeGraaffsRevenge(this.owner));
        this.owner.drawpile.addToRandomSpot(new GrossDisplay(this.owner));

        if(AbstractDungeon.ascensionLevel >= 19) {
            for (final CardGroup group : groups) {
                for (final AbstractCard card : group.group) {
                    card.upgrade();
                }
            }
        }
        AbstractDungeon.actionManager.addToBottom(new TalkAction(this.owner, TheShowmanBoss.DIALOG[MathUtils.random(4, 6)]));
    }

    @Override
    public int getPriority(ArrayList<AbstractCard> availableExhaustCards, int availableEnergy, int byrdHits) {
        return Integer.MAX_VALUE / 2;
    }

    @Override
    public void upgrade() {
        if(!this.upgraded) {
            this.upgradeName();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new SecondAct(this.owner);
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        NAME = cardStrings.NAME;
        DESCRIPTION = cardStrings.DESCRIPTION;
        EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    }
}
