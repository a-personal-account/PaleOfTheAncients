package paleoftheancients.timeeater.vfx;

import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public abstract class AbstractSignalEffect extends AbstractGameEffect {
    public boolean ending = false;

    protected void end() {
        this.ending = true;
    }
}
