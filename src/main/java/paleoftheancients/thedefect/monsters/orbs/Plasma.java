package paleoftheancients.thedefect.monsters.orbs;

import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.combat.OrbFlareEffect;
import com.megacrit.cardcrawl.vfx.combat.PlasmaOrbActivateEffect;
import com.megacrit.cardcrawl.vfx.combat.PlasmaOrbPassiveEffect;
import paleoftheancients.PaleMod;
import paleoftheancients.thedefect.monsters.TheDefectBoss;
import thedefect.vfx.OrbFlareCopyPaste;

public class Plasma extends AbstractBossOrb {
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] DIALOG;

    private float vfxTimer = 1.0F;
    private float vfxIntervalMin = 0.1F;
    private float vfxIntervalMax = 0.4F;

    public static final String ID = PaleMod.makeID("plasmaorb");
    public Plasma(TheDefectBoss owner) {
        super(owner, NAME, ID);

        this.img = ImageMaster.ORB_PLASMA;
        this.basePassiveAmount = TheDefectBoss.ENERGY_PER_STRENGTH;
        this.passiveAmount = this.basePassiveAmount;
        this.baseEvokeAmount = this.basePassiveAmount * 2;
        this.evokeAmount = this.baseEvokeAmount;

        this.angle = MathUtils.random(360.0F);
        this.channelAnimTimer = 0.5F;

        this.name = NAME;
        this.applyFocus();
        this.updateDescription();
    }

    public void updateAnimations() {
        super.updateAnimations();
        this.angle += Gdx.graphics.getDeltaTime() * 45.0F;
        this.vfxTimer -= Gdx.graphics.getDeltaTime();
        if (this.vfxTimer < 0.0F) {
            AbstractDungeon.effectList.add(new PlasmaOrbPassiveEffect(this.hb.cX, this.hb.cY));
            this.vfxTimer = MathUtils.random(this.vfxIntervalMin, this.vfxIntervalMax);
        }
    }

    public void render(SpriteBatch sb) {
        sb.setColor(new Color(1.0F, 1.0F, 1.0F, this.c.a / 2.0F));
        sb.draw(this.img, this.hb.cX - 48.0F, this.hb.cY - 48.0F + this.bobEffect.y, 48.0F, 48.0F, 96.0F, 96.0F, this.scale + MathUtils.sin(this.angle / 12.566371F) * 0.04F * Settings.scale, this.scale, this.angle, 0, 0, 96, 96, false, false);
        sb.setColor(new Color(1.0F, 1.0F, 1.0F, this.c.a / 2.0F));
        sb.setBlendFunction(770, 1);
        sb.draw(this.img, this.hb.cX - 48.0F, this.hb.cY - 48.0F + this.bobEffect.y, 48.0F, 48.0F, 96.0F, 96.0F, this.scale, this.scale + MathUtils.sin(this.angle / 12.566371F) * 0.04F * Settings.scale, -this.angle, 0, 0, 96, 96, false, false);
        sb.setBlendFunction(770, 771);
        super.render(sb);
    }

    public void updateDescription() {
        this.description = DIALOG[0] + this.passiveAmount + DIALOG[1] + this.evokeAmount + DIALOG[2];
    }

    public void evoke(AbstractCreature target) {
        CardCrawlGame.sound.play("ORB_PLASMA_EVOKE", 0.1F);
        AbstractDungeon.effectsQueue.add(new PlasmaOrbActivateEffect(this.hb.cX, this.hb.cY));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.owner, this.owner, new StrengthPower(this.owner, this.evokeAmount), this.evokeAmount));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.owner, this.owner, new LoseStrengthPower(this.owner, this.evokeAmount), this.evokeAmount));
    }
    public void passive(AbstractCreature target) {
        AbstractDungeon.actionManager.addToBottom(new VFXAction(new OrbFlareCopyPaste(this, OrbFlareEffect.OrbFlareColor.PLASMA), 0.0F));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.owner, this.owner, new StrengthPower(this.owner, this.passiveAmount), this.passiveAmount));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.owner, this.owner, new LoseStrengthPower(this.owner, this.passiveAmount), this.passiveAmount));
    }

    public void applyFocus() {
        this.passiveAmount = this.basePassiveAmount;
        this.evokeAmount = this.baseEvokeAmount;
        this.updateDescription();
    }

    public void playChannelSFX() { CardCrawlGame.sound.play("ORB_PLASMA_CHANNEL", 0.1F); }

    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
        NAME = ((OrbStrings) ReflectionHacks.getPrivateStatic(com.megacrit.cardcrawl.orbs.Plasma.class, "orbString")).NAME;
        DIALOG = monsterStrings.DIALOG;
    }
}
