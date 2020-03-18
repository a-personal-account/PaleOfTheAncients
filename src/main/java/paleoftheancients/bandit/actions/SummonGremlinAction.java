package paleoftheancients.bandit.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.exordium.*;
import paleoftheancients.bandit.board.spaces.asymmetrical.GremlinSpace;

public class SummonGremlinAction extends AbstractGameAction {
    public void update() {
        this.isDone = true;

        int count = 0;
        for(final AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if(!mo.isDeadOrEscaped()) {
                count++;
            }
        }
        if(count >= GremlinSpace.ENEMYLIMIT) {
            return;//Don't overload the room.
        }

        float offsetX = leftMostCoordinate();
        float offsetY = 0.0F;
        for (AbstractMonster m : (AbstractDungeon.getCurrRoom()).monsters.monsters) {
            offsetY = Math.min(m.drawY, offsetY);
        }
        int bruh = AbstractDungeon.cardRandomRng.random(4);
        AbstractMonster mo = null;
        switch (bruh) {
            case 0:
                mo = new GremlinFat(0, offsetY - 10.0F * Settings.scale);
                break;
            case 1:
                mo = new GremlinThief(0, offsetY - 10.0F * Settings.scale);
                break;
            case 2:
                mo = new GremlinTsundere(0, offsetY - 10.0F * Settings.scale);
                break;
            case 3:
                mo = new GremlinWarrior(0, offsetY - 10.0F * Settings.scale);
                break;
            case 4:
                mo = new GremlinWizard(0, offsetY - 10.0F * Settings.scale);
                break;
        }
        AbstractDungeon.actionManager.addToTop(new SpawnMonsterAutoPositionAction(mo, false, offsetX - 150.0F * Settings.scale));
    }
    public static float leftMostCoordinate() {
        float offsetX = 0F;
        for (AbstractMonster m : (AbstractDungeon.getCurrRoom()).monsters.monsters) {
            offsetX = Math.min((m.drawX - Settings.WIDTH * 0.75F) / Settings.scale, offsetX);
        }
        return offsetX;
    }
}