package paleoftheancients.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import paleoftheancients.dungeons.PaleOfTheAncients;

@SpirePatch(clz = CardCrawlGame.class, method = "startOverButShowCredits")
public class NoCutsceneAfterCreditsPatch {
    @SpirePostfixPatch
    public static void Prefix() {
        if(AbstractDungeon.id == PaleOfTheAncients.ID) {
            CardCrawlGame.playCreditsBgm = false;
        }
    }
}