package paleoftheancients.bandit.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.exordium.GremlinNob;
import paleoftheancients.bandit.monsters.TheBandit;

public class SummonNobAction extends AbstractGameAction {
    public void update() {
        this.isDone = true;
        for(final AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if(mo.id.equals(GremlinNob.ID) && !mo.isDeadOrEscaped()) {
                return;
            }
        }
        AbstractMonster bandit = AbstractDungeon.getCurrRoom().monsters.getMonster(TheBandit.ID);
        this.addToTop(new SpawnMonsterAutoPositionAction(new GremlinNob(0F, -30F), false, bandit.hb.x + bandit.hb.width, 1));
    }
}