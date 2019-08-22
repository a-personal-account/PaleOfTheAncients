package paleoftheancients.rooms;

import paleoftheancients.bard.helpers.AssetLoader;
import paleoftheancients.finarubossu.monsters.N;
import paleoftheancients.scenes.PreloadBottomScene;
import com.megacrit.cardcrawl.rooms.TreasureRoom;

public class PreloadTreasureRoom extends TreasureRoom {

    public void onPlayerEntry() {
        super.onPlayerEntry();
        AssetLoader.preloadAtlas(PreloadBottomScene.atlasUrl);
        AssetLoader.preloadAtlas(N.atlasFilepath);
    }
}
