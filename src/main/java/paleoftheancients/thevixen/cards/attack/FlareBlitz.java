package paleoftheancients.thevixen.cards.attack;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.RipAndTearEffect;

public class FlareBlitz {
    public static void VFX(AbstractCreature m) {
        AbstractDungeon.actionManager.addToBottom(new VFXAction(new RipAndTearEffect(m.hb.cX, m.hb.cY, Color.ORANGE, Color.SCARLET)));
        AbstractDungeon.actionManager.addToBottom(new VFXAction(new RipAndTearEffect(m.hb.cX, m.hb.cY, Color.ORANGE, Color.SCARLET)));
    }
}
