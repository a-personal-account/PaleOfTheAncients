package paleoftheancients.reimu.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import paleoftheancients.PaleMod;
import paleoftheancients.helpers.AssetLoader;
import paleoftheancients.reimu.monsters.Reimu;

public class SpellCardDeclarationVFX extends AbstractGameEffect {
    public static String textpath = "images/reimu/vfx/spellcard_text.png";
    public static String reimupath = "images/reimu/vfx/Reimu";

    private Texture text, reimuimage;
    private Reimu reimu;

    private int textwidth, textheight, reimuwidth, reimuheight;

    private float verticalTextX, verticalTextY;
    private float horizontalTextX, horizontalTextY;
    private float reimuX, reimuY;
    private float timer;
    private final float timePerPhase;
    private final float targetAlpha;

    private int step;

    public SpellCardDeclarationVFX(Reimu reimu, int num) {
        this.reimu = reimu;

        reimuimage = AssetLoader.loadImage(PaleMod.assetPath(reimupath + num + ".png"));
        text = AssetLoader.loadImage(PaleMod.assetPath(textpath));

        this.scale *= 1.3F;

        textwidth = text.getWidth();
        textheight = text.getHeight();
        reimuwidth = reimuimage.getWidth();
        reimuheight = reimuimage.getHeight();

        verticalTextX = Settings.WIDTH * 0.85F;
        verticalTextY = MathUtils.random(textheight);

        horizontalTextX = MathUtils.random(textwidth);
        horizontalTextY = Settings.HEIGHT * 0.1F;

        reimuX = Settings.WIDTH * 0.85F - reimuwidth / 2;
        reimuY = Settings.HEIGHT * -0.3F;

        this.color = Color.WHITE.cpy();
        this.color.a = 0F;

        targetAlpha = 0.75F;
        timePerPhase = 0.66F;

        step = 0;
    }

    @Override
    public void update() {
        reimuY += Gdx.graphics.getDeltaTime() * 66F * scale;

        horizontalTextX += Gdx.graphics.getDeltaTime() * 200F * scale;
        horizontalTextY += Gdx.graphics.getDeltaTime() * 33F * scale;
        if(horizontalTextX > textwidth * scale) {
            horizontalTextX -= textwidth * scale;
        }

        verticalTextX -= Gdx.graphics.getDeltaTime() * 33F * scale;
        verticalTextY += Gdx.graphics.getDeltaTime() * 200F * scale;
        if(verticalTextY > textwidth * scale) {
            verticalTextY -= textwidth * scale;
        }

        switch(step) {
            case 0:
                this.color.a += Gdx.graphics.getDeltaTime() * targetAlpha * timePerPhase;
                if(this.color.a >= targetAlpha) {
                    step++;
                    timer = timePerPhase;
                }
                break;
            case 1:
                timer -= Gdx.graphics.getDeltaTime();
                if(timer <= 0F) {
                    step++;
                }
                break;
            case 2:
                this.color.a -= Gdx.graphics.getDeltaTime() * targetAlpha * timePerPhase;
                if(this.color.a <= 0F) {
                    this.isDone = true;
                }
                break;
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setColor(color);
        sb.draw(reimuimage, reimuX, reimuY, reimuwidth / 2F, 0, reimuwidth, reimuheight, this.scale, this.scale, 0, 0, 0, reimuwidth, reimuheight, true, false);
        for(int i = -1; i < 2; i++) {
            sb.draw(text, verticalTextX, verticalTextY + i * textwidth * scale, 0, textheight / 2F, textwidth, textheight, this.scale, this.scale, 270, 0, 0, textwidth, textheight, false, false);
        }
        for(int i = -1; i < 3; i++) {
            sb.draw(text, horizontalTextX + i * textwidth * scale, horizontalTextY, 0, textheight / 2F, textwidth, textheight, this.scale, this.scale, 0, 0, 0, textwidth, textheight, false, false);
        }
        sb.setColor(Color.WHITE);
    }

    @Override
    public void dispose() {}

    public static void disposeAll() {
        try {
            AssetLoader.unLoad(PaleMod.assetPath(textpath));
        } catch (GdxRuntimeException ex) {}
        for(int i = 1; i < 5; i++) {
            try {
                AssetLoader.unLoad(PaleMod.assetPath(reimupath + i + ".png"));
            } catch (GdxRuntimeException ex) {}
        }
    }
}
