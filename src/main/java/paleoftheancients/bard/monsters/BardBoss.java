package paleoftheancients.bard.monsters;

import actlikeit.dungeons.CustomDungeon;
import basemod.ReflectionHacks;
import basemod.abstracts.CustomMonster;
import com.esotericsoftware.spine.AnimationState;
import com.evacipated.cardcrawl.mod.bard.characters.Bard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.unique.VampireDamageAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.vfx.combat.CleaveEffect;
import paleoftheancients.PaleMod;
import paleoftheancients.bard.actions.QueueNoteAction;
import paleoftheancients.bard.actions.RhapsodyAction;
import paleoftheancients.bard.helpers.CardNoteAllocator;
import paleoftheancients.bard.helpers.CardNoteRenderer;
import paleoftheancients.bard.helpers.MelodyManager;
import paleoftheancients.bard.intents.BardIntentEnum;
import paleoftheancients.bard.melodies.AbstractMelody;
import paleoftheancients.bard.notes.*;
import paleoftheancients.bard.powers.*;
import paleoftheancients.bard.vfx.LifeDrainEffect;
import paleoftheancients.relics.SoulOfTheBard;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BardBoss extends CustomMonster {
    public static final String ID = PaleMod.makeID("BardBoss");
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;

    public NoteQueue notequeue;

    private Map<Byte, EnemyMoveInfo> moves;
    private static final byte SONATA = 0;
    public static final byte RHAPSODY = 1;
    private static final byte SILENCE = 2;
    private static final byte DERVISHDANCE = 3; //Deal N damage + M damage for each attacknote
    private static final byte FLOURISH = 4; //Choose a note to queue and deal damage
    private static final byte LIFEDRAIN = 5; //Deal N damage and heal for half the unblocked damage dealt
    private static final byte ENCORE = 6; //Next melody performed twice

    private int turnCounter;

    public BardBoss() {
        super(NAME, ID, 650, 0.0F, -20.0F, 390.0F, 350.0F, (String)null, -225.0F, 20.0F);

        this.loadAnimation(PaleMod.assetPath("images/bard/character/skeleton.atlas"), PaleMod.assetPath("images/bard/character/skeleton.json"), 1.6F);

        AnimationState.TrackEntry e = this.state.setAnimation(0, "Idle", true);
        this.stateData.setMix("Hit", "Idle", 0.1F);
        e.setTimeScale(1.0F);

        int ascensionbonus = 0;
        if(AbstractDungeon.ascensionLevel >= 4) {
            ascensionbonus += 33;
        }
        if(AbstractDungeon.ascensionLevel >= 9) {
            ascensionbonus += 33;
        }
        if(AbstractDungeon.ascensionLevel >= 19) {
            ascensionbonus += 33;
        }

        this.moves = new HashMap<>();
        this.moves.put(SONATA, new EnemyMoveInfo(SONATA, Intent.BUFF, -1, 0, false));
        this.moves.put(RHAPSODY, new EnemyMoveInfo(RHAPSODY, Intent.MAGIC, -1, 0, false));
        this.moves.put(SILENCE, new EnemyMoveInfo(SILENCE, Intent.STRONG_DEBUFF, -1, 0, false));
        this.moves.put(DERVISHDANCE, new EnemyMoveInfo(DERVISHDANCE, BardIntentEnum.ATTACK_DERVISH_DANCE, 12 + ascensionbonus / 25, 1, true));
        this.moves.put(FLOURISH, new EnemyMoveInfo(FLOURISH, BardIntentEnum.ATTACK_FLOURISH, 18 + ascensionbonus / 7, 0, false));
        this.moves.put(LIFEDRAIN, new EnemyMoveInfo(LIFEDRAIN, Intent.ATTACK_BUFF, 28 + ascensionbonus / 7, 0, false));
        this.moves.put(ENCORE, new EnemyMoveInfo(ENCORE, Intent.BUFF, -1, 0, false));



        this.flipHorizontal = true;
        this.turnCounter = 0;
    }

    @Override
    public void usePreBattleAction() {
        MelodyManager.initializeNotes();
        MelodyManager.initializeMelodies();

        AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
            @Override
            public void update() {
                this.isDone = true;
                CardNoteAllocator.initialize();
            }
        });
        notequeue = new NoteQueue(this);
        AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(notequeue, false));
        if(!(PaleMod.bardLoaded() && AbstractDungeon.player instanceof Bard)) {
            AbstractDungeon.topLevelEffectsQueue.add(new CardNoteRenderer());
        }
        this.powers.add(new QueueNotePower(this, 10));

        for(final AbstractMelody m : MelodyManager.getAllMelodies()) {
            m.applyPowers();
        }
    }


    @Override
    public void takeTurn() {
        DamageInfo info = new DamageInfo(this, moves.get(this.nextMove).baseDamage, DamageInfo.DamageType.NORMAL);
        if(info.base > -1) {
            info.applyPowers(this, AbstractDungeon.player);
        }
        int multiplier;

        switch(this.nextMove) {
            case SONATA:
                this.powers.add(new DelaySonataPower(this));
                break;

            case SILENCE:
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new SilencedPower(AbstractDungeon.player, 2), 2));
                break;

            case DERVISHDANCE:
                int count = (int) ReflectionHacks.getPrivate(this, AbstractMonster.class, "intentMultiAmt");
                for (int i = 0; i < count; ++i) {
                    if (i == 0) {
                        AbstractDungeon.actionManager.addToBottom(new SFXAction("ATTACK_WHIRLWIND"));
                    }
                    AbstractDungeon.actionManager.addToBottom(new SFXAction("ATTACK_HEAVY"));
                    AbstractDungeon.actionManager.addToBottom(new VFXAction(new CleaveEffect(), 0));
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.NONE, true));
                }
                break;

            case FLOURISH: {
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                int lowest = notequeue.getMaxNotes() + 1;
                AbstractNote lowestnote = BuffNote.get();
                Class[] classes = new Class[]{AttackNote.class, BlockNote.class, DebuffNote.class, BuffNote.class};
                for (final Class c : classes) {
                    int notecount = notequeue.count(c);
                    if (notecount < lowest) {
                        try {
                            Method m = c.getMethod("get");
                            lowestnote = (AbstractNote)m.invoke(null);
                            lowest = notecount;
                        } catch (NoSuchMethodException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                }
                AbstractDungeon.actionManager.addToBottom(new QueueNoteAction(notequeue, lowestnote));
                break;
            }

            case LIFEDRAIN:
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new LifeDrainEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, this.hb.cX, this.hb.cY), LifeDrainEffect.DURATION));
                AbstractDungeon.actionManager.addToBottom(new VampireDamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.NONE));
                break;

            case ENCORE:
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new EncorePower(this, 1), 1));
                break;

            case RHAPSODY:
                AbstractDungeon.actionManager.addToBottom(new RhapsodyAction(this, this.notequeue));
                break;
        }

        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
        turnCounter++;
    }

    @Override
    public void getMove(int num) {
        if((this.currentHealth < this.maxHealth / 2 || this.turnCounter >= 16) && !this.hasPower(SonataPower.POWER_ID)) {
            this.setMoveShortcut(SONATA);
        } else {
            if(turnCounter % 5 == 4) {
                this.setMoveShortcut(SILENCE);
            } else {
                ArrayList<Byte> possibilities = new ArrayList<>();
                possibilities.add(RHAPSODY);
                possibilities.add(DERVISHDANCE);
                possibilities.add(DERVISHDANCE);
                possibilities.add(FLOURISH);
                possibilities.add(FLOURISH);
                possibilities.add(LIFEDRAIN);
                possibilities.add(LIFEDRAIN);
                possibilities.add(ENCORE);
                for(int i = this.moveHistory.size() - 1, found = 0; i >= 0 && found < 2; i--) {
                    boolean foundThisCycle = false;
                    int before;
                    do {
                        before = possibilities.size();
                        possibilities.remove(this.moveHistory.get(i));
                        if(!foundThisCycle && possibilities.size() != before) {
                            found++;
                            foundThisCycle = true;
                        }
                    } while(before != possibilities.size());
                }
                this.setMoveShortcut(possibilities.get(AbstractDungeon.monsterRng.random(possibilities.size() - 1)));
                //this.createIntent();
            }
        }
    }

    @Override
    public void die(boolean triggerRelics) {
        CustomDungeon.addRelicReward(SoulOfTheBard.ID);
        AbstractDungeon.actionManager.addToTop(new SuicideAction(notequeue));
        super.die(triggerRelics);
    }

    private void setMoveShortcut(byte next) {
        EnemyMoveInfo info = this.moves.get(next);
        this.setMove(MOVES[next], next, info.intent, info.baseDamage, info.multiplier, info.isMultiDamage);
    }

    @Override
    public void damage(DamageInfo info) {
        if (info.owner != null && info.type != DamageInfo.DamageType.THORNS && info.output > this.currentBlock) {
            AnimationState.TrackEntry e = this.state.setAnimation(0, "Hit", false);
            this.state.addAnimation(0, "Idle", true, 0.0F);
            e.setTimeScale(1.0F);
        }

        super.damage(info);
    }

    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
        NAME = monsterStrings.NAME;
        MOVES = monsterStrings.MOVES;
        DIALOG = monsterStrings.DIALOG;
    }

    public void calcDervishDance() {
        if(this.nextMove == DERVISHDANCE) {
            this.setDervishDance();
            this.createIntent();
        }
    }
    private void setDervishDance() {
        EnemyMoveInfo info = moves.get(DERVISHDANCE);
        int count = notequeue.count(AttackNote.class);
        this.setMove(MOVES[DERVISHDANCE], info.nextMove, info.intent, info.baseDamage, count + 1, true);
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
