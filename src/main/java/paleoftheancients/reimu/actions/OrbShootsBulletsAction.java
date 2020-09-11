package paleoftheancients.reimu.actions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import paleoftheancients.PaleMod;
import paleoftheancients.reimu.monsters.Reimu;
import paleoftheancients.reimu.monsters.YinYangOrb;
import paleoftheancients.reimu.util.FantasyHeavenBullet;

import java.util.ArrayList;

public class OrbShootsBulletsAction extends AbstractGameEffect {
    private YinYangOrb[] orbs;
    private Reimu reimu;

    private ArrayList<FantasyHeavenBullet> bullets;
    private TextureAtlas.AtlasRegion[] bulletTextures;
    private float startDuration;
    private int orbindex;

    public OrbShootsBulletsAction(TextureAtlas.AtlasRegion[] bulletTextures, Reimu reimu, YinYangOrb[] orbs, ArrayList<FantasyHeavenBullet> bullets) {
        this(bulletTextures, reimu, orbs, bullets, 0);
    }
    public OrbShootsBulletsAction(TextureAtlas.AtlasRegion[] bulletTextures, Reimu reimu, YinYangOrb[] orbs, ArrayList<FantasyHeavenBullet> bullets, int orbindex) {
        this.orbs = orbs;
        this.bullets = bullets;
        this.reimu = reimu;
        this.startDuration = 0.1F;
        this.duration = this.startDuration;
        this.bulletTextures = bulletTextures;
        this.orbindex = orbindex;
    }

    @Override
    public void update() {
        if(this.duration == this.startDuration) {
            FantasyHeavenBullet newbullet;
            final int bulletcount = 16;
            float angle = (new Vector2(orbs[orbindex].drawX - reimu.drawX, orbs[orbindex].hb.cY - reimu.hb.cY)).angle() + (float)Math.PI / 2F;
            CardCrawlGame.sound.playV(PaleMod.makeID("touhou_shot"), 0.25F);
            for(int i = 0; i < bulletcount; i++) {
                newbullet = new FantasyHeavenBullet(bulletTextures, orbs[orbindex].hb.cX, orbs[orbindex].hb.cY, (25 + (i % (bulletcount / 2)) * 2F) * Settings.scale, angle + 0.05F * ((i < bulletcount / 2) ? -1 : 1));
                bullets.add(newbullet);
                AbstractDungeon.effectsQueue.add(newbullet);
            }
        }

        this.duration -= Gdx.graphics.getRawDeltaTime();
        if(this.duration <= 0) {
            this.isDone = true;
            if(this.orbindex < orbs.length - 1) {
                AbstractDungeon.effectsQueue.add(new OrbShootsBulletsAction(bulletTextures, reimu, orbs, bullets, ++orbindex));
            }
        }
    }

    @Override
    public void render(SpriteBatch spriteBatch) {

    }

    @Override
    public void dispose() {

    }
}
