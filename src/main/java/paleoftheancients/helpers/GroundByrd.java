package paleoftheancients.helpers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.monsters.city.Byrd;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class GroundByrd extends AbstractGameEffect {
    private Byrd byrd;
    public GroundByrd(Byrd byrd) {
        this.byrd = byrd;
    }

    @Override
    public void update() {
        this.isDone = true;
        byrd.changeState("GROUNDED");
    }

    @Override
    public void render(SpriteBatch spriteBatch) {

    }

    @Override
    public void dispose() {

    }
}
