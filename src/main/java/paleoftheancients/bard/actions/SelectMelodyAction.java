package paleoftheancients.bard.actions;

import paleoftheancients.bard.helpers.MelodyCard;
import paleoftheancients.bard.melodies.AbstractMelody;
import paleoftheancients.bard.monsters.NoteQueue;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.*;

public class SelectMelodyAction extends AbstractGameAction {
    private List<AbstractMelody> melodies;
    private boolean consumeNotes;
    private boolean pickCard;
    private boolean canCancel;

    private NoteQueue noteQueue;

    public SelectMelodyAction(NoteQueue nq) {
        this(nq, (List)null);
    }

    public SelectMelodyAction(NoteQueue nq, List<AbstractMelody> melodies) {
        this(nq, melodies, true);
    }

    public SelectMelodyAction(NoteQueue nq, List<AbstractMelody> melodies, boolean consumeNotes) {
        this(nq, melodies, consumeNotes, true);
    }

    public SelectMelodyAction(NoteQueue nq, List<AbstractMelody> melodies, boolean consumeNotes, boolean canCancel) {
        this.pickCard = false;
        this.canCancel = true;
        this.melodies = melodies;
        this.consumeNotes = consumeNotes;
        this.canCancel = canCancel;
        this.actionType = ActionType.SPECIAL;
        this.duration = Settings.ACTION_DUR_MED;

        this.noteQueue = nq;
    }

    public void update() {
        if (this.duration != Settings.ACTION_DUR_MED) {
            if (this.pickCard && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
                this.pickCard = false;
                MelodyCard select = (MelodyCard)AbstractDungeon.gridSelectScreen.selectedCards.get(0);
                AbstractDungeon.gridSelectScreen.selectedCards.clear();

                select.consumeNotes = this.consumeNotes;
                select.use(AbstractDungeon.player, (AbstractMonster)null);
                this.isDone = true;
            }
        } else {
            if (this.melodies == null) {
                this.melodies = this.noteQueue.getPlayableMelodies();
                if (this.melodies == null || this.melodies.isEmpty()) {
                    this.isDone = true;
                    return;
                }
            }

            this.melodies = new ArrayList(this.melodies);

            this.melodies.removeIf((melodyx) -> {
                return melodyx.length() > noteQueue.getMaxNotes();
            });
            this.pickCard = true;
            CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            Iterator var3 = this.melodies.iterator();

            while(var3.hasNext()) {
                AbstractMelody melody = (AbstractMelody)var3.next();
                group.addToTop(melody.makeChoiceCard());
            }



            getLongestNotequeueCard(group).use(AbstractDungeon.player, null);
            AbstractDungeon.actionManager.addToBottom(new SelectMelodyAction(noteQueue));
            this.isDone = true;
        }

        this.tickDuration();
    }


    public static MelodyCard getLongestNotequeueCard(CardGroup group) {
        Map<Integer, CardGroup> cards = new HashMap<>();
        int highest = 0;
        for(final AbstractCard card : group.group) {
            int count = ((MelodyCard)card).notes.size();
            if(!cards.containsKey(count)) {
                cards.put(count, new CardGroup(CardGroup.CardGroupType.UNSPECIFIED));
                if(count > highest) {
                    highest = count;
                }
            }
            cards.get(count).addToTop(card);
        }
        return (MelodyCard)(cards.get(highest).getRandomCard(AbstractDungeon.aiRng));
    }
}
