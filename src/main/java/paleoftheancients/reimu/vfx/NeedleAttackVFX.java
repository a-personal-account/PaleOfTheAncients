package paleoftheancients.reimu.vfx;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import paleoftheancients.PaleMod;
import paleoftheancients.helpers.AssetLoader;
import paleoftheancients.reimu.monsters.Reimu;

import java.util.ArrayList;

public class NeedleAttackVFX extends AbstractDamagingVFX {
    private static final String path = "images/reimu/vfx/needle_regular.png";
    private Texture needleimage;

    private Reimu source;
    private ArrayList<Needle> needles;

    public NeedleAttackVFX(AbstractCreature target, Reimu source, DamageInfo info, int num) {
        super(target, info, num);
        needleimage = AssetLoader.loadImage(PaleMod.assetPath(path));

        this.source = source;

        this.setDelay();
    }

    @Override
    public void update() {
        if(needles == null) {
            needles = new ArrayList<>();
            for(int i = info.output * num / 4; i >= 0; i--) {
                Needle n = new Needle(needleimage, target, source.hb.x, source.hb.cY);
                needles.add(n);
                AbstractDungeon.effectsQueue.add(n);
            }
        } else {
            for(int i = needles.size() - 1; i >= 0; i--) {
                Needle n = needles.get(i);
                if(n.isDone) {
                    needles.remove(i);
                    if(needles.isEmpty()) {
                        this.isDone = true;
                        AbstractDungeon.effectsQueue.add(new FlashAtkImgEffect(n.x, n.y, AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                    }
                    if(delay != null) {
                        endDelay();
                        delay = null;
                        for(; num > 0; num--) {
                            AbstractDungeon.actionManager.addToTop(new DamageAction(target, info, AbstractGameAction.AttackEffect.NONE));
                        }
                    }
                }
            }
        }
    }

    @Override
    public void render(SpriteBatch sb) {

    }

    @Override
    public void dispose() {

    }


    public static void disposeAll() {
        try {
            AssetLoader.unLoad(PaleMod.assetPath(path));
        } catch (GdxRuntimeException ex) {}
    }
}
