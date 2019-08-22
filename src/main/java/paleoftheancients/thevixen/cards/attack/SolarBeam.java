package paleoftheancients.thevixen.cards.attack;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.HeartBuffEffect;
import com.megacrit.cardcrawl.vfx.combat.SweepingBeamEffect;

public class SolarBeam {
    public static void vfx(AbstractCreature p, boolean isFlipped) {
        AbstractDungeon.actionManager.addToBottom(new VFXAction(new HeartBuffEffect(p.hb.cX, p.hb.cY)));
        AbstractDungeon.actionManager.addToBottom(new WaitAction(0.4F));
        AbstractGameEffect age = new SweepingBeamEffect(p.hb.cX, p.hb.cY, isFlipped);
        ReflectionHacks.setPrivate(age, AbstractGameEffect.class, "color", Color.GREEN.cpy());
        AbstractDungeon.actionManager.addToBottom(new VFXAction(age));
    }
}