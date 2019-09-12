package paleoftheancients.thedefect.monsters.orbs;

import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.vfx.combat.FrostOrbPassiveEffect;
import com.megacrit.cardcrawl.vfx.combat.OrbFlareEffect;
import paleoftheancients.PaleMod;
import paleoftheancients.thedefect.monsters.TheDefectBoss;
import thedefect.vfx.OrbFlareCopyPaste;

public class Frost extends AbstractBossOrb {
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] DIALOG;

    public static final String ID = PaleMod.makeID("frostorb");

    private boolean hFlip1;
    private boolean hFlip2;
    private float vfxTimer = 1.0F;
    private float vfxIntervalMin = 0.15F;
    private float vfxIntervalMax = 0.8F;

    public Frost(TheDefectBoss owner) {
        super(owner, NAME, ID);

        this.hFlip1 = MathUtils.randomBoolean();
        this.hFlip2 = MathUtils.randomBoolean();

        this.baseEvokeAmount = 5;
        this.evokeAmount = this.baseEvokeAmount;
        this.basePassiveAmount = 2;
        this.passiveAmount = this.basePassiveAmount;

        this.channelAnimTimer = 0.5F;

        this.name = NAME;
        this.applyFocus();
        this.updateDescription();
    }

    public void updateAnimations() {
        super.updateAnimations();
        this.angle += Gdx.graphics.getDeltaTime() * 180.0F;
        this.vfxTimer -= Gdx.graphics.getDeltaTime();
        if (this.vfxTimer < 0.0F) {
            AbstractDungeon.effectList.add(new FrostOrbPassiveEffect(this.hb.cX, this.hb.cY));
            if (MathUtils.randomBoolean()) {
                AbstractDungeon.effectList.add(new FrostOrbPassiveEffect(this.hb.cX, this.hb.cY));
            }

            this.vfxTimer = MathUtils.random(this.vfxIntervalMin, this.vfxIntervalMax);
        }
    }

    public void render(SpriteBatch sb) {
        sb.setColor(this.c);
        sb.draw(ImageMaster.FROST_ORB_RIGHT, this.hb.cX - 48.0F + this.bobEffect.y / 4.0F, this.hb.cY - 48.0F + this.bobEffect.y / 4.0F, 48.0F, 48.0F, 96.0F, 96.0F, this.scale, this.scale, 0.0F, 0, 0, 96, 96, this.hFlip1, false);
        sb.draw(ImageMaster.FROST_ORB_LEFT, this.hb.cX - 48.0F + this.bobEffect.y / 4.0F, this.hb.cY - 48.0F - this.bobEffect.y / 4.0F, 48.0F, 48.0F, 96.0F, 96.0F, this.scale, this.scale, 0.0F, 0, 0, 96, 96, this.hFlip1, false);
        sb.draw(ImageMaster.FROST_ORB_MIDDLE, this.hb.cX - 48.0F - this.bobEffect.y / 4.0F, this.hb.cY - 48.0F + this.bobEffect.y / 2.0F, 48.0F, 48.0F, 96.0F, 96.0F, this.scale, this.scale, 0.0F, 0, 0, 96, 96, this.hFlip2, false);
        super.render(sb);
    }

    public void updateDescription() {
        this.description = DIALOG[0] + this.passiveAmount + DIALOG[1] + this.evokeAmount + DIALOG[2];
    }

    public void evoke(AbstractCreature target) {
        AbstractDungeon.actionManager.addToTop(new GainBlockAction(this.owner, this.owner, this.evokeAmount));
    }
    public void passive(AbstractCreature target) {
        AbstractDungeon.actionManager.addToBottom(new VFXAction(new OrbFlareCopyPaste(this, OrbFlareEffect.OrbFlareColor.FROST), 0.0F));
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this.owner, this.owner, this.passiveAmount, true));
    }

    public void playChannelSFX() { CardCrawlGame.sound.play("ORB_FROST_CHANNEL", 0.1F); }

    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
        NAME = ((OrbStrings) ReflectionHacks.getPrivateStatic(com.megacrit.cardcrawl.orbs.Frost.class, "orbString")).NAME;
        DIALOG = monsterStrings.DIALOG;
    }
}
