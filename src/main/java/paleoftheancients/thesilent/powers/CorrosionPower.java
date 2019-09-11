package paleoftheancients.thesilent.powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import paleoftheancients.PaleMod;


public class CorrosionPower extends AbstractSilentPower {
    public static final String POWER_ID = PaleMod.makeID("CorrosionPower");
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;

    public static PowerType POWER_TYPE = PowerType.DEBUFF;

    public CorrosionPower(AbstractCreature owner, int amount) {
        super();
        this.owner = owner;
        this.amount = amount;
        this.isTurnBased = true;

        this.name = NAME;
        this.ID = POWER_ID;
        this.type = POWER_TYPE;

        this.loadRegion("poison");
        updateDescription();
    }


    @Override
    public void atEndOfTurn(boolean isPlayer) {
        AbstractDungeon.actionManager.addToBottom(new DamageAction(this.owner, new DamageInfo(this.owner, this.amount, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.POISON, true));
        AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(this.owner, this.owner, this.POWER_ID, 2));
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
