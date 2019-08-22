package paleoftheancients.thedefect.monsters.orbs;

import paleoftheancients.thedefect.actions.DefectRemoveOrbAction;
import paleoftheancients.thedefect.monsters.TheDefectBoss;
import paleoftheancients.thedefect.powers.BossOrbPower;
import paleoftheancients.thedefect.powers.DivertingPowerPower;
import paleoftheancients.thedefect.powers.KineticBarrierPower;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.LockOnPower;
import com.megacrit.cardcrawl.vfx.BobEffect;

import java.util.ArrayList;

public abstract class AbstractBossOrb extends AbstractMonster {
    public String name;
    public String description;
    public String ID;
    protected ArrayList<PowerTip> tips = new ArrayList();
    public int evokeAmount = 0;
    public int passiveAmount = 0;
    protected int baseEvokeAmount = 0;
    protected int basePassiveAmount = 0;
    public float tX;
    public float tY;
    protected Color c;
    protected static final int W = 96;
    protected Texture img;
    protected BobEffect bobEffect;
    protected static final float NUM_X_OFFSET;
    protected static final float NUM_Y_OFFSET;
    protected float angle;
    protected float scale;
    protected float fontScale;
    protected boolean showEvokeValue;
    protected static final float CHANNEL_IN_TIME = 0.5F;
    protected float channelAnimTimer;

    public TheDefectBoss owner;
    public boolean addedThisTurn;

    public AbstractBossOrb(TheDefectBoss owner, String name, String id) {
        super(name, id, 1000, 0.0F, -0F, 96F, 96F, (String)null, 0.0F, 0.0F);

        this.c = Settings.CREAM_COLOR.cpy();

        this.img = null;
        this.bobEffect = new BobEffect(3.0F * Settings.scale, 3.0F);
        this.fontScale = 0.7F;
        this.showEvokeValue = false;
        this.channelAnimTimer = 0.5F;

        this.owner = owner;

        this.drawX = this.owner.drawX;
        this.drawY = this.owner.drawY + this.owner.hb.height / 2;
        this.tX = this.drawX;
        this.tY = this.drawY;

        if(!(this instanceof EmptyOrbSlot)) {
            this.powers.add(new BossOrbPower(this));
            //AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new BossOrbPower(this)));
            this.applyFocus();
        }

        this.setMove((byte)0, Intent.NONE);
        this.damage = new ArrayList<>();
        this.addedThisTurn = false;
    }

    public void usePreBattleAction() {}

    public void takeTurn() {
        if(this.addedThisTurn) {
            this.addedThisTurn = false;
        } else {
            this.passive(AbstractDungeon.player);
        }
    }

    protected void getMove(int num) {}

