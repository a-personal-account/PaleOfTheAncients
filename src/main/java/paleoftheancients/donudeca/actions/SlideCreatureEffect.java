package paleoftheancients.donudeca.actions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class SlideCreatureEffect extends AbstractGameEffect {
    public static float DURATION = 2F;

    private float x, y, startingx, startingy;
    private AbstractCreature target;

    public SlideCreatureEffect(AbstractCreature ac, float x, float y) {
        this.duration = DURATION;
        this.target = ac;
        startingx = this.target.drawX;
        startingy = this.target.drawY;
        this.x = startingx + x;
        this.y = startingy + y;
    }

    public void update() {
        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0.0F) {
            this.isDone = true;
        }

        this.target.drawX = Interpolation.fade.apply(x, startingx, this.duration);
        this.target.drawY = Interpolation.fade.apply(y, startingy, this.duration);
    }

    public void render(SpriteBatch sb) {}
    public void dispose() {}
}
