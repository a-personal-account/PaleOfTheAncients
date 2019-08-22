package paleoftheancients.theshowman.powers;

import paleoftheancients.PaleMod;
import paleoftheancients.theshowman.monsters.TheShowmanBoss;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.NonStackablePower;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;


public class ForMyNextTrickPower extends AbstractShowmanPower implements NonStackablePower {
    public static final String POWER_ID = PaleMod.makeID("ForMyNextTrickPower");
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;

    public static PowerType POWER_TYPE = PowerType.DEBUFF;
    public static final String IMG = "columbify.png";

    AbstractCard card;

    public ForMyNextTrickPower(TheShowmanBoss owner, AbstractCard card, int amount) {
        super(IMG);
        this.owner = owner;

        this.amount = amount;
        this.card = card;

        this.name = NAME;
        this.ID = POWER_ID;
        this.type = POWER_TYPE;

        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1] + this.card.name + DESCRIPTIONS[2];
    }

    @Override
    public void atEndOfRound() {
        AbstractCard tmp;
        for(int i = 0; i < this.amount; i++) {
            tmp = card.makeStatEquivalentCopy();
            tmp.cost--;
            tmp.costForTurn--;
            ((TheShowmanBoss) this.owner).hand.addToBottom(tmp);
        }
        ((TheShowmanBoss) this.owner).resetOrbPositions();
        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, this));
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    }
}
