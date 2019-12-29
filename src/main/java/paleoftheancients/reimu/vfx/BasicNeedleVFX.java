package paleoftheancients.reimu.vfx;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import paleoftheancients.PaleMod;
import paleoftheancients.helpers.AssetLoader;
import paleoftheancients.reimu.monsters.Reimu;

public class BasicNeedleVFX extends NeedleAttackVFX {
    private static final String path = "images/reimu/vfx/needle_regular.png";

    public BasicNeedleVFX(AbstractCreature target, Reimu reimu, DamageInfo info, int num) {
        super(AssetLoader.loadImage(PaleMod.assetPath(path)), Settings.scale, target, reimu.hb.x, reimu.hb.cY, info, num, info.output * num / 4);
    }

    @Override
    protected void needleFinished(Needle n) {
        AbstractDungeon.effectsQueue.add(new FlashAtkImgEffect(n.x, n.y, AbstractGameAction.AttackEffect.BLUNT_LIGHT));
    }

    public static void disposeAll() {
        AbstractDamagingVFX.disposeTry(PaleMod.assetPath(path));
    }
}
