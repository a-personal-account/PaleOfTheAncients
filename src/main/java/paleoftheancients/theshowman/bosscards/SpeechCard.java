package paleoftheancients.theshowman.bosscards;

import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.SpawnMonsterAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import paleoftheancients.PaleMod;
import paleoftheancients.theshowman.monsters.ShowmanStage;
import paleoftheancients.theshowman.monsters.TheShowmanBoss;

import java.util.ArrayList;
import java.util.Collections;

public class SpeechCard extends AbstractShowmanCard {
    public static final String ID = PaleMod.makeID("SpeechCard");
    public static final String NAME;
    public static final String DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION;
    public static final String IMG_PATH = "bosscards/Skill.png";

    private static final CardStrings cardStrings;

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;

    private static final int COST = 0;

    public SpeechCard(TheShowmanBoss owner) {
        super(ID, NAME, assetPath(IMG_PATH), COST, DESCRIPTION, TYPE, RARITY, TARGET, owner, AbstractMonster.Intent.UNKNOWN);
        this.purgeOnUse = true;
    }

    @Override
    public void use(ArrayList<AbstractCard> availableExhaustCards, AbstractPlayer p, AbstractMonster m) {

        this.owner.drawpile.addToTop(new Columbify(this.owner));
        this.owner.drawpile.addToTop(new CrashingLights(this.owner));
        this.owner.drawpile.addToTop(new CrashingLights(this.owner));
        this.owner.drawpile.addToTop(new DapperFlourish(this.owner));
        this.owner.drawpile.addToTop(new ExaggeratedArmSweeps(this.owner));
        this.owner.drawpile.addToTop(new ForMyNextTrick(this.owner));
        this.owner.drawpile.addToTop(new IsThisYourCard(this.owner));
        this.owner.drawpile.addToTop(new ReappearingTrick(this.owner));
        this.owner.drawpile.addToTop(new SybilFlourish(this.owner));
        this.owner.drawpile.addToTop(new WillingVolunteer(this.owner));
        Collections.shuffle(this.owner.drawpile.group, AbstractDungeon.monsterRng.random);

        this.owner.stage = new ShowmanStage(100, this.owner);
        AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(this.owner.stage, false, AbstractDungeon.getCurrRoom().monsters.monsters.size()));

        this.screwYourFastMode();
        AbstractDungeon.actionManager.addToBottom(new TalkAction(this.owner, EXTENDED_DESCRIPTION[0], 2.0F, 2.0F));
        this.screwYourFastMode();
        int i = (int)(Math.random() * 9.0D) + 1;
        AbstractDungeon.actionManager.addToBottom(new TalkAction(this.owner, EXTENDED_DESCRIPTION[i], 2.0F, 2.0F));
        this.screwYourFastMode();
        i = (int)(Math.random() * 9.0D) + 10;
        AbstractDungeon.actionManager.addToBottom(new TalkAction(this.owner, EXTENDED_DESCRIPTION[i], 2.0F, 2.0F));
        this.screwYourFastMode();
        i = (int)(Math.random() * 9.0D) + 19;
        if (i == 21) {
            i = (int)(Math.random() * 9.0D) + 19;
        }

        AbstractDungeon.actionManager.addToBottom(new TalkAction(this.owner, EXTENDED_DESCRIPTION[i], 2.0F, 2.0F));
    }

    private void screwYourFastMode() {
        for(int i = 0; i < 5; ++i) {
            AbstractDungeon.actionManager.addToBottom(new WaitAction(0.1F));
        }
    }

    @Override
    public int getPriority(ArrayList<AbstractCard> availableExhaustCards, int availableEnergy, int byrdHits) {
        return 1;
    }

    @Override
    public void upgrade() {
        if(!this.upgraded) {
            this.upgradeName();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new SpeechCard(this.owner);
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        NAME = cardStrings.NAME;
        DESCRIPTION = cardStrings.DESCRIPTION;
        EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
    }
}
