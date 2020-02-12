package paleoftheancients.reimu.util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.powers.InvinciblePower;
import paleoftheancients.PaleMod;
import paleoftheancients.finarubossu.actions.GuaranteePowerApplicationAction;
import paleoftheancients.reimu.monsters.Reimu;
import paleoftheancients.reimu.powers.DeathBombPower;

public class ReimuUserInterface {
    private static final String[] TEXT = CardCrawlGame.languagePack.getUIString(PaleMod.makeID("ReimuUserInterface")).TEXT;
    public int extralives;
    public int bombs;
    public int bombfragments;

    private Texture lifeimage, bombimage;
    private Texture[] bombfragmentimages;
    private final float scale;
    private Reimu reimu;
    private Hitbox bombhitbox, liveshitbox;

    private float lifewidth, lifeheight, bombwidth;

    public ReimuUserInterface(Reimu reimu) {
        this.extralives = 2;
        this.bombs = 3;
        this.bombfragments = 0;

        this.scale = Settings.scale;
        this.reimu = reimu;

        lifeimage = ImageMaster.loadImage(PaleMod.assetPath("images/reimu/ui/extralife.png"));
        bombimage = ImageMaster.loadImage(PaleMod.assetPath("images/reimu/ui/bomb.png"));
        bombfragmentimages = new Texture[4];
        for(int i = 0; i < bombfragmentimages.length; i++) {
            bombfragmentimages[i] = ImageMaster.loadImage(PaleMod.assetPath("images/reimu/ui/bomb" + (i + 1) + ".png"));
        }

        lifewidth = lifeimage.getWidth() * this.scale;
        lifeheight = lifeimage.getHeight() * this.scale;
        bombwidth = bombimage.getWidth() * this.scale;

        bombhitbox = new Hitbox(reimu.drawX - reimu.hb.width / 2F, reimu.drawY, 0, bombimage.getHeight() * scale);
        liveshitbox = new Hitbox(bombhitbox.x, reimu.drawY, 0, lifeheight);
    }

    public void update() {
        bombhitbox.width = bombwidth * (bombs + (bombfragments > 0 ? 1 : 0));
        liveshitbox.width = lifewidth * extralives;

        bombhitbox.update();
        liveshitbox.update();
    }

    public void render(SpriteBatch sb) {
        sb.setColor(Color.WHITE);
        float leftBorder = reimu.drawX - reimu.hb.width / 2F;
        for(int i = 0; i < extralives; i++) {
            sb.draw(lifeimage, leftBorder + i * lifewidth, reimu.drawY + lifeheight, 0, 0, lifeimage.getWidth(), lifeimage.getHeight(), this.scale, this.scale, 0, 0, 0, lifeimage.getWidth(), lifeimage.getHeight(), false, false);
        }
        for(int i = 0; i < bombs; i++) {
            sb.draw(bombimage, leftBorder + i * bombwidth, reimu.drawY, 0, 0, bombimage.getWidth(), bombimage.getHeight(), this.scale, this.scale, 0, 0, 0, bombimage.getWidth(), bombimage.getHeight(), false, false);
        }
        if(bombfragments > 0) {
            sb.draw(bombfragmentimages[bombfragments - 1], leftBorder + bombs * bombwidth, reimu.drawY, 0, 0, bombimage.getWidth(), bombimage.getHeight(), this.scale, this.scale, 0, 0, 0, bombimage.getWidth(), bombimage.getHeight(), false, false);
        }
        liveshitbox.render(sb);
        bombhitbox.render(sb);
        if(liveshitbox.hovered) {
            TipHelper.renderGenericTip(leftBorder, reimu.drawY, TEXT[0], TEXT[2]);
        } else if(bombhitbox.hovered) {
            TipHelper.renderGenericTip(leftBorder, reimu.drawY, TEXT[1], TEXT[3]);
        }
    }

    public void getBombFragment() {
        if(++bombfragments > 4) {
            bombfragments -= 5;
            addBomb();
        }
    }

    public void addBomb() {
        this.addBomb(1);
    }
    public void addBomb(int num) {
        bombs += num;
        if(extralives <= 1 && !reimu.hasPower(DeathBombPower.POWER_ID) && !reimu.hasPower(InvinciblePower.POWER_ID)) {
            AbstractDungeon.actionManager.addToBottom(new GuaranteePowerApplicationAction(reimu, reimu, new DeathBombPower(reimu)));
        }
    }

    public void dispose() {
        lifeimage.dispose();
        bombimage.dispose();
        for(int i = 0; i < bombfragmentimages.length; i++) {
            bombfragmentimages[i].dispose();
        }
    }
}
