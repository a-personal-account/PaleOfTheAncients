package paleoftheancients.bard.actions;

import paleoftheancients.bard.monsters.NoteQueue;
import paleoftheancients.bard.notes.AbstractNote;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;

public class QueueNoteAction extends AbstractGameAction {
    protected AbstractNote note;
    protected NoteQueue nq;

    public QueueNoteAction(NoteQueue nq, AbstractNote note) {
        this.note = note;
        this.duration = Settings.ACTION_DUR_FAST;
        this.nq = nq;
    }

    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            nq.queue(this.note);
            if (Settings.FAST_MODE) {
                this.isDone = true;
                return;
            }
        }

        this.tickDuration();
    }
}
