package paleoftheancients.vfx.BlurryLens;

import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public abstract class BlurryLensVFX extends AbstractGameEffect {
    public void reset() {
        this.dispose();
    }
}