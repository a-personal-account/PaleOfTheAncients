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
import paleoftheancients.PaleMod;

public class TossCardEffect extends AbstractGameEffect {
    public static final String[] IMG = new String[] {
            PaleMod.assetPath("images/TheShowman/vfx/cards/card_back_mk2.png"),
            PaleMod.assetPath("images/TheShowman/vfx/cards/card_face_clubs.png"),
            PaleMod.assetPath("images/TheShowman/vfx/cards/card_face_diamonds.png"),
            PaleMod.assetPath("images/TheShowman/vfx/cards/club_3.png"),
            PaleMod.assetPath("images/TheShowman/vfx/cards/club_7.png"),
            PaleMod.assetPath("images/TheShowman/vfx/cards/diamond_4.png"),
            PaleMod.assetPath("images/TheShowman/vfx/cards/diamond_8.png"),
            PaleMod.assetPath("images/TheShowman/vfx/cards/heart_2.png"),
            PaleMod.assetPath("images/TheShowman/vfx/cards/heart_6.png"),
            PaleMod.assetPath("images/TheShowman/vfx/cards/heart_ace.png"),
            PaleMod.assetPath("images/TheShowman/vfx/cards/spade_5.png"),
            PaleMod.assetPath("images/TheShowman/vfx/cards/spade_9.png")
    };

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

    private void chooseImage(int img) {
        this.img = new TextureRegion(cards[img]);
    }

    public TossCardEffect(float x, float y, AbstractCreature m, int damage, int specifyImg) {
        this.cards = new Texture[IMG.length];
        for(int i = 0; i < this.cards.length; i++) {
            this.cards[i] = new Texture(IMG[i]);
        }

        this.img = null;
        if (this.img == null) {
            if (specifyImg == -1) {
                if (MathUtils.randomBoolean()) {
                    this.chooseImage(MathUtils.random(1, this.cards.length));
                } else {
                    this.img = new TextureRegion(this.cards[0]);
                }
            } else {
                this.chooseImage(specifyImg);
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

    public TossCardEffect(float x, float y, AbstractCreature m, int damage) {
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
