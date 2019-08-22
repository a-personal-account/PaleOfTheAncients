package paleoftheancients.theshowman.patches;

import paleoftheancients.theshowman.powers.ColumbifyPower;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.powers.FlightPower;

import java.util.Iterator;

public class ColumbifyPatch {
    @SpirePatch(
            clz= AbstractPlayer.class,
            method="render"
    )
    public static class RenderPatch {
        public static SpireReturn Prefix(AbstractPlayer __instance, SpriteBatch sb) {
            if(ColumbifyPower.byrd != null) {

                __instance.renderHealth(sb);
                if (!__instance.orbs.isEmpty()) {
                    Iterator var2 = __instance.orbs.iterator();

                    while(var2.hasNext()) {
                        AbstractOrb o = (AbstractOrb)var2.next();
                        o.render(sb);
                    }
                }
                __instance.hb.render(sb);
                __instance.healthHb.render(sb);

                ColumbifyPower.byrd.render(sb);

                return SpireReturn.Return(null);
            } else {
                return SpireReturn.Continue();
            }
        }
    }

    @SpirePatch(
            clz= AbstractPlayer.class,
            method="update"
    )
    public static class UpdatePatch {
        public static void Prefix(AbstractPlayer __instance) {
            if(ColumbifyPower.byrd != null) {
                ColumbifyPower.byrd.update();
            }
        }
    }



    @SpirePatch(
            clz= FlightPower.class,
            method="onRemove"
    )
    public static class FlightPowerPatch {
        public static SpireReturn Prefix(FlightPower __instance) {
            if(__instance.owner instanceof AbstractPlayer) {
                if(ColumbifyPower.byrd != null) {
                    if(__instance.owner.hasPower(ColumbifyPower.POWER_ID)) {
                        ((ColumbifyPower)__instance.owner.getPower(ColumbifyPower.POWER_ID)).extend();
                    }
                    __instance.owner = ColumbifyPower.byrd;
                } else {
                    return SpireReturn.Return(null);
                }
            }
            return SpireReturn.Continue();
        }
    }
}
