package paleoftheancients.maker.monsters;

import actlikeit.dungeons.CustomDungeon;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.Exordium;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import com.megacrit.cardcrawl.powers.watcher.WrathNextTurnPower;
import com.megacrit.cardcrawl.stances.CalmStance;
import com.megacrit.cardcrawl.stances.DivinityStance;
import com.megacrit.cardcrawl.stances.NeutralStance;
import com.megacrit.cardcrawl.stances.WrathStance;
import com.megacrit.cardcrawl.vfx.combat.FlameBarrierEffect;
import com.megacrit.cardcrawl.vfx.combat.PressurePointEffect;
import com.megacrit.cardcrawl.vfx.combat.ViolentAttackEffect;
import paleoftheancients.PaleMod;
import paleoftheancients.dungeons.PaleOfTheAncients;
import paleoftheancients.helpers.AbstractMultiIntentMonster;
import paleoftheancients.maker.powers.GainPlatedArmorPower;
import paleoftheancients.reimu.powers.InvincibleTresholdPower;
import paleoftheancients.relics.SoulOfTheWatcher;
import paleoftheancients.watcher.actions.PressurePointDamageAction;
import paleoftheancients.watcher.actions.RemovePowerSilentlyAction;
import paleoftheancients.watcher.intent.WatcherIntentEnums;
import paleoftheancients.watcher.powers.*;
import paleoftheancients.watcher.stances.CalmEnemyStance;
import paleoftheancients.watcher.stances.DivinityEnemyStance;
import paleoftheancients.watcher.stances.NeutralEnemyStance;
import paleoftheancients.watcher.stances.WrathEnemyStance;

import java.util.ArrayList;

public class Casey extends AbstractMultiIntentMonster {
    public static final String ID = PaleMod.makeID("Casey");
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;

    private final static byte EXORDIUM = 0;
    private final static byte THECITY = 1;
    private final static byte THEBEYOND = 2;
    private final static byte LOUSEBARRAGE = 3;
    private final static byte FLAMEWREATH = 4;
    private final static byte RAGNAROK = 5;
    private final static byte TALKTOTHEHAND = 6;
    private final static byte FEARNOEVIL = 7;
    private final static byte SIMMERINGFURY = 8;
    private final static byte TANTRUM = 9;
    private final static byte BLASPHEMY = 10;
    private final static byte THIRDEYE = 11;
    private final static byte VIGILANCE = 12;

    private int actionsPerTurn = 2;

    public Casey() {
        super(NAME, ID, 4000,0.0F, -15.0F, 240.0F, 290.0F, null, 0, 0);

        this.type = EnemyType.BOSS;
        this.dialogX = -100.0F * Settings.scale;
        this.dialogY = 10.0F * Settings.scale;
        this.loadAnimation("images/monsters/theCity/champ/skeleton.atlas", "images/monsters/theCity/champ/skeleton.json", 1.0F);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "Idle", true);
        e.setTime(e.getEndTime() * MathUtils.random());
        e.setTimeScale(0.8F);

        this.setHp(calcAscensionNumber(this.maxHealth));



        addMove(EXORDIUM, Intent.UNKNOWN);
        addMove(THECITY, Intent.UNKNOWN);
        addMove(THEBEYOND, Intent.UNKNOWN);
        addMove(LOUSEBARRAGE, Intent.ATTACK, calcAscensionNumber(12), calcAscensionNumber(3));

