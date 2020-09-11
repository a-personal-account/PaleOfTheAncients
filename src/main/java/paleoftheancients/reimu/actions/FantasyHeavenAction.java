package paleoftheancients.reimu.actions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import paleoftheancients.PaleMod;
import paleoftheancients.helpers.AssetLoader;
import paleoftheancients.reimu.monsters.Reimu;
import paleoftheancients.reimu.monsters.YinYangOrb;
import paleoftheancients.reimu.powers.FreeControlPower;
import paleoftheancients.reimu.powers.Position;
import paleoftheancients.reimu.util.FantasyHeavenBullet;

import java.util.ArrayList;

public class FantasyHeavenAction extends AbstractGameEffect {
    public Reimu reimu;
    private boolean mouseControlled;
    private YinYangOrb[] orbs;
    private float distance, phase;

    private ArrayList<FantasyHeavenBullet> bullets;
    public ArrayList<FantasyHeavenBullet> toRemove;
    private float staggertime;
    private float bullettimer;

    private TextureAtlas atlas;
    private TextureAtlas.AtlasRegion[] bulletTextures;
    private Hitbox playerhitbox;
    private boolean hiddenBefore;
    private Texture hitboxImage;
    private int timeout;

    public FantasyHeavenAction(Reimu reimu) {
        this.reimu = reimu;
        AbstractDungeon.player.hand.group.clear();
        this.mouseControlled = false;

        this.duration = 40;
        if(AbstractDungeon.ascensionLevel >= 9) {
            this.duration += 10;
        }
        if(AbstractDungeon.ascensionLevel >= 19) {
            this.duration += 10;
        }
        this.startingDuration = this.duration;

        this.orbs = new YinYangOrb[8];
        for(int i = 0; i < this.orbs.length; i++) {
            this.orbs[i] = new YinYangOrb(0, 0, 0, 0, 0, reimu, 1.6F, false);
        }

        bullets = new ArrayList<>();
        toRemove = new ArrayList<>();
        this.staggertime = 3.5F;
        this.bullettimer = this.staggertime;
        this.phase = MathUtils.random((float)Math.PI / 4F);
        this.distance = 0F;

        this.rotation = 0F;
        this.hitboxImage = AssetLoader.loadImage(PaleMod.assetPath("images/reimu/ui/hitbox.png"));
        this.atlas = AssetLoader.loadAtlas(PaleMod.assetPath("images/reimu/bullets/bullets.atlas"));
        bulletTextures = new TextureAtlas.AtlasRegion[6];
        for(int i = 0; i < bulletTextures.length; i++) {
            this.bulletTextures[i] = this.atlas.findRegion("etama" + (i + 1));
        }
        this.playerhitbox = new Hitbox(0, 0, 2 * Settings.scale, 2 * Settings.scale);
        AbstractDungeon.player.currentBlock = 0;
        this.hiddenBefore = Settings.hidePopupDetails;
        Settings.hidePopupDetails = true;
        this.timeout = 10;
    }

