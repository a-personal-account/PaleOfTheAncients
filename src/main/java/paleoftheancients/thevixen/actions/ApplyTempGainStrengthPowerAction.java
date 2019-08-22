package thevixen.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class ApplyTempGainStrengthPowerAction extends AbstractGameAction {

    public ApplyTempGainStrengthPowerAction(AbstractCreature target, AbstractCreature source, int amount) {
        this.target = target;
        this.source = source;
        this.amount = amount;
        this.duration = Settings.ACTION_DUR_FAST;
        this.startDuration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if(this.duration == this.startDuration) {
            /* Apply the GainStrengthPower, but cancel it out with LoseStrengthPower. */
            int toLose = this.amount;

            if(this.target instanceof AbstractPlayer || !this.target.hasPower(ArtifactPower.POWER_ID)) {
                AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(
                        this.target, this.source, new StrengthPower(this.target, toLose), toLose));
            }

            if (this.target.hasPower(GainStrengthPower.POWER_ID)) {
                int mpower = this.target.getPower(GainStrengthPower.POWER_ID).amount;
                if (toLose >= mpower) {
                    toLose -= mpower;
                    AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(
                            this.target, this.source, GainStrengthPower.POWER_ID));

                } else {
                    AbstractDungeon.actionManager.addToTop(new ReducePowerAction(
                            this.target, this.source, GainStrengthPower.POWER_ID, toLose));
                    toLose = 0;
                }
            }
            if (toLose > 0) {
                AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(
                        this.target, this.source, new LoseStrengthPower(this.target, toLose), toLose, true, AbstractGameAction.AttackEffect.NONE));
            }

            this.isDone = true;
        }

        this.tickDuration();
    }
}
