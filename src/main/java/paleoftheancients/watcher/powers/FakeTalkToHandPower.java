package paleoftheancients.watcher.powers;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.powers.watcher.BlockReturnPower;

public class FakeTalkToHandPower extends BlockReturnPower {
    private boolean sameTurn = true;

    public FakeTalkToHandPower(AbstractCreature owner, int amount) {
        super(owner, amount);
    }

    public int onAttacked(DamageInfo info, int damageAmount) {
        if (info.type != DamageInfo.DamageType.THORNS && info.type != DamageInfo.DamageType.HP_LOSS && info.owner != null && info.owner != this.owner) {
            this.flash();
            this.addToTop(new GainBlockAction(info.owner, this.amount, Settings.FAST_MODE));
        }

        return damageAmount;
    }
}