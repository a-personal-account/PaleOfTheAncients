package paleoftheancients.thevixen.cards.status;

import basemod.BaseMod;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.RunicPyramid;
import paleoftheancients.PaleMod;
import paleoftheancients.thevixen.actions.SetPlayerBurnAction;

public class BossBurn extends AbstractCard {
    public static final String ID = PaleMod.makeID("BossBurn");
    public static final String NAME;
    public static final String DESCRIPTION;
    public static final String IMG_PATH = "status/burn";

    private static final CardStrings cardStrings;

    private static final CardType TYPE = CardType.STATUS;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.NONE;

    private static final int COST = 2;

    public BossBurn() {
        this(0);
    }
    public BossBurn(int timesUpgraded) {
        super(ID, NAME, IMG_PATH, COST, getBaseDescription(), TYPE, CardColor.CURSE, RARITY, TARGET);
        this.baseMagicNumber = this.magicNumber = 1;
        this.exhaust = true;
        if(this.timesUpgraded > 0) {
            if(this.timesUpgraded > 1) {
                this.upgradeMagicNumber(timesUpgraded - 1);
                this.timesUpgraded = timesUpgraded - 1;
            }
            this.upgrade();
        }
    }

    @Override
    public AbstractCard makeStatEquivalentCopy() {
        //SetPlayerBurnAction.addToBottom();
        return super.makeStatEquivalentCopy();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if(this.upgraded) {
            this.downgrade();
        } else {
            this.exhaust = true;
            SetPlayerBurnAction.addToBottom();
        }
    }

    @Override
    public void triggerOnEndOfPlayerTurn() {
        super.triggerOnEndOfPlayerTurn();
        if(AbstractDungeon.player.hasRelic(RunicPyramid.ID)) {
            AbstractDungeon.player.hand.moveToDiscardPile(this);
        }
    }

    @Override
    public void triggerOnExhaust() {
        if(this.upgraded) {
            final AbstractCard card = this;
            AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
                @Override
                public void update() {
                    this.isDone = true;
                    AbstractDungeon.player.exhaustPile.removeCard(card);
                    AbstractCard copy = card.makeCopy();
                    for(int i = card.timesUpgraded - 1; i > 0; i--) {
                        copy.upgrade();
                    }
                    AbstractDungeon.player.discardPile.addToTop(copy);
                    SetPlayerBurnAction.addToBottom();
                }
            });
        }
        SetPlayerBurnAction.addToBottom();
    }

    @Override
    public AbstractCard makeCopy() {
        return new BossBurn();
    }

    @Override
    public void upgrade() {
        this.upgradeMagicNumber(1);
        ++this.timesUpgraded;
        this.name = NAME + "+" + this.timesUpgraded;
        this.initializeTitle();
        if(!this.upgraded) {
            this.rawDescription = getUpgradedDescription();
            this.initializeDescription();
            this.upgraded = true;
        }
        this.exhaust = false;
        SetPlayerBurnAction.addToBottom();
    }

    public void downgrade() {
        this.upgradeMagicNumber(-1);
        if(this.timesUpgraded-- > 0) {
            if(this.timesUpgraded > 0) {
                this.name = NAME + "+" + this.timesUpgraded;
            } else {
                this.name = NAME;

                this.rawDescription = getBaseDescription();
                this.initializeDescription();
                this.upgraded = false;
            }
            this.initializeTitle();
        }
        SetPlayerBurnAction.addToBottom(this.timesUpgraded + 1);
    }

    private static String getBaseDescription() {
        return cardStrings.EXTENDED_DESCRIPTION[0] + DESCRIPTION;
    }
    private static String getUpgradedDescription() {
        return cardStrings.EXTENDED_DESCRIPTION[1] + DESCRIPTION + cardStrings.EXTENDED_DESCRIPTION[2];
    }

    @Override
    public boolean canUpgrade() {
        return false;
    }

    public void triggerWhenDrawn() {
        if(!AbstractDungeon.player.hasPower("No Draw")) {
            if (AbstractDungeon.player.hasPower("Evolve")) {
                AbstractDungeon.player.getPower("Evolve").flash();
                AbstractDungeon.actionManager.addToBottom(new DrawCardAction(AbstractDungeon.player, AbstractDungeon.player.getPower("Evolve").amount));
            } else {
                AbstractDungeon.actionManager.addToBottom(new DrawCardAction(AbstractDungeon.player, 1));
            }
        }
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        NAME = cardStrings.NAME;
        DESCRIPTION = cardStrings.DESCRIPTION;
    }
}
