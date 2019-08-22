package paleoftheancients.patches;

import paleoftheancients.scenes.PreloadBottomScene;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.scenes.AbstractScene;

@SpirePatch(
        clz = AbstractScene.class,
        method = SpirePatch.CONSTRUCTOR
)
public class ScenePreloadedPatch {
    public static SpireReturn<Void> Prefix(AbstractScene __instance, String atlasUrl) {
        if(__instance instanceof PreloadBottomScene) {
            return SpireReturn.Return(null);
        } else {
            return SpireReturn.Continue();
        }
    }
}
