package paleoftheancients.rooms;

import com.megacrit.cardcrawl.rooms.TreasureRoom;
import paleoftheancients.finarubossu.monsters.N;
import paleoftheancients.helpers.AssetLoader;
import paleoftheancients.scenes.PreloadBottomScene;

public class PreloadTreasureRoom extends TreasureRoom {

    public void onPlayerEntry() {
        super.onPlayerEntry();
        AssetLoader.preloadAtlas(PreloadBottomScene.atlasUrl);
        AssetLoader.preloadAtlas(N.atlasFilepath);
    }
}
