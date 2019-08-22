package paleoftheancients.thevixen.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class SwaggerEffect extends AbstractGameEffect {
    private float timer = 0.1F;
    private AbstractCreature ac;

    public SwaggerEffect(AbstractCreature ac) {
        this.duration = 2.0F;
        this.ac = ac;
    }

    public void update() {
        /*
        if (this.duration == 2.0F) {
            AbstractDungeon.effectsQueue.add(new SpotlightEffect());
        }
        */

        this.duration -= Gdx.graphics.getDeltaTime();
        this.timer -= Gdx.graphics.getDeltaTime();
        if (this.timer < 0.0F) {
            this.timer += 0.1F;
            AbstractDungeon.effectsQueue.add(new DollarBillEffect(ac));
            AbstractDungeon.effectsQueue.add(new DollarBillEffect(ac));
        }

        if (this.duration < 0.0F) {
            this.isDone = true;
        }

    }

    public void render(SpriteBatch sb) {
    }

    public void dispose() {
    }
}
