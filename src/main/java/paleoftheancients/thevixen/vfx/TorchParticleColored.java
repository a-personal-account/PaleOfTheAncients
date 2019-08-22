package paleoftheancients.thevixen.vfx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.vfx.scene.TorchParticleLEffect;

public class TorchParticleColored extends TorchParticleLEffect {

    public TorchParticleColored(float x, float y, Color color) {
        super(x, y);
        this.color.r = Math.min(1F, Math.max(0F, color.r + MathUtils.random(-0.1F, 0.1F)));
        this.color.g = Math.min(1F, Math.max(0F, color.g + MathUtils.random(-0.1F, 0.1F)));
        this.color.b = Math.min(1F, Math.max(0F, color.b + MathUtils.random(-0.1F, 0.1F)));
    }
}
