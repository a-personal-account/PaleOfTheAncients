package paleoftheancients.hexaghost.patches;

import paleoftheancients.hexaghost.monsters.HexaghostFamiliar;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.exordium.Hexaghost;
import javassist.CtBehavior;

public class BodyRenderPatch {

    @SpirePatch(
            clz= Hexaghost.class,
            method="render"
    )
    public static class RenderPrefixPatch {
        public static void Prefix(Hexaghost __instance, SpriteBatch sb) {
            if(__instance instanceof HexaghostFamiliar) {
                ((HexaghostFamiliar)__instance).before = __instance.drawY;
                __instance.drawY -= 256F * Settings.scale;
            }
        }
    }

    @SpirePatch(
            clz= Hexaghost.class,
            method="render"
    )
    public static class RenderPatch {
        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void Insert(Hexaghost __instance, SpriteBatch sb) {
            if(__instance instanceof HexaghostFamiliar) {
                __instance.drawY = ((HexaghostFamiliar)__instance).before;
            }
        }
        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractMonster.class, "render");
                return new int[]{LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher)[0]};
            }
        }
    }
}
