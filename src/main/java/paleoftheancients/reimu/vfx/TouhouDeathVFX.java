package paleoftheancients.reimu.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import paleoftheancients.PaleMod;
import paleoftheancients.reimu.monsters.Reimu;

import java.util.ArrayList;

public class TouhouDeathVFX extends AbstractGameEffect {
    private Texture leafimage;
    private int leafwidth, leafheight;

    private float timer, leaftimer;
    private int count;

    private Reimu reimu;

    private ArrayList<Leaf> leaves;

    public TouhouDeathVFX(Reimu reimu) {
        this.leafimage = ImageMaster.loadImage(PaleMod.assetPath("images/reimu/vfx/mapleleaf.png"));
        leafwidth = leafimage.getWidth();
        leafheight = leafimage.getHeight();

        this.reimu = reimu;
        this.timer = 0F;
        this.count = 9;

        this.rotation = MathUtils.random((float)Math.PI * 2F);
        this.leaves = new ArrayList<>();
    }

    @Override
    public void update() {
        if(count >= 0) {
            timer -= Gdx.graphics.getDeltaTime();
            if (timer <= 0F) {
                timer = 0.15F;
                if (count-- > 0) {
                    CardCrawlGame.sound.playV(PaleMod.makeID("touhou_attack"), 0.25F);
                } else {
                    CardCrawlGame.sound.playV(PaleMod.makeID("touhou_defeat"), 0.25F);

                    int leafcount = 16;
                    for(int i = 0; i < leafcount; i++) {
                        float tmprot = (float)i / (float)leafcount * 2F * (float)Math.PI;
                        leaves.add(new Leaf(reimu.hb.cX, reimu.hb.cY, rotation + tmprot + MathUtils.random(-tmprot, tmprot) / 4F, 1.3F));
                    }
                }
            }
            leaftimer -= Gdx.graphics.getDeltaTime();
            if(leaftimer <= 0) {
                rotation += (MathUtils.random(0.01F) + 0.12F) * (float)Math.PI;
                leaftimer = 0.04F;
                leaves.add(new Leaf(reimu.hb.cX, reimu.hb.cY, rotation));
            }
        }

        for(int i = leaves.size() - 1; i >= 0; i--) {
            Leaf l = leaves.get(i);
            l.update();
            if(l.color.a <= 0) {
                leaves.remove(i);
                if(count <= 0 && leaves.isEmpty()) {
                    this.isDone = true;
                }
            }
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        //sb.setBlendFunction(770, 1);

        for(final Leaf l : leaves) {
            l.render(sb);
        }

        sb.setBlendFunction(770, 771);
        sb.setColor(Color.WHITE);
    }

    @Override
    public void dispose() {
        leafimage.dispose();
    }

    class Leaf {
        private float rotation, scale, x, y, vX, vY, rotationVelocity;
        public Color color;

        public Leaf(float x, float y, float angle) {
            this(x, y, angle, 1F);
        }
        public Leaf(float x, float y, float angle, float scalefactor) {
            this.rotation = MathUtils.random(360F);
            this.rotationVelocity = MathUtils.random(180, 720);
            if(MathUtils.randomBoolean())
                this.rotationVelocity *= -1;
            this.scale = MathUtils.random(0.8F, 1.2F) * Settings.scale * 4F * scalefactor;
            this.x = x;
            this.y = y;
            this.color = new Color(MathUtils.random(0.7F, 1F), MathUtils.random(0.7F, 1F), MathUtils.random(0.7F, 1F), 1F);
            this.vX = (float)Math.sin(angle) * 300F;
            this.vY = (float)Math.cos(angle) * 300F;
        }

        public void update() {
            x += vX * Gdx.graphics.getDeltaTime();
            y += vY * Gdx.graphics.getDeltaTime();
            rotation += rotationVelocity * Gdx.graphics.getDeltaTime();
            this.color.a -= Gdx.graphics.getDeltaTime() / 2F;
        }

        public void render(SpriteBatch sb) {
            sb.setColor(color);
            sb.draw(leafimage, x - leafwidth / 2F, y - leafheight / 2F, leafwidth / 2F, leafheight / 2F, leafwidth, leafheight, this.scale, this.scale, this.rotation, 0, 0, leafwidth, leafheight, false, false);
        }
    }
}
