package paleoftheancients.maker.helpers;

import paleoftheancients.finarubossu.vfx.BackgroundMonster;

public class ThrownMonster extends BackgroundMonster {
    public ThrownMonster(boolean isfast, Class clz) {
        super(isfast, clz);
        rotationVelocity = 0;
    }
}
