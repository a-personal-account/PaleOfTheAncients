package paleoftheancients.bard.notes;

import paleoftheancients.theshowman.cards.BeSad;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;

public abstract class AbstractNote {
    protected Color color;

    protected AbstractNote(Color color) {
        this.color = color;
    }

    public String cardCode() {
        return "[" + this.name() + "Note]";
    }

    public abstract String name();

    public abstract String ascii();

    public abstract AbstractCard.CardTags cardTag();

    public Color color() {
        return this.color;
    }

    public boolean isFloaty() {
        return true;
    }

    public float floatFactor() {
        return this.isFloaty() ? 1.0F : 0.0F;
    }

    public boolean countsAsNote() {
        return true;
    }

    public abstract TextureAtlas.AtlasRegion getTexture();

    public TextureAtlas.AtlasRegion getQueuedTexture() {
        return this.getTexture();
    }

    public final void renderExact(SpriteBatch sb, float x, float y, float rotation) {
        Color oldColor = sb.getColor();
        sb.setColor(this.color());
        TextureAtlas.AtlasRegion tex = this.getQueuedTexture();
        sb.draw(tex, x, y, 0.0F, 0.0F, (float)tex.getRegionWidth(), (float)tex.getRegionHeight(), Settings.scale * 2.0F, Settings.scale * 2.0F, rotation);
        sb.setColor(oldColor);
    }

    public void render(SpriteBatch sb, float x, float y) {
        this.renderExact(sb, x, y, 0.0F);
    }

    public AbstractCard makeChoiceCard() {
        return new BeSad();
    }

    public boolean isNoteType(Class<? extends AbstractNote> type) {
        return type.isInstance(this);
    }

    public boolean equals(Object other) {
        if (other instanceof AbstractNote) {
            return !(this instanceof WildCardNote) && !(other instanceof WildCardNote) ? this.getClass().equals(other.getClass()) : true;
        } else {
            return super.equals(other);
        }
    }
}
