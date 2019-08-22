package paleoftheancients.thevixen.powers;

import paleoftheancients.PaleMod;
import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.BufferPower;


public class WishPower extends AbstractTheVixenPower {
    public static final String POWER_ID = PaleMod.makeID("WishPower");
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;

    public static PowerType POWER_TYPE = PowerType.BUFF;
    public static final String IMG = "wish.png";

    private int blockedDamage;

    public WishPower(AbstractCreature owner) {
        super(IMG);
        this.owner = owner;
        this.isTurnBased = false;

        this.name = NAME;
        this.ID = POWER_ID;
        this.type = POWER_TYPE;

        this.amount = 0;
        this.priority = 99;

        updateDescription();
    }

    @Override
    public void atEndOfRound() {
        this.blockedDamage += this.owner.currentBlock;
        if(this.blockedDamage > 0) {
            AbstractDungeon.actionManager.addToBottom(new AddTemporaryHPAction(this.owner, this.owner, (this.blockedDamage + 1) / 2));
        }
        if(this.amount > 0) {
            AbstractDungeon.actionManager.addToBottom(new HealAction(this.owner, this.owner, (this.amount + 1) / 2));
        }
        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if(!this.owner.hasPower(BufferPower.POWER_ID)) {
            if (this.amount <= 0) {
                this.amount = 0;
                this.blockedDamage += info.output - damageAmount;
            }
        }
        return damageAmount;
    }

    @Override
    public int onLoseHp(int damageAmount) {
        if(!this.owner.hasPower(BufferPower.POWER_ID) && !this.owner.hasPower(SubstitutePower.POWER_ID)) {
            if (this.amount < 0) {
                this.amount = 0;
            }
            this.amount += damageAmount;
        }
        return damageAmount;
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    }
}
