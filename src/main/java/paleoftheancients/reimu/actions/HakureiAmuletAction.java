package paleoftheancients.reimu.actions;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import paleoftheancients.reimu.vfx.HakureiAmuletVFX;

public class HakureiAmuletAction extends AbstractDamagingAction {
    private int amuletcount;

    public HakureiAmuletAction(AbstractCreature target, AbstractCreature source, DamageInfo info, int num, int amuletcount) {
        super(target, source, info, num);
        this.amuletcount = amuletcount;
    }
    @Override
    public void update() {
        AbstractDungeon.effectList.add(new HakureiAmuletVFX(AbstractDungeon.player, source, info, num, amuletcount));
        this.isDone = true;
    }
}
