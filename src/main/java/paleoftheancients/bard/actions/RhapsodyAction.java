package paleoftheancients.bard.actions;

import paleoftheancients.bard.helpers.MelodyCard;
import paleoftheancients.bard.helpers.MelodyManager;
import paleoftheancients.bard.melodies.AbstractMelody;
import paleoftheancients.bard.monsters.NoteQueue;
import paleoftheancients.bard.notes.AbstractNote;
import paleoftheancients.bard.powers.SonataPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.*;

public class RhapsodyAction extends AbstractGameAction {
    private List<AbstractMelody> melodies;
    private boolean pickCard = false;
    private NoteQueue noteQueue;

    public RhapsodyAction(AbstractCreature source, NoteQueue nq) {
        this.actionType = ActionType.SPECIAL;
        this.duration = Settings.ACTION_DUR_MED;

        this.noteQueue = nq;
        this.source = source;
    }

    public void update() {
        if (this.duration == Settings.ACTION_DUR_MED) {
            if (this.melodies == null) {
                this.melodies = new ArrayList();
                List<AbstractNote> allNotes = MelodyManager.getAllNotes();
                List<AbstractMelody> allMelodies = MelodyManager.getAllMelodies();
                Iterator var3 = allMelodies.iterator();

                while(true) {
                    if (!var3.hasNext()) {
                        if (this.melodies.isEmpty()) {
                            this.isDone = true;
                            return;
                        }
                        break;
                    }

                    AbstractMelody melody = (AbstractMelody)var3.next();
                    boolean good = true;
                    Iterator var6 = allNotes.iterator();

                    while(var6.hasNext()) {
                        AbstractNote note = (AbstractNote)var6.next();
                        int melodyCount = melody.count(note.getClass());
                        int queueCount = noteQueue.count(note.getClass());
                        if (queueCount < melodyCount) {
                            good = false;
                            break;
                        }
                    }

                    if (good) {
                        this.melodies.add(melody.makeCopy());
                    }
                }
            }

            CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            Iterator var12 = this.melodies.iterator();

            while(var12.hasNext()) {
                AbstractMelody melody = (AbstractMelody)var12.next();
                group.addToTop(melody.makeChoiceCard());
            }


            if (this.source.hasPower(SonataPower.POWER_ID)) {
                Iterator var2 = group.group.iterator();

                while(var2.hasNext()) {
                    AbstractCard c = (AbstractCard)var2.next();
                    MelodyCard select = (MelodyCard)c;
                    this.callback(select);
                }
            } else {
                this.callback(SelectMelodyAction.getLongestNotequeueCard(group));
            }
            this.isDone = true;
        }

        this.tickDuration();
    }

    private void callback(MelodyCard select) {
        select.consumeNotes = false;
        select.use(AbstractDungeon.player, null);

        // Manually consume notes
        for (AbstractNote melodyNote : select.notes) {
            Iterator<AbstractNote> iter = noteQueue.iterator();
            while (iter.hasNext()) {
                AbstractNote note = iter.next();
                if (note.getClass().equals(melodyNote.getClass())) {
                    iter.remove();
                    break;
                }
            }
        }
    }
}
