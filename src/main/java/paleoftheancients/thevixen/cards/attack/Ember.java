package paleoftheancients.thevixen.cards.attack;

import paleoftheancients.thevixen.helpers.RandomPoint;
import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.FireBurstParticleEffect;
import com.megacrit.cardcrawl.vfx.combat.LightFlareParticleEffect;

public class Ember {

    public static void VFX(AbstractCreature m) {
        VFX(m, 4);
    }
    public static void VFX(AbstractCreature m, int amount) {
        VFX(m, amount, 10);
    }
    public static void VFX(AbstractCreature m, int amount, int density) {
        for(int i = 0; i < amount; i++) {
            final float x = RandomPoint.x(m.hb);
            final float y = RandomPoint.y(m.hb);

            for(int j = 0; j < density; j++) {
                AbstractGameEffect gie = new FireBurstParticleEffect(x, y);
                ReflectionHacks.setPrivate(gie, AbstractGameEffect.class, "color", Color.ORANGE.cpy());
                AbstractDungeon.effectList.add(gie);
                AbstractDungeon.effectList.add(new LightFlareParticleEffect(x, y, Color.ORANGE));
            }
        }
    }
}