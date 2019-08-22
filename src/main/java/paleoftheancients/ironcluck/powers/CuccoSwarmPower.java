package paleoftheancients.ironcluck.powers;

import paleoftheancients.NRPower;
import paleoftheancients.PaleMod;
import paleoftheancients.ironcluck.monsters.IronCluck;
import com.megacrit.cardcrawl.actions.common.ChangeStateAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.GainStrengthPower;

public class CuccoSwarmPower extends NRPower {
    public static final String POWER_ID = PaleMod.makeID("CuccoSwarmPower");
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;

    private int origAmnt;

    public CuccoSwarmPower(AbstractCreature owner, int amount) {
        super("cuccoswarm.png");
        this.owner = owner;
        this.amount = amount;
        this.origAmnt = this.amount;

        this.name = NAME;
        this.ID = POWER_ID;
        this.type = PowerType.BUFF;

        this.priority = -99;

        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        this.amount = this.origAmnt;
        this.updateDescription();
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if(info.owner != this.owner && damageAmount > 0 && this.amount > 0) {
            this.amount -= damageAmount;
            if(this.amount <= 0) {
                AbstractPower p = this.owner.getPower(GainStrengthPower.POWER_ID);
                if(p != null) {
                    p.atEndOfTurn(true);
                }
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction((AbstractMonster) this.owner, IronCluck.COMMENCE_CHICKENS));
            }
            this.updateDescription();
        }

        return damageAmount;
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1] + IronCluck.CUCCO_DMG + DESCRIPTIONS[2] + IronCluck.CUCCO_COUNT + DESCRIPTIONS[3];
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    }
}
