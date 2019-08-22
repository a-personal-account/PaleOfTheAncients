package paleoftheancients.thevixen.hooks;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import javassist.CtBehavior;

@SpirePatch(
        clz = ApplyPowerAction.class,
        method = "update"
)
public class ReceivePowerHook {


    @SpireInsertPatch(
            locator = Locator.class
    )
    public static void Insert(ApplyPowerAction __instance) {
        for(final AbstractPower power : __instance.target.powers) {
            if(power instanceof OnReceivePower) {
                ((OnReceivePower) power).onReceivePower((AbstractPower) ReflectionHacks.getPrivate(__instance, ApplyPowerAction.class, "powerToApply"));
            }
        }
    }

    private static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.NewExprMatcher(FlashAtkImgEffect.class);
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }
}
