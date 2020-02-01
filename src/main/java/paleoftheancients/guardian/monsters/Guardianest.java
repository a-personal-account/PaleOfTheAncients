package paleoftheancients.guardian.monsters;

import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.exordium.TheGuardian;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ModeShiftPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.SurroundedPower;
import paleoftheancients.PaleMod;
import paleoftheancients.dungeons.PaleOfTheAncients;
import paleoftheancients.guardian.powers.EnemyDemonForm;
import paleoftheancients.guardian.powers.GuardianLinkedPower;
import paleoftheancients.relics.SoulOfTheGuardian;

public class Guardianest extends TheGuardian {
    public static String ID = PaleMod.makeID("Guardianest");

    public AbstractCreature partner;

    public Guardianest() {
        this(false);
    }
    public Guardianest(boolean left) {
        super();
        this.setHp(this.maxHealth * 3);
        ReflectionHacks.setPrivate(this, TheGuardian.class, "dmgThresholdIncrease", 10 + AbstractDungeon.ascensionLevel);
        ReflectionHacks.setPrivate(this, TheGuardian.class, "dmgThreshold", 50);
        if(left) {
            this.drawX -= 1000F * Settings.scale;
            this.flipHorizontal = true;
        } else {
            this.drawX += 100F * Settings.scale;
        }
        this.partner = null;
    }

    @Override
    public void usePreBattleAction() {
        if(this.drawX < Settings.WIDTH / 2F) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new SurroundedPower(AbstractDungeon.player)));
            AbstractDungeon.actionManager.addToTop(new AbstractGameAction() {
                @Override
                public void update() {
                    this.isDone = true;
                    AbstractDungeon.player.movePosition(Settings.WIDTH / 2.0F, AbstractDungeon.floorY);
                }
            });
        }
        super.usePreBattleAction();
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new EnemyDemonForm(this, 2), 2));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new GuardianLinkedPower(this)));
        for(final AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if(mo != this && mo instanceof Guardianest) {
                this.partner = mo;
                break;
            }
        }
    }

    @Override
    public void changeState(String stateName) {
        super.changeState(stateName);
        if(stateName.equals(ReflectionHacks.getPrivateStatic(TheGuardian.class, "DEFENSIVE_MODE"))) {
            AbstractPower pow = this.getPower(StrengthPower.POWER_ID);
            if(pow != null) {
                AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(this, this, pow, (pow.amount + 1) / 2));
            }
        }
    }

    public void damage(DamageInfo info) {
        int tmpHealth = this.currentHealth;
        super.damage(info);
        final int diffhealth = tmpHealth - this.currentHealth;
        if(partner != null && diffhealth > 0 && info.type != DamageInfo.DamageType.HP_LOSS) {
            AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
                @Override
                public void update() {
                    this.isDone = true;

                    AbstractPower pow = partner.getPower(ModeShiftPower.POWER_ID);
                    if(pow != null) {
                        pow.amount += diffhealth * 2;
                        int threshold = (int)ReflectionHacks.getPrivate(partner, TheGuardian.class, "dmgThreshold");
                        ReflectionHacks.setPrivate(partner, TheGuardian.class, "dmgTaken", threshold - pow.amount);
                    }

                    partner.damage(new DamageInfo(partner, diffhealth, DamageInfo.DamageType.HP_LOSS));
                }
            });
        }
    }

    @Override
    public void die(boolean triggerRelics) {
        PaleOfTheAncients.addRelicReward(SoulOfTheGuardian.ID);
        super.die(triggerRelics);
    }
}
