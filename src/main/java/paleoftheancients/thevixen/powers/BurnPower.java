package paleoftheancients.thevixen.powers;

import paleoftheancients.PaleMod;
import paleoftheancients.thevixen.actions.SetPlayerBurnAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;


public class BurnPower extends AbstractTheVixenPower {
    public static final String POWER_ID = PaleMod.makeID("BurnPower");
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;

    public static PowerType POWER_TYPE = PowerType.DEBUFF;
    public static final String IMG = "burn.png";

    public BurnPower(AbstractCreature owner, int amount) {
        super(IMG);
        this.owner = owner;
        this.amount = amount;
        this.isTurnBased = false;

        this.name = NAME;
        this.ID = POWER_ID;
        this.type = POWER_TYPE;

        updateDescription();
        this.priority = -99;
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
        if(this.owner instanceof AbstractPlayer) {
            this.description += DESCRIPTIONS[2];
        } else {
            this.description += DESCRIPTIONS[3] + 4 + DESCRIPTIONS[4];
        }
    }

    @Override
    public float atDamageReceive(float damage, DamageInfo.DamageType type) {
        if (type == DamageInfo.DamageType.NORMAL) {
            damage += this.amount;
        }
        return damage;
    }

    @Override
    public void onRemove() {
        if(this.owner instanceof AbstractPlayer) {
            SetPlayerBurnAction.addToBottom();
        }
    }

    @Override
    public void atEndOfRound() {
        if(!(this.owner instanceof AbstractPlayer)) {
            AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(this.owner, this.owner, this, (this.amount + 1) / 2));
        }
    }

    @Override
    public void stackPower(int amount) {
        super.stackPower(amount);
        if(this.owner instanceof AbstractMonster) {
            if(this.amount % 4 == 0) {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.owner, this.owner, new WeakPower(this.owner, 1, false), 1));
            }
        }
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    }
}
