package paleoftheancients.maker.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import paleoftheancients.PaleMod;

public class GainPlatedArmorPower extends AbstractPower {
    public static final String POWER_ID = PaleMod.makeID("GainPlatedArmorPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private int amount2;

    public GainPlatedArmorPower(AbstractCreature owner) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = 0;
        this.amount2 = 1;

        type = AbstractPower.PowerType.BUFF;
        isTurnBased = false;

        this.loadRegion("platedarmor");
        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount2 + DESCRIPTIONS[1];
    }

    @Override
    public void onRemove() {
        addToBot(new ApplyPowerAction(this.owner, this.owner, this));
    }

    @Override
    public int onAttackedToChangeDamage(DamageInfo info, int damageAmount) {
        this.amount += this.amount2;
        return damageAmount;
    }

    @Override
    public void atEndOfTurnPreEndTurnCards(boolean isPlayer) {
        if(this.amount > 0) {
            AbstractPower p = new PlatedArmorPower(this.owner, this.amount);
            this.addToBot(new ApplyPowerAction(this.owner, this.owner, p, p.amount));
            p.atEndOfTurnPreEndTurnCards(isPlayer);
        }
        this.amount = 0;
    }
}