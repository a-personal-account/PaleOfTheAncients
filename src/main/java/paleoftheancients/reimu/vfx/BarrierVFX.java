package paleoftheancients.reimu.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import paleoftheancients.PaleMod;
import paleoftheancients.helpers.AssetLoader;
import paleoftheancients.reimu.monsters.Reimu;

public class BarrierVFX extends AbstractGameEffect {
    private static final String path = "images/reimu/vfx/spellBarrier.png";

    private boolean ending;

    private Reimu reimu;
    private Texture barrier;
    private int barrierwidth, barrierheight;

    private final float spacing = 1.4F;

    public BarrierVFX(Reimu reimu) {
        this.reimu = reimu;
        this.ending = false;
        this.rotation = MathUtils.random() * (float)Math.PI * 2;

        this.barrier = AssetLoader.loadImage(PaleMod.assetPath(path));
        this.scale = reimu.hb.height * spacing / barrier.getHeight();
        this.barrierwidth = barrier.getWidth();
        this.barrierheight = barrier.getHeight();

        this.color = Color.WHITE.cpy();
        this.color.a = 0F;
    }

    @Override
    public void update() {
        this.rotation += Gdx.graphics.getDeltaTime() * 10;

        if(!ending) {
            if (this.color.a < 1F) {
                this.color.a += Gdx.graphics.getDeltaTime() * 2;
            } else {
                this.color.a = 1F;
            }
        } else {
            if(this.color.a > 0F) {
                this.color.a -= Gdx.graphics.getDeltaTime() * 2;
            } else {
                this.isDone = true;
            }
        }
    }

    public void end() {
        this.ending = true;
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setColor(color);
        sb.setBlendFunction(770, 1);
        sb.draw(barrier, reimu.drawX - barrierwidth / 2F, reimu.hb.cY - barrierheight / 2F, barrierwidth / 2, barrierheight / 2, barrierwidth, barrierheight, this.scale, this.scale, rotation, 0, 0, barrier.getWidth(), barrier.getHeight(), false, false);
        sb.setColor(Color.WHITE);
        sb.setBlendFunction(770, 771);
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
