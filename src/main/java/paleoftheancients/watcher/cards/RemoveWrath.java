package paleoftheancients.watcher.cards;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ChangeStateAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.stances.CalmStance;
import paleoftheancients.PaleMod;
import paleoftheancients.vfx.BlurryLens.BlurryTopPanelScrewer;
import paleoftheancients.watcher.monsters.TheWatcher;
import paleoftheancients.watcher.powers.FakeBlasphemyPower;
import paleoftheancients.watcher.powers.HandSpacePower;

public class RemoveWrath extends AbstractCard {
    public static final String ID = PaleMod.makeID("RemoveWrath");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    private boolean primed = false;

    public RemoveWrath() {
        super(ID, BlurryTopPanelScrewer.shuffleString(cardStrings.NAME), "curse/writhe", 1, cardStrings.DESCRIPTION, CardType.SKILL, CardColor.PURPLE, CardRarity.RARE, CardTarget.NONE);
        this.purgeOnUse = true;
        this.selfRetain = true;
        this.portrait = ((TextureAtlas) ReflectionHacks.getPrivateStatic(AbstractCard.class, "oldCardAtlas")).findRegion("curse/writhe");
    }

    public void prime() {
        this.primed = true;
        this.name = cardStrings.NAME;
        this.initializeTitle();
        this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
        this.initializeDescription();
        this.portrait = ((TextureAtlas) ReflectionHacks.getPrivateStatic(AbstractCard.class, "cardAtlas")).findRegion("purple/skill/tranquility");
        this.target = CardTarget.ENEMY;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        //Gotta prevent duplication shenanigans.
        if(primed && m instanceof TheWatcher) {
            this.addToBot(new ChangeStateAction(m, CalmStance.STANCE_ID));
            this.addToBot(new RemoveSpecificPowerAction(m, p, FakeBlasphemyPower.POWER_ID));
            this.addToBot(new RollMoveAction(m));
            this.addToBot(new AbstractGameAction() {
                @Override
                public void update() {
                    this.isDone = true;
                    m.createIntent();
                    ((TheWatcher) m).incrementPhaseChangeCounter();
                }
            });
            this.addToBot(new ReducePowerAction(p, p, HandSpacePower.POWER_ID, 1));
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new RemoveWrath();
    }
    @Override
    public AbstractCard makeStatEquivalentCopy() {
        return this.makeCopy();
    }
    @Override
    public AbstractCard makeSameInstanceOf() {
        return this.makeCopy();
    }

    @Override
    public void upgrade() {
        if(!this.upgraded) {
            this.upgradeName();
            this.updateCost(-1);
        }
    }
}
