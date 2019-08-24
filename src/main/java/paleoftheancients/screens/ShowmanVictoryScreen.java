package paleoftheancients.screens;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.Skeleton;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.city.Byrd;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import com.megacrit.cardcrawl.vfx.combat.SmokeBombEffect;
import paleoftheancients.helpers.DelayVFX;
import paleoftheancients.helpers.GroundByrd;
import paleoftheancients.helpers.PlaySFX;
import paleoftheancients.theshowman.vfx.TossCardVFX;

public class ShowmanVictoryScreen extends PaleVictoryScreen {
    private Byrd byrd = null;
    private int step = 0;

    @Override
    protected String watDo() {
        switch(step++) {
            case 0:
                this.eyes = new CustomEye[0];
                AbstractDungeon.topLevelEffects.add(new SmokeBombEffect(Settings.WIDTH / 2, Settings.HEIGHT / 3));
                byrd = new Byrd(0, 0);
                byrd.drawX = Settings.WIDTH / 2;
                byrd.drawY = Settings.HEIGHT / 4;
                byrd.hideHealthBar();
                byrd.hb.width = 0;
                byrd.healthBarUpdatedEvent();
                byrd.setMove((byte)0, AbstractMonster.Intent.NONE);
                byrd.createIntent();
                this.hideShadow();
                break;

            case 1: {
                float timepadding = 0.2F;
                for (int i = 0; i < 6; i++) {
                    AbstractDungeon.topLevelEffects.add(new DelayVFX(new TossCardVFX(Settings.WIDTH * -0.2F, MathUtils.random(0.3F, 0.7F) * Settings.HEIGHT, byrd, MathUtils.random(3, 6), -1), i * timepadding));
                    AbstractDungeon.topLevelEffects.add(new DelayVFX(new FlashAtkImgEffect(byrd.hb.cX, byrd.hb.cY, AbstractGameAction.AttackEffect.BLUNT_LIGHT, true), i * timepadding +  + 0.1F));
                    AbstractDungeon.topLevelEffects.add(new DelayVFX(new PlaySFX("BLUNT_FAST"), i * timepadding + 0.1F));
                }
                AbstractDungeon.topLevelEffects.add(new DelayVFX(new GroundByrd(byrd), 4 * timepadding));
                break;
            }
        }
        return charStrings.TEXT[this.currentDialog + 2];
    }

    private void hideShadow() {
        ((Skeleton) ReflectionHacks.getPrivate(byrd, AbstractCreature.class, "skeleton")).findBone("shadow").setScale(0F);
    }

    @Override
    public void update() {
        super.update();
        if(byrd != null) {
            byrd.update();
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        super.render(sb);
        if(byrd != null) {
            byrd.render(sb);
        }
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
