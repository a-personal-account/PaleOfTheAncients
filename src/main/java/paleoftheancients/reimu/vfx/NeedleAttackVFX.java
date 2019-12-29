package paleoftheancients.reimu.vfx;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;

public abstract class NeedleAttackVFX extends AbstractDamagingVFX {
    private Texture needleimage;

    private ArrayList<Needle> needles;

    private float startX, startY;
    private int needlecount;

    public NeedleAttackVFX(Texture tex, float scale, AbstractCreature target, float startX, float startY, DamageInfo info, int num, int needlecount) {
        super(target, info, num);
        needleimage = tex;

        this.startX = startX;
        this.startY = startY;

        this.scale = scale;
        this.needlecount = needlecount;

        this.setDelay();
    }

    @Override
    public void update() {
        if(needles == null) {
            needles = new ArrayList<>();
            for(int i = needlecount; i >= 0; i--) {
                Needle n = new Needle(needleimage, scale, target.hb, startX, startY);
                needles.add(n);
                AbstractDungeon.effectsQueue.add(n);
            }
        } else {
            for(int i = needles.size() - 1; i >= 0; i--) {
                Needle n = needles.get(i);
                if(n.isDone) {
                    needles.remove(i);
                    needleFinished(n);
                    if(needles.isEmpty()) {
                        this.isDone = true;
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

    protected abstract void needleFinished(Needle n);

    @Override
    public void render(SpriteBatch sb) {

    }

    @Override
    public void dispose() {

    }
}
