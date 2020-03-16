package paleoftheancients.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import javassist.CtBehavior;
import paleoftheancients.relics.PaleRelic;

@SpirePatch(
        clz = AbstractRelic.class,
        method = SpirePatch.CONSTRUCTOR
)
public class OptimizeRelicTipsPatch {
    @SpireInsertPatch(
            locator = Locator.class
    )
    public static SpireReturn<Void> Insert(AbstractRelic __instance, String setId, String imgName, AbstractRelic.RelicTier tier, AbstractRelic.LandingSound sfx) {
        if(__instance instanceof PaleRelic) {
            return SpireReturn.Return(null);
        } else {
            return SpireReturn.Continue();
        }
    }

    private static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractRelic.class, "initializeTips");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }
}
