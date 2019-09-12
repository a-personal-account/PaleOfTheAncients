package paleoftheancients.thedefect.monsters.orbs;

import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.OrbFlareEffect;
import paleoftheancients.PaleMod;
import paleoftheancients.thedefect.actions.DarkBossOrbEvocationAction;
import paleoftheancients.thedefect.monsters.TheDefectBoss;
import thedefect.vfx.OrbFlareCopyPaste;

public class Dark extends AbstractBossOrb {
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] DIALOG;

    public static final String ID = PaleMod.makeID("darkorb");
    public Dark(TheDefectBoss owner) {
        super(owner, NAME, ID);

        this.img = ImageMaster.ORB_DARK;
        this.baseEvokeAmount = 6;
        this.evokeAmount = this.baseEvokeAmount;
        this.basePassiveAmount = 6;
        this.passiveAmount = this.basePassiveAmount;

        this.angle = MathUtils.random(360.0F);
        this.channelAnimTimer = 0.5F;

        this.name = NAME;
        this.applyFocus();
        this.updateDescription();
    }

    public void updateAnimations() {
        super.updateAnimations();
        this.angle += Gdx.graphics.getDeltaTime() * 10.0F;
    }

    public void render(SpriteBatch sb) {
        sb.setColor(this.c);
        sb.draw(img, this.hb.cX - 48.0F + this.bobEffect.y / 8.0F, this.hb.cY - 48.0F - this.bobEffect.y / 8.0F, 48.0F, 48.0F, 96.0F, 96.0F, this.scale, this.scale, this.angle, 0, 0, 96, 96, false, false);
        super.render(sb);
    }

    public void updateDescription() {
        this.description = DIALOG[0] + this.passiveAmount + DIALOG[1] + this.evokeAmount + DIALOG[2];
    }

    public void evoke(AbstractCreature target) {
        AbstractDungeon.actionManager.addToTop(new DarkBossOrbEvocationAction(new DamageInfo(this.owner, this.evokeAmount, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.FIRE, target));
    }
    public void passive(AbstractCreature target) {
        AbstractDungeon.actionManager.addToBottom(new VFXAction(new OrbFlareCopyPaste(this, OrbFlareEffect.OrbFlareColor.DARK), 0.0F));
        this.evokeAmount += this.passiveAmount;
    }

    public void applyFocus() {
        AbstractPower power = this.owner.getPower("Focus");
        if (power != null) {
            this.passiveAmount = Math.max(0, this.basePassiveAmount + power.amount);
        } else {
            this.passiveAmount = this.basePassiveAmount;
        }
        this.updateDescription();
    }

    public void playChannelSFX() { CardCrawlGame.sound.play("ORB_DARK_CHANNEL", 0.1F); }

    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
        NAME = ((OrbStrings) ReflectionHacks.getPrivateStatic(com.megacrit.cardcrawl.orbs.Dark.class, "orbString")).NAME;
        DIALOG = monsterStrings.DIALOG;
    }
}
