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
    private boolean ending;

    private final float spacing = 1.3F;
    private float r, g, b;

    public SpellCircleVFX(Reimu reimu) {
        this.reimu = reimu;
        this.rotation = MathUtils.random() * (float)Math.PI * 2;

        this.circle = ImageMaster.loadImage(PaleMod.assetPath(path));
        this.scale = reimu.hb.height * spacing / circle.getHeight();
        this.circlewidth = circle.getWidth();
        this.circleheight = circle.getHeight();

        this.color = Color.SKY.cpy();
        this.color.a = 0F;
        r = this.color.r;
        g = this.color.g;
        b = this.color.b;

        this.renderBehind = true;
    }

    @Override
    public void update() {
        this.rotation += Gdx.graphics.getDeltaTime() * 50;

        if(!ending) {
            if (this.color.a < 0.7F) {
                this.color.a += Gdx.graphics.getDeltaTime() * 2;
            }
            if(r != this.color.r) {
                this.color.r = attuneColor(r, this.color.r);
            }
            if(g != this.color.g) {
                this.color.g = attuneColor(g, this.color.g);
            }
            if(b != this.color.b) {
                this.color.b = attuneColor(b, this.color.b);
            }
        } else {
            this.color.a -= Gdx.graphics.getDeltaTime() * 2;
            if(this.color.a <= 0F) {
                this.isDone = true;
            }
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

    public void end() {
        this.ending = true;
    }
    public void changeColor(float r, float g, float b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    private float attuneColor(float target, float current) {
        if(target < current) {
            current -= Gdx.graphics.getDeltaTime();
            if(target > current) {
                current = target;
            }
        } else {
            current += Gdx.graphics.getDeltaTime();
            if(target < current) {
                current = target;
            }
        }
        return current;
    }
}
