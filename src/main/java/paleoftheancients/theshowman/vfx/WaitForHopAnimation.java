package paleoftheancients.theshowman.vfx;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class WaitForHopAnimation extends AbstractGameEffect {
    private AbstractCreature target;

    public WaitForHopAnimation(AbstractCreature target) {
        this.target = target;
    }

    @Override
    public void update() {
        if((float) ReflectionHacks.getPrivate(this.target, AbstractCreature.class, "animationTimer") <= 0F) {
            this.target.useHopAnimation();
            this.isDone = true;
        }
    }

    @Override
    public void render(SpriteBatch spriteBatch) {

    }

    @Override
    public void dispose() {

    }
}
