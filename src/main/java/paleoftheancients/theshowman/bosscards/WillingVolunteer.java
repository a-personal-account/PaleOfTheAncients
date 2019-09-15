package paleoftheancients.theshowman.bosscards;

import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.NonStackablePower;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import paleoftheancients.PaleMod;
import paleoftheancients.theshowman.monsters.TheShowmanBoss;
import paleoftheancients.theshowman.powers.VolunteeringPower;
import paleoftheancients.theshowman.vfx.WillingVolunteerVFX;

import java.util.ArrayList;

public class WillingVolunteer extends AbstractShowmanCard implements NonStackablePower {
    public static final String ID = PaleMod.makeID("WillingVolunteer");
    public static final String NAME;
    public static final String DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "bosscards/WillingVolunteer.png";

    private static final CardStrings cardStrings;

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;

    private static final int COST = 2;

    public WillingVolunteer(TheShowmanBoss owner) {
        super(ID, NAME, assetPath(IMG_PATH), COST, DESCRIPTION, TYPE, RARITY, TARGET, owner, AbstractMonster.Intent.DEBUFF);
        this.baseMagicNumber = this.magicNumber = 1;
    }

    @Override
    public void use(ArrayList<AbstractCard> availableExhaustCards, AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new VFXAction(new WillingVolunteerVFX(p), 2F));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, m, new VolunteeringPower(p, this.magicNumber), this.magicNumber));
    }

    @Override
    public int getPriority(ArrayList<AbstractCard> availableExhaustCards, int availableEnergy, int byrdHits) {
        return 15;
    }

    @Override
    public void upgrade() {
        if(!this.upgraded) {
            this.upgradeName();
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new WillingVolunteer(this.owner);
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        NAME = cardStrings.NAME;
        DESCRIPTION = cardStrings.DESCRIPTION;
        UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    }
}
