package paleoftheancients.collector.monsters;

import paleoftheancients.PaleMod;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.city.BronzeOrb;

public class BronzeOrbThing extends BronzeOrb {
    public static final String ID = PaleMod.makeID("BronzeOrbThing");

    public BronzeOrbThing(float x, float y, int count) {
        super(x, y, count);
        this.id = ID;
    }

    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case 2:
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(AbstractDungeon.getMonsters().getMonster(SpireWaifu.ID), this, 12));
                break;

            default:
                super.takeTurn();
        }
    }
}
