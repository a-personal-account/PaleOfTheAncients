package paleoftheancients.thevixen.helpers;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.helpers.Hitbox;

public class RandomPoint {
    public static float x(Hitbox hb) {
        return hb.cX + MathUtils.random(-hb.width, hb.width) / 2;
    }
    public static float y(Hitbox hb) {
        return hb.cY + MathUtils.random(-hb.height, hb.height) / 2;
    }
}
