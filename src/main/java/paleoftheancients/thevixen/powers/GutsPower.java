package paleoftheancients.thevixen.powers;

import paleoftheancients.PaleMod;
import paleoftheancients.thevixen.actions.ReduceDebuffDurationAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;


public class GutsPower extends AbstractTheVixenPower {
    public static final String POWER_ID = PaleMod.makeID("GutsPower");
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;

    public static PowerType POWER_TYPE = PowerType.BUFF;
    public static final String IMG = "guts.png";

    public static int totalAmount = 0;

    public GutsPower(AbstractCreature owner, int amount) {
        super(IMG);
        this.owner = owner;
        this.amount = amount;
        this.isTurnBased = false;

        this.name = NAME;
        this.ID = POWER_ID;
        this.type = POWER_TYPE;

        updateDescription();
    }


    public void atEndOfTurn(boolean isPlayer) {
        this.flash();

        int block = ReduceDebuffDurationAction.getCumulativeDuration(this.owner);
        block *= 2;

        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this.owner, this.owner, block));
    }

    @Override
    public void onVictory() {
        totalAmount = 0;
    }

    @Override
    public void updateDescription() {
        this.description = (DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1]);
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    }
}
