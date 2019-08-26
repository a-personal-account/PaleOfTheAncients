package paleoftheancients.finarubossu.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class RestoreRelicsVFX extends AbstractGameEffect {
    private boolean[] relicDone;
    public RestoreRelicsVFX() {
        for(final AbstractRelic relic : AbstractDungeon.player.relics) {
            relic.targetX = relic.currentX;
            relic.targetY = relic.currentY;
            relic.currentX = Settings.WIDTH / 2 + MathUtils.random(-0.25F, 0.25F) * Settings.WIDTH;
            relic.currentY = Settings.HEIGHT / 2 + MathUtils.random(-0.25F, 0.25F) * Settings.HEIGHT;
            relic.isDone = true;
        }
        this.relicDone = new boolean[AbstractDungeon.player.relics.size()];
        for(int i = 0; i < this.relicDone.length; i++) {
            this.relicDone[i] = false;
        }
    }

    @Override
    public void update() {
        for(int i = 0; i < AbstractDungeon.player.relics.size() && i < this.relicDone.length; i++) {
            AbstractRelic relic = AbstractDungeon.player.relics.get(i);
            this.isDone = true;
            if(!this.relicDone[i]) {
                this.relicDone[i] = true;
                if (relic.currentX != relic.targetX) {
                    relic.currentX = MathUtils.lerp(relic.currentX, relic.targetX, Gdx.graphics.getDeltaTime() * 6.0F);
                    if (Math.abs(relic.currentX - relic.targetX) < 0.5F) {
                        relic.currentX = relic.targetX;
                    } else {
                        this.relicDone[i] = false;
                    }
                }

                if (relic.currentY != relic.targetY) {
                    relic.currentY = MathUtils.lerp(relic.currentY, relic.targetY, Gdx.graphics.getDeltaTime() * 6.0F);
                    if (Math.abs(relic.currentY - relic.targetY) < 0.5F) {
                        relic.currentY = relic.targetY;
                    } else {
                        this.relicDone[i] = false;
                    }
                }
                this.isDone &= this.relicDone[i];
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
