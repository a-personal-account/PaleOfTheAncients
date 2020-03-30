package paleoftheancients.watcher.powers;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.watcher.MarkPower;
import paleoftheancients.PaleMod;

public class FakeMarkPower extends MarkPower {
    public static final String POWER_ID = PaleMod.makeID("FakeMarkPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public FakeMarkPower(AbstractCreature owner, int newAmount) {
        super(owner, newAmount);
        this.ID = POWER_ID;
    }

    @Override
    public void triggerMarks(AbstractCard card) {

    }

    @Override
    public void updateDescription() {
        this.description = powerStrings.DESCRIPTIONS[0] + this.amount + powerStrings.DESCRIPTIONS[1];
    }
}
