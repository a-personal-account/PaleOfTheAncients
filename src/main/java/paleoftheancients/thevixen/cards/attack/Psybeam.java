package paleoftheancients.thevixen.cards.attack;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.SmallLaserEffect;

public class Psybeam {
    public static void vfx(AbstractCreature source, AbstractCreature target) {
        vfx(source, target, false);
    }
    public static void vfx(AbstractCreature source, AbstractCreature target, boolean negative) {
        AbstractDungeon.actionManager.addToBottom(new SFXAction("ATTACK_MAGIC_BEAM_SHORT", 0.5F));
        AbstractDungeon.actionManager.addToBottom(new VFXAction(new BorderFlashEffect(Color.SKY)));
        float sX = source.hb.cX + (AbstractDungeon.player.hb.width / 2) * (negative ? -1 : 1) + source.animX;
        if (Settings.FAST_MODE) {
            AbstractDungeon.actionManager.addToBottom(new VFXAction(new SmallLaserEffect(sX, source.hb.cY, target.hb.cX, target.hb.cY), 0.1F));
        } else {
            AbstractDungeon.actionManager.addToBottom(new VFXAction(new SmallLaserEffect(sX, source.hb.cY, target.hb.cX, target.hb.cY), 0.3F));
        }
    }
}