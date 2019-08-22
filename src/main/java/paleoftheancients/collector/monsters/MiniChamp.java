package paleoftheancients.collector.monsters;

import paleoftheancients.PaleMod;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.city.Champ;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class MiniChamp extends Champ {
    public static final String ID = PaleMod.makeID("MiniChamp");
    private static float divisor = 2.3F;

    public MiniChamp(float x, float y) {
        super();
        this.id = ID;
        this.drawX = (float) Settings.WIDTH * 0.75F + x * Settings.scale;
        this.drawY = AbstractDungeon.floorY + y * Settings.scale;
        this.hb_w /= divisor;
        this.hb_h /= divisor;
        this.hb.width /= divisor;
        this.hb.height /= divisor;

        this.setHp(this.maxHealth / 4);

        this.powers.add(new StrengthPower(this, -5));
    }

    @Override
    protected void loadAnimation(String atlasUrl, String skeletonUrl, float scale) {
        super.loadAnimation(atlasUrl, skeletonUrl, scale * divisor);
    }

    @Override
    public void damage(DamageInfo info) {
        super.damage(info);
    }

    @Override
    public void die() {
        super.die(true);
    }
}
