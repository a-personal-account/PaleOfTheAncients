package paleoftheancients.patches;

import paleoftheancients.rooms.DejaVuRoom;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.SaveHelper;

@SpirePatch(
        clz = SaveHelper.class,
        method = "shouldSave"
)
public class NoSavesDuringDejaVuPatch {
    public static SpireReturn<Boolean> Prefix() {
        if(AbstractDungeon.nextRoom != null && AbstractDungeon.nextRoom.room instanceof DejaVuRoom) {
            return SpireReturn.Return(false);
        } else {
            return SpireReturn.Continue();
        }
    }
}
