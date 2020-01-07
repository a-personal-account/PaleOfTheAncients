package paleoftheancients.thedefect.powers;

import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import paleoftheancients.PaleMod;

public class GrantStrengthNextTurnPower extends AbstractPower implements InvisiblePower {

    private int steps;

    public GrantStrengthNextTurnPower(AbstractCreature owner, int newAmount) {
        this.name = "";
        this.ID = PaleMod.makeID("Flex");
        this.owner = owner;
        this.amount = newAmount;
        this.type = PowerType.BUFF;// 21
        this.description = "";
        this.loadRegion("flex");
        this.priority++;
        steps = 1;
    }

    @Override
    public void stackPower(int amnt) {
        super.stackPower(amnt);
        steps++;
    }

    public void atEndOfTurn(boolean isPlayer) {
        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, this));
        int stepsize = this.amount / steps;
        for(int i = 0; i < steps; i++) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.owner, this.owner, new StrengthPower(this.owner, stepsize), stepsize));
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.owner, this.owner, new LoseStrengthPower(this.owner, stepsize), stepsize));
        }
    }
}
