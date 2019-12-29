package paleoftheancients.collector.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import paleoftheancients.PaleMod;


public class VulnNextTurnPower extends AbstractPower {
    public static final String POWER_ID = PaleMod.makeID("VulnNextTurnPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(VulnerablePower.POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public VulnNextTurnPower(AbstractCreature owner, int amount) {
        name = VulnerablePower.NAME;
        ID = POWER_ID;

        this.owner = owner;

        type = PowerType.DEBUFF;
        isTurnBased = false;

        this.amount = amount;

        this.loadRegion("vulnerable");

        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = "Thanks Shanakor <3";
    }

    @Override
    public void atStartOfTurn() {
        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(owner, owner, this));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(owner, owner, new VulnerablePower(owner, amount, false), amount));
    }
}