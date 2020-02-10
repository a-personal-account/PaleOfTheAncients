package paleoftheancients.watcher.monsters;

import actlikeit.dungeons.CustomDungeon;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.esotericsoftware.spine.*;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.purple.*;
import com.megacrit.cardcrawl.cards.tempCards.ThroughViolence;
import com.megacrit.cardcrawl.characters.Watcher;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.watcher.MarkPower;
import com.megacrit.cardcrawl.stances.CalmStance;
import com.megacrit.cardcrawl.stances.DivinityStance;
import com.megacrit.cardcrawl.stances.NeutralStance;
import com.megacrit.cardcrawl.stances.WrathStance;
import com.megacrit.cardcrawl.vfx.combat.FlameBarrierEffect;
import com.megacrit.cardcrawl.vfx.combat.PressurePointEffect;
import com.megacrit.cardcrawl.vfx.combat.ViolentAttackEffect;
import paleoftheancients.PaleMod;
import paleoftheancients.dungeons.PaleOfTheAncients;
import paleoftheancients.helpers.AbstractBossMonster;
import paleoftheancients.relics.SoulOfTheWatcher;
import paleoftheancients.watcher.cards.RemoveWrath;
import paleoftheancients.watcher.intent.WatcherIntentEnums;
import paleoftheancients.watcher.powers.*;
import paleoftheancients.watcher.stances.*;

import java.util.ArrayList;

public class TheWatcher extends AbstractBossMonster {
    public static final String ID = PaleMod.makeID("TheWatcher");
    private Bone eyeBone;
    protected TextureAtlas eyeAtlas = null;
    protected Skeleton eyeSkeleton;
    public AnimationState eyeState;
    protected AnimationStateData eyeStateData;

    public static final String NAME;
    public static final String[] MOVES;

    public final static byte PRESSUREPOINTS = 0;
    private final static byte REACHHEAVEN = 1;
    public final static byte THROUGHVIOLENCE = 2;
    private final static byte WALLOP = 3;
    private final static byte FLAMEWREATH = 4;
    private final static byte RAGNAROK = 5;
    private final static byte TALKTOTHEHAND = 6;
    private final static byte FEARNOEVIL = 7;
    private final static byte SIMMERINGFURY = 8;
    private final static byte TANTRUM = 9;
    public final static byte BLASPHEMY = 10;
    private final static byte THIRDEYE = 11;
    private final static byte VIGILANCE = 12;

    public AbstractEnemyStance stance = null;
    private boolean bloodied = false;
    private int untilPhaseChange = 2;
    public void incrementPhaseChangeCounter() {
        untilPhaseChange++;
    }

