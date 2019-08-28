package paleoftheancients.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.ui.buttons.ProceedButton;
import javassist.CtBehavior;
import paleoftheancients.dungeons.PaleOfTheAncients;
import paleoftheancients.rooms.PaleVictoryRoom;

@SpirePatch(
        clz = ProceedButton.class,
        method = "update"
)
public class ContinueOntoHeartPatch {
    @SpireInsertPatch(
            locator = Locator.class
    )

    public static void Insert(ProceedButton __instance) {
        //Mostly copied from the basegame and applied to custom dungeons on or higher than Beyond's level.
        if (AbstractDungeon.getCurrRoom() instanceof MonsterRoomBoss) {
            if (CardCrawlGame.dungeon instanceof PaleOfTheAncients) {
                if (!Settings.isEndless) {
                    CardCrawlGame.music.fadeOutBGM();
                    MapRoomNode node = new MapRoomNode(3, 4);
                    node.room = new PaleVictoryRoom();
                    AbstractDungeon.nextRoom = node;
                    AbstractDungeon.closeCurrentScreen();
                    AbstractDungeon.nextRoomTransitionStart();
                    __instance.hide();
                }
            }
        }
    }

    private static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.InstanceOfMatcher(MonsterRoomBoss.class);
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }
}
