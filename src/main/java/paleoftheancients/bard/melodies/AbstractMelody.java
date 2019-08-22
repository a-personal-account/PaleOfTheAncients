package paleoftheancients.bard.melodies;

import paleoftheancients.PaleMod;
import paleoftheancients.bard.actions.RemoveNoteFromQueueAction;
import paleoftheancients.bard.helpers.MelodyCard;
import paleoftheancients.bard.helpers.MelodyManager;
import paleoftheancients.bard.helpers.MelodyStrings;
import paleoftheancients.bard.hooks.OnMelodyPlayedHook;
import paleoftheancients.bard.monsters.BardBoss;
import paleoftheancients.bard.monsters.NoteQueue;
import paleoftheancients.bard.notes.AbstractNote;
import paleoftheancients.bard.powers.MelodyPotencyPower;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DescriptionLine;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class AbstractMelody {
    protected final String id;
    protected String name;
    protected String rawDescription;
    private String description = null;
    protected AbstractCard.CardType type;
    protected AbstractCard.CardTarget target;
    protected List<AbstractNote> notes = new ArrayList();
    public List<AbstractNote> getNotes() {return notes;}

    protected int baseMagicNumber;
    protected int magicNumber;
    public int getMagicNumber() {
        return this.magicNumber;
    }
    public static final float multiplier = 2F;
    public static int m(int val) {
        return (int)(val * multiplier);
    }

    public AbstractMelody(String ID, AbstractCard.CardTarget target) {
        this.id = ID;
        MelodyStrings melodyStrings = MelodyManager.getMelodyStrings(ID);
        this.name = melodyStrings.NAME;
        this.rawDescription = melodyStrings.DESCRIPTION;
        this.target = target;
        this.type = AbstractCard.CardType.POWER;
        if (melodyStrings.NOTES != null) {
            String[] var4 = melodyStrings.NOTES;
            int var5 = var4.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                String noteStr = var4[var6];
                AbstractNote note = MelodyManager.getNoteByAscii(noteStr);
                if (note == null) {
                    throw new RuntimeException("Invalid note: \"" + noteStr + "\"");
                }

                this.notes.add(note);
            }
        }
        this.baseMagicNumber = this.magicNumber = -1;
    }

    public AbstractMelody(String name, String rawDescription, AbstractCard.CardTarget target) {
        this.id = null;
        this.name = name;
        this.rawDescription = rawDescription;
        this.target = target;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder("Melody (");
        Iterator var2 = this.notes.iterator();

        while(var2.hasNext()) {
            AbstractNote note = (AbstractNote)var2.next();
            builder.append(note.ascii()).append(", ");
        }

        builder.setLength(builder.length() - 2);
        builder.append(")");
        return builder.toString();
    }

    public String getID() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        if (this.description == null) {
            StringBuilder builder = new StringBuilder();
            AbstractCard card = this.makeChoiceCard();
            Iterator var3 = card.description.iterator();

            while(var3.hasNext()) {
                DescriptionLine line = (DescriptionLine)var3.next();
                builder.append(line.getText()).append(" ");
            }

            builder.setLength(builder.length() - 1);
            this.description = builder.toString();
            this.description = this.description.replaceAll("\\*", "#y");
            this.description = this.description.replaceAll("(?<!#y)Inspiration", "#yInspiration");
        }

        return this.description;
    }

    public String makeNotesUIString() {
        StringBuilder builder = new StringBuilder();
        for (final AbstractNote note : notes) {
            builder.append(note.cardCode()).append(" ");
        }
        return builder.toString();
    }

    protected CustomCard.RegionName getRegionName() {
        String id2 = this.id.replaceFirst("^" + PaleMod.makeID(""), "");
        return new CustomCard.RegionName(String.format("%s/%s/melody%s", "bard", this.type.name().toLowerCase(), id2));
    }

    public AbstractCard makeChoiceCard() {
        this.applyPowers();
        AbstractCard ret = new MelodyCard(this.name, this.getRegionName(), this.rawDescription, new ArrayList(this.notes), this.target, this.magicNumber, this::doPlay);
        ret.type = this.type;
        return ret;
    }

    public int length() {
        return this.notes.size();
    }

    public int count(Class<? extends AbstractNote> type) {
        int count = 0;
        Iterator var3 = this.notes.iterator();

        while(var3.hasNext()) {
            AbstractNote note = (AbstractNote)var3.next();
            if (note.isNoteType(type)) {
                ++count;
            }
        }

        return count;
    }

    public boolean conflictsMelody(AbstractMelody other) {
        if (this.notes.size() == other.notes.size()) {
            return this.matchesNotes(other.notes);
        } else {
            AbstractMelody small;
            AbstractMelody large;
            if (this.notes.size() < other.notes.size()) {
                small = this;
                large = other;
            } else {
                small = other;
                large = this;
            }

            for(int i = 0; i < large.notes.size() - small.notes.size() + 1; ++i) {
                if (small.matchesNotes(large.notes.subList(i, i + small.notes.size()))) {
                    return true;
                }
            }

            return false;
        }
    }

    public boolean matchesNotes(List<AbstractNote> otherNotes) {
        if (this.notes.size() != otherNotes.size()) {
            return false;
        } else {
            for(int i = 0; i < this.notes.size(); ++i) {
                if (!((AbstractNote)this.notes.get(i)).getClass().equals(((AbstractNote)otherNotes.get(i)).getClass())) {
                    return false;
                }
            }

            return true;
        }
    }

    public boolean fuzzyMatchesNotes(List<AbstractNote> otherNotes) {
        return this.endIndexOf(otherNotes) != -1;
    }

    public int endIndexOf(List<AbstractNote> otherNotes) {
        if (otherNotes.size() < this.notes.size()) {
            return -1;
        } else {
            for(int i = 0; i < otherNotes.size() - this.notes.size() + 1; ++i) {
                if (this.notesMatch(this.notes, otherNotes.subList(i, i + this.notes.size()))) {
                    return i + this.notes.size();
                }
            }

            return -1;
        }
    }

    private boolean notesMatch(List<AbstractNote> lhs, List<AbstractNote> rhs) {
        for(int i = 0; i < lhs.size(); ++i) {
            if (!((AbstractNote)lhs.get(i)).equals(rhs.get(i))) {
                return false;
            }
        }

        return true;
    }

    protected void addToTop(AbstractGameAction action) {
        AbstractDungeon.actionManager.addToTop(action);
    }

    protected void addToBottom(AbstractGameAction action) {
        AbstractDungeon.actionManager.addToBottom(action);
    }

    public final void doPlay(boolean consumeNotes) {
        NoteQueue nq = (NoteQueue)AbstractDungeon.getCurrRoom().monsters.getMonster(NoteQueue.ID);
        Iterator var2 = nq.owner.powers.iterator();

        while(var2.hasNext()) {
            AbstractPower power = (AbstractPower)var2.next();
            if (power instanceof OnMelodyPlayedHook) {
                ((OnMelodyPlayedHook)power).onMelodyPlayed(this);
            }
        }

        /*
        var2 = AbstractDungeon.player.relics.iterator();

        while(var2.hasNext()) {
            AbstractRelic relic = (AbstractRelic)var2.next();
            if (relic instanceof OnMelodyPlayedHook) {
                ((OnMelodyPlayedHook)relic).onMelodyPlayed(this);
            }
        }*/
        this.applyPowers();
        this.play(nq.owner);
        if (consumeNotes) {
            int startIndex = nq.melodyPosition(this);
            this.addToBottom(new RemoveNoteFromQueueAction(nq, startIndex, this.notes.size()));
        }

    }

    public abstract void play(AbstractCreature target);

    public abstract AbstractMelody makeCopy();

    public void applyPowers() {
        float multi = 1F;
        if(AbstractDungeon.ascensionLevel >= 4) {
            multi += 0.1F;
        }
        if(AbstractDungeon.ascensionLevel >= 9) {
            multi += 0.2F;
        }
        if(AbstractDungeon.ascensionLevel >= 19) {
            multi += 0.4F;
        }
        this.magicNumber = (int)(this.baseMagicNumber * multi);

        AbstractMonster boss = AbstractDungeon.getCurrRoom().monsters.getMonster(BardBoss.ID);
        if(boss != null) {
            AbstractPower pow = boss.getPower(MelodyPotencyPower.POWER_ID);
            if(pow != null) {
                this.magicNumber = (int)(this.magicNumber * (1 + pow.amount / 100F));
            }
        }
    }
}
