package paleoftheancients.theshowman.ui;

import basemod.abstracts.CustomEnergyOrb;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import paleoftheancients.PaleMod;
import paleoftheancients.theshowman.monsters.TheShowmanBoss;

public class ShowmanEnergyOrb extends CustomEnergyOrb {
    private final float ORB_IMG_SCALE = 1.0F * Settings.scale;
    private final String rabbitTextureString = PaleMod.assetPath("images/TheShowman/character/orb/rabbit.png");
    private final Texture rabbitTexture = new Texture(rabbitTextureString);
    private final String hatBackString = PaleMod.assetPath("images/TheShowman/character/orb/orb_hat_back.png");
    private final Texture hatBackTexture = new Texture(hatBackString);
    private static final String[] orbTextures = new String[]{
            PaleMod.assetPath("images/TheShowman/character/orb/layer1.png"),
            PaleMod.assetPath("images/TheShowman/character/orb/layer2.png"),
            PaleMod.assetPath("images/TheShowman/character/orb/layer3.png"),
            PaleMod.assetPath("images/TheShowman/character/orb/layer4.png"),
            PaleMod.assetPath("images/TheShowman/character/orb/layer5.png"),
            PaleMod.assetPath("images/TheShowman/character/orb/layer6.png"),
            PaleMod.assetPath("images/TheShowman/character/orb/layer1d.png"),
            PaleMod.assetPath("images/TheShowman/character/orb/layer2d.png"),
            PaleMod.assetPath("images/TheShowman/character/orb/layer3d.png"),
            PaleMod.assetPath("images/TheShowman/character/orb/layer4d.png"),
            PaleMod.assetPath("images/TheShowman/character/orb/layer5d.png")
    };
    private static final String orbVFXPath = PaleMod.assetPath("images/TheShowman/character/orb/vfx.png");
    private boolean renderRabbit = false;
    private float YShift;
    private TheShowmanBoss owner;

    public ShowmanEnergyOrb(TheShowmanBoss owner) {
        super(orbTextures, orbVFXPath, (float[])null);
        this.renderRabbit = false;
        this.YShift = 0.0F;
        this.owner = owner;
    }

    public void updateOrb(int energyCount) {
        super.updateOrb(energyCount);
        if (this.angles[1] > 360.0F) {
            while(this.angles[1] > 360.0F) {
                float[] var10000 = this.angles;
                var10000[1] -= 360.0F;
            }
        }

        if (this.angles[1] > 90.0F && this.angles[1] < 270.0F) {
            this.renderRabbit = true;
            if (this.angles[1] > 90.0F && this.angles[1] < 120.0F) {
                this.YShift = Interpolation.linear.apply(0.0F, 85.0F, (this.angles[1] - 90.0F) / 30.0F);
            } else if (this.angles[1] > 240.0F && this.angles[1] < 270.0F) {
                this.YShift = Interpolation.linear.apply(85.0F, 0.0F, (this.angles[1] - 240.0F) / 30.0F);
            }
        } else {
            this.renderRabbit = false;
        }

    }

    public void renderOrb(SpriteBatch sb, boolean enabled, float current_x, float current_y) {
        sb.draw(hatBackTexture, current_x - 64.0F, current_y - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, ORB_IMG_SCALE, ORB_IMG_SCALE, this.angles[1], 0, 0, 128, 128, false, false);
        if (this.renderRabbit) {
            Vector2 vector = new Vector2(0.0F, this.YShift);
            vector.rotate(this.angles[1] - 180.0F);
            vector.scl(Settings.scale);
            sb.draw(rabbitTexture, current_x - 64.0F + vector.x, current_y - 64.0F + vector.y, 64.0F, 64.0F, 128.0F, 128.0F, ORB_IMG_SCALE, ORB_IMG_SCALE, this.angles[1] - 180.0F, 0, 0, 128, 128, false, false);
        }

        super.renderOrb(sb, enabled, current_x, current_y);

        FontHelper.renderFontCentered(sb, FontHelper.energyNumFontBlue, "" + this.owner.curenergy, current_x, current_y, Color.WHITE, 1.3F);
    }


    public void dispose() {
        hatBackTexture.dispose();
        rabbitTexture.dispose();

        baseLayer.dispose();
        for(final Texture t : energyLayers) {
            t.dispose();
        }
        for(final Texture t : noEnergyLayers) {
            t.dispose();
        }
        orbVfx.dispose();
    }
}
