package paleoftheancients.rooms;

import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.rooms.ShopRoom;
import com.megacrit.cardcrawl.shop.Merchant;
import paleoftheancients.finarubossu.monsters.N;
import paleoftheancients.helpers.AssetLoader;
import paleoftheancients.scenes.PreloadBottomScene;

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
