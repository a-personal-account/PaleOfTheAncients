package paleoftheancients.bard.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.HitboxListener;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import paleoftheancients.bard.helpers.MelodyManager;
import paleoftheancients.bard.melodies.AbstractMelody;
import paleoftheancients.bard.monsters.NoteQueue;
import paleoftheancients.helpers.AssetLoader;

import java.util.ArrayList;
import java.util.List;

public class MelodiesPanel {
    private static final int Y_POS = 220;
    private boolean show = true;
    private Hitbox melodiesToggleHb = new Hitbox(32.0F, 32.0F);
    private MelodiesPanel.MelodiesHitboxListener melodiesToggleHbListener = new MelodiesPanel.MelodiesHitboxListener();
    private List<Hitbox> melodyHbs = new ArrayList();
    private Hitbox extraMelodiesHb;

    private TextureAtlas noteAtlas;
    private NoteQueue noteQueue;
    private NotesPanel notesPanel;

    public MelodiesPanel(NoteQueue nq, NotesPanel notesPanel) {
        float y = (float) Settings.HEIGHT - 272.0F * Settings.scale;

        for(int i = 0; i < MelodyManager.getAllMelodies().size(); ++i) {
            Hitbox hb = new Hitbox(310.0F * Settings.scale, 26.0F * Settings.scale);
            hb.translate(0.0F, y);
            this.melodyHbs.add(hb);
            y -= 26.0F * Settings.scale;
        }

        this.extraMelodiesHb = new Hitbox(310.0F * Settings.scale, 26.0F * Settings.scale);

        this.noteQueue = nq;
        this.notesPanel = notesPanel;
        this.noteAtlas = AssetLoader.NoteAtlas();

        this.melodiesToggleHb.resize(48.0F * Settings.scale, 64.0F * Settings.scale);
    }

    public void toggleShow() {
        this.show = !this.show;
    }

    public void update(AbstractCreature player) {
        if (noteQueue.getMaxNotes() != 0) {
            melodiesToggleHb.resize(
                    48 * Settings.scale,
                    64 * Settings.scale
            );
            melodiesToggleHb.translate(
                    player.drawX + (NotesPanel.NOTE_SPACING * NotesPanel.DEFAULT_X_MULTIPLIER) + 0 * Settings.scale,
                    notesPanel.yOffset * Settings.scale + player.drawY + player.hb_h / 2.0f
            );

            melodiesToggleHb.encapsulatedUpdate(melodiesToggleHbListener);

            int skipped = 0;
            List<AbstractMelody> allMelodies = MelodyManager.getAllMelodies();
            float y = Settings.HEIGHT - (Y_POS + 52) * Settings.scale;
            for (int i = 0; i < allMelodies.size(); ++i) {
                if (allMelodies.get(i).length() <= noteQueue.getMaxNotes()) {
                    melodyHbs.get(i).translate(Settings.WIDTH - 320F * Settings.scale, y);
                    melodyHbs.get(i).update();
                    y -= 26 * Settings.scale;
                } else {
                    ++skipped;
                }
            }
            if (skipped > 0) {
                extraMelodiesHb.translate(Settings.WIDTH - 310F * Settings.scale, y);
                extraMelodiesHb.update();
            }
        }
    }

