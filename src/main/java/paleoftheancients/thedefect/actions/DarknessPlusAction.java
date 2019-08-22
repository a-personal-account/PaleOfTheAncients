package paleoftheancients.thedefect.actions;

import paleoftheancients.thedefect.monsters.orbs.AbstractBossOrb;
import paleoftheancients.thedefect.monsters.orbs.Dark;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;

public class DarknessPlusAction extends AbstractGameAction {

    private ArrayList<AbstractBossOrb> orbs;

    public DarknessPlusAction(ArrayList<AbstractBossOrb> orbs) {
        this.orbs = orbs;
    }

    public void update() {
        this.isDone = true;

        for(final AbstractBossOrb abo : orbs) {
            if(abo.id == Dark.ID) {
                abo.passive(AbstractDungeon.player);
            }
        }
    }
}
