package paleoftheancients.bard.helpers;

import paleoftheancients.PaleMod;
import paleoftheancients.bard.melodies.*;
import paleoftheancients.bard.notes.*;
import com.badlogic.gdx.Gdx;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.megacrit.cardcrawl.cards.AbstractCard;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class MelodyManager {
    private static List<AbstractNote> allNotes = new ArrayList();
    private static Map<String, AbstractNote> notes = new HashMap();
    private static Map<AbstractCard.CardTags, AbstractNote> noteTags = new HashMap();
    private static List<AbstractMelody> melodies = new ArrayList();
    private static Map<String, MelodyStrings> melodyStrings = new HashMap();

    public MelodyManager() {
    }

    public static void addNote(AbstractNote note) {
        allNotes.add(note);
        notes.put(note.name() + " Note", note);
        notes.put(note.cardCode(), note);
        noteTags.put(note.cardTag(), note);
    }

    public static AbstractNote getNoteByAscii(String key)
    {
        for (AbstractNote note : allNotes) {
            if (note.ascii().equals(key)) {
                return note;
            }
        }
        return null;
    }

    public static AbstractNote getNoteByTag(AbstractCard.CardTags tag)
    {
        return noteTags.get(tag);
    }

    public static AbstractNote getNote(String key)
    {
        return notes.get(key);
    }

    public static List<AbstractNote> getAllNotes()
    {
        List<AbstractNote> ret = new ArrayList<>(allNotes);
        ret.removeIf(n -> !n.countsAsNote());
        ret.removeIf(n -> n instanceof WildCardNote);
        return ret;
    }

    public static int getAllNotesCount()
    {
        int count = 0;
        for (AbstractNote note : allNotes) {
            if (note.countsAsNote() && !(note instanceof WildCardNote)) {
                ++count;
            }
        }
        return count;
    }

    public static void addMelody(AbstractMelody melody) {
        melodies.add(melody);
    }

    public static AbstractMelody getMelodByID(String id)
    {
        for (AbstractMelody melody : melodies) {
            if (melody.getID().equals(id)) {
                return melody.makeCopy();
            }
        }
        return null;
    }

    public static List<AbstractMelody> getAllMelodies()
    {
        return melodies;
    }

    public static AbstractMelody getMelodyFromNotes(List<AbstractNote> notes)
    {
        for (AbstractMelody melody : melodies) {
            if (melody.matchesNotes(notes)) {
                return melody.makeCopy();
            }
        }
        return null;
    }

    public static List<AbstractMelody> getAllMelodiesFromNotes(List<AbstractNote> notes)
    {
        List<AbstractMelody> ret = new ArrayList<>();
        for (AbstractMelody melody : melodies) {
            int idx = melody.endIndexOf(notes);
            if (idx != -1) {
                ret.add(melody.makeCopy());
            }
        }
        return ret;
    }

    public static void loadMelodyStrings(String filepath) {
        Gson gson = new Gson();
        Type melodyType = new TypeToken<Map<String, MelodyStrings>>(){}.getType();

        Map<String, MelodyStrings> notes = gson.fromJson(loadJson(PaleMod.assetPath("bard/MelodyNotes.json")), melodyType);
        Map<String, MelodyStrings> map = gson.fromJson(loadJson(filepath), melodyType);

        for (String key : map.keySet()) {
            if (notes.containsKey(key)) {
                map.get(key).NOTES = notes.get(key).NOTES;
            }
        }

        melodyStrings.putAll(map);
    }

    private static String loadJson(String jsonPath) {
        return Gdx.files.internal(jsonPath).readString(String.valueOf(StandardCharsets.UTF_8));
    }

    public static MelodyStrings getMelodyStrings(String melodyID) {
        return melodyStrings.get(melodyID);
    }

    public static void initializeNotes() {
        notes.clear();
        MelodyManager.addNote(AttackNote.get());
        MelodyManager.addNote(BlockNote.get());
        MelodyManager.addNote(BuffNote.get());
        MelodyManager.addNote(DebuffNote.get());
        MelodyManager.addNote(RestNote.get());
        MelodyManager.addNote(WildCardNote.get());
    }
    public static void initializeMelodies() {
        melodies.clear();
        MelodyManager.addMelody(new DamageSmallMelody());
        MelodyManager.addMelody(new DamageMediumMelody());
        MelodyManager.addMelody(new DamageLargeMelody());
        MelodyManager.addMelody(new BlockSmallMelody());
        MelodyManager.addMelody(new BlockMediumMelody());
        MelodyManager.addMelody(new BlockLargeMelody());
        MelodyManager.addMelody(new AttackUpSmallMelody());
        MelodyManager.addMelody(new DefenseUpSmallMelody());
        MelodyManager.addMelody(new InspireSmallMelody());
        MelodyManager.addMelody(new InspireLargeMelody());
        MelodyManager.addMelody(new WeakenSmallMelody());
        MelodyManager.addMelody(new VulnerabilitySmallMelody());
        MelodyManager.addMelody(new DrawMelody());
        MelodyManager.addMelody(new ArtifactMelody());
        MelodyManager.addMelody(new DivineProtectionMelody());
        MelodyManager.addMelody(new Cacophony());
    }
}
