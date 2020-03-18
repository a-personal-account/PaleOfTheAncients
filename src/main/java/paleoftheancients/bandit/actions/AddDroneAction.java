package paleoftheancients.bandit.actions;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;
import paleoftheancients.bandit.board.AbstractBoard;
import paleoftheancients.bandit.board.AbstractDrone;
import paleoftheancients.bandit.board.spaces.AbstractSpace;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class AddDroneAction extends AbstractGameAction {
    private final float DURATION = 0.1F;
    private int count;
    private AbstractBoard board;

    public AddDroneAction(AbstractBoard board, int count) {
        this.board = board;
        this.count = count;
        this.duration = DURATION;
    }

    @Override
    public void update() {
        if(this.duration == DURATION) {
            ArrayList<Integer> eligible = new ArrayList<>();
            Set<Integer> taken = new HashSet<>();
            for(final AbstractDrone d : board.getPieces()) {
                taken.add(d.position);
            }
            for (int i = 0; i < board.squareList.size(); i++) {
                if (!taken.contains(i)) {
                    eligible.add(i);
                }
            }

            if (!eligible.isEmpty()) {
                int index = eligible.get(AbstractDungeon.monsterRng.random(eligible.size() - 1));
                AbstractSpace s = board.squareList.get(index);
                this.addToTop(new VFXAction(new LightningEffect(s.hb.cX, s.hb.y), 0.0F));
                this.addToTop(new SFXAction("ORB_LIGHTNING_EVOKE", MathUtils.random(-0.1F, 0.1F)));
                board.droneList.add(new AbstractDrone(index, (int)s.hb.cX, (int)s.hb.cY));
            }
        }

        this.tickDuration();
    }
}
