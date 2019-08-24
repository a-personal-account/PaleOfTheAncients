package paleoftheancients.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class DelayVFX extends AbstractGameEffect {
    private AbstractGameEffect followup;
    public DelayVFX(AbstractGameEffect age, float duration) {
        this.followup = age;
        this.duration = duration;
    }

    @Override
    public void update() {
        this.duration -= Gdx.graphics.getDeltaTime();
        if(this.duration <= 0) {
            this.isDone = true;
            AbstractDungeon.effectsQueue.add(this.followup);
        }
    }

    @Override
    public void render(SpriteBatch spriteBatch) {

    }

    @Override
    public void dispose() {

    }
}
