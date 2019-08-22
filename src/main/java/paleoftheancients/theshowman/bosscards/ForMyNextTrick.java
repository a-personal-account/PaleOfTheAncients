package paleoftheancients.theshowman.bosscards;

import paleoftheancients.PaleMod;
import paleoftheancients.theshowman.monsters.TheShowmanBoss;
import paleoftheancients.theshowman.powers.ForMyNextTrickPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;

public class ForMyNextTrick extends AbstractShowmanExhaustingCard {
    public static final String ID = PaleMod.makeID("ForMyNextTrick");
    public static final String NAME;
    public static final String DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "bosscards/Skill.png";

    private static final CardStrings cardStrings;

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.NONE;

    private static final int COST = 1;

    public ForMyNextTrick(TheShowmanBoss owner) {
        super(ID, NAME, assetPath(IMG_PATH), COST, DESCRIPTION, TYPE, RARITY, TARGET, owner, AbstractMonster.Intent.STRONG_DEBUFF);
        this.baseMagicNumber = this.magicNumber = 2;
    }

    @Override
    public void use(ArrayList<AbstractCard> availableExhaustCards, AbstractPlayer p, AbstractMonster m) {
        this.highestExhaustPriority(availableExhaustCards);
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, m, new ForMyNextTrickPower(this.owner, this.toExhaust, this.magicNumber)));
    }

    @Override
    public int getPriority(ArrayList<AbstractCard> availableExhaustCards, int availableEnergy, int byrdHits) {
        return this.highestExhaustPriority(availableExhaustCards) * this.magicNumber / 2;
    }

    @Override
    public AbstractCard makeCopy() {
        return new ForMyNextTrick(this.owner);
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        NAME = cardStrings.NAME;
        DESCRIPTION = cardStrings.DESCRIPTION;
        UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    }
}
