package thevixen.actions;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;

public class PlayHealSFXAction extends AbstractGameAction {

    public PlayHealSFXAction() {

    }

    @Override
    public void update() {
        CardCrawlGame.sound.play("HEAL_" + MathUtils.random(1, 3));
        this.isDone = true;
    }
}
