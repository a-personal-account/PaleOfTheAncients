package paleoftheancients.screens;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cutscenes.NeowEye;

public class CustomEye extends NeowEye {
    public boolean stopped;

    public CustomEye(int eyePosition) {
        super(eyePosition);
        this.stopped = false;
    }

    @Override
    public void update() {
        if(!this.stopped) {
            super.update();
        }
    }

    public void stop() {
        this.stopped = true;
        Texture lid = (Texture) ReflectionHacks.getPrivateStatic(NeowEye.class, "lid6");
        ReflectionHacks.setPrivate(this, NeowEye.class, "eyeLid", lid);
    }
}
