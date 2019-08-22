package paleoftheancients.thevixen.vfx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.AnimatedSlashEffect;

public class RandomAnimatedSlashEffect extends AbstractGameEffect {
    private float x;
    private float y;
    private Color color2;

    public RandomAnimatedSlashEffect(float x, float y) {
        this.x = x;
        this.y = y;
        this.color = Color.MAROON;
        this.color2 = Color.SCARLET;
        this.startingDuration = 0.1F;
        this.duration = this.startingDuration;
    }

    public void update() {
        if (MathUtils.randomBoolean()) {
            CardCrawlGame.sound.playA("ATTACK_DAGGER_5", MathUtils.random(0.0F, -0.3F));
        } else {
            CardCrawlGame.sound.playA("ATTACK_DAGGER_6", MathUtils.random(0.0F, -0.3F));
        }

        float oX = MathUtils.random(-50.0F, 50.0F) * Settings.scale;
        float oY = MathUtils.random(-20.0F, 20.0F) * Settings.scale;
        float sX = MathUtils.random(-35.0F, 35.0F) * Settings.scale;
        float sY = MathUtils.random(-20.0F, 20.0F) * Settings.scale;
        float dX = MathUtils.random(-150.0F, 150.0F);
        float dY = MathUtils.random(-400.0F, 400.0F);
        float angle = MathUtils.random(360F);
        AbstractDungeon.effectsQueue.add(new AnimatedSlashEffect(this.x + sX * 1.5F + oX, this.y + sY * 1.5F + oY, dX, dY, angle, this.color, this.color2));
        AbstractDungeon.effectsQueue.add(new AnimatedSlashEffect(this.x + sX * 0.5F + oX, this.y + sY * 0.5F + oY, dX, dY, angle, this.color, this.color2));
        AbstractDungeon.effectsQueue.add(new AnimatedSlashEffect(this.x - sX * 0.5F + oX, this.y - sY * 0.5F + oY, dX, dY, angle, this.color, this.color2));
        AbstractDungeon.effectsQueue.add(new AnimatedSlashEffect(this.x - sX * 1.5F + oX, this.y - sY * 1.5F + oY, dX, dY, angle, this.color, this.color2));
        this.isDone = true;
    }

    public void render(SpriteBatch sb) {

    }

    public void dispose() {

    }
}
