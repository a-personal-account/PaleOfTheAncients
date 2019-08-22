package paleoftheancients.guardian.powers;

import paleoftheancients.PaleMod;
import paleoftheancients.guardian.monsters.Guardianest;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;


public class GuardianLinkedPower extends AbstractPower {
    public static final String POWER_ID = PaleMod.makeID("GuardianLinkedPower");
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;

    public static PowerType POWER_TYPE = PowerType.BUFF;

    public GuardianLinkedPower(AbstractCreature owner) {
        super();
        this.owner = owner;

        this.loadRegion("shackle");

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
    public void atEndOfRound() {
        if(this.owner instanceof Guardianest) {
            Guardianest g = (Guardianest)this.owner;
            if(g.partner != null && !g.isDeadOrEscaped()) {
                if (g.currentHealth > g.partner.currentHealth) {
                    g.currentHealth = g.partner.currentHealth;
                    g.healthBarUpdatedEvent();
                } else if (g.currentHealth < g.partner.currentHealth) {
                    g.partner.currentHealth = g.currentHealth;
                    g.partner.healthBarUpdatedEvent();
                }
            }
        }
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    }
}
