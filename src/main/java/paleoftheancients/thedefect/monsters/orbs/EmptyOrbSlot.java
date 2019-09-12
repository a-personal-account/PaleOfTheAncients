package paleoftheancients.thedefect.monsters.orbs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import paleoftheancients.PaleMod;
import paleoftheancients.thedefect.monsters.TheDefectBoss;

public class EmptyOrbSlot extends AbstractBossOrb {
    public static final String ID = PaleMod.makeID("emptyorbslot");

    private static Texture img1;
    private static Texture img2;

    public EmptyOrbSlot(TheDefectBoss tdb) {
        super(tdb, "", ID);
        if (img1 == null) {
            img1 = ImageMaster.loadImage("images/orbs/empty1.png");
            img2 = ImageMaster.loadImage("images/orbs/empty2.png");
        }

        this.angle = MathUtils.random(360.0F);
        this.evokeAmount = 0;
        this.drawX =  tdb.drawX + tdb.hb_x;
        this.drawY = tdb.drawY + tdb.hb_y + tdb.hb_h / 2.0F;
        this.channelAnimTimer = 0.5F;

        this.halfDead = true;
    }

    public void updateAnimations() {
        super.updateAnimations();
        this.angle += Gdx.graphics.getDeltaTime() * 10.0F;
    }

    public void render(SpriteBatch sb) {
        sb.setColor(this.c);
        sb.draw(img2, this.hb.cX - 48.0F - this.bobEffect.y / 8.0F, this.hb.cY - 48.0F + this.bobEffect.y / 8.0F, 48.0F, 48.0F, 96.0F, 96.0F, this.scale, this.scale, 0.0F, 0, 0, 96, 96, false, false);
        sb.draw(img1, this.hb.cX - 48.0F + this.bobEffect.y / 8.0F, this.hb.cY - 48.0F - this.bobEffect.y / 8.0F, 48.0F, 48.0F, 96.0F, 96.0F, this.scale, this.scale, this.angle, 0, 0, 96, 96, false, false);
    }

    public void updateDescription() {}

    public void die() {
        super.die();
    }

    public void playChannelSFX() {
    }
}
