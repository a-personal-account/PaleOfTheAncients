package paleoftheancients.theshowman.bosscards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import paleoftheancients.PaleMod;
import paleoftheancients.theshowman.misc.ShowmanDazed;
import paleoftheancients.theshowman.monsters.TheShowmanBoss;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import paleoftheancients.theshowman.vfx.VanDeGraaffVFX;

import java.util.ArrayList;

public class VanDeGraaffsRevenge extends AbstractShowmanCard {
    public static final String ID = PaleMod.makeID("VanDeGraaffsRevenge");
    public static final String NAME;
    public static final String DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "bosscards/VanDeGraaffsRevenge.png";

    private static final CardStrings cardStrings;

    private static final CardType TYPE = CardType.ATTACK;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;

    private static final int COST = 3;

    public VanDeGraaffsRevenge(TheShowmanBoss owner) {
        super(ID, NAME, assetPath(IMG_PATH), COST, DESCRIPTION, TYPE, RARITY, TARGET, owner, AbstractMonster.Intent.ATTACK_DEBUFF);
        this.baseDamage = 50;
        this.baseMagicNumber = this.magicNumber = 1;
    }

    @Override
    public void use(ArrayList<AbstractCard> availableExhaustCards, AbstractPlayer p, AbstractMonster m) {
        for(int i = 1; i <= 5; ++i) {
            AbstractDungeon.actionManager.addToBottom(new VFXAction(new VanDeGraaffVFX(m.hb.cX, m.hb.cY, m.hb.cX + (float)i * 200.0F - 400.0F, m.hb.cY + 300.0F, (float)(1.0D + (double)i * -0.1D), p)));
        }

        AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_HEAVY));

        for(int i = 0; i < this.magicNumber; i++) {
            this.owner.soulGroup.shuffleNewlyCreatedCard(new ShowmanDazed(this.owner));
            this.owner.exhaustpile.group.add(new ShowmanDazed(this.owner));
            this.owner.soulGroup.discard(new ShowmanDazed(this.owner));
            AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(new Dazed(), 1, true, true));
            AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(new Dazed(), 1));
            AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(new Dazed(), 1));
        }
    }

    @Override
    public void upgrade() {
        if(!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(1);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new VanDeGraaffsRevenge(this.owner);
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        NAME = cardStrings.NAME;
        DESCRIPTION = cardStrings.DESCRIPTION;
        UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    }
}
