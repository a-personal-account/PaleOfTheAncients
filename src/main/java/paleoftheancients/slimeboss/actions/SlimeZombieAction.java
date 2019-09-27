package paleoftheancients.slimeboss.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import paleoftheancients.slimeboss.powers.SlimeUnityPower;

public class SlimeZombieAction extends AbstractGameAction {
    public SlimeZombieAction(AbstractMonster mo) {
        this.target = mo;
    }

    public void update() {
        for(final AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if(mo != this.target && (!mo.isDeadOrEscaped() || mo.halfDead)) {
                SlimeUnityPower sup = (SlimeUnityPower) mo.getPower(SlimeUnityPower.POWER_ID);
                if(sup != null && sup.boss == this.target) {
                    this.target.halfDead = true;
                    break;
                }
            }
        }

        this.isDone = true;
    }
}
