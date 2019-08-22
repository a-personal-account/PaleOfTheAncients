package paleoftheancients.wokeone.vfx;

import basemod.ReflectionHacks;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.AwakenedWingParticle;

public class CultistWingParticle extends AwakenedWingParticle {
    public CultistWingParticle() {
        super();

        float x;
        float y;
        float tscale = Settings.scale * MathUtils.random(0.4F, 0.5F);


        int roll = MathUtils.random(0, 2);
        if (roll == 0) {
            x = MathUtils.random(-100, -60) * Settings.scale;
            y = MathUtils.random(-90, -40) * Settings.scale;
            this.rotation += 270;
        } else if (roll == 1) {
            x = MathUtils.random(-100, -80) * Settings.scale;
            y = MathUtils.random(-40, 40) * Settings.scale;
            this.renderBehind = true;
            this.rotation += 270;
        } else {
            x = MathUtils.random(-175.0F, -135.0F) * Settings.scale;
            y = MathUtils.random(50, 60) * Settings.scale;
            tscale = Settings.scale * MathUtils.random(0.4F, 0.7F);
        }

        ReflectionHacks.setPrivate(this, AwakenedWingParticle.class, "x", x);
        ReflectionHacks.setPrivate(this, AwakenedWingParticle.class, "y", y);
        ReflectionHacks.setPrivate(this, AwakenedWingParticle.class, "tScale", tscale);
    }
}
