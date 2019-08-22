package paleoftheancients.thevixen.actions;

import paleoftheancients.thevixen.helpers.RandomPoint;
import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.FireBurstParticleEffect;
import com.megacrit.cardcrawl.vfx.combat.LightFlareParticleEffect;

public class EmberEffectAction extends AbstractGameAction {

    private Hitbox hb;
    private int density;

    public EmberEffectAction(AbstractCreature m) {
        this(m, 4);
    }
    public EmberEffectAction(AbstractCreature m, int amount) {
        this(m, amount, 10);
    }
    public EmberEffectAction(AbstractCreature m, int amount, int density) {
        this.hb = m.hb;
        this.amount = amount;
        this.density = density;
    }

    @Override
    public void update() {
        this.isDone = true;

        for(int i = 0; i < amount; i++) {
            final float x = RandomPoint.x(this.hb);
            final float y = RandomPoint.y(this.hb);

            for(int j = 0; j < density; j++) {
                this.isDone = true;
                AbstractGameEffect gie = new FireBurstParticleEffect(x, y);
                ReflectionHacks.setPrivate(gie, AbstractGameEffect.class, "color", Color.ORANGE.cpy());
                AbstractDungeon.effectList.add(gie);
                AbstractDungeon.effectList.add(new LightFlareParticleEffect(x, y, Color.ORANGE));
            }
        }
    }
}
