package paleoftheancients.bandit.monsters;

import basemod.animations.SpriterAnimation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.utility.TextAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.SpeechBubble;
import com.megacrit.cardcrawl.vfx.SpotlightEffect;
import com.megacrit.cardcrawl.vfx.combat.HeartBuffEffect;
import paleoftheancients.PaleMod;
import paleoftheancients.bandit.actions.*;
import paleoftheancients.bandit.board.BanditBoard;
import paleoftheancients.bandit.board.rarespaces.DoomSpace;
import paleoftheancients.bandit.board.spaces.AbstractSpace;
import paleoftheancients.bandit.intent.EnumBuster;
import paleoftheancients.bandit.powers.BoardBoundEnemyPower;
import paleoftheancients.bandit.powers.BoardBoundPlayerPower;
import paleoftheancients.bandit.powers.ImprisonedPower;
import paleoftheancients.bandit.vfx.LeerVFX;
import paleoftheancients.dungeons.PaleOfTheAncients;
import paleoftheancients.finarubossu.actions.GuaranteePowerApplicationAction;
import paleoftheancients.helpers.AbstractBossMonster;
import paleoftheancients.relics.SoulOfTheBandit;

import java.util.ArrayList;

public class TheBandit extends AbstractBossMonster {
    public static final String ID = PaleMod.makeID("TheBandit");
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String[] DIALOG = monsterStrings.DIALOG;

    private final static byte MASSIVEPARTY = 0;
    private final static byte FABRICATEFRIEND = 1;
    private final static byte AWFULLOOK = 2; //weaker version of happyhit
    private final static byte EXTENDEDJUMP = 3;
    private final static byte BIGUPGRADE = 4;
    private final static byte DOUBLEDASH = 5;
    private final static byte DEADLYDASH = 6;
    private final static byte VERYLONGJUMP = 7;
    private final static byte HAPPYHIT = 8;
    private final static byte F = 9;
    private final static byte PEPEGA = 10;
    private final static byte SETUPBOARD = 11;

    private BanditBoard board = null;
    private ArrayList<Byte> cycletracker;
    public boolean phasetwo;
    private int turncounter;
    public boolean displayNumbers;

    public TheBandit() {
        super(NAME, ID, 15000,0.0F, -15.0F, 240.0F, 320.0F, null, 0, -20);
        this.animation = new SpriterAnimation(PaleMod.assetPath("images/bandit/spriter/bandit_resized_hat.scml"));
        this.animation.setFlip(true, false);

        this.setHp(calcAscensionNumber(this.maxHealth));
        this.flipHorizontal = true;
        this.dialogX -= 30F * Settings.scale;
        this.dialogY += 60F * Settings.scale;

        this.cycletracker = new ArrayList<>();
        this.resetCycles();
        this.phasetwo = false;
        this.turncounter = 0;
        this.displayNumbers = false;

        addMove(SETUPBOARD, Intent.UNKNOWN);
        addMove(MASSIVEPARTY, EnumBuster.MassivePartyIntent);
        addMove(FABRICATEFRIEND, Intent.UNKNOWN, -1, calcAscensionNumber(1.26F));
        addMove(BIGUPGRADE, Intent.BUFF, -1, calcAscensionNumber(2.26F));
        addMove(HAPPYHIT, EnumBuster.HappyHitIntent, calcAscensionNumber(20), calcAscensionNumber(2));
        addMove(AWFULLOOK, EnumBuster.HappyHitIntent, calcAscensionNumber(25), calcAscensionNumber(1));
        addMove(DEADLYDASH, EnumBuster.DeadlyDashIntent, calcAscensionNumber(22), 1, 1);
        addMove(DOUBLEDASH, EnumBuster.MoveAttackIntent, calcAscensionNumber(15), calcAscensionNumber(2.2F), true, 1);
        addMove(EXTENDEDJUMP, EnumBuster.MoveAttackIntent, calcAscensionNumber(23), 1, calcAscensionNumber(8));
        addMove(VERYLONGJUMP, EnumBuster.MoveAttackIntent, calcAscensionNumber(28), 1, calcAscensionNumber(13));
        addMove(F, Intent.ATTACK_DEFEND, calcAscensionNumber(30));
        addMove(PEPEGA, Intent.BUFF, -1, calcAscensionNumber(5F));
    }
    @Override
    public void usePreBattleAction() {
        addToBot(new TalkAction(this, DIALOG[0] + AbstractDungeon.player.name + DIALOG[1]));
    }

