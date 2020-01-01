package paleoftheancients.reimu.monsters;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;

public abstract class ReimuRazPhase extends ReimuPhase {
    protected ArrayList<Byte> cycletracker;

    public ReimuRazPhase() {
        cycletracker = new ArrayList<>();
        resetCycleTracker();
    }

    @Override
    public void getMove(Reimu reimu, int num) {
        getMoveFromCycletracker(reimu);
    }

    public void getMoveFromCycletracker(Reimu reimu) {
        getMoveFromCycletracker(reimu, 1);
    }
    public void getMoveFromCycletracker(Reimu reimu, int resetAt) {
        int index;
        if(cycletracker.size() <= resetAt) {
            cycletracker.clear();
            resetCycleTracker();
            if(reimu.rui.bombs > 0) {
                reimu.startSpellAnimation(false);
                setBombIntent(reimu);
                return;
            }
        }
        if(cycletracker.size() == 1) {
            index = 0;
        } else {
            index = AbstractDungeon.aiRng.random(cycletracker.size() - 1);
        }
        reimu.setMoveShortcut(cycletracker.get(index));
        cycletracker.remove(index);
    }
    protected abstract void setBombIntent(Reimu reimu);
    protected abstract void resetCycleTracker();
}