    @Override
    public void update() {
        phase += Gdx.graphics.getDeltaTime() * 4;
        for(int i = 0; i < this.orbs.length; i++) {
            final double tmpphase = phase + i * Math.PI * 2 / this.orbs.length;
            this.orbs[i].drawX = reimu.drawX + (float)Math.sin(tmpphase) * reimu.hb.width * distance;
            this.orbs[i].drawY = reimu.drawY + reimu.hb.height * 2F / 5F + (float)Math.cos(tmpphase) * (reimu.hb.width - this.orbs[i].hb.height / 2F) * distance;
            this.orbs[i].update();
        }
        if(distance < 0.8F) {
            if(distance <= 0F) {
                AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new FreeControlPower(AbstractDungeon.player)));
                AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(AbstractDungeon.player, AbstractDungeon.player, Position.POWER_ID));
            }
            distance += Gdx.graphics.getDeltaTime();
        }


        this.bullettimer -= Gdx.graphics.getDeltaTime();
        if(this.bullettimer <= 0F) {
            this.staggertime = Math.max(0.7F, this.staggertime - 0.2F);
            this.bullettimer = this.staggertime;
            AbstractDungeon.effectsQueue.add(new OrbShootsBulletsAction(bulletTextures, reimu, this.orbs, bullets));
        }

        this.handleControls();

        for(int i = bullets.size() - 1; i >= 0; i--) {
            FantasyHeavenBullet b = bullets.get(i);
            if(b.hb.intersects(playerhitbox)) {
                toRemove.add(bullets.remove(i));
            }
        }

        this.rotation += Gdx.graphics.getDeltaTime() * 90;
        this.duration -= Gdx.graphics.getDeltaTime();
        if(this.duration <= 0F) {
            this.isDone = true;
            Settings.hidePopupDetails = this.hiddenBefore;
        } else if(this.timeout > this.duration) {
            this.timeout--;
            CardCrawlGame.sound.playV(PaleMod.makeID("touhou_timeout"), 0.65F);
        }
    }


    private void handleControls() {
        if((InputActionSet.up.isPressed() && !InputActionSet.down.isPressed()) || (CInputActionSet.up.isPressed()) && !CInputActionSet.down.isPressed()) {
            if(AbstractDungeon.player.drawY + AbstractDungeon.player.hb.height < Settings.HEIGHT) {
                AbstractDungeon.player.drawY += 5F * Settings.scale;
            }
            mouseControlled = false;
        }
        if((InputActionSet.down.isPressed() && !InputActionSet.up.isPressed()) || (CInputActionSet.down.isPressed() && !CInputActionSet.up.isPressed())) {
            if(AbstractDungeon.player.drawY > 0) {
                AbstractDungeon.player.drawY -= 5F * Settings.scale;
            }
            mouseControlled = false;
        }
        if((InputActionSet.right.isPressed() && !InputActionSet.left.isPressed()) || (CInputActionSet.right.isPressed() && !CInputActionSet.left.isPressed())) {
            if(AbstractDungeon.player.drawX + AbstractDungeon.player.hb.width / 2F < Settings.WIDTH) {
                AbstractDungeon.player.drawX += 5F * Settings.scale;
            }
            mouseControlled = false;
        }
        if((InputActionSet.left.isPressed() && !InputActionSet.right.isPressed()) || (CInputActionSet.left.isPressed() && !CInputActionSet.right.isPressed())) {
            if(AbstractDungeon.player.drawX - AbstractDungeon.player.hb.width / 2F > 0) {
                AbstractDungeon.player.drawX -= 5F * Settings.scale;
            }
            mouseControlled = false;
        }
        if(InputHelper.didMoveMouse() || InputHelper.justClickedLeft || InputHelper.justClickedRight) {
            mouseControlled = true;
        }
        if(mouseControlled) {
            AbstractDungeon.player.drawX = MathUtils.lerp(AbstractDungeon.player.drawX, InputHelper.mX, Gdx.graphics.getDeltaTime() * 3F);
            AbstractDungeon.player.drawY = MathUtils.lerp(AbstractDungeon.player.drawY, InputHelper.mY - AbstractDungeon.player.hb.height / 2F, Gdx.graphics.getDeltaTime() * 3F);
        }
        AbstractDungeon.player.hb.move(AbstractDungeon.player.drawX, AbstractDungeon.player.drawY + AbstractDungeon.player.hb.height / 2F);
        this.playerhitbox.move(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY);
    }

    @Override
    public void render(SpriteBatch sb) {
        for(final YinYangOrb orb : orbs) {
            orb.render(sb);
        }

        sb.setColor(Color.WHITE);
        sb.draw(hitboxImage, AbstractDungeon.player.hb.cX + distance - hitboxImage.getWidth() / 2F, AbstractDungeon.player.hb.cY - hitboxImage.getHeight() / 2F, hitboxImage.getWidth() / 2F, hitboxImage.getHeight() / 2F, hitboxImage.getWidth(), hitboxImage.getHeight(), Settings.scale * 2, Settings.scale * 2, this.rotation, 0, 0, hitboxImage.getWidth(), hitboxImage.getHeight(), false, false);
        playerhitbox.render(sb);

        if(this.startingDuration - this.duration > 10) {
            FontHelper.renderFontCentered(sb, FontHelper.applyPowerFont, Integer.toString((int)this.duration), reimu.hb.cX, reimu.drawY + reimu.hb.height, this.duration > 10 ? Color.WHITE : Color.RED);
        }
    }

    @Override
    public void dispose() {
        atlas.dispose();
        for(final YinYangOrb orb : orbs) {
            orb.dispose();
        }
        Settings.hidePopupDetails = this.hiddenBefore;
    }
}
