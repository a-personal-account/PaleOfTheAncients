package paleoftheancients.patches;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.screens.DeathScreen;
import com.megacrit.cardcrawl.screens.DungeonMapScreen;
import javassist.CtBehavior;
import paleoftheancients.helpers.FakeDeathScreen;
import paleoftheancients.rooms.FixedMonsterRoom;

@SpirePatch(
        clz = MapRoomNode.class,
        method = "render"
)
public class MapRoomNodeRelicTipPatch {
    @SpireInsertPatch(
            locator = Locator.class
    )
    public static void Insert(MapRoomNode __instance, SpriteBatch sb) {
        if(__instance.hb.hovered && __instance.room instanceof FixedMonsterRoom) {
            ((FixedMonsterRoom) __instance.room).relicTips(sb
                    , __instance.x * (float)ReflectionHacks.getPrivateStatic(MapRoomNode.class, "SPACING_X") + (float)ReflectionHacks.getPrivateStatic(MapRoomNode.class, "OFFSET_X") - 64F + __instance.hb.width * 2 + __instance.offsetX
                    , __instance.y * Settings.MAP_DST_Y + (float)ReflectionHacks.getPrivateStatic(MapRoomNode.class, "OFFSET_Y") + DungeonMapScreen.offsetY - 96.0F + __instance.hb.height * 2 + __instance.offsetY);
        }
    }

    private static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(Hitbox.class, "render");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }
}
