package paleoftheancients.finarubossu.vfx;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.CardPoofEffect;

public class PoofRelicEffect extends AbstractGameEffect {
    private AbstractRelic relic;

    public PoofRelicEffect(AbstractRelic relic) {
        this.relic = relic;
        this.relic.targetX = Settings.WIDTH / 2;
        this.relic.targetY = Settings.HEIGHT / 2;
        this.relic.isDone = false;
        this.relic.isAnimating = false;
    }

    @Override
    public void update() {
        this.relic.update();
        if(this.relic.currentX == this.relic.targetX && this.relic.currentY == this.relic.targetY) {
            AbstractDungeon.effectsQueue.add(new CardPoofEffect(this.relic.currentX, this.relic.currentY));
            this.isDone = true;
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        relic.render(sb);
    }

    @Override
    public void dispose() {

    }
}
