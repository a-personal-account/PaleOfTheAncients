package paleoftheancients.reimu.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class FantasyHeavenBullet extends AbstractGameEffect {

    private TextureAtlas.AtlasRegion[] atlas;

    public Hitbox hb;
    private Vector2 position, velocity, tmp;

    private int textureIndex;
    private boolean transitioning;

    public FantasyHeavenBullet(TextureAtlas.AtlasRegion[] atlas, float x, float y, float speed, float angle) {
        this.atlas = atlas;
        this.textureIndex = 0;

        this.velocity = new Vector2(1, 1);
        this.velocity.setAngle((float)Math.toDegrees(angle));
        this.velocity.setLength(speed);
        tmp = new Vector2(velocity);
        tmp.setLength(velocity.len() * 2);
        this.startingDuration = 0.2F;

        this.scale = Settings.scale * 2;
        this.hb = new Hitbox(0, 0, atlas[0].getRegionWidth() * this.scale * 0.75F, 0);
        this.hb.height = this.hb.width;
        this.position = new Vector2(x - hb.width / 2F, y - hb.height / 2F);
    }

    @Override
    public void update() {
        switch (textureIndex) {
            case 0:
                velocity.setLength(Math.max(0, velocity.len() - tmp.len() * Gdx.graphics.getDeltaTime()));
                if(velocity.len() <= 0) {
                    this.textureIndex += 3;
                    this.duration = this.startingDuration;
                    this.transitioning = true;
                }
                this.position.add(this.velocity);
                break;

            case 1:
                this.position.x += this.velocity.x * (1 - Math.abs((float)Math.cos(this.duration))) * Gdx.graphics.getDeltaTime();
                this.position.y += this.velocity.y * (1 - Math.abs((float)Math.cos(this.duration))) * Gdx.graphics.getDeltaTime();
                this.duration -= Gdx.graphics.getDeltaTime() * 2;
                if(this.duration <= 0F) {
                    this.textureIndex += 3;
                    this.duration = this.startingDuration;
                    this.transitioning = true;
                }
                break;

            case 2:
                this.position.x += this.velocity.x * Gdx.graphics.getDeltaTime();
                this.position.y += this.velocity.y * Gdx.graphics.getDeltaTime();
                if(this.position.x < -Settings.WIDTH * 0.1F
                        || this.position.x > Settings.WIDTH * 1.1F
                        || this.position.y < -Settings.HEIGHT * 0.1F
                        || this.position.y > Settings.HEIGHT * 1.1F) {
                    this.isDone = true;
                }
                break;


            case 3:
            case 4:
            case 5:
                this.duration -= Gdx.graphics.getDeltaTime();
                if(this.duration <= 0) {
                    if(this.transitioning) {
                        this.transitioning = false;
                        this.textureIndex++;
                        this.duration = this.startingDuration;
                    } else {
                        this.textureIndex -= 3;
                        this.velocity.x = AbstractDungeon.player.hb.cX - this.hb.cX;
                        this.velocity.y = AbstractDungeon.player.hb.cY - this.hb.cY;
                        if(this.textureIndex == 1) {
                            this.velocity.setLength(300F * Settings.scale);
                            this.duration = (float)Math.PI;
                        } else {
                            this.velocity.setLength(700F * Settings.scale);
                        }
                    }
                }
                break;
        }
        this.hb.move(this.position.x + this.hb.width / 2F, this.position.y + this.hb.height / 2F);
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setColor(Color.WHITE);
        sb.setBlendFunction(770, 1);
        sb.draw(atlas[textureIndex], this.position.x, this.position.y, hb.width / 2, hb.height / 2, atlas[textureIndex].packedWidth, atlas[textureIndex].packedHeight, this.scale, this.scale, this.velocity.angle() + 90);
        sb.setBlendFunction(770, 771);
        this.hb.render(sb);
    }

    @Override
    public void dispose() {

    }
}
