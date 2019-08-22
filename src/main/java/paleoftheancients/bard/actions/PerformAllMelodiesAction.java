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

import java.util.Iterator;
import java.util.List;

public class PerformAllMelodiesAction extends AbstractGameAction {
    private List<AbstractMelody> melodies;
    private boolean consumeNotes;
    private NoteQueue nq;

    public PerformAllMelodiesAction(NoteQueue nq) {
        this(nq, (List)null);
    }

    public PerformAllMelodiesAction(NoteQueue nq, List<AbstractMelody> melodies) {
        this(nq, melodies, true);
    }

    public PerformAllMelodiesAction(NoteQueue nq, List<AbstractMelody> melodies, boolean consumeNotes) {
        this.melodies = melodies;
        this.consumeNotes = consumeNotes;
        this.actionType = ActionType.SPECIAL;
        this.duration = Settings.ACTION_DUR_MED;
        this.nq = nq;
    }

    public void update() {
        if (this.duration == Settings.ACTION_DUR_MED) {
            if (this.melodies == null) {
                this.melodies = nq.getPlayableMelodies();
                if (this.melodies == null || this.melodies.isEmpty()) {
                    this.isDone = true;
                    return;
                }
            }

            CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            Iterator var2 = this.melodies.iterator();

            while(var2.hasNext()) {
                AbstractMelody melody = (AbstractMelody)var2.next();
                group.addToTop(melody.makeChoiceCard());
            }

            Iterator var3 = group.group.iterator();

            while(var3.hasNext()) {
                AbstractCard c = (AbstractCard)var3.next();
                MelodyCard select = (MelodyCard)c;
                select.consumeNotes = this.consumeNotes;
                select.use(AbstractDungeon.player, (AbstractMonster)null);
            }
            this.isDone = true;
        }

        this.tickDuration();
    }
}
