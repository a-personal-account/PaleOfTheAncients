package paleoftheancients.bard.notes;

import paleoftheancients.bard.helpers.AssetLoader;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;

public class AttackNote extends AbstractNote {
    @SpireEnum(
            name = "BARD_ATTACK_NOTE_TAG"
    )
    public static AbstractCard.CardTags TAG;
    private static AttackNote singleton;

    public static AttackNote get() {
        if (singleton == null) {
            singleton = new AttackNote();
        }

        return singleton;
    }

    private AttackNote() {
        super(Color.valueOf("ce6e79"));
    }

    public String name() {
        return "Attack";
    }

    public String ascii() {
        return "A";
    }

    public AbstractCard.CardTags cardTag() {
        return TAG;
    }

    public TextureAtlas.AtlasRegion getTexture() {
        return AssetLoader.NoteAtlas().findRegion("noteAttack");
    }

    public void render(SpriteBatch sb, float x, float y) {
        super.render(sb, x, y + 12.0F * Settings.scale);
    }
}
