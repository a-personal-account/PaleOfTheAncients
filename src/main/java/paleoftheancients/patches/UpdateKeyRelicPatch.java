package paleoftheancients.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.ObtainKeyEffect;
import paleoftheancients.relics.KeyRelic;

@SpirePatch(
        clz = ObtainKeyEffect.class,
        method = "update"
)
public class UpdateKeyRelicPatch {
    @SpirePostfixPatch
    public static void Postfix(ObtainKeyEffect __instance) {
        if(__instance.isDone) {
            AbstractRelic relic = AbstractDungeon.player.getRelic(KeyRelic.ID);
            if(relic != null) {
                ((KeyRelic) relic).updateDesc();
            }
        }
    }
}
