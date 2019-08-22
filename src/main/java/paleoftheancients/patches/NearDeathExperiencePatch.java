package paleoftheancients.patches;

import paleoftheancients.relics.Timepiece;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import javassist.CtBehavior;

@SpirePatch(
        clz = AbstractPlayer.class,
        method =  "damage"
)
public class NearDeathExperiencePatch {
    @SpireInsertPatch(
            locator = Locator.class
    )
    public static SpireReturn<Void> Insert(AbstractPlayer __instance, DamageInfo info) {
        Timepiece piece = (Timepiece)__instance.getRelic(Timepiece.ID);
        if(piece != null && piece.counter > -2) {
            piece.onTrigger();

            return SpireReturn.Return(null);
        }

        return SpireReturn.Continue();
    }


    private static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractPlayer.class, "isDead");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }
}
