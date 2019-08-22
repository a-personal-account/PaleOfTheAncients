package paleoftheancients.thevixen.cards;

import paleoftheancients.thevixen.powers.ClearSkyPower;
import paleoftheancients.thevixen.powers.SunnyDayPower;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public abstract class AbstractVixenCard {

    public static void removeSunny(AbstractCreature p, int amount) {
        if(!p.hasPower(ClearSkyPower.POWER_ID)) {
            if (amount < 0) {
                AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(p, p, SunnyDayPower.POWER_ID));
            } else {
                p.getPower(SunnyDayPower.POWER_ID).flash();
                AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(p, p, SunnyDayPower.POWER_ID, amount));
            }
        }
    }
    public static void removeSunny(AbstractCreature p) {
        removeSunny(p, 1);
    }
}
