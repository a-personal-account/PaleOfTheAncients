package paleoftheancients.bard.patches;

import paleoftheancients.PaleMod;
import paleoftheancients.bard.helpers.MelodyManager;
import paleoftheancients.bard.notes.AbstractNote;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.helpers.FontHelper;

@SpirePatch(
        clz= FontHelper.class,
        method="identifyOrb"
)
public class IdentifyOrbNotes
{
    public static TextureAtlas.AtlasRegion Postfix(TextureAtlas.AtlasRegion __result, String word)
    {
        if(PaleMod.bardLoaded()) {
            return __result;
        }

        if (__result == null) {
            AbstractNote note = MelodyManager.getNote(word);
            if (note != null) {
                ColorIdentifyOrbNotes.isNote = note;
                return note.getTexture();
            }
        }
        return __result;
    }
}
