package paleoftheancients.theshowman.helpers;

import basemod.animations.SpriterAnimation;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.brashmonkey.spriter.PlayerTweener;
import com.brashmonkey.spriter.SCMLReader;
import com.brashmonkey.spriter.Timeline;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.SmokeBombEffect;
import paleoftheancients.thevixen.helpers.RainbowColor;
import paleoftheancients.thevixen.powers.SunnyDayPower;
import paleoftheancients.thevixen.vfx.GhostlyFireEffect;

public class ShowmanAnimation extends SpriterAnimation {
    private int playerTimeUntilIdle = 0;

    public ShowmanAnimation(String path) {
        super(path);
        this.setFlip(true, false);
    }

    public void damage() {
        this.myPlayer.getFirstPlayer().setAnimation(2);
        this.myPlayer.getFirstPlayer().setTime(0);
        this.playerTimeUntilIdle = 500;
    }

    public void attack() {
        this.myPlayer.getFirstPlayer().setAnimation(1);
        this.myPlayer.getFirstPlayer().setTime(0);
        this.playerTimeUntilIdle = 500;
    }

    @Override
    public void renderSprite(SpriteBatch sb, float x, float y) {
        if(playerTimeUntilIdle > 0) {
            if(this.myPlayer.getFirstPlayer().getTime() > playerTimeUntilIdle) {
                playerTimeUntilIdle = 0;
                this.myPlayer.getFirstPlayer().setAnimation(0);
                this.myPlayer.getFirstPlayer().setTime(0);
            }
        }
        super.renderSprite(sb, x, y);
    }
}
