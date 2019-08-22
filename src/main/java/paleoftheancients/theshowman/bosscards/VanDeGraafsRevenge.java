package paleoftheancients.theshowman.bosscards;

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

import java.util.ArrayList;

public class VanDeGraafsRevenge extends AbstractShowmanCard {
    public static final String ID = PaleMod.makeID("VanDeGraafsRevenge");
    public static final String NAME;
    public static final String DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "bosscards/Attack.png";

    private static final CardStrings cardStrings;

    private static final CardType TYPE = CardType.ATTACK;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;

    private static final int COST = 3;

    public VanDeGraafsRevenge(TheShowmanBoss owner) {
        super(ID, NAME, assetPath(IMG_PATH), COST, DESCRIPTION, TYPE, RARITY, TARGET, owner, AbstractMonster.Intent.ATTACK_DEBUFF);
        this.baseDamage = 50;
    }

    @Override
    public void use(ArrayList<AbstractCard> availableExhaustCards, AbstractPlayer p, AbstractMonster m) {
        super.use(availableExhaustCards, p, m);
        this.owner.soulGroup.shuffleNewlyCreatedCard(new ShowmanDazed(this.owner));
        this.owner.exhaustpile.group.add(new ShowmanDazed(this.owner));
        this.owner.soulGroup.discard(new ShowmanDazed(this.owner));
        AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(new Dazed(), 1, true, true));
        AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(new Dazed(), 1));
        AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(new Dazed(), 1));
    }

    @Override
    public AbstractCard makeCopy() {
        return new VanDeGraafsRevenge(this.owner);
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        NAME = cardStrings.NAME;
        DESCRIPTION = cardStrings.DESCRIPTION;
        UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    }
}
