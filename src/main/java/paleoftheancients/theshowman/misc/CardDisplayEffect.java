package paleoftheancients.theshowman.misc;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import paleoftheancients.theshowman.bosscards.AbstractShowmanCard;

public class CardDisplayEffect extends AbstractGameEffect {
    private AbstractShowmanCard card;

    public CardDisplayEffect(AbstractShowmanCard card) {
        this.card = card;
    }

    @Override
    public void update() {}

    @Override
    public void render(SpriteBatch sb) {
        card.hb.hovered = !card.hb.hovered;
        card.render(sb);
        card.hb.hovered = !card.hb.hovered;
    }

    @Override
    public void dispose() {

    }
}
