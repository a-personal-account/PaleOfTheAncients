package paleoftheancients.timeeater.monsters;

import actlikeit.dungeons.CustomDungeon;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.ShoutAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.unique.RemoveDebuffsAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Slimed;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.beyond.TimeEater;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.powers.watcher.EnergyDownPower;
import com.megacrit.cardcrawl.stances.CalmStance;
import com.megacrit.cardcrawl.stances.NeutralStance;
import com.megacrit.cardcrawl.vfx.combat.HeartMegaDebuffEffect;
import com.megacrit.cardcrawl.vfx.combat.ShockWaveEffect;
import paleoftheancients.PaleMod;
import paleoftheancients.dungeons.PaleOfTheAncients;
import paleoftheancients.helpers.AbstractMultiIntentMonster;
import paleoftheancients.helpers.DelayPower;
import paleoftheancients.relics.SoulOfTheTimeEater;
import paleoftheancients.theshowman.monsters.DummyMonster;
import paleoftheancients.timeeater.actions.WaitOnSignalAction;
import paleoftheancients.timeeater.intent.TimeEatererIntentEnums;
import paleoftheancients.timeeater.powers.TimeWarpIntentPower;
import paleoftheancients.timeeater.vfx.ApocalypseVFX;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TimeEaterer extends AbstractMultiIntentMonster {
    public static final String ID = PaleMod.makeID("TimeEaterer");
    private static final MonsterStrings monsterStrings;
    public static final String[] MOVES;
    public static final String[] DIALOG;

    public final static byte HASTE = 0;
    public final static byte SINGULARAPOCALYPSE = 1;
    public final static byte DUALAPOCALYPSE = 2;
    public final static byte TRIPLEAPOCALYPSE = 3;
    private final static byte REVERB = 4;
    private final static byte HEADSLAM = 5;
    private final static byte DEBUFF = 6;

    private int intentBaseAmount;

    private boolean hasteUsed = false;
    private boolean usingTripleApocalypse = false;
    private boolean[] apocalypseVoiceLines;
    private byte upcoming;
    private float timescale;


    public TimeEaterer() {
        super(TimeEater.NAME, ID, 456, -10.0F, -30.0F, 476.0F, 410.0F, (String) null, -50.0F, 30.0F);

        this.setHp(calcAscensionNumber(this.maxHealth * 1.5F));

        loadAnimation(PaleMod.assetPath("images/timeeater/skeleton.atlas"), PaleMod.assetPath("images/timeeater/skeleton.json"), 1.0F);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "Idle", true);
        e.setTime(e.getEndTime() * MathUtils.random());
        this.stateData.setMix("Hit", "Idle", 0.1F);
        e.setTimeScale(timescale = 0.8F);
        this.type = EnemyType.BOSS;
        this.dialogX = -200.0F * Settings.scale;
        this.dialogY = 10.0F * Settings.scale;


        addMove(HASTE, Intent.BUFF);
        addMove(SINGULARAPOCALYPSE, TimeEatererIntentEnums.SingularApocalypseIntent, calcAscensionNumber(16));
        addMove(DUALAPOCALYPSE, TimeEatererIntentEnums.DualApocalypseIntent, calcAscensionNumber(16), 4, true);
        addMove(TRIPLEAPOCALYPSE, TimeEatererIntentEnums.TripleApocalypseIntent, calcAscensionNumber(16), 9, true);
        addMove(REVERB, Intent.ATTACK, calcAscensionNumber(6), 3, true);
        addMove(HEADSLAM, Intent.ATTACK_DEBUFF, calcAscensionNumber(12));
        addMove(DEBUFF, Intent.DEFEND_DEBUFF);

        this.intentBaseAmount = 1;
        this.upcoming = -1;

        this.apocalypseVoiceLines = new boolean[3];
        Arrays.fill(apocalypseVoiceLines, false);
    }

    @Override
    public void usePreBattleAction() {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new TimeWarpIntentPower(this)));
        this.changeState(AbstractDungeon.ascensionLevel >= 4 ? CalmStance.STANCE_ID : NeutralStance.STANCE_ID);
        PaleOfTheAncients.playTempMusic(PaleMod.makeID("theextreme"));
    }

    @Override
    public void takeCustomTurn(DamageInfo info, int multiplier) {
        AbstractCreature dis = this;
        switch (this.nextMove) {
            case HASTE:
                AbstractDungeon.actionManager.addToBottom(new ShoutAction(this, TimeEater.DIALOG[1], 0.5F, 2.0F));
                AbstractDungeon.actionManager.addToBottom(new RemoveDebuffsAction(this));
                AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this, this, GainStrengthPower.POWER_ID));
                AbstractDungeon.actionManager.addToBottom(new HealAction(this, this, this.maxHealth / 2 - this.currentHealth));
                if (AbstractDungeon.ascensionLevel >= 19)
                    AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this, calcAscensionNumber(46)));
                timescale *= 2;
                this.state.getCurrent(0).setTimeScale(timescale);
                this.hasteUsed = true;
                intentBaseAmount++;
                break;

            case SINGULARAPOCALYPSE:
                if (!apocalypseVoiceLines[0]) {
                    apocalypseVoiceLines[0] = true;
                    this.addToBot(new ShoutAction(this, DIALOG[0]));
                }
                this.addToBot(new WaitOnSignalAction(new ApocalypseVFX(AbstractDungeon.player, this, 1, new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.NONE))));
                break;

            case DUALAPOCALYPSE:
                if (!apocalypseVoiceLines[1]) {
                    apocalypseVoiceLines[1] = true;
                    this.addToBot(new ShoutAction(this, DIALOG[1]));
                }
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new HeartMegaDebuffEffect()));
                this.addToBot(new WaitOnSignalAction(new ApocalypseVFX(AbstractDungeon.player, this, multiplier, new AbstractGameAction() {
                    @Override
                    public void update() {
                        this.isDone = true;
                        for (int i = multiplier; i > 0; i--) {
                            addToTop(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.NONE));
                        }
                        addToTop(new ApplyPowerAction(AbstractDungeon.player, dis, new StrengthPower(AbstractDungeon.player, -2), -2));
                    }
                })));
                break;

            case TRIPLEAPOCALYPSE:
                if (!apocalypseVoiceLines[2]) {
                    apocalypseVoiceLines[2] = true;
                    this.addToBot(new ShoutAction(this, DIALOG[2]));
                }
                usingTripleApocalypse = false;
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new HeartMegaDebuffEffect()));
                this.addToBot(new WaitOnSignalAction(new ApocalypseVFX(AbstractDungeon.player, this, multiplier, new AbstractGameAction() {
                    @Override
                    public void update() {
                        this.isDone = true;
                        for (int i = multiplier; i > 0; i--) {
                            addToTop(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.NONE));
                        }
                        addToTop(new ApplyPowerAction(AbstractDungeon.player, dis, new EnergyDownPower(AbstractDungeon.player, 1, true), 1));
                        addToTop(new ApplyPowerAction(AbstractDungeon.player, dis, new DexterityPower(AbstractDungeon.player, -2), -2));
                        addToTop(new ApplyPowerAction(AbstractDungeon.player, dis, new StrengthPower(AbstractDungeon.player, -2), -2));
                    }
                })));

                break;

            case REVERB:
                for (int i = 0; i < 3; i++) {
                    AbstractDungeon.actionManager.addToBottom(new VFXAction(this, new ShockWaveEffect(this.hb.cX, this.hb.cY, Settings.BLUE_TEXT_COLOR, ShockWaveEffect.ShockWaveType.CHAOTIC), 0.75F));
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.FIRE));
                }
                break;
            case DEBUFF:
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this, calcAscensionNumber(30)));
                DelayPower.addToBottom(new VulnerablePower(AbstractDungeon.player, 1, true), this);
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new WeakPower(AbstractDungeon.player, 1, true), 1));
                if (AbstractDungeon.ascensionLevel >= 19) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new FrailPower(AbstractDungeon.player, 1, true), 1));
                }
                break;
            case HEADSLAM:
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "ATTACK"));
                AbstractDungeon.actionManager.addToBottom(new WaitAction(0.4F));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.POISON));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new DrawReductionPower(AbstractDungeon.player, 1)));
                if (AbstractDungeon.ascensionLevel >= 19) {
                    AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(new Slimed(), 2));
                }
                break;
        }
    }

    @Override
    public void takeTurn() {
        DummyMonster mo = dummies.remove(dummies.size() - 1);
        super.takeTurn();
        dummies.add(mo);
    }

    public void newIntent() {
        AbstractDungeon.aiRng.random(100);

        if(upcoming == DUALAPOCALYPSE || upcoming == TRIPLEAPOCALYPSE) {
            upcoming--;
            for (int i = 0; i < dummies.size() - 1; i++) {
                DummyMonster monster = dummies.get(i);
                if (monster.nextMove == upcoming) {
                    dummies.remove(i);
                    break;
                }
            }
        }
        allocateMove();
        upcoming = dummies.get(dummies.size() - 1).nextMove;
        setDummyPositions();
    }

    @Override
    public void getMove(int num) {
        this.setIntentAmount(intentBaseAmount + 1);

        if(upcoming == DUALAPOCALYPSE || upcoming == TRIPLEAPOCALYPSE) {
            upcoming = SINGULARAPOCALYPSE;
        }

        for (int i = 0; i <= intentBaseAmount; i++) {
            if (upcoming > -1) {
                if (i == 0 && this.currentHealth < this.maxHealth / 2 && !hasteUsed) {
                    setMoveShortcut(HASTE, MOVES[HASTE]);
                } else {
                    setMoveShortcut(upcoming, upcoming < MOVES.length ? MOVES[upcoming] : null);
                    upcoming = -1;
                }
            } else {
                allocateMove(i);
            }
        }
        upcoming = dummies.get(dummies.size() - 1).nextMove;
    }

    private void allocateMove() {
        this.allocateMove(dummies.size());
    }

    private void allocateMove(int dummyIndex) {
        byte next;
        byte last = -1;
        if (dummyIndex > 0) {
            last = dummies.get(dummyIndex - 1).nextMove;
        }

        List<Byte> possibilities = new ArrayList<>();
        if (last != HEADSLAM) {
            possibilities.add(HEADSLAM);
        }
        if (last != REVERB) {
            possibilities.add(REVERB);
        }
        if (last != DEBUFF) {
            possibilities.add(DEBUFF);
        }
        if (!usingTripleApocalypse && (last != SINGULARAPOCALYPSE && last != DUALAPOCALYPSE)) {
            //50% chance that apocalypse comes up next everytime if there wasn't one last intent.
            for (int i = possibilities.size() - 1; i >= 0; i--) {
                possibilities.add(SINGULARAPOCALYPSE);
            }
        }

        next = possibilities.get(AbstractDungeon.aiRng.random(possibilities.size() - 1));
        if(next == SINGULARAPOCALYPSE && dummyIndex == dummies.size()) {
            for(int i = 0; i < dummyIndex; i++) {
                DummyMonster mo = dummies.get(i);
                switch (mo.nextMove) {
                    case DUALAPOCALYPSE:
                        usingTripleApocalypse = true;
                    case SINGULARAPOCALYPSE:
                        next = mo.nextMove;
                        next++;
                        mo = null;
                        break;
                }
                if(mo == null) {
                    break;
                }
            }
        }
        setMoveShortcut(next, next < MOVES.length ? MOVES[next] : null);
    }

    @Override
    public void changeState(String stateName) {
        switch (stateName) {
            case "ATTACK":
                this.state.setAnimation(0, "Attack", false).setTimeScale(timescale);
                this.state.addAnimation(0, "Idle", true, 0.0F).setTimeScale(timescale);
                break;
        }
    }

    public void damage(DamageInfo info) {
        super.damage(info);
        if (info.owner != null && info.type != DamageInfo.DamageType.THORNS && info.output > 0) {
            this.state.setAnimation(0, "Hit", false).setTimeScale(timescale);
            this.state.addAnimation(0, "Idle", true, 0.0F).setTimeScale(timescale);
        }
    }

    @Override
    public void die(boolean triggerRelics) {
        PaleOfTheAncients.addRelicReward(SoulOfTheTimeEater.ID);
        super.die(triggerRelics);
        CustomDungeon.resumeMainMusic();
    }

    @Override
    public void render(SpriteBatch sb) {
        dummies.get(dummies.size() - 1).intentAlpha = 0.5F;
        super.render(sb);
        dummies.get(dummies.size() - 1).intentAlpha = 1F;
    }

    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
        MOVES = monsterStrings.MOVES;
        DIALOG = monsterStrings.DIALOG;
    }
}