package paleoftheancients.thevixen.powers;

import paleoftheancients.PaleMod;
import paleoftheancients.thevixen.monsters.TheVixenBoss;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;


public class BossFacadePower extends AbstractTheVixenPower implements InvisiblePower {
    public static final String POWER_ID = PaleMod.makeID("BossFacadePower");
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;

    public static PowerType POWER_TYPE = PowerType.BUFF;
    public static final String IMG = "empty.png";

    public BossFacadePower(AbstractCreature owner) {
        super(IMG);
        this.owner = owner;
        this.isTurnBased = true;

        this.name = NAME;
        this.ID = POWER_ID;
        this.type = POWER_TYPE;

        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }

    @Override
    public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
        if(power.type == PowerType.DEBUFF && !target.hasPower(power.ID)) {
            calcFacade();
        }
    }
    @Override
    public void atStartOfTurn() {
        calcFacade();
    }

    private void calcFacade() {
        for (final AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if(mo instanceof TheVixenBoss) {
                ((TheVixenBoss)mo).calcFacade();
                break;
            }
        }
    }


    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    }
}
