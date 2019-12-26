package paleoftheancients.reimu.util;

import basemod.BaseMod;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import paleoftheancients.PaleMod;
import paleoftheancients.reimu.monsters.Reimu;

public class ReimuUserInterface {
    public int extralives;
    public int bombs;

    private Texture lifeimage, bombimage;
    private final float scale;

    float lifewidth, lifeheight, bombwidth, bombheight;

    public ReimuUserInterface() {
        this.extralives = 2;
        this.bombs = 3;

        this.scale = Settings.scale;

        lifeimage = ImageMaster.loadImage(PaleMod.assetPath("images/reimu/vfx/extralife.png"));
        bombimage = ImageMaster.loadImage(PaleMod.assetPath("images/reimu/vfx/bomb.png"));

        lifewidth = lifeimage.getWidth() * this.scale;
        lifeheight = lifeimage.getHeight() * this.scale;
        bombwidth = bombimage.getWidth() * this.scale;
        bombheight = bombimage.getHeight() * this.scale;
    }

    public boolean justAFleshWound() {
        if(--this.extralives >= 0) {
            this.bombs = 3;
            return true;
        }
        return false;
    }

    public void render(SpriteBatch sb, Reimu reimu) {
        sb.setColor(Color.WHITE);
        float leftBorder = reimu.drawX - reimu.hb.width / 2F;
        for(int i = 0; i < extralives; i++) {
            sb.draw(lifeimage, leftBorder + i * lifeimage.getWidth() * scale, reimu.drawY + lifeimage.getHeight() * scale, lifewidth / 2, lifeheight / 2, lifewidth, lifeheight, this.scale, this.scale, 0, 0, 0, lifeimage.getWidth(), lifeimage.getHeight(), false, false);
        }
        for(int i = 0; i < bombs; i++) {
            sb.draw(bombimage, leftBorder + i * lifeimage.getWidth() * scale, reimu.drawY, bombwidth / 2, bombheight / 2, bombwidth, bombheight, this.scale, this.scale, 0, 0, 0, bombimage.getWidth(), bombimage.getHeight(), false, false);
        }
    }

    public void dispose() {
        lifeimage.dispose();
        bombimage.dispose();
    }
}