    public TheWatcher() {
        super(NAME, ID, 1060,0.0F, -15.0F, 240.0F, 290.0F, null, 0, 0);

        this.setHp(calcAscensionNumber(this.maxHealth));
        this.flipHorizontal = true;

        loadAnimation("images/characters/watcher/idle/skeleton.atlas", "images/characters/watcher/idle/skeleton.json", 1.0F);
        loadEyeAnimation();
        AnimationState.TrackEntry e = this.state.setAnimation(0, "Idle", true);
        this.stateData.setMix("Hit", "Idle", 0.1F);
        e.setTimeScale(0.7F);
        this.eyeBone = this.skeleton.findBone("eye_anchor");


        addMove(PRESSUREPOINTS, WatcherIntentEnums.PressurePointsIntent, calcAscensionNumber(16));
        addMove(REACHHEAVEN, Intent.ATTACK_BUFF, calcAscensionNumber(20));
        addMove(THROUGHVIOLENCE, Intent.ATTACK, calcAscensionNumber(40));
        addMove(WALLOP, Intent.ATTACK_DEFEND, calcAscensionNumber(28));
        addMove(FLAMEWREATH, Intent.BUFF, -1, calcAscensionNumber(5));
        addMove(RAGNAROK, Intent.ATTACK, calcAscensionNumber(10), 5, true);
        addMove(TALKTOTHEHAND, Intent.ATTACK_DEBUFF, calcAscensionNumber(16));
        addMove(FEARNOEVIL, Intent.ATTACK_BUFF, calcAscensionNumber(32));
        addMove(SIMMERINGFURY, Intent.BUFF);
        addMove(TANTRUM, Intent.ATTACK_BUFF, calcAscensionNumber(12), 3, true);
        addMove(BLASPHEMY, WatcherIntentEnums.BlasphemyIntent);
        addMove(THIRDEYE, Intent.DEFEND_DEBUFF, -1, calcAscensionNumber(3));
        addMove(VIGILANCE, Intent.DEFEND_BUFF);
    }
    @Override
    public void usePreBattleAction() {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new FakeLikeWaterPower(this, AbstractDungeon.ascensionLevel >= 9 ? 14 : 10)));
        this.changeState(AbstractDungeon.ascensionLevel >= 4 ? CalmStance.STANCE_ID : NeutralStance.STANCE_ID);
        PaleOfTheAncients.playTempMusic(PaleMod.makeID("thunderrolls"));

        int cardAmount = 6 - calcAscensionNumber(3);
        this.addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new HandSpacePower(AbstractDungeon.player, cardAmount), cardAmount));
        this.addToBot(new MakeTempCardInHandAction(new RemoveWrath(), cardAmount));
        this.addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                this.isDone = true;
                for(final AbstractCard card : AbstractDungeon.player.hand.group) {
                    if(card.cardID == RemoveWrath.ID) {
                        ((RemoveWrath) card).prime();
                    }
                }
            }
        });
    }

    @Override
    public void takeCustomTurn(DamageInfo info, int multiplier) {
        if(info.base > -1 && this.nextMove != PRESSUREPOINTS) {
            this.addToBot(new RemoveSpecificPowerAction(this, this, FakeVigorPower.POWER_ID));
        }
        switch(this.nextMove) {
            case PRESSUREPOINTS:
                this.addToBot(new VFXAction(new PressurePointEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY)));
                this.addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new MarkPower(AbstractDungeon.player, this.getIntentBaseDmg()), this.getIntentBaseDmg()));
                info.output = getPressurePointsDamage(this);
                this.addToBot(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.FIRE));
                break;

            case REACHHEAVEN:
                this.addToBot(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.SLASH_HEAVY));
                this.addToBot(new ApplyPowerAction(this, this, new ReachedHeavenPower(this, 1), 1));
                break;

            case THROUGHVIOLENCE:
                this.addToBot(new VFXAction(new ViolentAttackEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, Color.VIOLET)));
                this.addToBot(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.SLASH_HEAVY));
                this.addToBot(new ReducePowerAction(this, this, ReachedHeavenPower.POWER_ID, 1));
                break;

            case WALLOP:
                this.addToBot(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                this.addToBot(new GainBlockAction(this, this, info.output));
                break;

            case FLAMEWREATH:
                this.addToBot(new VFXAction(this, new FlameBarrierEffect(hb.cX, hb.cY), 0.1F));
                this.addToBot(new ApplyPowerAction(this, this, new FakeVigorPower(this, multiplier), multiplier));
                break;

            case RAGNAROK:
                for(int i = 0; i < multiplier; ++i) {
                    this.addToBot(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.LIGHTNING));
                }
                break;

            case TALKTOTHEHAND:
                this.addToBot(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                this.addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new FakeTalkToHandPower(AbstractDungeon.player, 5), 5));
                break;

            case FEARNOEVIL:
                this.addToBot(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.SLASH_HEAVY));
                if(bloodied) {
                    this.addToBot(new ChangeStateAction(this, CalmStance.STANCE_ID));
                }
                break;

            case SIMMERINGFURY:
                this.untilPhaseChange = 2;
                stance.ID = WrathStance.STANCE_ID;
                this.addToBot(new ApplyPowerAction(this, this, new FakeWrathNextTurnPower(this)));
                break;

            case TANTRUM:
                for(int i = 0; i < multiplier; ++i) {
                    this.addToBot(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                }
                this.addToBot(new ChangeStateAction(this, WrathStance.STANCE_ID));
                break;

            case BLASPHEMY:
                this.addToBot(new ChangeStateAction(this, DivinityStance.STANCE_ID));
                this.addToBot(new ApplyPowerAction(this, this, new FakeBlasphemyPower(this)));
                this.addToBot(new RemoveSpecificPowerAction(this, this, FakeWrathNextTurnPower.POWER_ID));
                break;

            case THIRDEYE: {
                this.addToBot(new GainBlockAction(this, this, calcAscensionNumber(30)));
                for(int i = calcAscensionNumber(3); i > 0; i--) {
                    if(AbstractDungeon.player.drawPile.size() > i) {
                        final AbstractCard tmp = AbstractDungeon.player.drawPile.getNCardFromTop(i);
                        if (tmp.type == AbstractCard.CardType.ATTACK) {
                            addToTop(new AbstractGameAction() {
                                @Override
                                public void update() {
                                    this.isDone = true;
                                    AbstractDungeon.player.drawPile.moveToDiscardPile(tmp);
                                }
                            });
                        }
                    }
                }
                break;
            }

            case VIGILANCE:
                this.addToBot(new GainBlockAction(this, this, calcAscensionNumber(40)));
                this.addToBot(new ChangeStateAction(this, CalmStance.STANCE_ID));
                break;
        }
        this.bloodied = false;
        this.untilPhaseChange--;
    }

    @Override
    public void getMove(int num) {
        if(this.currentHealth < this.maxHealth * 0.4F && this.stance.ID != DivinityStance.STANCE_ID) {
            setMoveShortcut(BLASPHEMY, MOVES[BLASPHEMY]);
            return;
        }

        ArrayList<Byte> possibilities = new ArrayList<>();
        if(this.untilPhaseChange <= 0 && stance.ID != DivinityStance.STANCE_ID) {
            switch(stance.ID) {
                case WrathStance.STANCE_ID:
                    for(int i = -2; i < 0; i++) {
                        if(i < this.untilPhaseChange) {
                            possibilities.add(FEARNOEVIL);
                        } else {
                            possibilities.add(VIGILANCE);
                        }
                    }
                    break;
                default:
                    possibilities.add(SIMMERINGFURY);
                    possibilities.add(TANTRUM);
                    possibilities.add(TANTRUM);
                    break;
            }
        } else {
            try {
                switch (stance.ID) {
                    case DivinityStance.STANCE_ID:
                        if (!this.hasPower(FakeVigorPower.POWER_ID)) {
                            setMoveShortcut(RAGNAROK, MOVES[RAGNAROK]);
                            return;
                        }
                        if (this.hasPower(ReachedHeavenPower.POWER_ID)) {
                            setMoveShortcut(THROUGHVIOLENCE, MOVES[THROUGHVIOLENCE]);
                            return;
                        }
                        setMoveShortcut(TANTRUM, MOVES[TANTRUM]);
                        return;

                    case WrathStance.STANCE_ID:
                        if (this.hasPower(ReachedHeavenPower.POWER_ID)) {
                            possibilities.add(THROUGHVIOLENCE);
                            possibilities.add(THROUGHVIOLENCE);
                            possibilities.add(THROUGHVIOLENCE);
                        }
                        possibilities.add(TANTRUM);
                        possibilities.add(WALLOP);
                        possibilities.add(WALLOP);
                        possibilities.add(THIRDEYE);
                        possibilities.add(TALKTOTHEHAND);
                        possibilities.add(REACHHEAVEN);
                        possibilities.add(RAGNAROK);
                        possibilities.add(THIRDEYE);
                        break;

                    default:
                        throw new NullPointerException();
                }
            } catch(NullPointerException npe) {
                possibilities.add(PRESSUREPOINTS);
                possibilities.add(PRESSUREPOINTS);
                possibilities.add(PRESSUREPOINTS);
                possibilities.add(REACHHEAVEN);
                possibilities.add(REACHHEAVEN);
                possibilities.add(TALKTOTHEHAND);
                possibilities.add(WALLOP);
                possibilities.add(FLAMEWREATH);
                possibilities.add(THIRDEYE);
                possibilities.add(THIRDEYE);
            }
        }

        for(int i = this.moveHistory.size() - 1, found = 0; i >= this.moveHistory.size() - 4 && i >= 0 && found < 2 && possibilities.size() > 1; i--) {
            Byte b = moveHistory.get(i);
            if(possibilities.contains(b)) {
                found++;

                while(possibilities.remove(b));
                if(possibilities.isEmpty()) {
                    possibilities.add(b);
                }
            }
        }
        byte move = possibilities.get(AbstractDungeon.monsterRng.random(possibilities.size() - 1));
        this.setMoveShortcut(move, MOVES[move]);
    }

    public static int getPressurePointsDamage(AbstractMonster mo) {
        AbstractPower pow = AbstractDungeon.player.getPower(MarkPower.POWER_ID);
        int num = mo.getIntentBaseDmg();
        if(pow != null) {
            num += pow.amount;
        }
        return num;
    }

    @Override
    public void changeState(String state) {
        switch(state) {
            case CalmStance.STANCE_ID:
            case WrathStance.STANCE_ID:
            case DivinityStance.STANCE_ID:
                this.untilPhaseChange = 2;
                this.eyeState.setAnimation(0, state, true);

                switch(state) {
                    case CalmStance.STANCE_ID:
                        stance = new CalmEnemyStance(this);
                        break;
                    case WrathStance.STANCE_ID:
                        stance = new WrathEnemyStance(this);
                        this.addToTop(new ApplyPowerAction(this, this, new WrathStancePower(this)));
                        break;
                    case DivinityStance.STANCE_ID:
                        stance = new DivinityEnemyStance(this);
                        this.addToTop(new ApplyPowerAction(this, this, new DivinityStancePower(this)));
                        break;
                }
                this.addToTop(new RemoveSpecificPowerAction(this, this, AbstractStancePower.POWER_ID));
                break;
            case NeutralStance.STANCE_ID:
                this.eyeState.setAnimation(0, "None", true);
                stance = new NeutralEnemyStance(this);
                this.addToTop(new RemoveSpecificPowerAction(this, this, AbstractStancePower.POWER_ID));
                break;
        }
    }

    @Override
    public void damage(DamageInfo info) {
        if (info.owner != null && info.type != DamageInfo.DamageType.THORNS && info.output - this.currentBlock > 0 && this.atlas != null) {
            AnimationState.TrackEntry e = this.state.setAnimation(0, "Hit", false);
            this.state.addAnimation(0, "Idle", true, 0.0F);
            e.setTime(0.9F);
        }
        this.bloodied = true;
        super.damage(info);
    }

    @Override
    public void die(boolean triggerRelics) {
        CustomDungeon.addRelicReward(SoulOfTheWatcher.ID);
        super.die(triggerRelics);
        CustomDungeon.resumeMainMusic();
    }

    private void loadEyeAnimation() {
        this.eyeAtlas = new TextureAtlas(Gdx.files.internal("images/characters/watcher/eye_anim/skeleton.atlas"));
        SkeletonJson json = new SkeletonJson(this.eyeAtlas);
        json.setScale(Settings.scale / 1.0F);
        SkeletonData skeletonData = json.readSkeletonData(Gdx.files.internal("images/characters/watcher/eye_anim/skeleton.json"));
        this.eyeSkeleton = new Skeleton(skeletonData);
        this.eyeSkeleton.setColor(Color.WHITE);
        this.eyeStateData = new AnimationStateData(skeletonData);
        this.eyeState = new AnimationState(this.eyeStateData);
        this.eyeStateData.setDefaultMix(0.2F);
        this.eyeState.setAnimation(0, "None", true);
    }

    @Override
    public void update() {
        super.update();
        if(stance != null) {
            stance.update();
        }
    }
    @Override
    public void render(SpriteBatch sb) {
        if(stance != null) {
            stance.render(sb);
        }
        super.render(sb);

        this.eyeState.update(Gdx.graphics.getDeltaTime());
        this.eyeState.apply(this.eyeSkeleton);
        this.eyeSkeleton.updateWorldTransform();
        this.eyeSkeleton.setPosition(this.skeleton.getX() + this.eyeBone.getWorldX(), this.skeleton.getY() + this.eyeBone.getWorldY());
        this.eyeSkeleton.setColor(this.tint.color);
        this.eyeSkeleton.setFlip(this.flipHorizontal, this.flipVertical);
        sb.end();
        CardCrawlGame.psb.begin();
        sr.draw(CardCrawlGame.psb, this.eyeSkeleton);
        CardCrawlGame.psb.end();
        sb.begin();
    }

    static {
        NAME = Watcher.NAMES[0];
        MOVES = new String[] {
                PaleMod.getCardName(PressurePoints.class),
                PaleMod.getCardName(ReachHeaven.class),
                PaleMod.getCardName(ThroughViolence.class),
                PaleMod.getCardName(Wallop.class),
                PaleMod.getCardName(WreathOfFlame.class),
                PaleMod.getCardName(Ragnarok.class),
                PaleMod.getCardName(TalkToTheHand.class),
                PaleMod.getCardName(FearNoEvil.class),
                PaleMod.getCardName(SimmeringFury.class),
                PaleMod.getCardName(Tantrum.class),
                PaleMod.getCardName(Blasphemy.class),
                PaleMod.getCardName(ThirdEye.class),
                PaleMod.getCardName(Vigilance.class)
        };
    }
}
