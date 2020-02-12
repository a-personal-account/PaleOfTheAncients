package paleoftheancients.reimu.powers;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import paleoftheancients.PaleMod;
import paleoftheancients.reimu.monsters.YinYangOrb;

public class BlockUponDeathPower extends AbstractPower {
    public static final String POWER_ID = PaleMod.makeID("BlockUponDeathPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public static int BLOCK = 10;

    public BlockUponDeathPower(AbstractCreature owner, int amount) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = amount;

        type = PowerType.BUFF;
        isTurnBased = false;

        this.loadRegion("deva");
        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];

        description += (AbstractDungeon.ascensionLevel >= 19 ? 2 : 1);
        description += DESCRIPTIONS[2];
        description += DESCRIPTIONS[AbstractDungeon.ascensionLevel >= 19 ? 4 : 3];
        description += DESCRIPTIONS[5];
    }

    public void onTrigger() {
        this.addToBot(new GainBlockAction(AbstractDungeon.player, this.owner, this.amount));
        ((YinYangOrb) this.owner).master.rui.getBombFragment();
        if(AbstractDungeon.ascensionLevel >= 19) {
            ((YinYangOrb) this.owner).master.rui.getBombFragment();
        }
    }
}