package paleoftheancients.patches;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import javassist.CtBehavior;
import paleoftheancients.helpers.OnReceivePowerRelic;

@SpirePatch(clz = ApplyPowerAction.class, method = "update")
public class OnReceivePowerRelicPatch {
    @SpireInsertPatch(locator = Locator.class)
    public static void Insert(ApplyPowerAction __instance) {
        if(__instance.target == AbstractDungeon.player) {
            AbstractPower pow = (AbstractPower) ReflectionHacks.getPrivate(__instance, ApplyPowerAction.class, "powerToApply");
            for(final AbstractRelic relic : AbstractDungeon.player.relics) {
                if(relic instanceof OnReceivePowerRelic) {
                    ((OnReceivePowerRelic) relic).onTrigger(pow);
                }
            }
        }
    }

    private static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractPlayer.class, "hasRelic");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }
}
