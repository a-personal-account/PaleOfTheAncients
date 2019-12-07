package paleoftheancients.vfx.BlurryLens;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.AbstractCreature;

public abstract class BlurryCharacterVFX extends BlurryLensVFX {
    protected AbstractCreature ac;

    public BlurryCharacterVFX(AbstractCreature ac) {
        this.ac = ac;
    }

    @Override
    public void render(SpriteBatch spriteBatch) {

    }

    @Override
    public void dispose() {

    }
}
