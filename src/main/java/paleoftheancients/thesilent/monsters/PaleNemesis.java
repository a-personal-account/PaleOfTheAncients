package paleoftheancients.thesilent.monsters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.Bone;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.beyond.Nemesis;
import com.megacrit.cardcrawl.powers.IntangiblePower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.NemesisFireParticle;
import com.megacrit.cardcrawl.vfx.combat.ShockWaveEffect;
import paleoftheancients.PaleMod;
import paleoftheancients.dungeons.PaleOfTheAncients;
import paleoftheancients.helpers.AbstractBossMonster;
import paleoftheancients.relics.SoulOfTheSilent;
import paleoftheancients.thesilent.powers.CorrosiveFumesPower;
import paleoftheancients.thevixen.actions.SetPlayerBurnAction;
import paleoftheancients.thevixen.cards.status.BossBurn;

public class PaleNemesis extends AbstractBossMonster {
    public static final String ID = PaleMod.makeID("PaleNemesis");
    private float fireTimer = 0.0F;
    private static final float FIRE_TIME = 0.05F;
    private Bone eye1;
    private Bone eye2;
    private Bone eye3;

    private static final byte BIGBURN = 0;
    private static final byte SCYTHE = 1;
    private static final byte TRIPLE = 2;
    private static final byte WRAITHFORM = 3;

    public PaleNemesis() {
        super(Nemesis.NAME, ID, 350, 0.0F, -20.0F, 260.0F, 360.0F, null, 0.0F, 20.0F);

        this.loadAnimation("images/monsters/theForest/nemesis/skeleton.atlas", "images/monsters/theForest/nemesis/skeleton.json", 1.0F);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "Idle", true);
        e.setTime(e.getEndTime() * MathUtils.random());
        this.stateData.setMix("Hit", "Idle", 0.1F);
        e.setTimeScale(0.8F);
        this.eye1 = this.skeleton.findBone("eye0");
        this.eye2 = this.skeleton.findBone("eye1");
        this.eye3 = this.skeleton.findBone("eye2");

        this.addMove(BIGBURN, Intent.STRONG_DEBUFF);
        this.addMove(SCYTHE, Intent.ATTACK_DEBUFF, 27);
        this.addMove(TRIPLE, Intent.ATTACK, 9, 3, true);
        this.addMove(WRAITHFORM, Intent.BUFF);
    }

    @Override
    public void takeCustomTurn(DamageInfo info, int multiplier) {
        switch(this.nextMove) {
            case BIGBURN:
                AbstractDungeon.actionManager.addToBottom(new SFXAction("VO_NEMESIS_1C"));
                AbstractDungeon.actionManager.addToBottom(new VFXAction(this, new ShockWaveEffect(this.hb.cX, this.hb.cY, Settings.GREEN_TEXT_COLOR, ShockWaveEffect.ShockWaveType.CHAOTIC), 1.5F));
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(new BossBurn(1), AbstractDungeon.ascensionLevel >= 19 ? 3 : 2, true, true));
                SetPlayerBurnAction.addToBottom();
                break;

            case SCYTHE: {
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "ATTACK"));
                playSfx();
                AbstractDungeon.actionManager.addToBottom(new WaitAction(0.4F));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.SLASH_HEAVY));
                //Halve the player's Strength
                int amnt = AbstractDungeon.ascensionLevel >= 19 ? 3 : 2;
                if(AbstractDungeon.player.hasPower(StrengthPower.POWER_ID)) {
                    amnt = Math.max(amnt, AbstractDungeon.player.getPower(StrengthPower.POWER_ID).amount / 2);
                }
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new StrengthPower(AbstractDungeon.player, -amnt), -amnt));
                break;
            }

            case TRIPLE:
                for (int i = 0; i < multiplier; i++) {
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.FIRE));
                }
                break;

            case WRAITHFORM: {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new IntangiblePower(this, 1), 1));
                int amnt = AbstractDungeon.ascensionLevel >= 19 ? 3 : 2;
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new CorrosiveFumesPower(this, amnt), amnt));
                break;
            }
        }
    }

    @Override
    public void changeState(String key) {
        AnimationState.TrackEntry e;
        switch (key) {
            case "ATTACK":
                e = this.state.setAnimation(0, "Attack", false);
                this.state.addAnimation(0, "Idle", true, 0.0F);
                e.setTimeScale(0.8F);
                break;
        }
    }

    @Override
    public void damage(DamageInfo info) {
        super.damage(info);

        if (info.owner != null && info.type != DamageInfo.DamageType.THORNS && info.output > 0) {
            AnimationState.TrackEntry e = this.state.setAnimation(0, "Hit", false);
            this.state.addAnimation(0, "Idle", true, 0.0F);
            e.setTimeScale(0.8F);
        }
    }

    @Override
    public void die(boolean triggerRelics) {
        PaleOfTheAncients.addRelicReward(SoulOfTheSilent.ID);
        super.die(triggerRelics);
    }

    private void playSfx() {
        int roll = MathUtils.random(1);
        if (roll == 0) {
            AbstractDungeon.actionManager.addToBottom(new SFXAction("VO_NEMESIS_1A"));
        } else {
            AbstractDungeon.actionManager.addToBottom(new SFXAction("VO_NEMESIS_1B"));
        }
    }

    private void playDeathSfx() {
        int roll = MathUtils.random(1);
        if (roll == 0) {
            CardCrawlGame.sound.play("VO_NEMESIS_2A");
        } else {
            CardCrawlGame.sound.play("VO_NEMESIS_2B");
        }
    }

    public void die() {
        playDeathSfx();
        PaleOfTheAncients.addRelicReward(SoulOfTheSilent.ID);
        super.die();
    }

    public void update() {
        super.update();
        if (!this.isDying) {
            this.fireTimer -= Gdx.graphics.getDeltaTime();
            if (this.fireTimer < 0.0F) {
                this.fireTimer = 0.05F;
                AbstractDungeon.effectList.add(new NemesisFireParticle(this.skeleton
                        .getX() + this.eye1.getWorldX(), this.skeleton.getY() + this.eye1.getWorldY()));
                AbstractDungeon.effectList.add(new NemesisFireParticle(this.skeleton
                        .getX() + this.eye2.getWorldX(), this.skeleton.getY() + this.eye2.getWorldY()));
                AbstractDungeon.effectList.add(new NemesisFireParticle(this.skeleton
                        .getX() + this.eye3.getWorldX(), this.skeleton.getY() + this.eye3.getWorldY()));
            }
        }
    }
}
