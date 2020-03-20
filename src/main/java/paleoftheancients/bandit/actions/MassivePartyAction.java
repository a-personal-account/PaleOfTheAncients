package paleoftheancients.bandit.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.PetalEffect;
import paleoftheancients.bandit.board.AbstractBoard;
import paleoftheancients.bandit.board.spaces.AbstractSpace;
import paleoftheancients.bandit.board.spaces.symmetrical.EmptySpace;

import java.lang.reflect.Field;

public class MassivePartyAction extends AbstractGameAction {
    private int count;
    private AbstractBoard board;
    private AbstractCreature actor;
    private Field targetField = null;

    public MassivePartyAction(AbstractCreature actor, AbstractBoard board, int count) {
        this.actor = actor;
        this.board = board;
        this.count = count;
        try {
            targetField = AbstractGameEffect.class.getDeclaredField("scale");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        targetField.setAccessible(true);
    }

    @Override
    public void update() {
        for (final AbstractSpace space : board.squareList) {
            if (!(space instanceof EmptySpace)) {
                space.splat();
                space.uponLand(actor);
                /*
                for(int i = 1; i < count; i++) {
                    space.onLanded(actor);
                }
                 */
                for(int j = 0; j < 3; j++) {
                    this.addPetal();
                }
                this.addPetal(0.1F);
            }
        }

        this.tickDuration();
    }

    private void addPetal() {
        this.addPetal(0F);
    }
    private void addPetal(float delay) {
        PetalEffect pe = new PetalEffect();
        try {
            targetField.set(pe, (float)targetField.get(pe) / 2F);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        this.addToTop(new VFXAction(pe, delay));
    }
}
