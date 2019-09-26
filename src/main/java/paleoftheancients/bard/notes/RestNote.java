package paleoftheancients.bard.notes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import paleoftheancients.helpers.AssetLoader;

public class RestNote extends AbstractNote {
    @SpireEnum(
            name = "BARD_REST_NOTE_TAG"
    )
    public static AbstractCard.CardTags TAG;
    private static RestNote singleton;

    public static RestNote get() {
        if (singleton == null) {
            singleton = new RestNote();
        }

        return singleton;
    }

    private RestNote() {
        super(Color.valueOf("969696"));
    }

    public String name() {
        return "Rest";
    }

    public String ascii() {
        return "R";
    }

    public AbstractCard.CardTags cardTag() {
        return TAG;
    }

    public boolean isFloaty() {
        return false;
    }

    public boolean countsAsNote() {
        return false;
    }

    public TextureAtlas.AtlasRegion getTexture() {
        return AssetLoader.NoteAtlas().findRegion("noteRest");
    }

    public void render(SpriteBatch sb, float x, float y) {
        super.render(sb, x, y + 4.0F * Settings.scale);
    }
}
