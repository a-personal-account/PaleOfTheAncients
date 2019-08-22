package paleoftheancients.theshowman.bosscards;

import paleoftheancients.PaleMod;
import paleoftheancients.theshowman.monsters.TheShowmanBoss;
import paleoftheancients.theshowman.powers.EnergizedShowmanPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class SybilFlourish extends AbstractShowmanCard {
    public static final String ID = PaleMod.makeID("SybilFlourish");
    public static final String NAME;
    public static final String DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "bosscards/Skill.png";

    private static final CardStrings cardStrings;

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    private static final int COST = 2;

    public SybilFlourish(TheShowmanBoss owner) {
        super(ID, NAME, assetPath(IMG_PATH), COST, DESCRIPTION, TYPE, RARITY, TARGET, owner, AbstractMonster.Intent.DEFEND);
        this.baseBlock = 25;
        this.baseMagicNumber = this.magicNumber = 2;
        this.exhaustTrigger = true;
    }

    @Override
    public void triggerOnExhaust() {
        this.owner.tempenergy += this.magicNumber;
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.owner, this.owner, new EnergizedShowmanPower(this.owner, this.magicNumber), this.magicNumber));
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        this.exhaustPriority *= 2;
    }

    @Override
    public AbstractCard makeCopy() {
        return new SybilFlourish(this.owner);
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        NAME = cardStrings.NAME;
        DESCRIPTION = cardStrings.DESCRIPTION;
        UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    }
}
