package paleoftheancients.reimu.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import paleoftheancients.PaleMod;
import paleoftheancients.helpers.AssetLoader;
import paleoftheancients.reimu.vfx.AbstractDamagingVFX;
import paleoftheancients.reimu.vfx.Needle;
import paleoftheancients.reimu.vfx.NeedleAttackVFX;

public class PersuasionNeedleAction extends AbstractDamagingAction {
    private static final String path = "images/reimu/vfx/needle_regular.png";
    public PersuasionNeedleAction(AbstractCreature target, AbstractCreature source, DamageInfo info, int num) {
        super(target, source, info, num);
    }
    @Override
    public void update() {
        AbstractDungeon.effectList.add(new NeedleAttackVFX(AssetLoader.loadImage(PaleMod.assetPath(path)), Settings.scale, target, source.hb.x, source.hb.cY, info, num, info.output * num / 4) {
            @Override
            protected void needleFinished(Needle n) {
                AbstractDungeon.effectsQueue.add(new FlashAtkImgEffect(n.x, n.y, AbstractGameAction.AttackEffect.BLUNT_LIGHT));
            }
        });
        this.isDone = true;
    }

    public static void disposeAll() {
        AbstractDamagingVFX.disposeTry(PaleMod.assetPath(path));
    }
}
