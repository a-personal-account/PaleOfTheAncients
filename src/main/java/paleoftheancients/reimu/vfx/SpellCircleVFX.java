package paleoftheancients.reimu.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import paleoftheancients.PaleMod;
import paleoftheancients.reimu.monsters.Reimu;

public class SpellCircleVFX extends AbstractGameEffect {
    private static final String path = "images/reimu/vfx/spellcircle.png";

    private Reimu reimu;
    private Texture circle;
    private int circlewidth, circleheight;

    private final float spacing = 1.3F;

    public SpellCircleVFX(Reimu reimu) {
        this.reimu = reimu;
        this.rotation = MathUtils.random() * (float)Math.PI * 2;

        this.circle = ImageMaster.loadImage(PaleMod.assetPath(path));
        this.scale = reimu.hb.height * spacing / circle.getHeight();
        this.circlewidth = circle.getWidth();
        this.circleheight = circle.getHeight();

        this.color = Color.SKY.cpy();
        this.color.a = 0F;

        this.renderBehind = true;
    }

    @Override
    public void update() {
        this.rotation += Gdx.graphics.getDeltaTime() * 50;

        if (this.color.a < 0.7F) {
            this.color.a += Gdx.graphics.getDeltaTime() * 2;
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setColor(color);
        sb.setBlendFunction(770, 1);
        sb.draw(circle, reimu.drawX - circlewidth / 2F, reimu.hb.cY - circleheight / 2F, circlewidth / 2F, circleheight / 2F, circlewidth, circleheight, this.scale, this.scale, rotation, 0, 0, circlewidth, circleheight, false, false);
        sb.setColor(Color.WHITE);
        sb.setBlendFunction(770, 771);
    }

    @Override
    public void dispose() {
        this.circle.dispose();
    }
}
