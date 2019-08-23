package paleoftheancients.screens;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.city.Byrd;
import com.megacrit.cardcrawl.vfx.combat.SmokeBombEffect;
import paleoftheancients.theshowman.vfx.TossCardEffect;

public class ShowmanVictoryScreen extends PaleVictoryScreen {
    private Byrd byrd = null;
    private int step = 0;

    @Override
    protected void watDo() {
        switch(step++) {
            case 0:
                this.eyes = new CustomEye[0];
                AbstractDungeon.topLevelEffects.add(new SmokeBombEffect(Settings.WIDTH / 2, Settings.HEIGHT / 3));
                byrd = new Byrd(0, 0);
                byrd.drawX = Settings.WIDTH / 2;
                byrd.drawY = Settings.HEIGHT / 3;
                byrd.hideHealthBar();
                byrd.hb.width = 0;
                byrd.healthBarUpdatedEvent();
                break;

            case 1:
                for(int i = 0; i < 6; i++) {
                    AbstractDungeon.topLevelEffects.add(new TossCardEffect(Settings.WIDTH * -0.2F, MathUtils.random(0.3F, 0.7F) * Settings.HEIGHT, byrd, MathUtils.random(11, 23), -1));
                }
                byrd.changeState("GROUNDED");
                break;
        }
    }

    @Override
    public void update() {
        super.update();
        byrd.update();
    }

    @Override
    public void render(SpriteBatch sb) {
        super.render(sb);
        byrd.render(sb);
    }

    @Override
    protected void playSfx() {
        if(step == 0) {
            super.playSfx();
        } else {
            CardCrawlGame.sound.playA("BYRD_DEATH", -0.2F);
            CardCrawlGame.sound.playA("BYRD_DEATH", -0.4F);
        }
    }
}
