package paleoftheancients.ironcluck.powers;

import paleoftheancients.PaleMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ConstrictedPower;

public class ChickenBurnPower extends AbstractPower {
    public static final String POWER_ID = PaleMod.makeID("BurnPower");

    public ChickenBurnPower(AbstractCreature owner, int amount) {
        super();
        this.owner = owner;
        this.amount = amount;

        this.name = PaleMod.getCardName(Burn.class);
        this.ID = POWER_ID;
        this.type = PowerType.DEBUFF;

        this.loadRegion("combust");

        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        AbstractDungeon.actionManager.addToBottom(new DamageAction(this.owner, new DamageInfo(this.owner, this.amount, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.NONE));
    }

    @Override
    public void updateDescription() {
        this.description = ConstrictedPower.DESCRIPTIONS[0] + this.amount +  ConstrictedPower.DESCRIPTIONS[1];
    }
}
