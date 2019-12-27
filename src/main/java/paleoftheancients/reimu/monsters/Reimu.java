package paleoftheancients.reimu.monsters;

import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.unique.CannotLoseAction;
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
import paleoftheancients.reimu.powers.HakureiShrineMaidenPower;
import paleoftheancients.reimu.powers.Position;
import paleoftheancients.reimu.util.ReimuUserInterface;
import paleoftheancients.reimu.vfx.*;

import java.util.ArrayList;

public class Reimu extends CustomMonster {
    public static final String ID = PaleMod.makeID("Reimu");
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;

    public static final String Deathbomb = "Deathbomb";

    private static final int HP = 6;//250;

    public static final float orbOffset = 225.0F * Settings.scale;
    public ArrayList[][] orbs = new ArrayList[3][3];

    public ReimuUserInterface rui;
    public int turnsSinceBomb;
    public ReimuPhase phase;

    public boolean deathbombing = false;
    private boolean lockAnimation;
    private BarrierVFX barrier = null;
    private SpellCircleVFX spellcircle = null;

    public Reimu() {
        this(0.0f, 0.0f);
    }

    public Reimu(final float x, final float y) {
        super(Reimu.NAME, ID, HP, -5.0F, 0, 340.0f, 300.0f, null, x, y);

        this.flipHorizontal = true;
        this.loadAnimation(PaleMod.assetPath("images/reimu/monsters/Reimu/Reimu.atlas"), PaleMod.assetPath("images/reimu/monsters/Reimu/Reimu.json"), 0.6F);
        runAnim(ReimuAnimation.Intro);

        this.type = EnemyType.BOSS;
        this.dialogX = (this.hb_x - 70.0F) * Settings.scale;
        this.dialogY -= (this.hb_y - 55.0F) * Settings.scale;


        for(int i = 0; i < orbs.length; i++) {
            for (int j = 0; j < orbs[i].length; j++) {
                orbs[i][j] = new ArrayList<AbstractMonster>();
            }
        }

        this.turnsSinceBomb = 0;
        this.phase = new ReimuOne();
        this.rui = new ReimuUserInterface();
        this.lockAnimation = false;

        this.setHp(phase.calcAscensionNumber(this.maxHealth));
    }

    @Override
    public void usePreBattleAction() {
        PaleOfTheAncients.playTempMusic(PaleMod.makeID("finaldream"));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new Position(AbstractDungeon.player, 1)));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new HakureiShrineMaidenPower(this, 2)));
        AbstractDungeon.actionManager.addToBottom(new CannotLoseAction());
    }

    public int orbNum() {
        int counter = 0;
        for (int i = 0; i < orbs.length; i++) {
            for (int j = 0; j < orbs[i].length; j++) {
                counter += orbs[i][j].size();
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

        phase.takeTurn(this, rmi, info);

        turnsSinceBomb++;
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

        AbstractDungeon.effectList.add(new FantasySealVFX(AbstractDungeon.player, this, new DamageInfo(this, 0), 1));
    }

    @Override
    public void die(boolean triggerRelics) {
        boolean revival = rui.justAFleshWound();

        if(!revival) {
            AbstractDungeon.getCurrRoom().cannotLose = false;
            for(final AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
                if(!mo.isDeadOrEscaped()) {
                    AbstractDungeon.actionManager.addToBottom(new SuicideAction(mo));
                }
            }
            runAnim(ReimuAnimation.Defeat, ReimuAnimation.None);
            AbstractDungeon.effectList.add(new TouhouDeathVFX(this));
        }
        super.die(!revival && triggerRelics);
        if(revival) {
            runAnim(ReimuAnimation.Guardbreak);
            this.isDying = false;
            this.maxHealth *= 1.5F;
            AbstractDungeon.actionManager.addToBottom(new HealAction(this, this, this.maxHealth));
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this, this, StrengthPower.POWER_ID));
            phase.die();
            CardCrawlGame.sound.playV(PaleMod.makeID("touhou_powerup"), 0.15F);
            if(spellcircle == null) {
                spellcircle = new SpellCircleVFX(this);
                AbstractDungeon.effectList.add(spellcircle);
            }
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setColor(Color.WHITE);
        super.render(sb);
        rui.render(sb, this);
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
    }

    public void runAnim(ReimuAnimation animation) {
        runAnim(animation, ReimuAnimation.Idle);
    }
    public void runAnim(ReimuAnimation animation, ReimuAnimation followUp) {
        if(!lockAnimation) {
            this.state.setAnimation(0, animation.name(), false);
            if (followUp != ReimuAnimation.None) {
                this.state.addAnimation(0, followUp.name(), true, 0.0F);
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
        barrier = new BarrierVFX(this);
        AbstractDungeon.effectList.add(barrier);
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
        Closeattack,
        Guard,
        Guardbreak,
        Hithigh,
        Hitlow,
        SpellA,
        SpellA_Loop,
        SpellA_End
    }
}