package paleoftheancients.watcher.intent;

import paleoftheancients.PaleMod;
import paleoftheancients.RazIntent.CustomIntent;
import paleoftheancients.watcher.monsters.TheWatcher;

public class BlasphemyIntent extends CustomIntent {

    public BlasphemyIntent() {
        super(WatcherIntentEnums.BlasphemyIntent, TheWatcher.MOVES[TheWatcher.BLASPHEMY],
                PaleMod.assetPath("images/ui/intent/blasphemy_L.png"),
                PaleMod.assetPath("images/ui/intent/blasphemy.png"));
        this.simpledescription = "";
    }
}
