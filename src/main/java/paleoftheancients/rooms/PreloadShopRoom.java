package paleoftheancients.rooms;

import paleoftheancients.bard.helpers.AssetLoader;
import paleoftheancients.finarubossu.monsters.N;
import paleoftheancients.scenes.PreloadBottomScene;
import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.rooms.ShopRoom;
import com.megacrit.cardcrawl.shop.Merchant;

import java.util.ArrayList;
import java.util.Collections;

public class PreloadShopRoom extends ShopRoom {
    public void onPlayerEntry() {
        super.onPlayerEntry();
        AssetLoader.preloadAtlas(PreloadBottomScene.atlasUrl);
        AssetLoader.preloadAtlas(N.atlasFilepath);

        ArrayList<String> idleMessages = (ArrayList<String>) ReflectionHacks.getPrivate(merchant, Merchant.class, "idleMessages");
        idleMessages.clear();

        Collections.addAll(idleMessages, Merchant.ENDING_TEXT);
    }
}
