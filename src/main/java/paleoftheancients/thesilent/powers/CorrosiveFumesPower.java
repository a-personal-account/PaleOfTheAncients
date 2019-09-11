package paleoftheancients.thesilent.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import paleoftheancients.PaleMod;


public class CorrosiveFumesPower extends AbstractSilentPower {
    public static final String POWER_ID = PaleMod.makeID("CorrosiveFumesPower");
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;

    public static PowerType POWER_TYPE = PowerType.BUFF;

    public CorrosiveFumesPower(AbstractCreature owner, int amount) {
        super();
        this.owner = owner;
        this.amount = amount;
        this.isTurnBased = true;

        this.name = NAME;
        this.ID = POWER_ID;
        this.type = POWER_TYPE;

        this.loadRegion("fumes");
        updateDescription();
    }

    @Override
    public void atEndOfRound() {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this.owner, new CorrosionPower(AbstractDungeon.player, this.amount), this.amount));
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    }
}