        addMove(RAGNAROK, Intent.ATTACK, calcAscensionNumber(10), 5, true);
        addMove(TALKTOTHEHAND, Intent.ATTACK_DEBUFF, calcAscensionNumber(16));
        addMove(FEARNOEVIL, Intent.ATTACK_BUFF, calcAscensionNumber(18));
        addMove(SIMMERINGFURY, Intent.BUFF);
        addMove(TANTRUM, Intent.ATTACK_BUFF, calcAscensionNumber(12), 3, true);
        addMove(BLASPHEMY, WatcherIntentEnums.BlasphemyIntent);
        addMove(THIRDEYE, Intent.DEFEND_DEBUFF, -1, calcAscensionNumber(3));
        addMove(VIGILANCE, Intent.DEFEND_BUFF);
    }
    @Override
    public void usePreBattleAction() {
        PaleOfTheAncients.playTempMusic(PaleMod.makeID("promisestokeep"));
        addToBot(new ApplyPowerAction(this, this, new InvincibleTresholdPower(this, 1F / 4F)));
        addToBot(new ApplyPowerAction(this, this, new GainPlatedArmorPower(this)));
        addToBot(new ApplyPowerAction(this, this, new PlatedArmorPower(this, calcAscensionNumber(calcAscensionNumber(8)))));
    }

    @Override
    public void takeCustomTurn(DamageInfo info, int multiplier) {
        if(info.base > -1 && this.nextMove != PRESSUREPOINTS) {
            this.addToBot(new RemoveSpecificPowerAction(this, this, FakeVigorPower.POWER_ID));
        }
        switch(this.nextMove) {
            case PRESSUREPOINTS:
                this.addToBot(new VFXAction(new PressurePointEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY)));
                this.addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new FakeMarkPower(AbstractDungeon.player, this.getIntentBaseDmg()), this.getIntentBaseDmg()));
                this.addToBot(new PressurePointDamageAction(AbstractDungeon.player, this));
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
                this.untilPhaseChange = turnsPerStance;
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
    }

    @Override
    public void takeTurn() {
        if(this.hasPower(WrathNextTurnPower.POWER_ID)) {
            this.addToTop(new RemoveSpecificPowerAction(this, this, WrathNextTurnPower.POWER_ID));
            this.addToTop(new ChangeStateAction(this, WrathStance.STANCE_ID));
        }
        super.takeTurn();
        this.bloodied = false;
        this.untilPhaseChange--;
    }

    @Override
    public void getMove(int num) {
        if(this.currentHealth < this.maxHealth * 0.4F && !this.stance.ID.equals(DivinityStance.STANCE_ID)) {
            this.setIntentAmount(1);
            setMoveShortcut(BLASPHEMY, MOVES[BLASPHEMY]);
            return;
        }

        boolean changeStance = this.untilPhaseChange <= 0 && !stance.ID.equals(DivinityStance.STANCE_ID);
        this.setIntentAmount(actionsPerTurn + (changeStance ? 1 : 0));
        ArrayList<Byte> possibilities = new ArrayList<>();

        if (changeStance) {
            switch (stance.ID) {
                case WrathStance.STANCE_ID:
                    for (int i = -2; i < 0; i++) {
                        if (i < this.untilPhaseChange) {
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
            byte move = possibilities.get(AbstractDungeon.monsterRng.random(possibilities.size() - 1));
            this.setMoveShortcut(move, MOVES[move]);
        }

        for(int moveIndex = 0; moveIndex < actionsPerTurn; moveIndex++) {
            possibilities.clear();
            try {
                switch (stance.ID) {
                    case DivinityStance.STANCE_ID:
                        if (!this.hasPower(FakeVigorPower.POWER_ID)) {
                            possibilities.add(RAGNAROK);
                        }
                        if (this.hasPower(ReachedHeavenPower.POWER_ID)) {
                            possibilities.add(THROUGHVIOLENCE);
                        }
                        possibilities.add(TANTRUM);
                        break;

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
            } catch (NullPointerException npe) {
                possibilities.add(PRESSUREPOINTS);
                possibilities.add(PRESSUREPOINTS);
                possibilities.add(PRESSUREPOINTS);
                possibilities.add(REACHHEAVEN);
                possibilities.add(REACHHEAVEN);
                possibilities.add(TALKTOTHEHAND);
                possibilities.add(WALLOP);
                //possibilities.add(FLAMEWREATH);
                possibilities.add(THIRDEYE);
                possibilities.add(THIRDEYE);
            }
            this.removeDuplicates(possibilities);

            for (int i = this.moveHistory.size() - 1, found = 0; i >= this.moveHistory.size() - 4 && i >= 0 && found < 2 && possibilities.size() > 1; i--) {
                Byte b = moveHistory.get(i);
                if (possibilities.contains(b)) {
                    found++;

                    while (possibilities.remove(b)) ;
                    if (possibilities.isEmpty()) {
                        possibilities.add(b);
                    }
                }
            }
            byte move = possibilities.get(AbstractDungeon.monsterRng.random(possibilities.size() - 1));
            this.setMoveShortcut(move, MOVES[move]);
        }
    }

    public static int getPressurePointsDamage(AbstractMonster mo) {
        if(AbstractDungeon.player.hasPower(IntangiblePlayerPower.POWER_ID)) {
            return 1;
        }
        AbstractPower pow = AbstractDungeon.player.getPower(FakeMarkPower.POWER_ID);
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
                if(stance == null || !stance.ID.equals(state)) {
                    this.untilPhaseChange = turnsPerStance;
                    this.eyeState.setAnimation(0, state, true);

                    switch (state) {
                        case CalmStance.STANCE_ID:
                            stance = new CalmEnemyStance(this);
                            this.addToTop(new ApplyPowerAction(this, this, new CalmStancePower(this)));
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
                    this.addToTop(new RemovePowerSilentlyAction(this, this, AbstractStancePower.POWER_ID));
                }
                break;
            case NeutralStance.STANCE_ID:
                this.eyeState.setAnimation(0, "None", true);
                stance = new NeutralEnemyStance(this);
                this.addToTop(new RemovePowerSilentlyAction(this, this, AbstractStancePower.POWER_ID));
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
        PaleOfTheAncients.addRelicReward(SoulOfTheWatcher.ID);
        super.die(triggerRelics);
        CustomDungeon.resumeMainMusic();
    }

    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
        NAME = monsterStrings.NAME;
        MOVES = monsterStrings.MOVES;
        DIALOG = monsterStrings.DIALOG;
    }
}