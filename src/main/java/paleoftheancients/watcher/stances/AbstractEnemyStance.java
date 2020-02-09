package paleoftheancients.watcher.stances;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.StanceStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.stances.AbstractStance;
import com.megacrit.cardcrawl.vfx.stance.StanceAuraEffect;

public abstract class AbstractEnemyStance extends AbstractStance {
    protected AbstractMonster owner;
    public AbstractEnemyStance(AbstractMonster owner) {
        this.owner = owner;
    }

    @Override
    public void render(SpriteBatch sb) {
        if (this.img != null) {
            sb.setColor(this.c);
            sb.setBlendFunction(770, 1);
            sb.draw(this.img, owner.drawX - 256.0F + owner.animX, owner.drawY - 256.0F + owner.animY + owner.hb_h / 2.0F, 256.0F, 256.0F, 512.0F, 512.0F, Settings.scale, Settings.scale, -this.angle, 0, 0, 512, 512, true, false);
            sb.setBlendFunction(770, 771);
        }
    }

    protected static StanceStrings getStanceString(Class clz) {
        return (StanceStrings) ReflectionHacks.getPrivateStatic(clz, "stanceString");
    }

    protected void AdjustStanceAuraEffect(StanceAuraEffect sae) {
        TextureAtlas.AtlasRegion img = (TextureAtlas.AtlasRegion) ReflectionHacks.getPrivate(sae, StanceAuraEffect.class, "img");
        ReflectionHacks.setPrivate(sae, StanceAuraEffect.class, "x", owner.hb.cX + MathUtils.random(-owner.hb.width / 16.0F, owner.hb.width / 16.0F) - img.packedWidth / 2F);
        ReflectionHacks.setPrivate(sae, StanceAuraEffect.class, "y", owner.hb.cY + MathUtils.random(-owner.hb.height / 16.0F, owner.hb.height / 12.0F) - img.packedHeight / 2F);
    }
}
