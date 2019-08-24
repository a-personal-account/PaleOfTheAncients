package paleoftheancients.helpers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class PlaySFX extends AbstractGameEffect {
    private String key;
    public PlaySFX(String key) {
        this.key = key;
    }

    @Override
    public void update() {
        this.isDone = true;
        CardCrawlGame.sound.play(key);
    }

    @Override
    public void render(SpriteBatch spriteBatch) {

    }

    @Override
    public void dispose() {

    }
}
