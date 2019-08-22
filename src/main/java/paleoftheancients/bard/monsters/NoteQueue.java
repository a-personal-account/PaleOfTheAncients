package paleoftheancients.bard.monsters;

import paleoftheancients.PaleMod;
import paleoftheancients.bard.actions.RemoveNoteFromQueueAction;
import paleoftheancients.bard.actions.SelectMelodyAction;
import paleoftheancients.bard.helpers.MelodyManager;
import paleoftheancients.bard.hooks.OnNoteQueuedHook;
import paleoftheancients.bard.melodies.AbstractMelody;
import paleoftheancients.bard.notes.AbstractNote;
import paleoftheancients.bard.ui.MelodiesPanel;
import paleoftheancients.bard.ui.NotesPanel;
import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.bard.BardMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.*;
import java.util.function.Predicate;

public class NoteQueue extends CustomMonster {
    public static final String ID = PaleMod.makeID("Notequeue");
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;

    private NotesPanel notePanel;
    private MelodiesPanel melodiesPanel;
    public BardBoss owner;

    private Map<Byte, EnemyMoveInfo> moves;
    private static final byte BARRICADE = 0;

    public NoteQueue(BardBoss owner) {
        super(NAME, ID, 650, 0.0F, -20.0F, 390.0F, 0F, (String) null, -225.0F, 410.0F);

        this.moves = new HashMap<>();
        this.moves.put(BARRICADE, new EnemyMoveInfo(BARRICADE, Intent.DEFEND_BUFF, -1, 0, false));

        this.halfDead = true;
        this.hideHealthBar();

        this.notePanel = new NotesPanel(this);
        this.melodiesPanel = new MelodiesPanel(this, notePanel);
        this.owner = owner;

        this.setMove((byte) 0, Intent.NONE);
    }


    @Override
    public void takeTurn() {
        AbstractDungeon.actionManager.addToBottom(new SelectMelodyAction(this));
    }

    @Override
    public void getMove(int num) {}

    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
        NAME = monsterStrings.NAME;
        MOVES = monsterStrings.MOVES;
        DIALOG = monsterStrings.DIALOG;
    }


    private int masterMaxNotes = 8;
    private int maxNotes = masterMaxNotes;
    private Deque<AbstractNote> notes = new ArrayDeque<>();

    public int size() {
        return (int) notes.stream()
                .filter(AbstractNote::countsAsNote)
                .count();
    }

    public int getMaxNotes() {
        return maxNotes;
    }

    public void increaseMaxNotes(int amount) {
        maxNotes += amount;
    }

    public void setMasterMaxNotes(int amount) {
        masterMaxNotes = amount;
    }

    public Iterator<AbstractNote> iterator() {
        return notes.iterator();
    }

    public void clear() {
        notes.clear();
    }

    public void reset() {
        clear();
        maxNotes = masterMaxNotes;
        while (notes.size() > maxNotes) {
            notes.pollFirst();
        }
    }

    public void queue(AbstractNote note) {
        if (note.countsAsNote()) {
            for (AbstractPower power : this.owner.powers) {
                if (power instanceof OnNoteQueuedHook) {
                    note = ((OnNoteQueuedHook) power).onNoteQueued(note);
                    if (note == null) {
                        break;
                    }
                }
            }

        }
        if (note != null) {
            notes.addLast(note);
            if (masterMaxNotes == 0 && maxNotes == 0) {
                setMasterMaxNotes(NotesPanel.BARD_MAX_NOTES);
                increaseMaxNotes(NotesPanel.BARD_MAX_NOTES);
            }
            while (notes.size() > maxNotes) {
                notes.removeFirst();
            }
        }
    }

    public boolean removeNote(int index) {
        if (index < 0 || index >= notes.size()) {
            return false;
        }

        Iterator<AbstractNote> iter = notes.iterator();
        int i = 0;
        while (iter.hasNext()) {
            iter.next();
            if (i == index) {
                iter.remove();
                for (AbstractGameAction action : AbstractDungeon.actionManager.actions) {
                    if (action instanceof RemoveNoteFromQueueAction) {
                        ((RemoveNoteFromQueueAction) action).removed(index);
                    }
                }
                return true;
            }
            ++i;
        }
        return false;
    }

    public boolean removeNotesIf(Predicate<? super AbstractNote> pred) {
        return notes.removeIf(pred);
    }

    public int count(Class<? extends AbstractNote> type) {
        int count = 0;
        for (AbstractNote note : notes) {
            if (note.isNoteType(type)) {
                ++count;
            }
        }
        return count;
    }

    public int uniqueCount() {
        Set<String> noteSet = new HashSet<>();
        for (AbstractNote note : notes) {
            if (note.countsAsNote()) {
                noteSet.add(note.ascii());
            }
        }
        return noteSet.size();
    }

    public List<String> getNotesForSaving() {
        List<String> noteNames = new ArrayList<>();
        for (AbstractNote note : notes) {
            noteNames.add(note.name() + " Note");
        }
        return noteNames;
    }

    public void loadNotes(List<String> noteNames) {
        if (noteNames == null) {
            return;
        }

        notes.clear();
        for (String noteName : noteNames) {
            AbstractNote note = MelodyManager.getNote(noteName);
            if (note != null) {
                notes.addLast(note);
            } else {
                BardMod.logger.warn("Failed to find note: " + noteName);
            }
        }
    }

    public int melodyPosition(AbstractMelody melody) {
        int endIndex = melody.endIndexOf(new ArrayList<>(notes));
        if (endIndex < 0) {
            return -1;
        }
        endIndex -= melody.length();
        return endIndex;
    }

    public boolean canPlayAnyMelody() {
        for (AbstractMelody melody : MelodyManager.getAllMelodies()) {
            if (canPlayMelody(melody)) {
                return true;
            }
        }
        return false;
    }

    public boolean canPlayMelody(AbstractMelody melody) {
        return melody.fuzzyMatchesNotes(new ArrayList<>(notes));
    }

    public List<AbstractMelody> getPlayableMelodies() {
        return MelodyManager.getAllMelodiesFromNotes(new ArrayList<>(notes));
    }

    @Override
    public void update() {
        super.update();
        this.melodiesPanel.update(this.owner);
        this.notePanel.update(this.owner);
    }

    @Override
    public void render(SpriteBatch sb) {
        //super.render(sb);
        this.melodiesPanel.render(sb, this.owner);
        this.notePanel.render(sb, this.owner);
    }
}
