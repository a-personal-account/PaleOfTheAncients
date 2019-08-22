package paleoftheancients.thevixen.powers;

import paleoftheancients.PaleMod;
import paleoftheancients.thevixen.monsters.TheVixenBoss;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.BufferPower;


public class EndurePower extends AbstractTheVixenPower {
    public static final String POWER_ID = PaleMod.makeID("EndurePower");
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;

    public static PowerType POWER_TYPE = PowerType.BUFF;
    public static final String IMG = "endure.png";

    private static int sunnycount = 10;

    public EndurePower(AbstractCreature owner) {
        super(IMG);
        this.owner = owner;
        this.isTurnBased = false;

        this.name = NAME;
        this.ID = POWER_ID;
        this.type = POWER_TYPE;

        updateDescription();
        this.priority = 98;
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + sunnycount + DESCRIPTIONS[1];
    }


    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if(!this.owner.hasPower(BufferPower.POWER_ID) && !this.owner.hasPower(SubstitutePower.POWER_ID) && damageAmount >= this.owner.currentHealth) {
            damageAmount = this.owner.currentHealth - 1;

            AbstractPower sun = this.owner.getPower(SunnyDayPower.POWER_ID);
            if(sun != null) {
                AbstractDungeon.actionManager.addToBottom(new HealAction(this.owner, this.owner, sun.amount * sunnycount));
            }

            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, info.owner, this));
            if(this.owner instanceof TheVixenBoss) {
                ((TheVixenBoss)this.owner).oneHP = true;
            }
        }
        return damageAmount;
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    }
}
