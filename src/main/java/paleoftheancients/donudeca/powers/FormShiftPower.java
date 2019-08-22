package paleoftheancients.donudeca.powers;

import paleoftheancients.PaleMod;
import paleoftheancients.donudeca.monsters.DonuDeca;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ChangeStateAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class FormShiftPower extends AbstractPower {
    public static final String POWER_ID = PaleMod.makeID("FormShiftPower");
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;

    private static final int DECA = 30;
    private static final int DONU = 6;
    private static final float ATTAC = 1.3F;

    DonuDeca donudeca;

    public FormShiftPower(AbstractCreature owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = -1;
        this.loadRegion("modeShift");

        this.donudeca = (DonuDeca) owner;

        if(donudeca.mode == DonuDeca.Mode.Deca) {
            this.amount = DECA;
        } else {
            this.amount = DONU;
        }

        this.updateDescription();
    }

    public int onAttacked(DamageInfo info, int damageAmount) {
        if(damageAmount > 0 && donudeca.mode == DonuDeca.Mode.Deca) {
            this.amount -= damageAmount;
            if (this.amount <= 0) {
                this.amount = DONU;
                delayUpdate();
                AbstractDungeon.actionManager.addToTop(new ChangeStateAction(donudeca, DonuDeca.FORM_ATTACK));
            }
            this.updateDescription();
        }
        return damageAmount;
    }
    public int onAttackToChangeDamage(DamageInfo info, int damageAmount) {
        if(info.type != DamageInfo.DamageType.THORNS && donudeca.mode == DonuDeca.Mode.Donu) {
            damageAmount *= ATTAC;
        }
        return damageAmount;
    }

    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        this.onInflictDamage(info, damageAmount, target);
    }
    public void onInflictDamage(DamageInfo info, int damageAmount, AbstractCreature target) {
        if(info.type != DamageInfo.DamageType.THORNS && donudeca.mode == DonuDeca.Mode.Donu) {
            this.amount--;
            if(this.amount <= 0) {
                this.amount = DECA;
                delayUpdate();
                AbstractDungeon.actionManager.addToTop(new ChangeStateAction(donudeca, DonuDeca.FORM_DEFEND));
            }
            this.updateDescription();
        }
    }

    public void updateDescription() {
        if(donudeca.mode == DonuDeca.Mode.Deca) {
            this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
        } else {
            this.description = DESCRIPTIONS[3] + this.amount + DESCRIPTIONS[4] + (int)(Math.round((ATTAC - 1F) * 10) * 10) + DESCRIPTIONS[5];
        }
    }

    private void delayUpdate() {
        AbstractDungeon.actionManager.addToTop(new AbstractGameAction() {
            @Override
            public void update() {
                this.isDone = true;
                updateDescription();
            }
        });
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    }
}
