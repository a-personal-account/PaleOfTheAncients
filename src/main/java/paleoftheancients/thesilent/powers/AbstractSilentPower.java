package paleoftheancients.thesilent.powers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class AbstractSilentPower extends AbstractPower {
    @Override
    public void renderIcons(SpriteBatch sb, float x, float y, Color c) {
        super.renderIcons(sb, x, y, Color.ORANGE);
    }
}
