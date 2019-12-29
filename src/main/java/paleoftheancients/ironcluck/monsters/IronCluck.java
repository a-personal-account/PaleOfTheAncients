package paleoftheancients.ironcluck.monsters;

import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.red.*;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.BarricadePower;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.Girya;
import com.megacrit.cardcrawl.relics.RedSkull;
import com.megacrit.cardcrawl.vfx.combat.FlameBarrierEffect;
import com.megacrit.cardcrawl.vfx.combat.HemokinesisEffect;
import com.megacrit.cardcrawl.vfx.combat.InflameEffect;
import paleoftheancients.PaleMod;
import paleoftheancients.dungeons.PaleOfTheAncients;
import paleoftheancients.ironcluck.powers.ChickenBarrierPower;
import paleoftheancients.ironcluck.powers.ChickenBurnPower;
import paleoftheancients.ironcluck.powers.ConditionalShackles;
import paleoftheancients.ironcluck.powers.CuccoSwarmPower;
import paleoftheancients.ironcluck.vfx.CuccoSwarmEffect;
import paleoftheancients.relics.SoulOfTheIroncluck;
import paleoftheancients.thevixen.helpers.RandomPoint;
import paleoftheancients.thevixen.vfx.RandomAnimatedSlashEffect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class IronCluck extends CustomMonster {
    public static final String ID = PaleMod.makeID("IronCluck");
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;

    public static final byte STARTING_STRENGTH = 10;

    private Map<Byte, EnemyMoveInfo> moves;
    private static final byte BARRICADE = 0;
    private static final byte BODYSLAM = 1;
    private static final byte IMPERVIOUS = 2;
    private static final byte LIMITBREAK = 3;
    private static final byte HEMOKINESIS = 4;
    private static final byte DISARM = 5;
    private static final byte IMMOLATE = 6;
    private static final byte SWORDBOOMERANG = 7;
    private static final byte FLAMEBARRIER = 8;
    private static final byte CUCCOSWARM = 10;

    private int turnCounter;
    private String animationsuffix;

    public static final byte CUCCO_DMG = 2;
    public static final byte CUCCO_COUNT = 15;

    public IronCluck() {
        this(6F);
    }
    public IronCluck(float scaling) {
        super(NAME, ID, 650, 0.0F, -20.0F, 220.0F, 220.0F, (String)null, -125.0F, 20.0F);

        this.type = EnemyType.BOSS;
        this.moves = new HashMap<>();
        this.moves.put(BARRICADE, new EnemyMoveInfo(BARRICADE, Intent.DEFEND_BUFF, -1, 0, false));
        this.moves.put(BODYSLAM, new EnemyMoveInfo(BODYSLAM, Intent.ATTACK, 0, 0, false));
        this.moves.put(SWORDBOOMERANG, new EnemyMoveInfo(SWORDBOOMERANG, Intent.ATTACK, calcAscensionNumber(6.4F), calcAscensionNumber(2.6F), true));
        this.moves.put(IMPERVIOUS, new EnemyMoveInfo(IMPERVIOUS, Intent.DEFEND, -1, 0, false));
        this.moves.put(LIMITBREAK, new EnemyMoveInfo(LIMITBREAK, Intent.BUFF, -1, 0, false));
        this.moves.put(HEMOKINESIS, new EnemyMoveInfo(HEMOKINESIS, Intent.ATTACK, calcAscensionNumber(25), 0, false));
        this.moves.put(DISARM, new EnemyMoveInfo(DISARM, Intent.STRONG_DEBUFF, -1, 0, false));
        this.moves.put(IMMOLATE, new EnemyMoveInfo(IMMOLATE, Intent.ATTACK, calcAscensionNumber(30), 0, false));
        this.moves.put(FLAMEBARRIER, new EnemyMoveInfo(FLAMEBARRIER, Intent.DEFEND_BUFF, -1, 0, false));

        this.moves.put(CUCCOSWARM, new EnemyMoveInfo(CUCCOSWARM, Intent.ATTACK, CUCCO_DMG, CUCCO_COUNT, true));

        this.turnCounter = 0;

        this.loadAnimation(PaleMod.assetPath("images/ironcluck/ironcluck.atlas"), PaleMod.assetPath("images/ironcluck/ironcluck.json"), scaling);

        this.animationsuffix = "";
        if(Loader.isModLoaded("cowboy-ironclad")) {
            this.animationsuffix = "_Hat";
        }

        this.flipHorizontal = true;

        this.moveHistory.add(BARRICADE);

        this.stateData.setMix("Hit" + this.animationsuffix, "Idle" + this.animationsuffix, 0.1F);
    }

    public static boolean cowboyClad() {
        return Loader.isModLoaded("cowboy-ironclad");
    }

    @Override
    public void usePreBattleAction() {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new ConditionalShackles(this)));
        AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(this, new Girya()));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, STARTING_STRENGTH), STARTING_STRENGTH));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new CuccoSwarmPower(this, 50), 50));

        CardCrawlGame.sound.playA(PaleMod.makeID("cluck"), MathUtils.random(-0.2F, 0.2F));

        this.state.addAnimation(0, "Idle" + this.animationsuffix, true, 0.0F);
    }

    @Override
    public void takeTurn() {
        DamageInfo info = new DamageInfo(this, moves.get(this.nextMove).baseDamage, DamageInfo.DamageType.NORMAL);
        if(info.base > -1) {
            info.applyPowers(this, AbstractDungeon.player);
        }
        int multiplier;
        switch(this.nextMove) {
            case BARRICADE:
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new BarricadePower(this)));
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this, calcAscensionNumber(30)));
                turnCounter--;
                break;
            case IMPERVIOUS:
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this, calcAscensionNumber(45)));
                turnCounter--;
                break;
            case FLAMEBARRIER:
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new FlameBarrierEffect(this.hb.cX, this.hb.cY)));
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this, calcAscensionNumber(40)));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new ChickenBarrierPower(this, calcAscensionNumber(4)), calcAscensionNumber(4)));
                turnCounter--;
                break;
            case BODYSLAM:
                this.useFastAttackAnimation();
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this, this, BarricadePower.POWER_ID));
                break;

            case CUCCOSWARM:
                this.useJumpAnimation();
                multiplier = this.moves.get(this.nextMove).multiplier;
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new CuccoSwarmEffect(AbstractDungeon.player, multiplier), 1F));
                for(int i = 0; i < multiplier; i++) {
                    AbstractDungeon.actionManager.addToBottom(new VFXAction(new RandomAnimatedSlashEffect(RandomPoint.x(AbstractDungeon.player.hb), RandomPoint.y(AbstractDungeon.player.hb))));
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.NONE, true));
                }
                break;

            case LIMITBREAK:
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new InflameEffect(this)));
                AbstractPower strength = this.getPower(StrengthPower.POWER_ID);
                AbstractPower gainstrength = this.getPower(GainStrengthPower.POWER_ID);
                if(strength != null) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, strength.amount), strength.amount));
                }
                if(gainstrength != null) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new GainStrengthPower(this, gainstrength.amount), gainstrength.amount));
                }

                if(strength == null && gainstrength == null) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, STARTING_STRENGTH), STARTING_STRENGTH));
                }
                break;
            case HEMOKINESIS:
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new HemokinesisEffect(this.hb.cX, this.hb.cY, AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY), 0.5F));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                AbstractDungeon.actionManager.addToBottom(new LoseHPAction(this, this, 15));
                break;
            case DISARM:
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new StrengthPower(AbstractDungeon.player, calcAscensionNumber(-3)), calcAscensionNumber(-3)));
                break;
            case IMMOLATE:
                this.useFastAttackAnimation();
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.FIRE));
                if(AbstractDungeon.ascensionLevel < 9) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new ChickenBurnPower(this, 2), 2));
                }
                break;
            case SWORDBOOMERANG:
                this.useFastAttackAnimation();
                multiplier = this.moves.get(this.nextMove).multiplier;
                for(int i = 0; i < multiplier; i++) {
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
                }
                break;
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
        this.turnCounter++;
    }

    @Override
    public void getMove(int num) {
        switch(this.nextMove) {
            case BARRICADE:
                this.setMoveShortcut(IMPERVIOUS);
                break;
            case IMPERVIOUS:
                this.setMoveShortcut(FLAMEBARRIER);
                break;
            case FLAMEBARRIER:
                this.setMove(MOVES[BODYSLAM], BODYSLAM, this.moves.get(BODYSLAM).intent, this.currentBlock);
                this.moveHistory.remove(this.moveHistory.size() - 1);
                break;

            case BODYSLAM:
                this.moveHistory.remove(this.moveHistory.size() - 1);
            default:
                if(turnCounter % 5 == 4) {
                    this.setMoveShortcut(LIMITBREAK);
                } else if(this.turnCounter % 7 == 6) {
                    this.setMoveShortcut(DISARM);
                } else {
                    ArrayList<Byte> possibilities = new ArrayList<>();
                    possibilities.add(BARRICADE);
                    possibilities.add(BARRICADE);
                    possibilities.add(HEMOKINESIS);
                    possibilities.add(HEMOKINESIS);
                    possibilities.add(SWORDBOOMERANG);
                    possibilities.add(SWORDBOOMERANG);
                    possibilities.add(SWORDBOOMERANG);
                    possibilities.add(IMMOLATE);
                    possibilities.add(IMMOLATE);
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
                }
                if(!this.moveHistory.isEmpty() && this.moveHistory.get(this.moveHistory.size() - 1) == BODYSLAM) {
                    this.moveHistory.remove(this.moveHistory.size() - 1);
                }
        }
    }

    private void setMoveShortcut(byte next) {
        EnemyMoveInfo info = this.moves.get(next);
        this.setMove(MOVES[next], next, info.intent, info.baseDamage, info.multiplier, info.isMultiDamage);
    }

    @Override
    public void die(boolean triggerRelics) {
        PaleOfTheAncients.addRewardRelic(SoulOfTheIroncluck.ID);
        super.die(triggerRelics);
    }

    @Override
    public void damage(DamageInfo info) {
        int prevhp = this.currentHealth;
        super.damage(info);
        if(this.currentHealth <= this.maxHealth / 2 && prevhp > this.maxHealth / 2) {
            AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(this, new RedSkull()));
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, 3), 3));
        }

        if(prevhp > this.currentHealth) {
            this.state.setAnimation(0, "Hit" + this.animationsuffix, false);
            this.state.addAnimation(0, "Idle" + this.animationsuffix, true, 0.0F);
        }

        if(this.nextMove == BODYSLAM) {
            this.setMove(BODYSLAM, this.moves.get(BODYSLAM).intent, this.currentBlock);
            this.createIntent();
        }
    }

    public static final String COMMENCE_CHICKENS = "Cucco";
    @Override
    public void changeState(String statename) {
        switch(statename) {
            case COMMENCE_CHICKENS:
                EnemyMoveInfo info = this.moves.get(CUCCOSWARM);
                this.setMove(CUCCOSWARM, info.intent, info.baseDamage, info.multiplier, info.isMultiDamage);
                this.createIntent();
                break;
        }
    }


    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
        NAME = monsterStrings.NAME;
        MOVES = new String[] {
                Barricade.NAME,
                BodySlam.NAME,
                Impervious.NAME,
                LimitBreak.NAME,
                Hemokinesis.NAME,
                Disarm.NAME,
                Immolate.NAME,
                SwordBoomerang.NAME,
                FlameBarrier.NAME,
        };
        DIALOG = monsterStrings.DIALOG;
    }

    public void setRotation(float rotation) {
        this.skeleton.getRootBone().setRotation(rotation + (float)Math.PI * 3 / 2);
    }

    public int calcAscensionNumber(float base) {
        if(AbstractDungeon.ascensionLevel >= 19) {
            base *= 1.35F;
        } else if(AbstractDungeon.ascensionLevel >= 9) {
            base *= 1.20F;
        } else if(AbstractDungeon.ascensionLevel >= 4) {
            base *= 1.15F;
        }
        return Math.round(base);
    }
}
