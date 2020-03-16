package paleoftheancients.reimu.monsters;

import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.unique.CannotLoseAction;
import com.megacrit.cardcrawl.actions.unique.RemoveDebuffsAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.powers.InvinciblePower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import paleoftheancients.PaleMod;
import paleoftheancients.dungeons.PaleOfTheAncients;
import paleoftheancients.reimu.actions.PersuasionNeedleAction;
import paleoftheancients.reimu.actions.WaitOnVFXAction;
import paleoftheancients.reimu.powers.HakureiShrineMaidenPower;
import paleoftheancients.reimu.powers.Position;
import paleoftheancients.reimu.util.ReimuUserInterface;
import paleoftheancients.reimu.vfx.*;
import paleoftheancients.relics.SoulOfTheShrineMaiden;

public class Reimu extends CustomMonster {
    public static final String ID = PaleMod.makeID("Reimu");
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;

    public static final String Deathbomb = "Deathbomb";

    private static final int HP = 250;

    public static final float orbOffset = 225.0F * Settings.scale;
    public YinYangOrb[][] orbs = new YinYangOrb[3][3];

    public ReimuUserInterface rui;
    public ReimuPhase phase;

    private boolean lockAnimation;
    private BarrierVFX barrier = null;
    private SpellCircleVFX spellcircle = null;

    public Reimu() {
        this(0.0f, 0.0f);
    }

    public Reimu(final float x, final float y) {
        super(Reimu.NAME, ID, HP, -5.0F, 0, 340.0f, 300.0f, null, x, y);

        if(AbstractDungeon.ascensionLevel >= 9) {
            setHp((int) (this.maxHealth * 1.2F));
        }

        this.flipHorizontal = true;
        this.loadAnimation(PaleMod.assetPath("images/reimu/monsters/Reimu/Reimu.atlas"), PaleMod.assetPath("images/reimu/monsters/Reimu/Reimu.json"), 0.6F);
        runAnim(ReimuAnimation.Intro);

        this.type = EnemyType.BOSS;
        this.dialogX = (this.hb_x - 70.0F) * Settings.scale;
        this.dialogY -= (this.hb_y - 55.0F) * Settings.scale;


        for(int i = 0; i < orbs.length; i++) {
            for (int j = 0; j < orbs[i].length; j++) {
                orbs[i][j] = null;
            }
        }

        this.phase = new ReimuOne();
        this.rui = new ReimuUserInterface(this);
        this.lockAnimation = false;

        this.setHp(phase.calcAscensionNumber(this.maxHealth));
    }

