package paleoftheancients.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.monsters.beyond.AwakenedOne;
import javassist.CtBehavior;
import paleoftheancients.wokeone.monsters.WokeOne;

@SpirePatch(
        clz = AwakenedOne.class,
        method = "damage"
)
public class WokeBlokePatch {
    @SpireInsertPatch(
            locator = Locator.class
    )
    public static SpireReturn<Void> Insert(AwakenedOne __instance, DamageInfo info) {
        if(__instance instanceof WokeOne) {
            return SpireReturn.Return(null);
        }
        return SpireReturn.Continue();
    }

    private static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(AwakenedOne.class, "currentHealth");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }
}