    public void render(SpriteBatch sb, AbstractCreature player) {
        sb.setColor(Color.WHITE);
        TextureAtlas.AtlasRegion tex = noteAtlas.findRegion(show ? "toggleOff" : "toggleOn");
        sb.draw(
                tex,
                player.drawX + (NotesPanel.NOTE_SPACING * NotesPanel.DEFAULT_X_MULTIPLIER) + 32 * Settings.scale,
                (notesPanel.yOffset + NotesPanel.EXTRA_OFFSET) * Settings.scale + player.drawY + player.hb_h / 2.0f,
                0,
                0,
                tex.getRegionWidth(),
                tex.getRegionHeight(),
                -Settings.scale * 2,
                Settings.scale * 2,
                0
        );

        if (show) {
            FontHelper.renderFontLeftTopAligned(
                    sb,
                    FontHelper.tipHeaderFont,
                    NotesPanel.performStrings.TEXT[4],
                    Settings.WIDTH - 310 * Settings.scale,
                    Settings.HEIGHT - Y_POS * Settings.scale,
                    Settings.GOLD_COLOR
            );

            StringBuilder body = new StringBuilder();
            for (AbstractMelody melody : MelodyManager.getAllMelodies()) {
                if (melody.length() <= noteQueue.getMaxNotes()) {
                    if(melody.getNotes().size() == noteQueue.getMaxNotes()) {
                        body.append(melody.getNotes().get(0).cardCode() + " x" + noteQueue.getMaxNotes());
                    } else {
                        body.append(melody.makeNotesUIString());
                    }
                    body.append(" NL ");
                }
            }
            body.setLength(body.length() - 4);

            FontHelper.renderSmartText(
                    sb,
                    FontHelper.tipBodyFont,
                    body.toString(),
                    Settings.WIDTH - 310 * Settings.scale,
                    Settings.HEIGHT - (Y_POS + 30) * Settings.scale,
                    280 * Settings.scale,
                    26 * Settings.scale,
                    Settings.CREAM_COLOR
            );

            int skipped = 0;
            float y = Settings.HEIGHT - (Y_POS + 36) * Settings.scale;
            for (AbstractMelody melody : MelodyManager.getAllMelodies()) {
                if (melody.length() <= noteQueue.getMaxNotes()) {
                    Color color = Settings.CREAM_COLOR;
                    if (noteQueue.canPlayMelody(melody)) {
                        color = Settings.GOLD_COLOR;
                    }
                    FontHelper.renderFontRightAligned(
                            sb,
                            FontHelper.tipBodyFont,
                            melody.getName(),
                            Settings.WIDTH - 10 * Settings.scale,
                            y,
                            color
                    );
                    y -= 26 * Settings.scale;
                } else {
                    ++skipped;
                }
            }
            if (skipped > 0) {
                // "+X more" text
                FontHelper.renderFontRightAligned(
                        sb,
                        FontHelper.tipBodyFont,
                        String.format(NotesPanel.performStrings.TEXT[5], skipped),
                        300 * Settings.scale,
                        y,
                        Settings.CREAM_COLOR
                );
            }

            // Toggle tooltip
            if (melodiesToggleHb.hovered && !AbstractDungeon.isScreenUp) {
                float height = -FontHelper.getSmartHeight(FontHelper.tipBodyFont, "", 280.0F * Settings.scale, 26.0F * Settings.scale);
                height += 74 * Settings.scale; // accounts for header height, box border, and a bit of spacing

                TipHelper.renderGenericTip(
                        melodiesToggleHb.x,
                        melodiesToggleHb.y + melodiesToggleHb.height + height,
                        NotesPanel.performStrings.TEXT[3],
                        ""
                );
            }

            melodiesToggleHb.render(sb);

            if (show) {
                // Hitboxes / Melody tooltips
                skipped = 0;
                List<AbstractMelody> allMelodies = MelodyManager.getAllMelodies();
                for (int i = 0; i < melodyHbs.size(); ++i) {
                    if (allMelodies.get(i).length() <= noteQueue.getMaxNotes()) {
                        Hitbox hb = melodyHbs.get(i);
                        if (hb.hovered && !AbstractDungeon.isScreenUp) {
                            AbstractMelody m = allMelodies.get(i);
                            TipHelper.renderGenericTip(
                                    hb.x,
                                    InputHelper.mY,
                                    m.getName(),
                                    m.makeNotesUIString() + " NL " + m.getDescription().replace("!M!", Integer.toString(m.getMagicNumber()))
                            );
                        }

                        hb.render(sb);
                    } else {
                        ++skipped;
                    }
                }
                if (skipped > 0) {
                    if (extraMelodiesHb.hovered && !AbstractDungeon.isScreenUp) {
                        TipHelper.renderGenericTip(
                                extraMelodiesHb.x + extraMelodiesHb.width,
                                InputHelper.mY,
                                NotesPanel.performStrings.TEXT[6],
                                NotesPanel.performStrings.TEXT[7]
                        );
                    }
                    extraMelodiesHb.render(sb);
                }
            }
        }
    }

    private class MelodiesHitboxListener implements HitboxListener {
        private MelodiesHitboxListener() {
        }

        public void hoverStarted(Hitbox hitbox) {
        }

        public void startClicking(Hitbox hitbox) {
        }

        public void clicked(Hitbox hitbox) {
            MelodiesPanel.this.toggleShow();
        }
    }
}
