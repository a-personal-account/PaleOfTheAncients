package paleoftheancients.thevixen.cards.power;

import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.GhostIgniteEffect;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;

public class SynergyBurst {
    public static void VFX(AbstractCreature source) {
        AbstractDungeon.actionManager.addToTop(new VFXAction(new LightningEffect(source.drawX, source.drawY), 0.1F));
        AbstractDungeon.actionManager.addToTop(new SFXAction("ORB_LIGHTNING_EVOKE"));
        AbstractDungeon.actionManager.addToBottom(new VFXAction(new GhostIgniteEffect(source.hb.cX, source.hb.cY), 0.4F));
    }
}