    @Override
    public void takeCustomTurn(DamageInfo info, int multiplier) {
        final AbstractCreature actor = this;
        this.displayNumbers = false;
        switch(this.nextMove) {
            case SETUPBOARD: {
                addToBot(new TalkAction(this, DIALOG[2]));
                board = new BanditBoard(this);
                board.init();
                addToBot(new GuaranteePowerApplicationAction(AbstractDungeon.player, this, new BoardBoundPlayerPower(AbstractDungeon.player, board)));
                addToBot(new GuaranteePowerApplicationAction(this, this, new BoardBoundEnemyPower(this, board, calcAscensionNumber(1.4F))));
                ArrayList<AbstractCard>[] groups = new ArrayList[]{
                        AbstractDungeon.player.hand.group,
                        AbstractDungeon.player.exhaustPile.group,
                        AbstractDungeon.player.discardPile.group,
                        AbstractDungeon.player.drawPile.group,
                        AbstractDungeon.player.limbo.group
                };
                for (final ArrayList<AbstractCard> list : groups) {
                    for (final AbstractCard card : list) {
                        board.processCard(card);
                    }
                }
                break;
            }

            case MASSIVEPARTY: {
                this.addToBot(new VFXAction(new SpotlightEffect()));
                this.addToBot(new MassivePartyAction(this, board, getTriggerCount()));
                break;
            }

            case FABRICATEFRIEND:
                this.addToBot(new VFXAction(new HeartBuffEffect(this.hb.cX, this.hb.cY)));
                for(int i = 0; i < multiplier; i++) {
                    this.addToBot(new AddDroneAction(this.board, multiplier));
                }
                phasetwo = true;
                this.resetCycles();
                break;

            case AWFULLOOK:
            case HAPPYHIT: {
                if(nextMove == AWFULLOOK) {
                    AbstractDungeon.actionManager.addToBottom(new VFXAction(new LeerVFX(this.hb.cX, this.hb.cY, AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, this.flipHorizontal)));// 30
                    this.addToBot(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.FIRE));
                } else {
                    this.addToBot(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                }
                this.addToBot(new AbstractGameAction() {
                    @Override
                    public void update() {
                        this.isDone = true;
                        board.triggerNextSpaces(actor, multiplier, getTriggerCount());
                    }
                });
                break;
            }

            case DEADLYDASH:
                this.addToBot(new DeadlyDashAction(board, info));
                break;

            case DOUBLEDASH:
                for(int i = 0; i < multiplier; ++i) {
                    this.addToBot(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                    this.addToBot(new BoardMoveAction(this, board, getMotion()));
                }
                break;

            case EXTENDEDJUMP:
            case VERYLONGJUMP:
                this.addToBot(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                this.addToBot(new BoardMoveAction(this, board, getMotion()));
                break;

            case F:
                this.addToBot(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                this.addToBot(new GainBlockAction(this, this, info.base));
                this.addToBot(new TransformEmptySpaceAction(board, DoomSpace.class));
                break;

            case PEPEGA:
                this.addToBot(new ApplyPowerAction(this, this, new StrengthPower(this, multiplier), multiplier));
                this.addToBot(new TransformEmptySpaceAction(board, DoomSpace.class));
                break;

            case BIGUPGRADE: {
                AbstractSpace.GOODNESS[] goodra = new AbstractSpace.GOODNESS[]{AbstractSpace.GOODNESS.BAD, AbstractSpace.GOODNESS.GOOD};
                int amount = multiplier;
                AbstractSpace sp;
                for(int i = 0; i < goodra.length && multiplier > 0; i++) {
                    ArrayList<AbstractSpace> spaces = new ArrayList<>();
                    for(final AbstractSpace s : board.squareList) {
                        if(!s.triggersWhenPassed && s.goodness == goodra[i]) {
                            spaces.add(s);
                        }
                    }
                    while(!spaces.isEmpty() && amount-- > 0) {
                        sp = spaces.remove(AbstractDungeon.monsterRng.random(spaces.size() - 1));
                        sp.triggersWhenPassed = true;
                        sp.splat();
                    }
                }
                break;
            }
        }
    }

    @Override
    public void getMove(int num) {
        if(board == null) {
            setMoveShortcut(SETUPBOARD);
            return;
        }
        this.addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                this.isDone = true;
                displayNumbers = true;
            }
        });


        if(cycletracker.isEmpty() || (AbstractDungeon.ascensionLevel >= 9 && cycletracker.size() <= 1)) {
            resetCycles();
            this.setMoveShortcut(MASSIVEPARTY, MOVES[MASSIVEPARTY]);
            return;
        }

        if(!phasetwo && (turncounter++ > 10 || phaseTransitionTreshold())) {
            phaseTransition(turncounter < 5 ? 4 : 3, false);
            return;
        }

        byte move = cycletracker.remove(AbstractDungeon.monsterRng.random(cycletracker.size() - 1));
        this.setMoveShortcut(move, MOVES[move]);
    }

