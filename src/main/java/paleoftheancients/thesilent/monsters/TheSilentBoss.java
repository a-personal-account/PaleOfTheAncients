package paleoftheancients.thesilent.monsters;

import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState.TrackEntry;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.unique.CanLoseAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.cards.green.*;
import com.megacrit.cardcrawl.cards.tempCards.Insight;
import com.megacrit.cardcrawl.characters.TheSilent;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.powers.watcher.EndTurnDeathPower;
import com.megacrit.cardcrawl.vfx.combat.GrandFinalEffect;
import com.megacrit.cardcrawl.vfx.combat.PotionBounceEffect;
import paleoftheancients.PaleMod;
import paleoftheancients.dungeons.PaleOfTheAncients;
import paleoftheancients.relics.SoulOfTheSilent;
import paleoftheancients.thesilent.actions.NotBouncingFlaskAction;
import paleoftheancients.thesilent.powers.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TheSilentBoss extends CustomMonster {

    private Map<Byte, EnemyMoveInfo> moves;
    private static final byte ENVENOM_CONST = 0;
    private static final byte BOUNCINGFLASK_CONST = 1;
    private static final byte NEUTRALIZE_CONST = 2;
    private static final byte STRIKE_CONST = 3;
    private static final byte DODGEANDROLL_CONST = 4;

    private static final byte NOXIOUSFUMES_CONST = 5;

    private static final byte DASH_CONST = 6;
    private static final byte STORMOFSTEEL_CONST = 7;
    private static final byte LEGSWEEP_CONST = 8;
    private static final byte GRANDFINALE_CONST = 9;
    private static final byte WRAITHFORM_CONST = 10;

    private static final byte CATALYST_CONST = 11;

    private static final byte DEAD = 12;

    public static final String ID = PaleMod.makeID("TheSilent");
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;


    private static final int CORROSION = 1;
    private static final int A_9_CORROSION = 2;
    private static final int A_19_CORROSION = 3;
    private int corrosion;


    private static final int ENCORROSION = 2;
    private static final int A_9_ENCORROSION = 3;
    private static final int A_19_ENCORROSION = 4;
    private int encorrosion;

    private static final int BOUNCE = 4;
    private static final int A_9_BOUNCE = 5;
    private int bounce;

    private static final int BOUNCEPOTENCY = 5;
    private static final int A_4_BOUNCEPOTENCY = 6;
    private static final int A_19_BOUNCEPOTENCY = 7;
    private int bouncepotency;

    private static final int BLOCK_AMT = 25;
    private static final int A_9_BLOCK_AMT = 30;
    private int blockAmt;

    private static final int STRIKE = 28;
    private static final int A_4_STRIKE = 30;
    private static final int A_19_STRIKE = 32;
    private int strike;

    private static final int NEUTRALIZE = 20;
    private static final int A_4_NEUTRALIZE = 22;
    private static final int A_19_NEUTRALIZE = 25;
    private int neutralize;

    private static final int NEUTRALIZE_WEAK = 2;
    private static final int A_9_NEUTRALIZE_WEAK = 3;
    private int neutralize_weak;

    private static final int STORM = 5;
    private static final int A_4_STORM = 6;
    private static final int A_19_STORM = 8;
    private int storm;

    private static final int STORM_DAMAGE = 4;
    private static final int A_9_STORM_DAMAGE = 5;
    private int storm_damage;

    private static final int WRAITH = 2;
    private static final int A_9_WRAITH = 3;
    private int wraith;

    private static final int FINALE = 40;
    private static final int A_4_FINALE = 45;
    private static final int A_19_FINALE = 50;
    private int finale;

    private int threshold = 0;
    private ArrayList<Byte> cycletracker = new ArrayList<>();

    public TheSilentBoss() {
        super(NAME, ID, 530, 0.0F, -15.0F, 300.0F, 230.0F, (String)null, 0.0F, 0.0F);
        this.dialogX = -30.0F * Settings.scale;
        this.dialogY = 30.0F * Settings.scale;
        this.type = EnemyType.BOSS;
        if (AbstractDungeon.ascensionLevel >= 9) {
            this.currentHealth /= 2;
            this.blockAmt = A_9_BLOCK_AMT;
            this.corrosion = A_9_CORROSION;
            this.encorrosion = A_9_ENCORROSION;
            this.bounce = A_9_BOUNCE;
            this.neutralize_weak = A_9_NEUTRALIZE_WEAK;
            this.storm_damage = A_9_STORM_DAMAGE;
            this.wraith = A_9_WRAITH;
        } else {
            this.blockAmt = BLOCK_AMT;
            this.corrosion = CORROSION;
            this.encorrosion = ENCORROSION;
            this.bounce = BOUNCE;
            this.neutralize_weak = NEUTRALIZE_WEAK;
            this.storm_damage = STORM_DAMAGE;
            this.wraith = WRAITH;
        }

        if (AbstractDungeon.ascensionLevel >= 19) {
            this.corrosion = A_19_CORROSION;
            this.encorrosion = A_19_ENCORROSION;
            this.bouncepotency = A_19_BOUNCEPOTENCY;
            this.strike = A_19_STRIKE;
            this.neutralize = A_19_NEUTRALIZE;
            this.storm = A_19_STORM;
            this.finale = A_19_FINALE;
        } else if (AbstractDungeon.ascensionLevel >= 4) {
            this.strike = A_4_STRIKE;
            this.neutralize = A_4_NEUTRALIZE;
            this.storm = A_4_STORM;
            this.finale = A_4_FINALE;
            this.bouncepotency = A_4_BOUNCEPOTENCY;
        } else {
            this.strike = STRIKE;
            this.neutralize = NEUTRALIZE;
            this.storm = STORM;
            this.finale = FINALE;
            this.bouncepotency = BOUNCEPOTENCY;
        }

        this.moves = new HashMap<>();
        this.moves.put(NOXIOUSFUMES_CONST, new EnemyMoveInfo(NOXIOUSFUMES_CONST, Intent.BUFF, -1, 0, false));
        this.moves.put(ENVENOM_CONST, new EnemyMoveInfo(ENVENOM_CONST, Intent.BUFF, -1, 0, false));
        this.moves.put(BOUNCINGFLASK_CONST, new EnemyMoveInfo(BOUNCINGFLASK_CONST, Intent.DEBUFF, -1, 0, false));
        this.moves.put(NEUTRALIZE_CONST, new EnemyMoveInfo(NEUTRALIZE_CONST, Intent.ATTACK_DEBUFF, this.neutralize, 0, false));
        this.moves.put(STRIKE_CONST, new EnemyMoveInfo(STRIKE_CONST, Intent.ATTACK, this.strike, 0, false));
        this.moves.put(DODGEANDROLL_CONST, new EnemyMoveInfo(DODGEANDROLL_CONST, Intent.DEFEND, -1, 0, false));
        this.moves.put(STORMOFSTEEL_CONST, new EnemyMoveInfo(STORMOFSTEEL_CONST, Intent.ATTACK, this.storm_damage, this.storm, true));
        this.moves.put(DASH_CONST, new EnemyMoveInfo(DASH_CONST, Intent.ATTACK_DEFEND, this.blockAmt + 5, 0, false));
        this.moves.put(LEGSWEEP_CONST, new EnemyMoveInfo(LEGSWEEP_CONST, Intent.DEFEND_DEBUFF, -1, 0, false));
        this.moves.put(GRANDFINALE_CONST, new EnemyMoveInfo(GRANDFINALE_CONST, Intent.ATTACK, this.finale, 0, false));
        this.moves.put(WRAITHFORM_CONST, new EnemyMoveInfo(WRAITHFORM_CONST, Intent.BUFF, -1, 0, false));
        this.moves.put(CATALYST_CONST, new EnemyMoveInfo(CATALYST_CONST, Intent.STRONG_DEBUFF, -1, 0, false));

        this.moves.put(DEAD, new EnemyMoveInfo(DEAD, Intent.NONE, -1, 0, false));

        this.loadAnimation("images/characters/theSilent/idle/skeleton.atlas", "images/characters/theSilent/idle/skeleton.json", 1.0F);
        TrackEntry e = this.state.setAnimation(0, "Idle", true);
        e.setTime(e.getEndTime() * MathUtils.random());

        this.flipHorizontal = true;
    }

    @Override
    public void usePreBattleAction() {
        int fumes = 1;
        if(AbstractDungeon.ascensionLevel >= 19) {
            fumes++;
        }
        if(AbstractDungeon.ascensionLevel >= 9) {
            fumes++;
        }
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new CorrosiveFumesPower(this, fumes), fumes));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new EncorrosionPower(this, this.encorrosion), this.encorrosion));
    }

    public void takeTurn() {
        DamageInfo info = new DamageInfo(this, moves.get(this.nextMove).baseDamage, DamageInfo.DamageType.NORMAL);
        if(info.base > -1) {
            info.applyPowers(this, AbstractDungeon.player);
            this.useFastAttackAnimation();
        }
        int multiplier;

        switch(this.nextMove) {

            case NOXIOUSFUMES_CONST:
                AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[1]));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new CorrosiveFumesPower(this, this.corrosion), this.corrosion));
                break;


            case BOUNCINGFLASK_CONST:
                AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[0]));
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new PotionBounceEffect(this.hb.cX, this.hb.cY, AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY), 0.3F));
                AbstractDungeon.actionManager.addToBottom(new NotBouncingFlaskAction(this, this.bouncepotency, this.bounce));
                break;

            case NEUTRALIZE_CONST:
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AttackEffect.BLUNT_HEAVY));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new WeakPower(AbstractDungeon.player, this.neutralize_weak, true), this.neutralize_weak));
                break;

            case STRIKE_CONST:
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AttackEffect.SLASH_HORIZONTAL));
                break;

            case DODGEANDROLL_CONST:
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this, this.blockAmt + this.dexterity()));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new NextTurnBlockPower(this, this.blockAmt + this.dexterity()), this.blockAmt + this.dexterity()));
                break;

            case STORMOFSTEEL_CONST:
                AttackEffect ae = AttackEffect.NONE;
                multiplier = moves.get(this.nextMove).multiplier;
                for(int i = 0; i < multiplier; i++) {
                    switch((int)(Math.random() * 3)) {
                        case 0:
                            ae = AttackEffect.SLASH_HORIZONTAL;
                            break;
                        case 1:
                            ae = AttackEffect.SLASH_VERTICAL;
                            break;
                        case 2:
                            ae = AttackEffect.SLASH_DIAGONAL;
                            break;
                    }
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, ae));
                }
                break;

            case GRANDFINALE_CONST:
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new GrandFinalEffect(), 1.0F));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AttackEffect.SLASH_HEAVY));
                break;


            case DASH_CONST:
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AttackEffect.SLASH_HORIZONTAL));
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this, this.blockAmt + this.dexterity()));
                break;

            case LEGSWEEP_CONST:
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this, this.blockAmt + this.dexterity() + 5));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new WeakPower(AbstractDungeon.player, this.neutralize_weak, false), this.neutralize_weak));
                break;


            case WRAITHFORM_CONST:
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new IntangiblePlayerPower(this, this.wraith + 1), this.wraith + 1));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new NotWraithFormPower(this, -1), -1));
                this.wraith = 0;
                break;

            case CATALYST_CONST:
                if(AbstractDungeon.player.hasPower(CorrosionPower.POWER_ID)) {
                    int amount = AbstractDungeon.player.getPower(CorrosionPower.POWER_ID).amount;
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new CorrosionPower(AbstractDungeon.player, amount), amount));
                }
                break;


            case DEAD: {
                PaleNemesis pn = new PaleNemesis();
                AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(AbstractDungeon.player, this, EndTurnDeathPower.POWER_ID));
                AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(pn, false));
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(new Insight(), 1));
                AbstractPower p = this.getPower(CorrosiveFumesPower.POWER_ID);
                if(p != null) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(pn, this, new CorrosiveFumesPower(pn, p.amount), p.amount));
                }
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(pn, pn, new IntangiblePower(pn, 1), 1));
                AbstractDungeon.actionManager.addToBottom(new CanLoseAction());
                this.halfDead = false;
                super.die(false);
                break;
            }
        }

        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(int num) {
        byte move = 0;
        byte index;

        switch(threshold) {
            case 0:
                if(this.currentHealth < this.maxHealth * 3 / 4) {
                    this.threshold = 1;
                    move = NOXIOUSFUMES_CONST;
                    resetCycleTwo();
                } else {
                    if(!this.hasPower(EncorrosionPower.POWER_ID) && !cycletracker.isEmpty()) {
                        move = ENVENOM_CONST;
                    } else if(cycletracker.isEmpty()) {
                        resetCycleOne();
                        move = BOUNCINGFLASK_CONST;
                    } else {
                        index = (byte)AbstractDungeon.monsterRng.random(cycletracker.size() - 1);
                        move = cycletracker.get(index);
                        cycletracker.remove(index);
                    }
                }
                break;

            case 1:
                if(this.currentHealth < this.maxHealth / 2) {
                    this.threshold = 2;
                    move = WRAITHFORM_CONST;
                    break;
                }
            case 2:
                if(cycletracker.size() == 0) {
                    resetCycleTwo();
                    if(AbstractDungeon.ascensionLevel >= 19 && AbstractDungeon.player.hasPower(CorrosionPower.POWER_ID)) {
                        move = CATALYST_CONST;
                    } else {
                        move = BOUNCINGFLASK_CONST;
                    }
                }
                if(move == 0){
                    index = (byte)AbstractDungeon.monsterRng.random(cycletracker.size() - 1);
                    move = cycletracker.get(index);
                    cycletracker.remove(index);
                }
                break;
        }

        if(move == GRANDFINALE_CONST) {
            AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[2]));
        }
        this.setMoveShortcut(move);
    }
    @Override
    public void rollMove() {
        this.getMove(0);
    }

    private void setMoveShortcut(byte next) {
        EnemyMoveInfo info = this.moves.get(next);
        this.setMove(MOVES[next], next, info.intent, info.baseDamage, info.multiplier, info.isMultiDamage);
    }

    private void resetCycleOne() {
        cycletracker.clear();
        cycletracker.add(NEUTRALIZE_CONST);
        cycletracker.add(STRIKE_CONST);
        cycletracker.add(DODGEANDROLL_CONST);
    }
    private void resetCycleTwo() {
        cycletracker.clear();
        cycletracker.add(STORMOFSTEEL_CONST);
        cycletracker.add(LEGSWEEP_CONST);
        cycletracker.add(DASH_CONST);
        if(this.wraith == 0) {
            cycletracker.add(GRANDFINALE_CONST);
        }
    }

    private int dexterity() {
        int amount = 0;
        if(this.hasPower(NotDexterityPower.POWER_ID)) {
            amount += this.getPower(NotDexterityPower.POWER_ID).amount;
        }
        return amount;
    }

    public void damage(DamageInfo info) {
        super.damage(info);
        if (info.owner != null && info.type != DamageType.THORNS && info.output > 0) {
            TrackEntry e = this.state.setAnimation(0, "Hit", false);
            this.state.addAnimation(0, "Idle", true, 0.0F);
            e.setTimeScale(0.9F);
        }
    }

    public void die(boolean triggerRelics) {
        if(AbstractDungeon.ascensionLevel >= 9) {
            this.halfDead = true;
            AbstractDungeon.getCurrRoom().cannotLose = true;
            this.setMoveShortcut(DEAD);
            this.createIntent();
            this.dispose();
            this.img = ImageMaster.loadImage("images/characters/theSilent/corpse.png");
        } else {
            PaleOfTheAncients.addRelicReward(SoulOfTheSilent.ID);
            AbstractDungeon.actionManager.addToTop(new TalkAction(this, DIALOG[(int) (Math.random() * 6) + 3]));
            super.die(triggerRelics);
        }
    }

    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
        NAME = TheSilent.NAMES[0];
        MOVES = new String[] {
                PaleMod.getCardName(Envenom.class),
                PaleMod.getCardName(BouncingFlask.class),
                PaleMod.getCardName(Neutralize.class),
                PaleMod.getCardName(MasterfulStab.class),
                PaleMod.getCardName(DodgeAndRoll.class),
                PaleMod.getCardName(NoxiousFumes.class),
                PaleMod.getCardName(Dash.class),
                PaleMod.getCardName(StormOfSteel.class),
                PaleMod.getCardName(LegSweep.class),
                PaleMod.getCardName(GrandFinale.class),
                PaleMod.getCardName(WraithForm.class),
                PaleMod.getCardName(Catalyst.class),
                ""
        };
        DIALOG = monsterStrings.DIALOG;
    }
}




