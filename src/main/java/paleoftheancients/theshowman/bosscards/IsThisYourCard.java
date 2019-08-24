package paleoftheancients.theshowman.bosscards;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import paleoftheancients.PaleMod;
import paleoftheancients.theshowman.monsters.TheShowmanBoss;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import paleoftheancients.theshowman.vfx.PrepareSayCardVFX;
import paleoftheancients.theshowman.vfx.TossCardVFX;

import java.util.ArrayList;

public class IsThisYourCard extends AbstractShowmanCard {
    public static final String ID = PaleMod.makeID("IsThisYourCard");
    public static final String NAME;
    public static final String DESCRIPTION;
    public static final String IMG_PATH = "bosscards/IsThisYourCard.png";

    private static final CardStrings cardStrings;

    private static final CardType TYPE = CardType.ATTACK;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;

    private static final int COST = 2;

    public IsThisYourCard(TheShowmanBoss owner) {
        super(ID, NAME, assetPath(IMG_PATH), COST, DESCRIPTION, TYPE, RARITY, TARGET, owner, AbstractMonster.Intent.ATTACK);
        this.baseDamage = 7;
    }

    @Override
    public void applyPowers() {
        this.multiplier = this.owner.exhaustpile.size();
        super.applyPowers();
        this.magicNumber = this.multiplier;
    }

    @Override
    public void use(ArrayList<AbstractCard> availableExhaustCards, AbstractPlayer p, AbstractMonster m) {

        int specifiedCard = MathUtils.random(1, 11);
        AbstractDungeon.actionManager.addToBottom(new VFXAction(new PrepareSayCardVFX(p, specifiedCard)));

        for(int i = 0; i < this.owner.exhaustpile.size(); ++i) {
            if (i == this.owner.exhaustpile.size() - 1) {
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new TossCardVFX(m.hb.cX, m.hb.cY, p, this.damage, specifiedCard), 0.1F));
            } else {
                int notSpecified;
                for(notSpecified = specifiedCard; notSpecified == specifiedCard; notSpecified = MathUtils.random(1, 11)) {
                }

                AbstractDungeon.actionManager.addToBottom(new VFXAction(new TossCardVFX(m.hb.cX, m.hb.cY, p, this.damage, notSpecified), 0.1F));
            }

            AbstractDungeon.actionManager.addToBottom(new DamageAction(p, new DamageInfo(m, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new IsThisYourCard(this.owner);
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        NAME = cardStrings.NAME;
        DESCRIPTION = cardStrings.DESCRIPTION;
    }
}