    private void resetCycles() {
        cycletracker.clear();
        if(phasetwo) {
            cycletracker.add(DEADLYDASH);
            cycletracker.add(VERYLONGJUMP);
            cycletracker.add(HAPPYHIT);
            cycletracker.add(F);
            cycletracker.add(PEPEGA);
        } else {
            cycletracker.add(BIGUPGRADE);
            cycletracker.add(AWFULLOOK);
            cycletracker.add(EXTENDEDJUMP);
            cycletracker.add(DOUBLEDASH);
        }
    }

    @Override
    public void damage(DamageInfo info) {
        int curhp = this.currentHealth;
        super.damage(info);
        if(board != null && !phasetwo && nextMove != FABRICATEFRIEND && curhp * 3 > this.maxHealth * 2 && phaseTransitionTreshold()) {
            phaseTransition(4, true);
        }
    }
    private void phaseTransition(int dialogue, boolean createIntent) {
        if(createIntent && this.nextMove != FABRICATEFRIEND) {
            AbstractDungeon.actionManager.addToBottom(new TextAboveCreatureAction(this, TextAboveCreatureAction.TextType.INTERRUPTED));
        }
        this.setMoveShortcut(FABRICATEFRIEND);
        if(createIntent) {
            this.createIntent();
        } else {
            this.nextMove = FABRICATEFRIEND;
        }
        addToBot(new TalkAction(this, DIALOG[dialogue]));
    }

    private int getMotion() {
        EnemyMoveInfo emi = moves.get(nextMove);
        if(emi instanceof BanditMoveInfo) {
            return ((BanditMoveInfo) emi).move;
        }
        return 0;
    }
    public int getDisplayMotion() {
        if(this.hasPower(ImprisonedPower.POWER_ID)) {
            return 0;
        }
        EnemyMoveInfo emi = moves.get(nextMove);
        return getMotion() * (emi.isMultiDamage ? emi.multiplier : 1);
    }

    private int getTriggerCount() {
        int count = 1;
        /*
        AbstractPower pow = this.getPower(KeyFinisherPower.POWER_ID);
        if(pow != null) {
            count += pow.amount;
        }
         */
        return count;
    }

    @Override
    public void die(boolean triggerRelics) {
        PaleOfTheAncients.addRelicReward(SoulOfTheBandit.ID);
        AbstractDungeon.effectList.add(new SpeechBubble(this.hb.cX + this.dialogX, this.hb.cY + this.dialogY, 2.5F, DIALOG[5], false));
        super.die(triggerRelics);
    }

    @Override
    public void update() {
        super.update();
        if(board != null) {
            board.update();
        }
    }
    @Override
    public void render(SpriteBatch sb) {
        super.render(sb);
        if(board != null) {
            board.render(sb);
        }
    }

    private boolean phaseTransitionTreshold() {
        return this.currentHealth * 3 <= this.maxHealth * 2;
    }

    public static class BanditMoveInfo extends EnemyMoveInfo {
        public int move;
        public BanditMoveInfo(byte nextMove, Intent intent, int intentBaseDmg, int multiplier, boolean isMultiDamage, int move) {
            super(nextMove, intent, intentBaseDmg, multiplier, isMultiDamage);
            this.move = move;
        }
    }
    protected void addMove(byte moveCode, Intent intent, int baseDamage, int multiplier, int move) {
        this.addMove(moveCode, intent, baseDamage, multiplier, false, move);
    }
    protected void addMove(byte moveCode, Intent intent, int baseDamage, int multiplier, boolean isMultiDamage, int move) {
        this.moves.put(moveCode, new BanditMoveInfo(moveCode, intent, baseDamage, multiplier, isMultiDamage, move));
    }
}
