package paleoftheancients.wokeone.monsters;

import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.esotericsoftware.spine.Bone;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.unique.CanLoseAction;
import com.megacrit.cardcrawl.actions.unique.RemoveDebuffsAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.monsters.beyond.AwakenedOne;
import com.megacrit.cardcrawl.monsters.exordium.Cultist;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.RitualPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.UnawakenedPower;
import com.megacrit.cardcrawl.vfx.AwakenedEyeParticle;
import com.megacrit.cardcrawl.vfx.AwakenedWingParticle;
import com.megacrit.cardcrawl.vfx.combat.IntenseZoomEffect;
import paleoftheancients.PaleMod;
import paleoftheancients.dungeons.PaleOfTheAncients;
import paleoftheancients.wokeone.powers.PrematureAwakening;
import paleoftheancients.wokeone.vfx.CultistWingParticle;

import java.util.ArrayList;

public class WokeCultist extends Cultist {
    public static final String ID = PaleMod.makeID("WokeCultist");

    private boolean reborn;
    private AwakenedOne wokeone;
    private float fireTimer = 0.0F;
    private Bone head;
    private Bone back;
    private ArrayList<AwakenedWingParticle> wParticles;

    public WokeCultist(float x, float y) {
        super(x, y, false);
        this.setHp(this.maxHealth * 2);
        this.reborn = false;
        this.head = this.skeleton.findBone("head");
        this.back = this.skeleton.findBone("body");
        this.wParticles = new ArrayList<>();
        this.type = EnemyType.BOSS;
    }

    @Override
    public void usePreBattleAction() {
        //AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new UnawakenedPower(this)));
        AbstractDungeon.getCurrRoom().cannotLose = true;


        this.wokeone = new AwakenedOne(0, 0);
        ReflectionHacks.setPrivate(this.wokeone, AwakenedOne.class, "form1", false);
        this.wokeone.hb.cX = this.hb.cX;
        this.wokeone.hb.cY = this.hb.cY;
        this.wokeone.powers = this.powers;
        this.wokeone.setMove((byte)0, Intent.NONE);
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        this.wokeone.applyPowers();
    }

    @Override
    public void takeTurn() {
        if(this.halfDead) {
            rebirth();
        } else if(this.reborn) {
            this.useFastAttackAnimation();
            this.wokeone.takeTurn();
            AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
        } else {
            super.takeTurn();
        }
    }

    @Override
    protected void getMove(int num) {
        if(this.reborn) {
            EnemyMoveInfo info = (EnemyMoveInfo) ReflectionHacks.getPrivate(this.wokeone, AbstractMonster.class, "move");
            this.setMove(info.nextMove, info.intent, info.baseDamage, info.multiplier, info.isMultiDamage);
            this.wokeone.createIntent();
            //this.createIntent();
        } else {
            super.getMove(num);
        }
    }

    @Override
    public void update() {
        super.update();
        if (!this.isDying && this.reborn) {
            this.fireTimer -= Gdx.graphics.getDeltaTime();
            if (this.fireTimer < 0.0F) {
                this.fireTimer = 0.1F;
                AbstractDungeon.effectList.add(new AwakenedEyeParticle(this.skeleton.getX() + this.head.getWorldX() - 8 * Settings.scale, this.skeleton.getY() + this.head.getWorldY() + 8 * Settings.scale));

                AwakenedWingParticle awp = new CultistWingParticle();
                this.wParticles.add(awp);
            }
        }
        for(int i = this.wParticles.size() - 1; i >= 0; i--) {
            AwakenedWingParticle awp = this.wParticles.get(i);
            awp.update();
            if(awp.isDone) {
                this.wParticles.remove(i);
            }
        }
    }

    private void rebirth() {
        this.halfDead = false;
        this.reborn = true;
        AbstractDungeon.actionManager.addToBottom(new SFXAction("VO_AWAKENEDONE_1"));
        AbstractDungeon.actionManager.addToBottom(new VFXAction(this, new IntenseZoomEffect(this.hb.cX, this.hb.cY, true), 0.05F, true));
        this.setHp(this.maxHealth * 2);
        AbstractDungeon.actionManager.addToBottom(new HealAction(this, this, this.maxHealth));
        AbstractDungeon.actionManager.addToBottom(new CanLoseAction());
        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this, this, RitualPower.POWER_ID));
        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this, this, UnawakenedPower.POWER_ID));
        AbstractPower pow = this.getPower(StrengthPower.POWER_ID);
        if(pow != null) {
            AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(this, this, pow, (pow.amount + 1) / 2));
        }
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new PrematureAwakening(this,3)));
        (new RollMoveAction(this.wokeone)).update();
        this.wokeone.createIntent();
        (new RollMoveAction(this)).update();
    }

    @Override
    public void die() {
        if(!this.reborn) {
            if(!this.halfDead) {
                this.setMove((byte) 255, Intent.UNKNOWN);
                this.createIntent();
                this.halfDead = true;

                AbstractDungeon.actionManager.addToBottom(new RemoveDebuffsAction(this));
                PaleOfTheAncients.deathTriggers(this);
            }
        } else {
            super.die();
        }
    }


    public void render(SpriteBatch sb) {
        for(final AwakenedWingParticle p : this.wParticles) {
            if (p.renderBehind) {
                p.render(sb, this.skeleton.getX() + this.back.getWorldX() + this.hb.width / 2F, this.skeleton.getY() + this.back.getWorldY());
            }
        }

        super.render(sb);

        for(final AwakenedWingParticle p : this.wParticles) {
            if (!p.renderBehind) {
                p.render(sb, this.skeleton.getX() + this.back.getWorldX() + this.hb.width / 2F, this.skeleton.getY() + this.back.getWorldY());
            }
        }
    }
}
