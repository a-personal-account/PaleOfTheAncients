package paleoftheancients.reimu.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import paleoftheancients.reimu.vfx.AbstractDamagingVFX;

public class DamagingAction extends AbstractGameAction {
    private GetDamageVFX vfx;

    public DamagingAction(GetDamageVFX vfx) {
        this.vfx = vfx;
    }

    @Override
    public void update() {
        AbstractDungeon.effectList.add(vfx.get());
        this.isDone = true;
    }

    public interface GetDamageVFX {
        AbstractDamagingVFX get();
    }
}
