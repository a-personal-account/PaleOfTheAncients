package paleoftheancients.watcher.actions;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.PowerExpireTextEffect;

public class RemovePowerSilentlyAction extends RemoveSpecificPowerAction {
    private boolean firstCycle = true;
    public RemovePowerSilentlyAction(AbstractCreature target, AbstractCreature source, String powerToRemove) {
        super(target, source, powerToRemove);
    }

    public RemovePowerSilentlyAction(AbstractCreature target, AbstractCreature source, AbstractPower powerInstance) {
        super(target, source, powerInstance);
    }

    @Override
    public void update() {
        super.update();
        if (firstCycle) {
            AbstractGameEffect age;
            for (int i = AbstractDungeon.effectList.size() - 1; i >= 0; i--) {
                age = AbstractDungeon.effectList.get(i);
                if (age instanceof PowerExpireTextEffect) {
                    AbstractDungeon.effectList.remove(i);
                    break;
                }
            }
            firstCycle = false;
        }
    }
}