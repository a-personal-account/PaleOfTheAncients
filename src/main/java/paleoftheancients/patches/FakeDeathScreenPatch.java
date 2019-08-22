package paleoftheancients.patches;

import paleoftheancients.helpers.FakeDeathScreen;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.screens.DeathScreen;
import javassist.CtBehavior;

@SpirePatch(
        clz = DeathScreen.class,
        method = SpirePatch.CONSTRUCTOR
)
public class FakeDeathScreenPatch {
    @SpireInsertPatch(
            locator = Locator.class
    )
    public static SpireReturn<Void> Insert(DeathScreen __instance, MonsterGroup m) {
        if(__instance instanceof FakeDeathScreen) {
            FakeDeathScreen.init(__instance);
            return SpireReturn.Return(null);
        } else {
            return SpireReturn.Continue();
        }
    }

    private static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractDungeon.class, "getCurrRoom");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }
}
