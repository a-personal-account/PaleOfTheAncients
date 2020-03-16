package paleoftheancients.donudeca.monsters;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.green.AThousandCuts;
import com.megacrit.cardcrawl.cards.status.VoidCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.relics.Circlet;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.vfx.combat.WeightyImpactEffect;
import paleoftheancients.PaleMod;
import paleoftheancients.donudeca.powers.FormShiftPower;
import paleoftheancients.donudeca.powers.LifestealPower;
import paleoftheancients.helpers.PaleRewardItem;
import paleoftheancients.relics.SoulOfTheShapes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DonuDeca extends AbstractMonster {
    public static final String ID = PaleMod.makeID("DonuDeca");
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;

    public static final String FORM_ATTACK = "ATTAC";
    public static final String FORM_DEFEND = "PROTEC";

    private String animationsuffix;
    public Mode mode;

    private static final byte VOID_ATTACK = 0;
    private static final byte BIG_ATTACK = 1;
    private static final byte DOUBLE_ATTACK = 2;
    private static final byte SMALL_DEBUFF_ATTACK = 3;

    private Map<Byte, EnemyMoveInfo> moves;

    public DonuDeca(AbstractMonster... monsters) {
        super(NAME, ID, 250, 0.0F, -20.0F, 390.0F, 390.0F, (String)null, -125.0F, 20.0F);
        this.loadAnimation(PaleMod.assetPath("images/donudeca/skeleton.atlas"), PaleMod.assetPath("images/donudeca/skeleton.json"), 1.0F);

        this.moves = new HashMap<>();
        this.moves.put(VOID_ATTACK, new EnemyMoveInfo(VOID_ATTACK, Intent.ATTACK_DEBUFF, 15, 0, false));
        this.moves.put(BIG_ATTACK, new EnemyMoveInfo(BIG_ATTACK, Intent.ATTACK, 30, 0, false));
        this.moves.put(DOUBLE_ATTACK, new EnemyMoveInfo(DOUBLE_ATTACK, Intent.ATTACK, 10, 2, true));
        this.moves.put(SMALL_DEBUFF_ATTACK, new EnemyMoveInfo(SMALL_DEBUFF_ATTACK, Intent.ATTACK_DEBUFF, 10, 0, false));

        Map<String, AbstractPower> instances = new HashMap<>();
        int hp = 0;
        for(final AbstractMonster mo : monsters) {
            if (mo != null) {
                hp += mo.maxHealth;
                if (mo.currentHealth > 0) {
                    hp += mo.currentHealth;
                }
                for(final AbstractPower pow : mo.powers) {
                    if(!instances.containsKey(pow.ID)) {
                        instances.put(pow.ID, pow);
                        pow.owner = this;
                    } else {
                        if(pow.amount > 0 || pow.canGoNegative) {
                            instances.get(pow.ID).amount += pow.amount;
                        } else if(pow.amount == -1) {
                            instances.get(pow.ID).amount = -1;
                        }
                    }
                }
            }
        }

        if(hp > 0) {
            this.setHp(hp);
        }

        instances.remove(LifestealPower.POWER_ID);
        instances.remove(PainfulStabsPower.POWER_ID);
        if(instances.containsKey(StrengthPower.POWER_ID)) {
            instances.get(StrengthPower.POWER_ID).amount /= 4;
        }
        for(final AbstractPower entry : instances.values()) {
            this.powers.add(entry);
        }

        this.stateData.setMix("Hit", "Idle", 0.1F);
        this.stateData.setMix("Attack_2", "Idle", 0.1F);

        this.stateData.setMix("Hit_deca", "Idle_deca", 0.1F);
        this.stateData.setMix("Attack_2_deca", "Idle_deca", 0.1F);

        this.type = EnemyType.BOSS;
        this.dialogX = -200.0F * Settings.scale;
        this.dialogY = 10.0F * Settings.scale;

        if(AbstractDungeon.monsterRng.random(1) == 0) {
            mode = Mode.Deca;
            this.changeState(FORM_ATTACK);
        } else {
            mode = Mode.Donu;
            this.changeState(FORM_DEFEND);
        }

        this.powers.add(new FormShiftPower(this));
        Collections.sort(this.powers);

    }

    public void toggleAnimation() {
        if(this.mode == Mode.Donu) {
            this.animationsuffix = "";
        } else {
            this.animationsuffix = "_deca";
        }
        this.state.addAnimation(0, "Idle" + this.animationsuffix, true, 0.0F);
    }

    @Override
    public void takeTurn() {
        DamageInfo info = new DamageInfo(this, moves.get(this.nextMove).baseDamage, DamageInfo.DamageType.NORMAL);
        info.applyPowers(this, AbstractDungeon.player);

        switch(this.nextMove) {
            case VOID_ATTACK:
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "ATTACK"));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(new VoidCard(), 1, true, true));
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(new VoidCard(), 1));
                break;
            case BIG_ATTACK:
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new WeightyImpactEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY)));
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "ATTACK"));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.NONE));
                break;
            case DOUBLE_ATTACK:
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "ATTACK"));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.SLASH_VERTICAL));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
                break;
            case SMALL_DEBUFF_ATTACK:
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "ATTACK"));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.POISON));
                if(mode == Mode.Donu) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new VulnerablePower(AbstractDungeon.player, 1, true), 1));
                } else {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new WeakPower(AbstractDungeon.player, 1, true), 1));
                }
                break;

        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    public void getMove(int num) {
        ArrayList<Byte> possibilities = new ArrayList<>();
        possibilities.add(VOID_ATTACK);
        possibilities.add(BIG_ATTACK);
        possibilities.add(DOUBLE_ATTACK);
        possibilities.add(DOUBLE_ATTACK);
        possibilities.add(SMALL_DEBUFF_ATTACK);

        if(!this.moveHistory.isEmpty()) {
            possibilities.remove((Byte) this.moveHistory.get(this.moveHistory.size() - 1));
        }
        num = num * possibilities.size() / 100;

        EnemyMoveInfo info = moves.get(possibilities.get(num));
        this.setMove(info.nextMove, info.intent, info.baseDamage, info.multiplier, info.isMultiDamage);
    }

    @Override
    public void changeState(String stateName) {
        switch(stateName) {
            case "ATTACK":
                this.state.setAnimation(0, "Attack_2" + this.animationsuffix, false);
                this.state.addAnimation(0, "Idle" + this.animationsuffix, true, 0.0F);
                break;

            case FORM_ATTACK:
                if(mode == Mode.Deca) {
                    mode = Mode.Donu;
                    this.toggleAnimation();
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new AngerPower(this, 1), 1));
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new PainfulStabsPower(this)));
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new LifestealPower(this)));
                    AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this, this, MetallicizePower.POWER_ID));
                    AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this, this, ThornsPower.POWER_ID));
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new ArtifactPower(this, 1), 1));
                }
                break;
            case FORM_DEFEND:
                if(mode == Mode.Donu) {
                    mode = Mode.Deca;
                    this.toggleAnimation();
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new MetallicizePower(this, 10), 10));
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new ThornsPower(this, 3), 3));
                    AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this, this, AngerPower.POWER_ID));
                    AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this, this, PainfulStabsPower.POWER_ID));
                    AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this, this, LifestealPower.POWER_ID));
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new ArtifactPower(this, 1), 1));
                }
                break;
        }
    }

    @Override
    public void damage(DamageInfo info) {
        super.damage(info);
        if (info.owner != null && info.type != DamageInfo.DamageType.THORNS && info.output > 0) {
            this.state.setAnimation(0, "Hit" + this.animationsuffix, false);
            this.state.addAnimation(0, "Idle" + this.animationsuffix, true, 0.0F);
        }
    }

    @Override
    public void die(boolean triggerRelics) {
        if(!AbstractDungeon.player.hasRelic(SoulOfTheShapes.ID)) {
            RewardItem[] rewards = new RewardItem[2];
            rewards[0] = new PaleRewardItem(RelicLibrary.getRelic(SoulOfTheShapes.ID).makeCopy());
            rewards[1] = new PaleRewardItem(RelicLibrary.getRelic(SoulOfTheShapes.ID).makeCopy());
            rewards[1].relic.onUseCard(CardLibrary.getCard(AThousandCuts.ID), null);

            rewards[0].relicLink = rewards[1];
            rewards[1].relicLink = rewards[0];
            AbstractDungeon.getCurrRoom().rewards.add(rewards[0]);
            AbstractDungeon.getCurrRoom().rewards.add(rewards[1]);
        } else {
            AbstractDungeon.getCurrRoom().rewards.add(new RewardItem(RelicLibrary.getRelic(Circlet.ID).makeCopy()));
        }
        super.die(triggerRelics);
    }
    @Override
    public void onBossVictoryLogic() {}

    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
        NAME = monsterStrings.NAME;
        MOVES = monsterStrings.MOVES;
        DIALOG = monsterStrings.DIALOG;
    }

    public enum Mode {
        Donu,
        Deca
    }
}
