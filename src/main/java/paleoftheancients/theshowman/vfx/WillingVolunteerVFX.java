package paleoftheancients.theshowman.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class WillingVolunteerVFX extends AbstractGameEffect {
    private AbstractCreature target;

    private int jumped;

    public WillingVolunteerVFX(AbstractCreature c) {
        this.target = c;
        this.duration = 4.0F;
        this.jumped = 0;
        this.color = Color.WHITE.cpy();
    }

    @Override
    public void update() {

        if (this.duration == 4.0F) {
            CardCrawlGame.sound.playA("GHOST_ORB_IGNITE_1", -0.6F);
        }

        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration > 3.0F) {
            this.color.a = Interpolation.pow5In.apply(0.5F, 0.0F, (this.duration - 1.0F) / 1.0F);
        } else if(this.duration < 1.0F) {
            this.color.a = Interpolation.exp10In.apply(0.0F, 0.5F, this.duration / 1.0F);
        } else {
            if((jumped < 1 && this.duration > 2.9F) || (jumped < 2 && this.duration > 2.4F)) {
                jumped++;
                this.target.useHopAnimation();
            }
        }

        if (this.duration < 0.0F) {
            this.color.a = 0.0F;
            this.isDone = true;
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setColor(this.color);
        sb.setBlendFunction(770, 1);
        sb.draw(ImageMaster.SPOTLIGHT_VFX, -Settings.WIDTH + target.drawX, 0.0F, (float) Settings.WIDTH, (float)Settings.HEIGHT);
        sb.setBlendFunction(770, 771);
    }

    public void dispose() {
    }
}
