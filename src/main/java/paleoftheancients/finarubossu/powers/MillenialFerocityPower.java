package paleoftheancients.finarubossu.powers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import paleoftheancients.PaleMod;
import paleoftheancients.thevixen.actions.ApplyTempGainStrengthPowerAction;

public class MillenialFerocityPower extends AbstractPower {
    public static final String POWER_ID = PaleMod.makeID("MillenialFerocityPower");
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;

    private int damagePerStrength;

    public MillenialFerocityPower(AbstractCreature owner) {
        super();

        this.ID = POWER_ID;
        this.name = NAME;
        this.isTurnBased = false;

        this.owner = owner;
        if(AbstractDungeon.ascensionLevel >= 19) {
            this.damagePerStrength = 5;
        } else if(AbstractDungeon.ascensionLevel >= 9) {
            this.damagePerStrength = 8;
        } else {
            this.damagePerStrength = 10;
        }
        this.amount = damagePerStrength;

        this.loadRegion("phantasmal");
        this.updateDescription();
    }

    public int onAttacked(DamageInfo info, int damageAmount) {
        if (damageAmount > 0) {
            int residue = damageAmount % this.damagePerStrength;
            int increaseBy = damageAmount / this.damagePerStrength;
            this.amount -= residue;
            if(this.amount <= 0) {
                increaseBy++;
                this.amount += this.damagePerStrength;
            }
            if(increaseBy > 0) {
                AbstractDungeon.actionManager.addToBottom(new ApplyTempGainStrengthPowerAction(this.owner, this.owner, increaseBy));
            }
        }

        return damageAmount;
    }

    @Override
    public void atStartOfTurn() {
        this.amount = this.damagePerStrength;
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.damagePerStrength + DESCRIPTIONS[1];
    }

    @Override
    public void renderIcons(SpriteBatch sb, float x, float y, Color c) {
        super.renderIcons(sb, x, y, Color.BLACK);
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    }
}
