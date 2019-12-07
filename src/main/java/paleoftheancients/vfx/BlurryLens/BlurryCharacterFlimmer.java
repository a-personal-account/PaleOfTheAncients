package paleoftheancients.vfx.BlurryLens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class BlurryCharacterFlimmer extends BlurryCharacterVFX {

    private float flimmertimer;

    public BlurryCharacterFlimmer(AbstractCreature ac) {
        super(ac);

        flimmertimer = 0;
    }

    @Override
    public void update() {
        flimmertimer -= Gdx.graphics.getDeltaTime();
        if(flimmertimer <= 0F) {
            flimmertimer = MathUtils.random(0.2F, 0.4F);
            ac.tint.color.a = MathUtils.random(0F, 1F);
        }
    }

    @Override
    public void dispose() {
        ac.tint.color.a = 1F;
    }
}
