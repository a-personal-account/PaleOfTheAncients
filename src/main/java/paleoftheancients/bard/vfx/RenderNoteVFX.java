package paleoftheancients.bard.vfx;

import paleoftheancients.bard.notes.AbstractNote;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.BobEffect;

public class RenderNoteVFX extends AbstractGameEffect {
    AbstractNote note;
    BobEffect effect;
    float x, y, duration;

    public RenderNoteVFX(AbstractNote note, float x, float y, float duration) {
        this.note = note;
        this.effect = new BobEffect();
        this.x = x;
        this.y = y;
        this.duration = duration;
    }

    public void update() {
        effect.update();
        this.duration -= Gdx.graphics.getDeltaTime();
        if(this.duration <= 0F) {
            this.isDone = true;
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        note.render(
                sb,
                x,// - note.getTexture().packedWidth / 2,
                y - note.getTexture().packedHeight / 2 + effect.y
        );
    }

    @Override
    public void dispose() {

    }
}
