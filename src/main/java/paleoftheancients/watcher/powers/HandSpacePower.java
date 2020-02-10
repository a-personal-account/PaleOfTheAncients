package paleoftheancients.watcher.powers;

import basemod.BaseMod;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import paleoftheancients.PaleMod;

public class HandSpacePower extends AbstractPower {
    public static final String POWER_ID = PaleMod.makeID("HandSpacePower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public HandSpacePower(AbstractCreature owner, int newAmount) {
        this.name = powerStrings.NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = newAmount;
        this.updateDescription();
        this.loadRegion("draw");
        this.type = PowerType.BUFF;
    }

    @Override
    public void updateDescription() {
        this.description = powerStrings.DESCRIPTIONS[0] + this.amount + LocalizedStrings.PERIOD;
    }

    @Override
    public void onInitialApplication() {
        BaseMod.MAX_HAND_SIZE += this.amount;
    }

    @Override
    public void reducePower(int reduceAmount) {
        super.reducePower(reduceAmount);
        BaseMod.MAX_HAND_SIZE--;
    }

    @Override
    public void onVictory() {
        BaseMod.MAX_HAND_SIZE -= this.amount;
    }
    @Override
    public void onRemove() {
        onVictory();
    }
}
