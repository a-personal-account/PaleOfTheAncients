package paleoftheancients.theshowman.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class VanDeGraaffVFX extends AbstractGameEffect {
    private float x;
    private float y;
    private float targetX;
    private float targetY;
    private AbstractCreature targetMonster;
    private TextureAtlas.AtlasRegion img;
    private boolean playedSound = false;

    public VanDeGraaffVFX(float x, float y, float targetX, float targetY, float duration, AbstractCreature targetMonster) {
        this.img = ImageMaster.GLOW_SPARK_2;
        this.x = x - (float)this.img.packedWidth / 2.0F;
        this.y = y - (float)this.img.packedHeight / 2.0F;
        this.targetX = targetX;
        this.targetY = targetY;
        this.startingDuration = duration;
        this.duration = duration;
        this.targetMonster = targetMonster;
        this.scale = 3.0F * Settings.scale;
        this.color = Color.WHITE.cpy();
    }

    private void playSfX() {
        CardCrawlGame.sound.play("ORB_LIGHTNING_CHANNEL", 0.1F);
    }

    public void update() {
        if (!this.playedSound) {
            this.playSfX();
            this.playedSound = true;
        }

        this.duration -= Gdx.graphics.getDeltaTime();
        this.x = MathUtils.lerp(this.x, this.targetX, Gdx.graphics.getDeltaTime() * 5.0F);
        this.y = MathUtils.lerp(this.y, this.targetY, Gdx.graphics.getDeltaTime() * 5.0F);
        if (this.duration <= 0.0F) {
            AbstractDungeon.actionManager.addToTop(new VFXAction(new LightningJumpVFX(this.targetX, this.targetY, this.targetMonster.hb.cX, this.targetMonster.hb.cY, true), 0.0F));
            this.isDone = true;
        }

    }

    public void render(SpriteBatch sb) {
        sb.setColor(this.color);
        sb.draw(this.img, this.x, this.y, (float)this.img.packedWidth / 2.0F, (float)this.img.packedHeight / 2.0F, (float)this.img.packedWidth, (float)this.img.packedHeight, this.scale, this.scale, this.rotation);
    }

    public void dispose() {
    }
}
