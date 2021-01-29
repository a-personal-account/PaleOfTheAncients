package paleoftheancients.timeeater.powers;

import com.evacipated.cardcrawl.mod.stslib.powers.abstracts.TwoAmountPower;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.TimeWarpPower;
import paleoftheancients.PaleMod;
import paleoftheancients.timeeater.monsters.TimeEaterer;

public class TimeWarpIntentPower extends TwoAmountPower {
    public static final String POWER_ID = PaleMod.makeID("TimeWarpIntentPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public TimeWarpIntentPower(AbstractCreature owner) {
        this.name = TimeWarpPower.NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = 0;
        this.amount2 = 5;
        if(AbstractDungeon.ascensionLevel >= 9) {
            this.amount2--;
        }
        if(AbstractDungeon.ascensionLevel >= 19) {
            this.amount2--;
        }
        this.updateDescription();
        this.loadRegion("time");
        this.type = PowerType.BUFF;
    }

    @Override
    public void updateDescription() {
        this.description = powerStrings.DESCRIPTIONS[0] + this.amount2 + powerStrings.DESCRIPTIONS[1];
    }

    @Override
    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
        this.flashWithoutSound();
        int prevAmount = this.amount;
        if(!card.freeToPlayOnce) {
            if(card.cost > -1) {
                this.amount += card.costForTurn;
            } else {
                this.amount += card.energyOnUse;
            }
        }
        if(prevAmount >= this.amount) {
            this.amount = ++prevAmount;
        }
        if (this.amount >= this.amount2) {
            this.amount -= this.amount2;
            ((TimeEaterer)this.owner).newIntent();
        }

        this.updateDescription();
    }
}
