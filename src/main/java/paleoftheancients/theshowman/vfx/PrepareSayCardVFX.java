package paleoftheancients.theshowman.vfx;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.SpeechBubble;

public class PrepareSayCardVFX extends AbstractGameEffect {
    private AbstractCreature m;
    private int specifiedCard;

    public PrepareSayCardVFX(AbstractCreature m, int specifiedCard) {
        this.m = m;
        this.specifiedCard = specifiedCard;
    }

    public void update() {
        this.isDone = true;
        AbstractDungeon.effectsQueue.add(new SpeechBubble(this.m.dialogX, this.m.dialogY, 2.0F, "", this.m.isPlayer));
        AbstractDungeon.effectsQueue.add(new SayCardVFX(this.m, this.specifiedCard));
    }

    public void render(SpriteBatch sb) {
    }

    public void dispose() {
    }
}
