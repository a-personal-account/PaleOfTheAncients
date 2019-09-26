package paleoftheancients.bard.notes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import paleoftheancients.helpers.AssetLoader;

public class DebuffNote extends AbstractNote {
    @SpireEnum(
            name = "BARD_DEBUFF_NOTE_TAG"
    )
    public static AbstractCard.CardTags TAG;
    private static DebuffNote singleton;

    public static DebuffNote get() {
        if (singleton == null) {
            singleton = new DebuffNote();
        }

        return singleton;
    }

    private DebuffNote() {
        super(Color.valueOf("ce9564"));
    }

    public String name() {
        return "Debuff";
    }

    public String ascii() {
        return "D";
    }

    public AbstractCard.CardTags cardTag() {
        return TAG;
    }

    public TextureAtlas.AtlasRegion getTexture() {
        return AssetLoader.NoteAtlas().findRegion("noteDebuff");
    }

    public TextureAtlas.AtlasRegion getQueuedTexture() {
        return AssetLoader.NoteAtlas().findRegion("queuedDebuff");
    }

    public void render(SpriteBatch sb, float x, float y) {
        super.render(sb, x, y + 18.0F * Settings.scale);
    }
}
