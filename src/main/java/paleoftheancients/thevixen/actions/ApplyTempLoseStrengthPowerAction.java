package thevixen.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class ApplyTempLoseStrengthPowerAction extends AbstractGameAction {

    public ApplyTempLoseStrengthPowerAction(AbstractCreature target, AbstractCreature source, int amount) {
        this.target = target;
        this.source = source;
        this.amount = amount;
        this.duration = Settings.ACTION_DUR_FAST;
        this.startDuration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {

        if(this.amount > 0) {
            boolean artifact = this.target.hasPower(ArtifactPower.POWER_ID);
            if(!artifact) {
                AbstractDungeon.actionManager.addToTop(
                        new ApplyPowerAction(this.target, this.source, new StrengthPower(this.target, -this.amount), -this.amount, true, AttackEffect.NONE));
            }



            /* Apply the GainStrengthPower, but cancel it out with LoseStrengthPower. */
            int toGain = this.amount;
            if (this.target.hasPower(LoseStrengthPower.POWER_ID)) {
                int mpower = this.target.getPower(LoseStrengthPower.POWER_ID).amount;
                if (toGain >= mpower) {
                    toGain -= mpower;
                    AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(
                            this.target, this.source, LoseStrengthPower.POWER_ID));

                } else {
                    AbstractDungeon.actionManager.addToTop(new ReducePowerAction(
                            this.target, this.source, LoseStrengthPower.POWER_ID, toGain));
                    toGain = 0;
                }
            }

            if(toGain <= 0 && artifact) {
                toGain = 1;
            }
            if (toGain > 0) {
                AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(
                        this.target, this.source, new GainStrengthPower(this.target, toGain), toGain, true, AttackEffect.NONE));
            }
        }
        this.isDone = true;
    }
}
