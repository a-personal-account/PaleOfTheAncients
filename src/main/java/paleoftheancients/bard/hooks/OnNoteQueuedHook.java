package paleoftheancients.bard.hooks;

import paleoftheancients.bard.notes.AbstractNote;

public interface OnNoteQueuedHook {
    AbstractNote onNoteQueued(AbstractNote var1);
}
