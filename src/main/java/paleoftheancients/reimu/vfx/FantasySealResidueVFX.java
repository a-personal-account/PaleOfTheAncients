package paleoftheancients.reimu.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import paleoftheancients.PaleMod;
import paleoftheancients.helpers.AssetLoader;
import paleoftheancients.reimu.monsters.Reimu;

public class FantasySealResidueVFX extends AbstractGameEffect {
    private Texture orb;
    private int orbwidth, orbheight;
    private float targetScale;

    private Reimu source;
    private float timer;

    private Seal[] seals;

    public FantasySealResidueVFX(float x, float y) {

        this.orb = AssetLoader.loadImage(PaleMod.assetPath("images/reimu/vfx/fantasyseal.png"));
        this.orbwidth = orb.getWidth();
        this.orbheight = orb.getHeight();

        this.scale = Settings.scale / 3;

        seals = new Seal[] {
                new Seal(x + MathUtils.random(-orbwidth, orbwidth) / 4F, y + MathUtils.random(-orbheight, orbheight) / 4F, Color.BLUE.cpy()),
                new Seal(x + MathUtils.random(-orbwidth, orbwidth) / 4F, y + MathUtils.random(-orbheight, orbheight) / 4F, Color.GREEN.cpy()),
                new Seal(x + MathUtils.random(-orbwidth, orbwidth) / 4F, y + MathUtils.random(-orbheight, orbheight) / 4F, Color.RED.cpy())
        };
    }

    @Override
    public void update() {
        for(final Seal s : seals) {
            s.update();
        }
        if(seals[0].color.a <= 0F) {
            this.isDone = true;
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setBlendFunction(770, 1);
        for(final Seal s : seals) {
            s.render(sb);
        }
        sb.setBlendFunction(770, 771);
    }

    @Override
    public void dispose() {

    }

    class Seal {
        private float x, y, vX, vY;
        private float rotation, rotationVelocity;
        public Color color;

        public Seal(float x, float y, Color color) {
            this.x = x;
            this.y = y;

            vX = MathUtils.random(-50F, 50F) * Settings.scale;
            vY = MathUtils.random(-50F, 50F) * Settings.scale;

            this.color = color;
        }

        public void update() {
            rotation += rotationVelocity * Gdx.graphics.getDeltaTime();
            x += vX * Gdx.graphics.getDeltaTime();
            y += vY * Gdx.graphics.getDeltaTime();

            color.a -= Gdx.graphics.getDeltaTime() / 2F;
        }

        public void render(SpriteBatch sb) {
            sb.setColor(color);

            sb.draw(orb, x - orbwidth / 2F, y - orbheight / 2F, orbwidth / 2F, orbheight / 2F, orbwidth, orbheight, scale, scale, rotation, 0, 0, orbwidth, orbheight, false, false);

            sb.setColor(Color.WHITE);
        }
    }
}
