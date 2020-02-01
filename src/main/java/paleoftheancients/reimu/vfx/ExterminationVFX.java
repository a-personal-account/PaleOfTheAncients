package paleoftheancients.reimu.vfx;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import paleoftheancients.PaleMod;
import paleoftheancients.helpers.AssetLoader;
import paleoftheancients.reimu.monsters.Reimu;

public class ExterminationVFX extends NeedleAttackVFX {
    private static final String path = "images/reimu/vfx/needle_extermination.png";

    public ExterminationVFX(AbstractCreature target, Reimu reimu, DamageInfo info, int num) {
        super(AssetLoader.loadImage(PaleMod.assetPath(path)), Settings.scale, target, reimu.hb.x, reimu.hb.cY, info, num, info.output * num / 20 + 1);
    }

    @Override
    protected void needleFinished(Needle n) {
        AbstractDungeon.effectsQueue.add(new ExterminationAfterVFX(n.x, n.y, 360F - n.getRotation()));
    }

    public static void disposeAll() {
        AbstractDamagingVFX.disposeTry(PaleMod.assetPath(path));
    }
}