    public void damage(DamageInfo info) {
        if(info.type == DamageInfo.DamageType.NORMAL || info.output > 5) {
            AbstractDungeon.actionManager.addToTop(new SuicideAction(this));
            AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this.owner));
        }
    }

    public void evoke(AbstractCreature target) {}
    public void passive(AbstractCreature target) {}

    @Override
    public void die() {
        die(true);
    }

    @Override
    public void die(boolean adjustPower) {
        if(!(this instanceof EmptyOrbSlot)) {
            this.evoke(AbstractDungeon.player);
            this.removeFromRoom();

            AbstractDungeon.actionManager.addToTop(new DefectRemoveOrbAction(this));

            if (this.owner.hasPower(DivertingPowerPower.POWER_ID)) {
                AbstractDungeon.actionManager.addToTop(new ReducePowerAction(this.owner, this.owner, DivertingPowerPower.POWER_ID, 1));
            } else {
                AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(this.owner, this.owner, new KineticBarrierPower(this.owner, 1), 1));
            }
        }
    }

    public void addToRoom() {
        AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(this, false));
    }
    public void removeFromRoom() {
        AbstractDungeon.getCurrRoom().monsters.monsters.remove(this);
    }


    public static AbstractBossOrb getRandomOrb(TheDefectBoss tdb, boolean useCardRng) {
        ArrayList<AbstractBossOrb> orbs = new ArrayList();
        orbs.add(new Lightning(tdb));
        orbs.add(new Frost(tdb));
        orbs.add(new Dark(tdb));
        orbs.add(new Plasma(tdb));
        return useCardRng ? (AbstractBossOrb)orbs.get(AbstractDungeon.cardRandomRng.random(orbs.size() - 1)) : (AbstractBossOrb)orbs.get(MathUtils.random(orbs.size() - 1));
    }

    public void onStartOfTurn() {
    }

    public void onEndOfTurn() {
    }

    public void applyFocus() {
        AbstractPower power = this.owner.getPower("Focus");
        if (power != null) {
            this.passiveAmount = Math.max(0, this.basePassiveAmount + power.amount);
            this.evokeAmount = Math.max(0, this.baseEvokeAmount + power.amount);
        } else {
            this.passiveAmount = this.basePassiveAmount;
            this.evokeAmount = this.baseEvokeAmount;
        }
        this.updateDescription();
    }

    public static int applyLockOn(AbstractCreature target, int dmg) {
        int retVal = dmg;
        if (target.hasPower(LockOnPower.POWER_ID)) {
            retVal = (int)((float)dmg * 1.5F);
        }

        return retVal;
    }

    public void update() {
        //this.hb.move(this.tX, this.tY);

        /*this.updateReticle();
        this.updateHealthBar();
        this.updateAnimations();*/
        super.update();

        if (this.hb.hovered) {
            TipHelper.renderGenericTip(this.tX + 96.0F * Settings.scale, this.tY + 64.0F * Settings.scale, this.name, this.description);
        }
        this.fontScale = MathHelper.scaleLerpSnap(this.fontScale, 0.7F);
    }

    public void updateAnimations() {
        super.updateAnimations();

        this.bobEffect.update();
        this.drawX = MathHelper.orbLerpSnap(this.drawX, this.owner.animX + this.tX);
        this.drawY = MathHelper.orbLerpSnap(this.drawY + this.hb.height / 2, this.owner.animY + this.tY) - this.hb.height / 2;
        if (this.channelAnimTimer != 0.0F) {
            this.channelAnimTimer -= Gdx.graphics.getDeltaTime();
            if (this.channelAnimTimer < 0.0F) {
                this.channelAnimTimer = 0.0F;
            }
        }

        this.c.a = Interpolation.pow2In.apply(1.0F, 0.01F, this.channelAnimTimer / 0.5F);
        this.scale = Interpolation.swingIn.apply(Settings.scale, 0.01F, this.channelAnimTimer / 0.5F);
    }

    public void setSlot(int slotNum, int maxOrbs) {
        float dist = 160.0F * Settings.scale + (float)maxOrbs * 10.0F * Settings.scale;
        float angle = 100.0F + (float)maxOrbs * 12.0F;
        float offsetAngle = angle / 2.0F;
        angle *= (float)slotNum / ((float)maxOrbs - 1.0F);
        angle += 90.0F - offsetAngle;
        this.tX = -dist * MathUtils.cosDeg(angle) + this.owner.drawX;
        this.tY = dist * MathUtils.sinDeg(angle) + this.owner.drawY + this.owner.hb_h / 2.0F;
        if (maxOrbs == 1) {
            this.tX = this.owner.drawX;
            this.tY = 160.0F * Settings.scale + this.owner.drawY + this.owner.hb_h / 2.0F;
        }

        this.hb.move(this.tX, this.tY);
    }

    public void render(SpriteBatch sb) {
        //super.render(sb);
        this.renderText(sb);
        //this.hb.render(sb);
    }

    public abstract void updateDescription();

    protected void renderText(SpriteBatch sb) {
        if (!(this instanceof EmptyOrbSlot)) {
            FontHelper.renderFontCentered(sb, FontHelper.cardEnergyFont_L, Integer.toString(this.evokeAmount), this.hb.cX + NUM_X_OFFSET, this.hb.cY + this.bobEffect.y / 2.0F + NUM_Y_OFFSET - 4.0F * Settings.scale, new Color(0.2F, 1.0F, 1.0F, this.c.a), this.fontScale);
            FontHelper.renderFontCentered(sb, FontHelper.cardEnergyFont_L, Integer.toString(this.passiveAmount), this.hb.cX + NUM_X_OFFSET, this.hb.cY + this.bobEffect.y / 2.0F + NUM_Y_OFFSET + 20.0F * Settings.scale, this.c, this.fontScale);
        }

    }

    public abstract void playChannelSFX();

    static {
        NUM_X_OFFSET = 20.0F * Settings.scale;
        NUM_Y_OFFSET = -12.0F * Settings.scale;
    }
}