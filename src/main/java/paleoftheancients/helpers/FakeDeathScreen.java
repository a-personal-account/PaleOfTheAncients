package paleoftheancients.helpers;

import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.screens.DeathScreen;
import com.megacrit.cardcrawl.screens.GameOverScreen;
import com.megacrit.cardcrawl.ui.buttons.ReturnToMenuButton;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.DeathScreenFloatyEffect;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class FakeDeathScreen extends DeathScreen {
    private Method uss, uci;
    private boolean playedDeathAnimation, shownstats;
    private ArrayList<DeathScreenFloatyEffect> parentParticles;
    private float waitForDeath = 1.0F;

    public FakeDeathScreen(MonsterGroup m) {
        super(m);

        ReflectionHacks.setPrivate(this, DeathScreen.class, "monsters", m);

        try {
            uss = DeathScreen.class.getDeclaredMethod("updateStatsScreen");
            uss.setAccessible(true);
            uci = DeathScreen.class.getDeclaredMethod("updateControllerInput");
            uci.setAccessible(true);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        parentParticles = (ArrayList<DeathScreenFloatyEffect>) ReflectionHacks.getPrivate(this, DeathScreen.class, "particles");

        playedDeathAnimation = false;
        shownstats = false;

        float progress;
        ReflectionHacks.setPrivate(this, GameOverScreen.class, "unlockProgress", progress = (float) UnlockTracker.getCurrentProgress(AbstractDungeon.player.chosenClass));
        ReflectionHacks.setPrivate(this, GameOverScreen.class, "unlockTargetStart", progress);
        ReflectionHacks.setPrivate(this, GameOverScreen.class, "unlockCost", UnlockTracker.getCurrentScoreCost(AbstractDungeon.player.chosenClass));
    }

    public static void init(DeathScreen ds) {
        resetScoreChecks();

        AbstractDungeon.dungeonMapScreen.closeInstantly();
        AbstractDungeon.overlayMenu.showBlackScreen(1.0F);
        AbstractDungeon.overlayMenu.cancelButton.hideInstantly();
        //AbstractDungeon.isScreenUp = true;

        try {
            Method tmp = DeathScreen.class.getDeclaredMethod("getDeathText");
            tmp.setAccessible(true);
            ReflectionHacks.setPrivate(ds, DeathScreen.class, "deathText", tmp.invoke(ds));
        } catch(Exception ex) {
            ex.printStackTrace();
        }


        ReflectionHacks.setPrivate(ds, GameOverScreen.class, "showingStats", false);

        ReturnToMenuButton rtmb = new ReturnToMenuButton();
        ReflectionHacks.setPrivate(ds, GameOverScreen.class, "returnButton", rtmb);
        rtmb.appear((float)Settings.WIDTH / 2.0F, (float)Settings.HEIGHT * 0.15F, TEXT[0]);

        try {
            Method tmp = DeathScreen.class.getDeclaredMethod("getDeathBannerText");
            tmp.setAccessible(true);
            AbstractDungeon.dynamicBanner.appear((String)tmp.invoke(ds));
        } catch(Exception ex) {
            ex.printStackTrace();
        }


        CardCrawlGame.music.dispose();
        CardCrawlGame.sound.play("DEATH_STINGER", true);
        CardCrawlGame.music.playTempBgmInstantly("STS_DeathStinger_" + MathUtils.random(1, 4) + "_v3_MUSIC.ogg", false);

        Color tmp = (Color) ReflectionHacks.getPrivate(ds, DeathScreen.class, "defeatTextColor");
        tmp.a = 0.0F;
        tmp = (Color) ReflectionHacks.getPrivate(ds, DeathScreen.class, "deathTextColor");
        tmp.a = 0.0F;

        //this.calculateUnlockProgress();

        try {
            Method cgos = DeathScreen.class.getDeclaredMethod("createGameOverStats");
            cgos.setAccessible(true);
            cgos.invoke(ds);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void update() {
        try {
            uci.invoke(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.returnButton.update();
        if (this.returnButton.hb.clicked || this.returnButton.show && CInputActionSet.select.isJustPressed()) {
            CInputActionSet.topPanel.unpress();
            if (Settings.isControllerMode) {
                Gdx.input.setCursorPosition(10, Settings.HEIGHT / 2);
            }

            this.returnButton.hb.clicked = false;

            if(!shownstats) {
                shownstats = true;
                ReflectionHacks.setPrivate(this, GameOverScreen.class, "showingStats", true);
                ReflectionHacks.setPrivate(this, GameOverScreen.class, "statsTimer", 0.5F);
            }
        }


        try {
            uss.invoke(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(!playedDeathAnimation) {
            //float deathAnimWaitTimer = (float) ReflectionHacks.getPrivate(this, DeathScreen.class, "deathAnimWaitTimer");
            waitForDeath -= Gdx.graphics.getDeltaTime();

            if (waitForDeath < 0.0F) {
                waitForDeath = 0.0F;
                AbstractDungeon.player.playDeathAnimation();
                playedDeathAnimation = true;
            }
            //ReflectionHacks.setPrivate(this, DeathScreen.class, "deathAnimWaitTimer", deathAnimWaitTimer);
        } else {
            float deathTextTimer = (float) ReflectionHacks.getPrivate(this, DeathScreen.class, "deathTextTimer");
            deathTextTimer -= Gdx.graphics.getDeltaTime();
            if (deathTextTimer < 0.0F) {
                deathTextTimer = 0.0F;
            }

            Color deathTextColor = (Color) ReflectionHacks.getPrivate(this, DeathScreen.class, "deathTextColor");
            Color defeatTextColor = (Color) ReflectionHacks.getPrivate(this, DeathScreen.class, "defeatTextColor");
            deathTextColor.a = Interpolation.fade.apply(0.0F, 1.0F, 1.0F - deathTextTimer / 5.0F);
            defeatTextColor.a = Interpolation.fade.apply(0.0F, 1.0F, 1.0F - deathTextTimer / 5.0F);
            ReflectionHacks.setPrivate(this, DeathScreen.class, "deathTextColor", deathTextColor);
            ReflectionHacks.setPrivate(this, DeathScreen.class, "defeatTextColor", defeatTextColor);
        }

        if (parentParticles.size() < 50) {
            parentParticles.add(new DeathScreenFloatyEffect());
        }
    }
}
