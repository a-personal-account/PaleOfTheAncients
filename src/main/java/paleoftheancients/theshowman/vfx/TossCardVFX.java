package paleoftheancients.theshowman.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import paleoftheancients.theshowman.helpers.Cards;

public class TossCardVFX extends AbstractGameEffect {

    private Texture[] cards;

    private static final int TOTAL_CHOICE_AMOUNT = 11;
    private TextureRegion img;
    private AbstractMonster m;
    private float x;
    private float y;
    private float targetX;
    private float targetY;
    private float progress;
    private int damage;

    public TossCardVFX(float x, float y, AbstractCreature m, int damage, int specifyImg) {
        this.img = null;
        if (this.img == null) {
            if (specifyImg == -1) {
                if (MathUtils.randomBoolean()) {
                    this.img = new TextureRegion(Cards.getCard(MathUtils.random(1, Cards.size() - 1)));
                } else {
                    this.img = new TextureRegion(Cards.getCard(0));
                }
            } else {
                this.img = new TextureRegion(Cards.getCard(specifyImg));
            }
        }

        this.damage = damage;
        this.x = x;
        this.y = y;
        this.targetX = m.hb.cX;
        this.targetY = m.hb.cY;
        this.color = Color.WHITE.cpy();
        this.duration = 0.1F;
        this.startingDuration = 0.1F;
        this.scale = 1.2F * Settings.scale;
    }

    public TossCardVFX(float x, float y, AbstractCreature m, int damage) {
        this(x, y, m, damage, -1);

        this.scale = 0.3F * Settings.scale;
    }

    public void update() {
        if (this.m == null || !this.m.isDeadOrEscaped()) {
            this.duration -= Gdx.graphics.getDeltaTime();
            this.progress += Gdx.graphics.getDeltaTime();
            this.x = Interpolation.linear.apply(this.x, this.targetX, Math.min(1.0F, this.progress / this.startingDuration));
            this.y = Interpolation.linear.apply(this.y, this.targetY, Math.min(1.0F, this.progress / this.startingDuration));
            if (this.duration < 0.0F) {
                int j = this.damage;
                if (j > 20) {
                    j = 20;
                }

                for(int i = 0; i < j; ++i) {
                    AbstractDungeon.effectsQueue.add(new ExplodeCardVFX(this.targetX, this.targetY, this.img));
                }

                this.isDone = true;
            }

        } else {
            this.isDone = true;
        }
    }

    public void render(SpriteBatch sb) {
        sb.setColor(this.color);
        sb.draw(this.img, this.x - (float)this.img.getRegionWidth() / 2.0F, this.y - (float)this.img.getRegionHeight() / 2.0F, (float)this.img.getRegionWidth() / 2.0F, (float)this.img.getRegionHeight() / 2.0F, (float)this.img.getRegionWidth(), (float)this.img.getRegionHeight(), this.scale, this.scale, 90.0F);
    }

    public void dispose() {
        for(final Texture t : cards) {
            t.dispose();
        }
    }
}