    @Override
    public void usePreBattleAction() {
        PaleOfTheAncients.playTempMusic(PaleMod.makeID("finaldream"));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new Position(AbstractDungeon.player, 1)));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new HakureiShrineMaidenPower(this)));
        AbstractDungeon.actionManager.addToBottom(new CannotLoseAction());
    }

    public int orbNum() {
        int counter = 0;
        for (int i = 0; i < orbs.length; i++) {
            for (int j = 0; j < orbs[i].length; j++) {
                if(orbs[i][j] != null) {
                    counter++;
                }
            }
        }
        return counter;
    }

    @Override
    public void takeTurn() {
        ReimuPhase.ReimuMoveInfo rmi = phase.moves.get(this.nextMove);
        DamageInfo info = new DamageInfo(this, rmi.baseDamage, DamageInfo.DamageType.NORMAL);
        if(info.base > -1) {
            info.applyPowers(this, AbstractDungeon.player);
        }
        if(rmi.animation != ReimuAnimation.None) {
            runAnim(rmi.animation);
        }

        if(this.halfDead) {
            //Arise, Shrine Maiden!
            this.halfDead = false;
            this.isDying = false;
            this.isDead = false;
            this.maxHealth *= 1.5F;
            AbstractDungeon.actionManager.addToBottom(new HealAction(this, this, this.maxHealth));
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this, this, StrengthPower.POWER_ID));
            phase.die(this);
            CardCrawlGame.sound.playV(PaleMod.makeID("touhou_powerup"), 0.25F);
            if(spellcircle == null) {
                spellcircle = new SpellCircleVFX(this);
                AbstractDungeon.effectList.add(spellcircle);
            }

            //Return to idle
            lockAnimation = false;
            runAnim(ReimuAnimation.DizzyEnd, ReimuAnimation.Spellcall, ReimuAnimation.Idle);

            //Decrement lives and reset bombs
            rui.extralives--;
            rui.bombs = 0;
            rui.addBomb(3);

            //Refresh the intent because every Death, one further column starts attacking
            int playerPosition = Position.playerPosition() - 1;
            for(int i = 1; i <= 2 - rui.extralives; i++) {
                if(orbs[i][playerPosition] != null) {
                    orbs[i][playerPosition].getMove(0);
                    orbs[i][playerPosition].createIntent();
                }
            }
        } else if(this.nextMove == ReimuPhase.BOMBREFILL) {
            rui.addBomb();

            for(final YinYangOrb[] orbarray : this.orbs) {
                for(final YinYangOrb orb : orbarray) {
                    if(orb != null) {
                        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(orb, this, 20));
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(orb, this, new StrengthPower(orb, 10), 10));
                    }
                }
            }
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, rui.extralives + 1), rui.extralives + 1));
        } else {
            phase.takeTurn(this, rmi, info);
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this, this, InvinciblePower.POWER_ID));
        }

        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(int num) {
        phase.getMove(this, num);
    }

    public void setMoveShortcut(byte next, int moveIndex) {
        EnemyMoveInfo info = this.phase.moves.get(next);
        this.setMove(MOVES[moveIndex], next, info.intent, info.baseDamage, info.multiplier, info.isMultiDamage);
    }
    public void setMoveShortcut(byte next) {
        EnemyMoveInfo info = this.phase.moves.get(next);
        this.setMove(next, info.intent, info.baseDamage, info.multiplier, info.isMultiDamage);
    }

    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
        NAME = Reimu.monsterStrings.NAME;
        MOVES = Reimu.monsterStrings.MOVES;
        DIALOG = Reimu.monsterStrings.DIALOG;
    }

    @Override
    public void changeState(String state) {
        switch(state) {
            case Deathbomb:
                AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(this, this, new InvinciblePower(this, -1), -1));
                phase.setDeathbombIntent(this);
                CardCrawlGame.sound.playV(PaleMod.makeID("touhou_death"), 0.15F);
                this.createIntent();

                startSpellAnimation();
                break;
        }
    }

    @Override
    public void damage(DamageInfo info) {
        int before = this.currentHealth;
        super.damage(info);
        if(this.currentHealth > 0 && before > 1) {
            if(before > this.currentHealth) {
                if(MathUtils.randomBoolean()) {
                    runAnim(ReimuAnimation.Hithigh);
                } else {
                    runAnim(ReimuAnimation.Hitlow);
                }
            } else {
                runAnim(ReimuAnimation.Guard);
            }
        }
    }

    @Override
    public void die(boolean triggerRelics) {
        this.halfDead = true;
        if(rui.extralives <= 0) {
            AbstractDungeon.getCurrRoom().cannotLose = false;
            for(final AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
                if(!mo.isDeadOrEscaped() && mo.id.equals(YinYangOrb.ID)) {
                    AbstractDungeon.actionManager.addToBottom(new SuicideAction(mo));
                }
            }
            spellcircle.end();
            lockAnimation = true;
            this.state.setAnimation(0, ReimuAnimation.Defeat.name(), false);
            PaleOfTheAncients.addRelicReward(SoulOfTheShrineMaiden.ID);
            AbstractDungeon.actionManager.addToBottom(new WaitOnVFXAction(new TouhouDeathVFX(this)));
            AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
                @Override
                public void update() {
                    PaleOfTheAncients.resumeMainMusic();
                    delayedDie(triggerRelics);
                    this.isDone = true;
                }
            });
        } else {
            PaleOfTheAncients.deathTriggers(this);
            lockAnimation = false;
            runAnim(ReimuAnimation.Guardbreak, ReimuAnimation.Dizzy);
            lockAnimation = true;
            this.setMove(MOVES[rui.extralives], (byte)0, Intent.BUFF);
            this.createIntent();
            CardCrawlGame.sound.playV(PaleMod.makeID("touhou_death"), 0.25F);
            if(spellcircle != null) {
                spellcircle.end();
                spellcircle = null;
            }
            AbstractDungeon.actionManager.addToBottom(new RemoveDebuffsAction(this));
        }
    }

    private void delayedDie(boolean triggerRelics) {
        this.halfDead = false;
        super.die(triggerRelics);
    }

    @Override
    public void update() {
        rui.update();
        super.update();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setColor(Color.WHITE);
        super.render(sb);
        rui.render(sb);
    }

    @Override
    public void dispose() {
        super.dispose();
        rui.dispose();
        SpellCardDeclarationVFX.disposeAll();
        BarrierVFX.disposeAll();
        SpellCardBackgroundVFX.disposeAll();
        FSBlinkVFX.disposeAll();
        FantasySealVFX.disposeAll();
        HakureiBarrierVFX.disposeAll();
        PersuasionNeedleAction.disposeAll();
        ExterminationVFX.disposeAll();
        HakureiAmuletVFX.disposeAll();
    }

    public void runAnim(ReimuAnimation animation) {
        runAnim(animation, ReimuAnimation.Idle);
    }
    public void runAnim(ReimuAnimation... animation) {
        if(!lockAnimation) {
            this.state.setAnimation(0, animation[0].name(), false);
            if (animation[1] != ReimuAnimation.None) {
                for(int i = 1; i < animation.length; i++) {
                    this.state.addAnimation(0, animation[i].name(), i == animation.length - 1, 0.0F);
                }
            }
        }
    }

    public void endSpellAnimation() {
        if(barrier != null) {
            barrier.end();
            barrier = null;
        }
        lockAnimation = false;
        runAnim(ReimuAnimation.SpellA_End);
    }
    public void startSpellAnimation() {
        this.startSpellAnimation(true);
    }
    public void startSpellAnimation(boolean withBarrier) {
        if(withBarrier) {
            barrier = new BarrierVFX(this);
            AbstractDungeon.effectList.add(barrier);
        }
        runAnim(ReimuAnimation.SpellA, ReimuAnimation.SpellA_Loop);
        lockAnimation = true;
    }

    @Override
    public boolean lastMove(byte move) {
        return super.lastMove(move);
    }
    @Override
    public boolean lastMoveBefore(byte move) {
        return super.lastMoveBefore(move);
    }

    public enum ReimuAnimation {
        None,
        Intro,
        Idle,
        Defeat,
        Spellcall,
        CloseAttack,
        Guard,
        Guardbreak,
        Hithigh,
        Hitlow,
        SpellA,
        SpellA_Loop,
        SpellA_End,
        Flipkick,
        Kick,
        Dizzy,
        DizzyEnd,
        MagicUp,
        MagicForward,
        ForwardOccult,
        DashAttack
    }
}