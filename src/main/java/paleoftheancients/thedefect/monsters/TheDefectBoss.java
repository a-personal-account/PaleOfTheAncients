package paleoftheancients.thedefect.monsters;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState.TrackEntry;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.defect.ThunderStrikeAction;
import com.megacrit.cardcrawl.actions.utility.HideHealthBarAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.cards.blue.*;
import com.megacrit.cardcrawl.characters.Defect;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.relics.DataDisk;
import com.megacrit.cardcrawl.relics.Inserter;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.combat.WeightyImpactEffect;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import paleoftheancients.PaleMod;
import paleoftheancients.dungeons.PaleOfTheAncients;
import paleoftheancients.relics.SoulOfTheDefect;
import paleoftheancients.thedefect.actions.*;
import paleoftheancients.thedefect.monsters.orbs.*;
import paleoftheancients.thedefect.powers.KineticBarrierPower;
import paleoftheancients.thedefect.powers.NotEchoPower;
import paleoftheancients.thedefect.powers.OrbReactivePower;
import paleoftheancients.thedefect.vfx.RainbowCopyPaste;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class TheDefectBoss extends AbstractMonster {

    private static final byte RAINBOW_CONST = 0;
    private static final byte BALLLIGHTNING_CONST = 1;
    private static final byte CONSUME_CONST = 2;
    private static final byte DARKNESS_CONST = 3;
    private static final byte BULLSEYE_CONST = 4;

    private static final byte SUNDER_CONST = 5;

    private static final byte TEMPEST_CONST = 6;
    private static final byte CORESURGE_CONST = 7;
    private static final byte MULTICAST_CONST = 8;
    private static final byte THUNDERSTRIKE_CONST = 9;
    private static final byte ECHOFORM_CONST = 10;

    private static final byte BARRAGE_CONST = 11;
    private static final byte FISSION_CONST = 12;
    private static final byte METEORSTRIKE_CONST = 13;
    private static final byte CHAOS_CONST = 14;

    private static final int INSERTER_INTERVAL = 2;
    public static final int ENERGY_PER_STRENGTH = 2;


    private static final Logger logger = LogManager.getLogger(TheDefectBoss.class.getName());
    public static final String ID = PaleMod.makeID("TheDefect");
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;
    public static final int HP = 1000;
    public static final int A_2_HP = 1300;

    private static final int BALLLIGHTNING = 12;
    private static final int A_4_BALLLIGHTNING = 14;
    private static final int A_19_BALLLIGHTNING = 17;
    private int ball_lightning;

    private static final int BULLSEYE_DAMAGE = 12;
    private static final int A_4_BULLSEYE_DAMAGE = 14;
    private static final int A_19_BULLSEYE_DAMAGE = 17;
    private int bullseye_damage;

    private static final int BULLSEYE_DEBUFF = 2;
    private static final int A_9_BULLSEYE_DEBUFF = 3;
    private int bullseye_debuff;

    private static final int SUNDER = 24;
    private static final int A_4_SUNDER = 28;
    private static final int A_19_SUNDER = 32;
    private int sunder;


    private static final int TEMPEST = 3;
    private static final int A_9_TEMPEST = 4;
    private int base_tempest;


    private static final int CORESURGE = 16;
    private static final int A_4_CORESURGE = 18;
    private static final int A_19_CORESURGE = 20;
    private int core_surge;

    private static final int CORESURGEBUFF = 1;
    private static final int A_9_CORESURGEBUFF = 2;
    private int core_surge_buff;

    private static final int THUNDERSTRIKE = 2;
    private static final int A_4_THUNDERSTRIKE = 3;
    private static final int A_19_THUNDERSTRIKE = 5;
    private int thunderstrike;

    private static final int BARRAGE = 2;
    private static final int A_4_BARRAGE = 3;
    private static final int A_19_BARRAGE = 5;
    private int barrage;

    private static final int CONSUME = 2;
    private static final int A_9_CONSUME = 3;
    private int consume;

    private static final int METEORSTRIKE = 20;
    private static final int A_4_METEORSTRIKE = 23;
    private static final int A_19_METEORSTRIKE = 25;
    private int meteorstrike;


    public int maxOrbs;
    private static final int MAXORBSLOTS = 10;
    public int masterMaxOrbs;
    public ArrayList<AbstractBossOrb> orbs;

    private int threshold = 0;
    private ArrayList<Byte> cycletracker = new ArrayList<>();
    private Random random;
    private Map<Byte, Integer> damagevalues = new HashMap<>();
    private Map<Byte, Intent> intents = new HashMap<>();


    private int turncounter;
    public int lightningChanneled;
    private int fissiond;
    private boolean firstturn;
    private boolean spokenThisTurn;
    private byte lastExecuted;

    public TheDefectBoss() {
        super(NAME, ID, 400, 0.0F, -15.0F, 300.0F, 230.0F, (String)null, 0.0F, 0.0F);
        this.dialogX = -30.0F * Settings.scale;
        this.dialogY = 30.0F * Settings.scale;
        this.type = EnemyType.BOSS;
        this.maxOrbs = 0;
        this.masterMaxOrbs = MAXORBSLOTS;
        this.orbs = new ArrayList<>();
        turncounter = 0;
        lightningChanneled = 0;
        fissiond = 0;
        firstturn = true;
        if (AbstractDungeon.ascensionLevel >= 9) {
            this.setHp(A_2_HP);
            this.bullseye_debuff = A_9_BULLSEYE_DEBUFF;
            this.base_tempest = A_9_TEMPEST;
            this.core_surge_buff = A_9_CORESURGEBUFF;
            this.consume = A_9_CONSUME;
        } else {
            this.setHp(HP);
            this.bullseye_debuff = BULLSEYE_DEBUFF;
            this.base_tempest = TEMPEST;
            this.core_surge_buff = CORESURGEBUFF;
            this.consume = CONSUME;
        }

        if (AbstractDungeon.ascensionLevel >= 19) {
            this.ball_lightning = A_19_BALLLIGHTNING;
            this.sunder = A_19_SUNDER;
            this.core_surge = A_19_CORESURGE;
            this.thunderstrike = A_19_THUNDERSTRIKE;
            this.barrage = A_19_BARRAGE;
            this.meteorstrike = A_19_METEORSTRIKE;
        } else if (AbstractDungeon.ascensionLevel >= 4) {
            this.ball_lightning = A_4_BALLLIGHTNING;
            this.sunder = A_4_SUNDER;
            this.core_surge = A_4_CORESURGE;
            this.thunderstrike = A_4_THUNDERSTRIKE;
            this.barrage = A_4_BARRAGE;
            this.meteorstrike = A_4_METEORSTRIKE;
        } else {
            this.ball_lightning = BALLLIGHTNING;
            this.sunder = SUNDER;
            this.core_surge = CORESURGE;
            this.thunderstrike = THUNDERSTRIKE;
            this.barrage = BARRAGE;
            this.meteorstrike = METEORSTRIKE;
        }

        this.intents.put(RAINBOW_CONST, Intent.UNKNOWN);
        this.intents.put(BALLLIGHTNING_CONST, Intent.ATTACK_BUFF);
        this.damagevalues.put(BALLLIGHTNING_CONST, this.ball_lightning);

        this.intents.put(CONSUME_CONST, Intent.BUFF);

        this.intents.put(DARKNESS_CONST, Intent.BUFF);

        this.intents.put(BULLSEYE_CONST, Intent.ATTACK_DEBUFF);
        this.damagevalues.put(BULLSEYE_CONST, this.bullseye_damage);

        this.intents.put(SUNDER_CONST, Intent.ATTACK);
        this.damagevalues.put(SUNDER_CONST, this.sunder);

        this.intents.put(TEMPEST_CONST, Intent.UNKNOWN);

        this.intents.put(CORESURGE_CONST, Intent.ATTACK_BUFF);
        this.damagevalues.put(CORESURGE_CONST, this.core_surge);

        this.intents.put(MULTICAST_CONST, Intent.MAGIC);

        this.intents.put(ECHOFORM_CONST, Intent.BUFF);

        this.intents.put(THUNDERSTRIKE_CONST, Intent.ATTACK);
        this.damagevalues.put(THUNDERSTRIKE_CONST, this.thunderstrike);

        this.intents.put(BARRAGE_CONST, Intent.ATTACK);
        this.damagevalues.put(BARRAGE_CONST, this.barrage);

        this.intents.put(FISSION_CONST, Intent.UNKNOWN);

        this.intents.put(METEORSTRIKE_CONST, Intent.ATTACK_BUFF);
        this.damagevalues.put(METEORSTRIKE_CONST, this.meteorstrike);

        this.intents.put(CHAOS_CONST, Intent.UNKNOWN);

        this.loadAnimation("images/characters/defect/idle/skeleton.atlas", "images/characters/defect/idle/skeleton.json", 1.0F);
        TrackEntry e = this.state.setAnimation(0, "Idle", true);
        e.setTime(e.getEndTime() * MathUtils.random());

        this.flipHorizontal = true;
    }

    public void usePreBattleAction() {
        CardCrawlGame.music.unsilenceBGM();
        AbstractDungeon.scene.fadeOutAmbiance();
        PaleOfTheAncients.playTempMusic(PaleMod.makeID("phoenix"));

        UnlockTracker.markBossAsSeen(NAME);

        int STARTINGORBSLOTS = 3;
        if(AbstractDungeon.ascensionLevel >= 9) {
            STARTINGORBSLOTS++;
        }
        if(AbstractDungeon.ascensionLevel >= 19) {
            STARTINGORBSLOTS++;
        }
        this.increaseMaxOrbSlots(STARTINGORBSLOTS, false);

        if(AbstractDungeon.ascensionLevel >= 4) {
            AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(this, new DataDisk()));
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new FocusPower(this, 1), 1));
        }

        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new OrbReactivePower(this)));
        AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(this, this, new KineticBarrierPower(this, STARTINGORBSLOTS - 1), STARTINGORBSLOTS - 1));

    }

    @Override
    public void update() {
        super.update();
        for(final AbstractBossOrb abo : orbs) {
            if(abo.id == EmptyOrbSlot.ID) {
                abo.updateAnimations();
                abo.update();
            }
        }
    }

    public void takeTurn() {
        takeTurn(0);
    }
    public void takeTurn(int echoed) {
        DamageInfo tmp = null;
        if(this.damagevalues.containsKey(this.nextMove)) {
            tmp = new DamageInfo(this, this.damagevalues.get(this.nextMove));
            tmp.applyPowers(this, AbstractDungeon.player);

            this.useFastAttackAnimation();
        }

        int energy = this.base_tempest;
        {
            AbstractPower ap = this.getPower(StrengthPower.POWER_ID);
            if (ap != null && ap.amount > 0) {
                //Strength comes from plasma orbs, so it is an indicator for how much energy defect should have.
                energy += ap.amount / ENERGY_PER_STRENGTH;
            }
        }
        //energy from fission last turn
        energy += fissiond;
        fissiond = 0;

        switch(this.nextMove) {
            case RAINBOW_CONST:
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new RainbowCopyPaste(this)));
                this.addOrb(new Lightning(this));
                this.addOrb(new Frost(this));
                this.addOrb(new Dark(this));
                firstturn = false;
                break;

            case BALLLIGHTNING_CONST:
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, tmp, AttackEffect.SLASH_HEAVY));
                this.addOrb(new Lightning(this));
                break;

            case CONSUME_CONST:
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new FocusPower(this, this.consume), this.consume));
                this.decreaseMaxOrbSlots(1);
                break;

            case DARKNESS_CONST:
                this.addOrb(new Dark(this));
                AbstractDungeon.actionManager.addToBottom(new DarknessPlusAction(this.orbs));
                break;

            case BULLSEYE_CONST:
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, tmp, AttackEffect.BLUNT_LIGHT));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new LockOnPower(AbstractDungeon.player, this.bullseye_debuff), this.bullseye_debuff));
                break;

            case SUNDER_CONST:
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new WeightyImpactEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY)));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, tmp, AttackEffect.NONE));
                break;

            case TEMPEST_CONST:
                for(int i = 0; i < energy; i++) {
                    this.addOrb(new Lightning(this));
                }
                break;

            case CORESURGE_CONST:
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, tmp, AttackEffect.BLUNT_HEAVY));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new ArtifactPower(this, this.core_surge_buff), this.core_surge_buff));
                break;

            case MULTICAST_CONST:
                AbstractDungeon.actionManager.addToBottom(new MultiCastAction(this, energy));
                break;

            case ECHOFORM_CONST:
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new NotEchoPower(this, 1), 1));
                turncounter = turncounter % INSERTER_INTERVAL;
                break;

            case THUNDERSTRIKE_CONST:
                AbstractDungeon.actionManager.addToBottom(new ThunderStrikeAction(AbstractDungeon.player, new DamageInfo(this, this.thunderstrike), this.lightningChanneled));
                break;

            case BARRAGE_CONST:
                for(int i = 0; i < this.orbs.size(); i++) {
                    if(this.orbs.get(i) instanceof EmptyOrbSlot) {
                        break;
                    }
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, tmp, AttackEffect.BLUNT_LIGHT, true));
                }
                break;

            case FISSION_CONST:
                AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
                    @Override
                    public void update() {
                        for(int i = 0; i < orbs.size(); i++) {
                            if(orbs.get(i).id == EmptyOrbSlot.ID) {
                                break;
                            }
                            AbstractDungeon.actionManager.addToBottom(new SuicideAction(orbs.get(i)));
                            fissiond++;
                        }
                        this.isDone = true;
                    }
                });
                break;

            case METEORSTRIKE_CONST:
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new WeightyImpactEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY)));
                AbstractDungeon.actionManager.addToBottom(new WaitAction(0.8F));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, tmp, AttackEffect.NONE));
                this.addOrb(new Plasma(this));
                this.addOrb(new Plasma(this));
                this.addOrb(new Plasma(this));

                break;

            case CHAOS_CONST:
                for(int i = 0; i < 3; i++) {
                    this.addOrb(AbstractBossOrb.getRandomOrb(this, true));
                }
                break;


            default:
                logger.info("ERROR: Default Take Turn was called on " + this.name);
        }

        if(!this.hasPower(NotEchoPower.POWER_ID) || this.getPower(NotEchoPower.POWER_ID).amount == echoed) {
            turncounter++;

            //Add a frost orb, because Frozen Core
            AbstractDungeon.actionManager.addToBottom(new DefectAddOrbAction(new Frost(this), false));

            //Add an orb slot, because Inserter
            if (turncounter % INSERTER_INTERVAL == 0) {
                AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(this, RelicLibrary.getRelic(Inserter.ID).makeCopy()));
                this.increaseMaxOrbSlots(1, true);
            }
            AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));

            spokenThisTurn = false;
            this.lastExecuted = this.nextMove;
        } else {
            //Do it again for each stack of echo form, because that's balanced~
            AbstractDungeon.actionManager.addToBottom(new WaitAction(1F));
            this.takeTurn(echoed + 1);
        }

    }

    protected void getMove(int num) {
        if(!this.moveHistory.isEmpty()) {
            switch (this.nextMove) {
                case BARRAGE_CONST:
                    if(this.nextMove != this.lastExecuted) {
                        this.setMove(MOVES[BARRAGE_CONST], BARRAGE_CONST, intents.get(BARRAGE_CONST), this.damagevalues.get(BARRAGE_CONST), this.orbcount(), true);
                        this.createIntent();
                        return;
                    }
                    break;

                case FISSION_CONST:
                case TEMPEST_CONST:
                case ECHOFORM_CONST:
                case CONSUME_CONST:
                    if(this.nextMove != this.lastExecuted) {
                        return;
                    }
                    break;
            }
        }



        byte move;
        int dialogue = -1;
        int activeOrbs = 0;

        int dark = 0;
        int lightning = 0;
        int plasma = 0;
        if(this.hasPower(StrengthPower.POWER_ID)) {
            plasma = this.getPower(StrengthPower.POWER_ID).amount / ENERGY_PER_STRENGTH;
        }

        for(final AbstractBossOrb abo : this.orbs) {
            if(!(abo instanceof EmptyOrbSlot)) {
                activeOrbs++;

                if(abo instanceof Lightning) {
                    lightning++;
                } else if(abo instanceof Dark) {
                    dark++;
                } else if(abo instanceof Plasma) {
                    plasma++;
                }
            }
        }

        if(firstturn) {
            move = RAINBOW_CONST;
        } else if((this.currentHealth <= this.maxHealth / 3 && !this.hasPower(NotEchoPower.POWER_ID)) || this.turncounter % 16 == 0) {
            move = ECHOFORM_CONST;
            if(this.currentHealth <= this.maxHealth / 3) {
                dialogue = 1;
            } else {
                dialogue = 2;
            }
        } else if(this.fissiond > 0) {
            move = TEMPEST_CONST;
            dialogue = 0;
        } else if(turncounter % 4 == 0) {
            move = CONSUME_CONST;
            dialogue = 3;
        } else {
            ArrayList<Byte> possibilities = new ArrayList<>();
            if(activeOrbs == 0) {
                possibilities.add(RAINBOW_CONST);
                possibilities.add(RAINBOW_CONST);
            }
            if(activeOrbs <= 1) {
                possibilities.add(RAINBOW_CONST);
            }
            if(activeOrbs < this.maxOrbs - 5 && !this.lastMove(RAINBOW_CONST)) {
                possibilities.add(RAINBOW_CONST);
            }
            if(activeOrbs > 2 && activeOrbs < this.maxOrbs) {
                if(!this.lastMove(BALLLIGHTNING_CONST)) {
                    possibilities.add(BALLLIGHTNING_CONST);
                }
                possibilities.add(DARKNESS_CONST);
            }
            for(int i = 0; i < dark; i++) {
                possibilities.add(DARKNESS_CONST);
            }
            if(activeOrbs > 6) {
                possibilities.add(FISSION_CONST);
                possibilities.add(FISSION_CONST);
                possibilities.add(BARRAGE_CONST);
                possibilities.add(BARRAGE_CONST);
                possibilities.add(BARRAGE_CONST);
            }
            if(plasma > 0) {
                possibilities.add(CORESURGE_CONST);
                possibilities.add(CORESURGE_CONST);
                if(activeOrbs > 0) {
                    possibilities.add(MULTICAST_CONST);
                    possibilities.add(MULTICAST_CONST);
                }
            }
            if(!this.hasPower(ArtifactPower.POWER_ID)) {
                possibilities.add(CORESURGE_CONST);
            }
            if(this.turncounter > 8) {
                possibilities.add(SUNDER_CONST);
            }
            if(this.lightningChanneled > 4) {
                possibilities.add(THUNDERSTRIKE_CONST);
            }
            if(this.turncounter > 4 && activeOrbs < 5) {
                possibilities.add(METEORSTRIKE_CONST);
            }
            if(lightning + dark > 4) {
                possibilities.add(BULLSEYE_CONST);
                possibilities.add(BULLSEYE_CONST);
            }

            //Remove duplicates except when it's rainbow, cause orb refill is important.
            if(!this.lastMove(RAINBOW_CONST)) {
                Byte lastMove = this.moveHistory.get(this.moveHistory.size() - 1);
                for(int i = possibilities.size() - 1; i > 0; i--) {
                    if(possibilities.get(i).equals(lastMove)) {
                        possibilities.remove(i);
                    }
                }
            }
            if(!possibilities.isEmpty()) {
                move = possibilities.get(AbstractDungeon.monsterRng.random(possibilities.size() - 1));
            } else {
                move = CHAOS_CONST;
            }

            switch(move) {
                case BARRAGE_CONST:
                    dialogue = 5;
                    break;
                case FISSION_CONST:
                    dialogue = 4;
                    break;
            }
        }

        if(this.damagevalues.containsKey(move)) {
            if(move == BARRAGE_CONST) {
                this.setMove(MOVES[move], move, intents.get(move), this.damagevalues.get(move), this.orbcount(), true);
            } else if(move == THUNDERSTRIKE_CONST) {
                this.setMove(MOVES[move], move, intents.get(move), this.damagevalues.get(move), this.lightningChanneled, true);
            } else {
                this.setMove(MOVES[move], move, intents.get(move), this.damagevalues.get(move));
            }
        } else {
            this.setMove(MOVES[move], move, intents.get(move));
        }
        this.createIntent();

        if(!spokenThisTurn) {
            spokenThisTurn = true;
            if(dialogue >= 0) {
                AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[dialogue], 4, 4));
                AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this, this, OrbReactivePower.POWER_ID));
            } else if(!this.hasPower(OrbReactivePower.POWER_ID)) {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new OrbReactivePower(this)));
            }
        }
    }

    private int orbcount() {
        int barrage_amnt;
        for(barrage_amnt = 0; barrage_amnt < this.orbs.size(); barrage_amnt++) {
            if(this.orbs.get(barrage_amnt) instanceof EmptyOrbSlot) {
                break;
            }
        }
        return Math.max(0, barrage_amnt);
    }

    public void damage(DamageInfo info) {
        super.damage(info);
        if (info.owner != null && info.type != DamageType.THORNS && info.output > 0) {
            TrackEntry e = this.state.setAnimation(0, "Hit", false);
            this.state.addAnimation(0, "Idle", true, 0.0F);
            e.setTimeScale(0.9F);
        }
    }


    public void increaseMaxOrbSlots(int amount, boolean playSfx) {
        if (this.maxOrbs == this.masterMaxOrbs) {

        } else {
            if (playSfx) {
                CardCrawlGame.sound.play("ORB_SLOT_GAIN", 0.1F);
            }

            if(amount + this.maxOrbs > this.masterMaxOrbs) {
                amount = this.masterMaxOrbs - this.maxOrbs;
            }

            for(int i = 0; i < amount; ++i) {
                AbstractDungeon.actionManager.addToBottom(new DefectAddOrbSlotAction(this));
            }

        }
    }

    public void decreaseMaxOrbSlots(int amount) {
        if (this.maxOrbs > 0) {
            if (this.maxOrbs - amount < 0) {
                amount = this.maxOrbs;
            }

            for(int i = 0; i < amount; i++) {
                AbstractDungeon.actionManager.addToBottom(new DefectRemoveOrbSlotAction(this));
            }

        }
    }

    public void addOrb(AbstractBossOrb orb) {
        AbstractDungeon.actionManager.addToBottom(new DefectAddOrbAction(orb));
    }


    public void die(boolean triggerRelics) {
        PaleOfTheAncients.addRelicReward(SoulOfTheDefect.ID);
        PaleOfTheAncients.resumeMainMusic();
        AbstractDungeon.actionManager.addToTop(new TalkAction(this, DIALOG[6]));
        this.useFastShakeAnimation(5.0F);
        CardCrawlGame.screenShake.rumble(4.0F);
        ++this.deathTimer;

        Iterator var1 = AbstractDungeon.getCurrRoom().monsters.monsters.iterator();

        while(var1.hasNext()) {
            AbstractMonster m = (AbstractMonster)var1.next();
            if(m instanceof AbstractBossOrb) {
                ((AbstractBossOrb) m).evoked = true;
            }
            if (!m.isDead && !m.isDying && m != this) {
                AbstractDungeon.actionManager.addToTop(new HideHealthBarAction(m));
                AbstractDungeon.actionManager.addToTop(new SuicideAction(m));
            }
        }
        super.die(triggerRelics);
        this.onBossVictoryLogic();
    }


    @Override
    public void render(SpriteBatch sb) {
        if (!this.orbs.isEmpty()) {
            for(final AbstractBossOrb abo : this.orbs) {
                if(abo.id == EmptyOrbSlot.ID) {
                    abo.render(sb);
                }
            }
        }

        sb.setColor(Color.WHITE);
        super.render(sb);
    }

    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
        NAME = Defect.NAMES[0];
        MOVES = new String[]{
                PaleMod.getCardName(Rainbow.class),
                PaleMod.getCardName(BallLightning.class),
                PaleMod.getCardName(Consume.class),
                PaleMod.getCardName(Darkness.class) + '+',
                PaleMod.getCardName(LockOn.class),
                PaleMod.getCardName(Sunder.class),
                PaleMod.getCardName(Tempest.class),
                PaleMod.getCardName(CoreSurge.class),
                PaleMod.getCardName(MultiCast.class),
                PaleMod.getCardName(ThunderStrike.class),
                PaleMod.getCardName(EchoForm.class),
                PaleMod.getCardName(Barrage.class),
                PaleMod.getCardName(Fission.class) + '+',
                PaleMod.getCardName(MeteorStrike.class),
                PaleMod.getCardName(Chaos.class)
        };
        DIALOG = monsterStrings.DIALOG;
    }
}




