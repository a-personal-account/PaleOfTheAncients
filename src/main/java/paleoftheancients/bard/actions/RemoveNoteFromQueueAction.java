package paleoftheancients.bard.actions;

import paleoftheancients.bard.monsters.NoteQueue;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class RemoveNoteFromQueueAction extends AbstractGameAction {
    private int startIndex;
    private int count;
    private boolean currentlyRemoving = false;
    private NoteQueue nq;

    public RemoveNoteFromQueueAction(NoteQueue nq, int startIndex, int count) {
        this.actionType = ActionType.SPECIAL;
        this.duration = Settings.ACTION_DUR_XFAST;
        this.startIndex = startIndex;
        this.count = count;
        this.nq = nq;
    }

    public void removed(int index) {
        if (!this.currentlyRemoving && this.count > 0) {
            if (index >= this.startIndex && index < this.startIndex + this.count) {
                --this.count;
            } else if (index >= 0 && index < this.startIndex) {
                --this.startIndex;
            }

        }
    }

    public void update() {
        if (this.count > 0) {
            this.currentlyRemoving = true;

            for(int i = 0; i < this.count; ++i) {
                nq.removeNote(this.startIndex);
            }

            this.currentlyRemoving = false;
            AbstractDungeon.player.hand.applyPowers();
        }

        this.isDone = true;
    }
}
