package paleoftheancients.collector.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class BloodyChair extends AbstractGameEffect {
    private TextureAtlas.AtlasRegion throne;
    private TextureAtlas.AtlasRegion throneGlow;

    public BloodyChair() {
        TextureAtlas ta = new TextureAtlas(Gdx.files.internal("cityScene/scene.atlas"));
        this.throne = ta.findRegion("mod/throne");
        this.throneGlow = ta.findRegion("mod/throneGlow");
        this.renderBehind = true;
    }

    @Override
    public void update() {

    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setColor(Color.WHITE.cpy());
        sb.draw(this.throne.getTexture(), this.throne.offsetX * Settings.scale, this.throne.offsetY * Settings.scale + AbstractDungeon.sceneOffsetY, 0.0F, 0.0F, (float)this.throne.packedWidth, (float)this.throne.packedHeight, Settings.scale, Settings.scale, 0.0F, this.throne.getRegionX(), this.throne.getRegionY(), this.throne.getRegionWidth(), this.throne.getRegionHeight(), false, false);
        sb.setBlendFunction(770, 1);
        sb.draw(this.throneGlow.getTexture(), this.throneGlow.offsetX * Settings.scale, this.throneGlow.offsetY * Settings.scale + AbstractDungeon.sceneOffsetY, 0.0F, 0.0F, (float)this.throneGlow.packedWidth, (float)this.throneGlow.packedHeight, Settings.scale, Settings.scale, 0.0F, this.throneGlow.getRegionX(), this.throneGlow.getRegionY(), this.throneGlow.getRegionWidth(), this.throneGlow.getRegionHeight(), false, false);
        sb.setBlendFunction(770, 771);
    }

    @Override
    public void dispose() {

    }
}
