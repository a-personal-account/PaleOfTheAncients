package paleoftheancients.thevixen.helpers;

import paleoftheancients.thevixen.powers.SunnyDayPower;
import paleoftheancients.thevixen.vfx.GhostlyFireEffect;
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

public class BraixenAnimation extends SpriterAnimation {


    private PlayerTweener braixen;
    private PlayerTweener substitute;
    private AbstractCreature owner;
    private int damaged = 0;
    public boolean shiny;
    private String origpath;


    public BraixenAnimation(String path, boolean shiny) {
        this(null, path, shiny);
    }
    public BraixenAnimation(AbstractCreature owner, String path, boolean shiny) {
        super(path);
        this.substitute = this.myPlayer;
        this.origpath = path;

        this.shiny = shiny;

        this.braixen = new PlayerTweener((new SCMLReader(Gdx.files.internal(path).read())).getData().getEntity(shiny ? 2 : 1));
        this.braixen.setScale(Settings.scale);
        resetAnimation(false);

        if(owner != null) {
            this.setOwner(owner);
        }
    }

    public void setShiny(boolean shiny) {
        if(this.shiny != shiny) {
            this.braixen = new PlayerTweener((new SCMLReader(Gdx.files.internal(origpath).read())).getData().getEntity(shiny ? 2 : 1));
            this.braixen.setScale(Settings.scale);
            resetAnimation(false);
            this.shiny = shiny;
        }
    }

    public void setOwner(AbstractCreature owner) {
        this.owner = owner;

        this.setFlip(true, false);
        this.substitute.flip(true, false);
    }

    public void damage() {
        this.myPlayer.getFirstPlayer().setAnimation("hurt");
        this.myPlayer.getFirstPlayer().setTime(0);

        damaged = 400;
    }

    public void substitute() {
        if(this.myPlayer.getEntity().name != "substitute") {
            this.myPlayer = substitute;
            this.myPlayer.getFirstPlayer().setAnimation("enter");
            this.myPlayer.getFirstPlayer().setTime(0);
            damaged = 800; //to reset to idle

            AbstractDungeon.actionManager.addToTop(new VFXAction(new SmokeBombEffect(this.owner.hb.cX, this.owner.hb.cY)));
        }
    }
    public void resetAnimation() {
        resetAnimation(true);
    }
    public void resetAnimation(boolean smokebomb) {
        this.myPlayer = braixen;
        this.myPlayer.getFirstPlayer().setAnimation("idle");
        this.myPlayer.getFirstPlayer().setTime(0);
        if(smokebomb) {
            AbstractDungeon.actionManager.addToTop(new VFXAction(new SmokeBombEffect(this.owner.hb.cX, this.owner.hb.cY)));
        }
    }

    @Override
    public void renderSprite(SpriteBatch sb, float x, float y) {
        if(damaged > 0) {
            if(this.myPlayer.getFirstPlayer().getTime() > damaged) {
                damaged = 0;
                this.myPlayer.getFirstPlayer().setAnimation("idle");
                this.myPlayer.getFirstPlayer().setTime(0);
            }
        }
        super.renderSprite(sb, x, y);


        fire(true);
    }


    private float particleTimer = 0.0F;
    private RainbowColor rainbow = new RainbowColor(1F, 0F, 0F, 1.0F);
    private void fire(boolean useRainbow) {
        try {
            if(owner.hasPower(SunnyDayPower.POWER_ID)) {
                int amnt = owner.getPower(SunnyDayPower.POWER_ID).amount - 1;
                Color color;

                if(amnt >= 10) {
                    color = Color.WHITE.cpy();
                } else if(useRainbow) {
                    rainbow.update();
                    color = rainbow.cpy();
                } else {
                    color = Color.ORANGE.cpy();
                }

                if (this.particleTimer < 0.0F) {
                    color.add(amnt * 0.1F, amnt * 0.1F, amnt * 0.1F, 1F);

                    Timeline.Key.Bone bone = this.myPlayer.getFirstPlayer().getBone("bone_010");
                    double angle = Math.toRadians(180 - (bone.angle % 360) + 50);
                    float x = (float) (60 * Math.cos(angle) + bone.position.x + 100) * Settings.scale;
                    float y = (float) (60 * Math.sin(angle) + bone.position.y + 35) * Settings.scale;
                    AbstractDungeon.effectList.add(
                            new GhostlyFireEffect(owner.drawX + owner.animX - (x * -1), owner.drawY + owner.animY + AbstractDungeon.sceneOffsetY + y, color, amnt * 0));

                    this.particleTimer = 0.06F;
                }
                this.particleTimer -= Gdx.graphics.getDeltaTime();
            }
        } catch(NullPointerException ex) {}
    }
}
