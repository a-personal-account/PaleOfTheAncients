package paleoftheancients.watcher.powers;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.watcher.LikeWaterPower;
import com.megacrit.cardcrawl.stances.CalmStance;
import paleoftheancients.watcher.monsters.TheWatcher;

public class FakeLikeWaterPower extends LikeWaterPower {
    public FakeLikeWaterPower(AbstractCreature owner, int newAmount) {
        super(owner, newAmount);
    }

    @Override
    public void atEndOfTurnPreEndTurnCards(boolean isPlayer) {}

    @Override
    public void atEndOfRound() {
        TheWatcher p = (TheWatcher)this.owner;
        if (p.stance.ID.equals(CalmStance.STANCE_ID)) {
            this.flash();
            this.addToBot(new GainBlockAction(this.owner, this.owner, this.amount));
        }
    }
}
