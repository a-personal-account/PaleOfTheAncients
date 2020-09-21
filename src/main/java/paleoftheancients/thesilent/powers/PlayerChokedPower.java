package paleoftheancients.thesilent.powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.BeatOfDeathPower;
import com.megacrit.cardcrawl.powers.ChokePower;
import paleoftheancients.PaleMod;


public class PlayerChokedPower extends BeatOfDeathPower {
    public static final String POWER_ID = PaleMod.makeID("PlayerChokedPower");
    private static final PowerStrings powerStrings;
    public static final String NAME;

    public static PowerType POWER_TYPE = PowerType.DEBUFF;

    public PlayerChokedPower(AbstractCreature owner, int amount) {
        super(owner, amount);
        this.loadRegion("choke");

        this.name = ChokePower.NAME;
        this.ID = POWER_ID;
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerStrings.NAME;
    }
}
