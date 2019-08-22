package paleoftheancients.theshowman.cards;

import paleoftheancients.PaleMod;
import paleoftheancients.theshowman.TheShowman;
import paleoftheancients.theshowman.powers.ColumbifyPower;
import com.megacrit.cardcrawl.actions.animations.SetAnimationAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class BeSad extends ByrdCard {
    public static final String ID = PaleMod.makeID("BeSad");
    public static final String NAME;
    public static final String DESCRIPTION;
    public static final String IMG_PATH = TheShowman.getResourcePath("cards/besad.png");

    private static final CardStrings cardStrings;

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    private static final int COST = 0;

    public BeSad() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, CardColor.CURSE, RARITY, TARGET);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if(ColumbifyPower.byrd != null) {
            AbstractDungeon.actionManager.addToBottom(new SetAnimationAction(ColumbifyPower.byrd, "head_lift"));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new BeSad();
    }

    @Override
    public void upgrade() {
        if(!this.upgraded) {
            this.upgradeName();
        }
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        NAME = cardStrings.NAME;
        DESCRIPTION = cardStrings.DESCRIPTION;
    }
}
