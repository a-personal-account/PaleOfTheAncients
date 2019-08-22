package paleoftheancients.thevixen.cards.attack;

import paleoftheancients.thevixen.vfx.OverheatEffect;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;

public class Overheat {
    public static void VFX(AbstractCreature m) {
        AbstractDungeon.actionManager.addToBottom(new VFXAction(new OverheatEffect(m)));
        AbstractDungeon.actionManager.addToBottom(new VFXAction(new ExplosionSmallEffect(m.hb.cX, m.hb.cY)));
    }
}