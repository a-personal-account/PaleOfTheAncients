package paleoftheancients.slimeboss.actions;

import paleoftheancients.slimeboss.powers.SlimeUnityPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class ReallocateSlimeUnityAction extends AbstractGameAction {
    public ReallocateSlimeUnityAction(AbstractMonster mo) {
        this.target = mo;
    }

    public void update() {
        SlimeUnityPower sup = (SlimeUnityPower)this.target.getPower(SlimeUnityPower.POWER_ID);
        if(sup != null) {
            sup.onAttacked(null, 0);
            for(final AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
                if(!mo.isDeadOrEscaped()) {
                    SlimeUnityPower minionsup = (SlimeUnityPower)mo.getPower(SlimeUnityPower.POWER_ID);
                    if(minionsup != null && minionsup.boss == this.target) {
                        minionsup.boss = sup.boss;
                    }
                }
            }
        }

        this.isDone = true;
    }
}
