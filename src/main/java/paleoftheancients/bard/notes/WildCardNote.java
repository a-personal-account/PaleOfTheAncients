package paleoftheancients.bard.notes;

import paleoftheancients.bard.helpers.MelodyManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;

import java.util.List;

public class WildCardNote extends AbstractNote {
    @SpireEnum(
            name = "BARD_WILD_NOTE_TAG"
    )
    public static AbstractCard.CardTags TAG;
    private static WildCardNote singleton;
    private static final float NOTE_TIME = 0.16F;
    private static final float COLOR_TIME = 0.24F;
    private float timer = 0.0F;
    private int noteType = 0;
    private float timerColor = 0.0F;
    private int noteColor = 0;
    private int prev_noteColor = 0;

    public static WildCardNote get() {
        if (singleton == null) {
            singleton = new WildCardNote();
        }

        return singleton;
    }

    private WildCardNote() {
        super(Color.valueOf("ffffff"));
    }

    public void update() {
        this.timer += Gdx.graphics.getDeltaTime();
        if (this.timer > 0.16F) {
            this.timer -= 0.16F;

            int nextNoteType;
            for(nextNoteType = this.noteType; nextNoteType == this.noteType; nextNoteType = MathUtils.random(MelodyManager.getAllNotesCount() - 1)) {
            }

            this.noteType = nextNoteType;
        }

        this.timerColor += Gdx.graphics.getDeltaTime();
        if (this.timerColor > 0.24F) {
            this.timerColor -= 0.24F;
            this.prev_noteColor = this.noteColor;
            this.noteColor = MathUtils.random(MelodyManager.getAllNotesCount() - 1);
            List<AbstractNote> allNotes = MelodyManager.getAllNotes();
            this.color = ((AbstractNote)allNotes.get(this.noteColor)).color();
        }

    }

    public String name() {
        return "Wild";
    }

    public String ascii() {
        return "*";
    }

    public AbstractCard.CardTags cardTag() {
        return TAG;
    }

    public Color color() {
        return this.prevColor().cpy().lerp(this.color, this.timerColor / 0.24F);
    }

    private Color prevColor() {
        List<AbstractNote> allNotes = MelodyManager.getAllNotes();
        return ((AbstractNote)allNotes.get(this.prev_noteColor)).color();
    }

    public float floatFactor() {
        return 3.0F;
    }

    public boolean isNoteType(Class<? extends AbstractNote> type) {
        return true;
    }

    public TextureAtlas.AtlasRegion getTexture() {
        List<AbstractNote> allNotes = MelodyManager.getAllNotes();
        return ((AbstractNote)allNotes.get(this.noteType)).getTexture();
    }

    public TextureAtlas.AtlasRegion getQueuedTexture() {
        List<AbstractNote> allNotes = MelodyManager.getAllNotes();
        return ((AbstractNote)allNotes.get(this.noteType)).getQueuedTexture();
    }

    public void render(SpriteBatch sb, float x, float y) {
        super.render(sb, x, y + 12.0F * Settings.scale);
    }
}
