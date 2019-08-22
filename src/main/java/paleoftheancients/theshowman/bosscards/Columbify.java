package paleoftheancients.theshowman.bosscards;

import paleoftheancients.PaleMod;
import paleoftheancients.theshowman.monsters.TheShowmanBoss;
import paleoftheancients.theshowman.powers.ColumbifyPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;

public class Columbify extends AbstractShowmanCard {
    public static final String ID = PaleMod.makeID("Columbify");
    public static final String NAME;
    public static final String DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "bosscards/Skill.png";

    private static final CardStrings cardStrings;

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;

    private static final int COST = 3;
    private int usedLast;

    public Columbify(TheShowmanBoss owner) {
        super(ID, NAME, assetPath(IMG_PATH), COST, DESCRIPTION, TYPE, RARITY, TARGET, owner, AbstractMonster.Intent.STRONG_DEBUFF);
        this.usedLast = 2;
    }

    @Override
    public void use(ArrayList<AbstractCard> availableExhaustCards, AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, m, new ColumbifyPower(p)));
        this.usedLast = 0;
    }

    @Override
    public void upgrade() {
        if(!this.upgraded) {
            this.upgradeName();
            this.upgradeBaseCost(2);
        }
    }

    @Override
    public int getPriority(ArrayList<AbstractCard> availableExhaustCards, int availableEnergy, int byrdHits) {
        int priority;
        if(AbstractDungeon.player.hasPower(ColumbifyPower.POWER_ID)) {
            priority = -1;
        } else {
            priority = this.usedLast++ * 10;
        }
        return priority;
    }

    @Override
    public AbstractCard makeCopy() {
        return new Columbify(this.owner);
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        NAME = cardStrings.NAME;
        DESCRIPTION = cardStrings.DESCRIPTION;
        UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    }
}
