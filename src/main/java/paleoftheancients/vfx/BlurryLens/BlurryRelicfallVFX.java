package paleoftheancients.vfx.BlurryLens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class BlurryRelicfallVFX extends BlurryLensVFX {
    protected AbstractRelic ac;
    private int relicnum;
    private boolean bounced;
    private float vX, vY;
    private float rotation;

    private float origX, origY;

    public BlurryRelicfallVFX(AbstractRelic ac, int relicnum) {
        this.ac = ac;
        this.relicnum = relicnum;
        this.bounced = false;
        vX = MathUtils.random(-4F, 4F);
        vY = 0F;
        rotation = 0;

        origX = ac.currentX;
        origY = ac.currentY;
    }

    @Override
    public void update() {
        if(relicnum / 25 == AbstractRelic.relicPage) {
            vY -= MathUtils.random(3.8F, 4.2F);
            ac.currentY += vY * Gdx.graphics.getDeltaTime();
            if (!bounced && ac.currentY < AbstractDungeon.floorY) {
                bounced = true;
                vY *= MathUtils.random(-0.4F, -0.2F);
                vX = MathUtils.random(-50F, 50F) * Settings.scale;
                if((ac.currentX < Settings.WIDTH * 0.05F && vX < 0) || (ac.currentX > Settings.WIDTH * 0.95F && vX > 0)) {
                    vX *= -1;
                }
            } else if(ac.currentY < -128F * Settings.scale) {
                bounced = false;
                ac.currentY += Settings.HEIGHT + 128F * 2 * Settings.scale;
            }
        }
    }

    @Override
    public void render(SpriteBatch spriteBatch) {

    }

    @Override
    public void dispose() {
        ac.currentX = origX;
        ac.currentY = origY;
    }
}
