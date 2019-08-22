package paleoftheancients.theshowman.bosscards;

import paleoftheancients.PaleMod;
import paleoftheancients.theshowman.monsters.TheShowmanBoss;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class GrossDisplay extends AbstractShowmanCard {
    public static final String ID = PaleMod.makeID("GrossDisplay");
    public static final String NAME;
    public static final String DESCRIPTION;
    public static final String IMG_PATH = "bosscards/Attack.png";

    private static final CardStrings cardStrings;

    private static final CardType TYPE = CardType.ATTACK;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;

    private static final int COST = 3;

    public GrossDisplay(TheShowmanBoss owner) {
        super(ID, NAME, assetPath(IMG_PATH), COST, DESCRIPTION, TYPE, RARITY, TARGET, owner, AbstractMonster.Intent.ATTACK);
        this.baseDamage = 12;
        this.baseMagicNumber = this.magicNumber = 12;
    }

    @Override
    public void applyPowers() {
        DamageInfo info = new DamageInfo(this.owner, this.baseDamage, this.damageTypeForTurn);
        info.applyPowers(this.owner, AbstractDungeon.player);
        this.damage = info.output - this.owner.drawpile.size();
        this.magicNumber = this.baseMagicNumber - this.owner.drawpile.size();
        this.multiplier = this.magicNumber;
        this.exhaustPriority = this.baseDamage * this.baseMagicNumber / 6;

        if(!this.owner.drawpile.isEmpty()) {
            this.isDamageModified = true;
            this.isMagicNumberModified = true;
        }
    }

    @Override
    public void upgrade() {
        if(!this.upgraded) {
            //this.upgradeName();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new GrossDisplay(this.owner);
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        NAME = cardStrings.NAME;
        DESCRIPTION = cardStrings.DESCRIPTION;
    }
}
