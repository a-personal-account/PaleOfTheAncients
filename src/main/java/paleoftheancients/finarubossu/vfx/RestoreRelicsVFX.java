package paleoftheancients.finarubossu.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class RestoreRelicsVFX extends AbstractGameEffect {
    public RestoreRelicsVFX() {
        for(final AbstractRelic relic : AbstractDungeon.player.relics) {
            relic.targetX = relic.currentX;
            relic.targetY = relic.currentY;
            relic.currentX = Settings.WIDTH / 2 + MathUtils.random(-0.25F, 0.25F) * Settings.WIDTH;
            relic.currentY = Settings.HEIGHT / 2 + MathUtils.random(-0.25F, 0.25F) * Settings.HEIGHT;
            relic.isDone = false;
        }
    }

    @Override
    public void update() {
        for(final AbstractRelic relic : AbstractDungeon.player.relics) {
            this.isDone = true;
            if(!relic.isDone) {
                relic.isDone = true;
                if (relic.currentX != relic.targetX) {
                    relic.currentX = MathUtils.lerp(relic.currentX, relic.targetX, Gdx.graphics.getDeltaTime() * 6.0F);
                    if (Math.abs(relic.currentX - relic.targetX) < 0.5F) {
                        relic.currentX = relic.targetX;
                    } else {
                        relic.isDone = false;
                    }
                }

                if (relic.currentY != relic.targetY) {
                    relic.currentY = MathUtils.lerp(relic.currentY, relic.targetY, Gdx.graphics.getDeltaTime() * 6.0F);
                    if (Math.abs(relic.currentY - relic.targetY) < 0.5F) {
                        relic.currentY = relic.targetY;
                    } else {
                        relic.isDone = false;
                    }
                }
                this.isDone &= relic.isDone;
            }
        }
    }

    @Override
    public void render(SpriteBatch spriteBatch) {

    }

    @Override
    public void dispose() {

    }
}
