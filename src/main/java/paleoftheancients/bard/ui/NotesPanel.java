package paleoftheancients.bard.ui;

import paleoftheancients.PaleMod;
import paleoftheancients.bard.actions.PerformAllMelodiesAction;
import paleoftheancients.bard.actions.SelectMelodyAction;
import paleoftheancients.bard.helpers.AssetLoader;
import paleoftheancients.bard.monsters.NoteQueue;
import paleoftheancients.bard.notes.AbstractNote;
import paleoftheancients.bard.notes.WildCardNote;
import paleoftheancients.bard.powers.SonataPower;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.localization.UIStrings;

import java.util.Iterator;

public class NotesPanel {
    static final UIStrings performStrings;
    static final float NOTE_SPACING = 32.0F;
    private static final float DEFAULT_Y_OFFSET = 246.0F;
    static final float EXTRA_OFFSET = 12.0F;
    float yOffset = 146.0F;
    private float noteFloatTimer = 0.0F;
    private Hitbox notesHb = new Hitbox(32.0F, 32.0F);

    private TextureAtlas noteAtlas;
    private NoteQueue noteQueue;

    public static final float DEFAULT_X_MULTIPLIER = 5 * Settings.scale;
    public static final byte BARD_MAX_NOTES = 4;

    public NotesPanel(NoteQueue nq) {
        this.noteQueue = nq;
        this.noteAtlas = AssetLoader.loader.loadAtlas(PaleMod.assetPath("images/bard/notes/notes.atlas"));
    }

    public float getX() {
        AbstractPlayer player = AbstractDungeon.player;
        return player.drawX - 96.0F * Settings.scale;
    }

    public float getY() {
        AbstractPlayer player = AbstractDungeon.player;
        return (this.yOffset + 12.0F) * Settings.scale + player.drawY + player.hb_h / 2.0F;
    }

    public void update(AbstractCreature player) {
        WildCardNote.get().update();

        notesHb.resize(
                64 * Settings.scale
                        + 32 * Settings.scale * noteQueue.getMaxNotes(),
                64 * Settings.scale
        );

        yOffset = DEFAULT_Y_OFFSET;

        notesHb.translate(
                player.drawX - (NOTE_SPACING * DEFAULT_X_MULTIPLIER),
                yOffset * Settings.scale + player.drawY + player.hb_h / 2.0f
        );
    }

    public void render(SpriteBatch sb, AbstractCreature player) {
        noteFloatTimer += Gdx.graphics.getDeltaTime() * 2;

        boolean canPlay = noteQueue.canPlayAnyMelody();

        sb.setColor(Color.WHITE);
        TextureAtlas.AtlasRegion tex = noteAtlas.findRegion(canPlay ? "barsGlow" : "bars");
        // Left section of bars
        sb.draw(
                tex.getTexture(),
                player.drawX - (NOTE_SPACING * DEFAULT_X_MULTIPLIER),
                (yOffset) * Settings.scale + player.drawY + player.hb_h / 2.0f,
                0,
                0,
                32,
                32,
                Settings.scale * 2,
                Settings.scale * 2,
                0,
                tex.getRegionX(),
                tex.getRegionY(),
                32,
                32,
                false,
                false
        );
        // Middle (extendable) section of bars
        sb.draw(
                tex.getTexture(),
                player.drawX - (NOTE_SPACING * DEFAULT_X_MULTIPLIER) + (64 * Settings.scale),
                (yOffset) * Settings.scale + player.drawY + player.hb_h / 2.0f,
                0,
                0,
                32,
                32,
                Settings.scale * (2 + (noteQueue.getMaxNotes() - BARD_MAX_NOTES)),
                Settings.scale * 2,
                0,
                tex.getRegionX() + 32,
                tex.getRegionY(),
                32,
                32,
                false,
                false
        );
        // Right section of bars
        sb.draw(
                tex.getTexture(),
                player.drawX - (NOTE_SPACING * DEFAULT_X_MULTIPLIER) + (64 * Settings.scale) + (32 * (2 + (noteQueue.getMaxNotes() - BARD_MAX_NOTES)) * Settings.scale),
                (yOffset) * Settings.scale + player.drawY + player.hb_h / 2.0f,
                0,
                0,
                32,
                32,
                Settings.scale * 2,
                Settings.scale * 2,
                0,
                tex.getRegionX() + 64,
                tex.getRegionY(),
                32,
                32,
                false,
                false
        );

        sb.setColor(Color.WHITE);
        // Clef
        float offset = 1.5f * (float) Math.sin(noteFloatTimer - 1.2);
        tex = noteAtlas.findRegion(canPlay ? "clefTrebleGlow" : "clefTreble");
        sb.draw(
                tex,
                player.drawX - (NOTE_SPACING * DEFAULT_X_MULTIPLIER) - 16 * Settings.scale,
                (offset + yOffset) * Settings.scale + player.drawY + player.hb_h / 2.0f - 16 * Settings.scale,
                0,
                0,
                tex.getRegionWidth(),
                tex.getRegionHeight(),
                Settings.scale * 2,
                Settings.scale * 2,
                0
        );

        // Notes
        int i = 0;
        Iterator<AbstractNote> iter = noteQueue.iterator();
        while (iter.hasNext()) {
            AbstractNote note = iter.next();
            offset = note.floatFactor() * 3 * (float) Math.sin(noteFloatTimer + i*1.2);
            note.render(
                    sb,
                    player.drawX - (NOTE_SPACING * DEFAULT_X_MULTIPLIER * 2 / 3) + (i * NOTE_SPACING * Settings.scale),
                    (offset + yOffset + EXTRA_OFFSET) * Settings.scale + player.drawY + player.hb_h / 2.0f
            );
            ++i;
        }

        // Tooltip
        if (notesHb.hovered && !AbstractDungeon.isScreenUp) {
            String body = performStrings.TEXT[1] + noteQueue.getMaxNotes() + performStrings.TEXT[2];

            float height = -FontHelper.getSmartHeight(FontHelper.tipBodyFont, body, 280.0F * Settings.scale, 26.0F * Settings.scale);
            height += 74 * Settings.scale; // accounts for header height, box border, and a bit of spacing

            TipHelper.renderGenericTip(
                    notesHb.x,
                    notesHb.y + notesHb.height + height,
                    performStrings.TEXT[0],
                    body
            );
        }

        notesHb.render(sb);
    }

    public void perform(final AbstractCreature source, NoteQueue noteQueue) {
        if (noteQueue.canPlayAnyMelody()) {
            AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
                public void update() {
                    if (source.hasPower(SonataPower.POWER_ID)) {
                        AbstractDungeon.actionManager.addToTop(new PerformAllMelodiesAction(noteQueue));
                    } else {
                        AbstractDungeon.actionManager.addToTop(new SelectMelodyAction(noteQueue));
                    }

                    this.isDone = true;
                }
            });
        }
    }

    static {
        performStrings = CardCrawlGame.languagePack.getUIString(PaleMod.makeID("Perform"));
    }
}
