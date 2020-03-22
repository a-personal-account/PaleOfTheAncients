package paleoftheancients.patches;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import paleoftheancients.relics.SoulOfTheBandit;

@SpirePatch(
        clz = AbstractPlayer.class,
        method = "renderRelics"
)
public class BanditRelicPatch {
    @SpirePrefixPatch
    public static void Prefix(AbstractPlayer __instance, SpriteBatch sb) {
        AbstractRelic relic = __instance.getRelic(SoulOfTheBandit.ID);
        if(relic != null) {
            ((SoulOfTheBandit) relic).renderBoard(sb);
        }
    }
}
