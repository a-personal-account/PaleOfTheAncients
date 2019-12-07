package paleoftheancients.vfx.BlurryLens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;

public class BlurryCharacterJump extends BlurryCharacterVFX {

    private float jumptimer;

    public BlurryCharacterJump(AbstractCreature ac) {
        super(ac);

        jumptimer = 0;
    }

    @Override
    public void update() {
        jumptimer -= Gdx.graphics.getDeltaTime();
        if(jumptimer <= 0F) {
            jumptimer = MathUtils.random(2F, 3F);
            ac.useHopAnimation();
        }
    }
}
