package paleoftheancients.thevixen.powers;

import paleoftheancients.PaleMod;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ReactivePower;


public class ConfusionPower extends AbstractTheVixenPower {
    public static final String POWER_ID = PaleMod.makeID("ConfusionPower");
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;

    public static PowerType POWER_TYPE = PowerType.DEBUFF;
    public static final String IMG = "confusion.png";

    private boolean justApplied;

    public ConfusionPower(AbstractCreature owner) {
        this(owner, false);
    }
    public ConfusionPower(AbstractCreature owner, boolean fromMonster) {
        super(IMG);
        this.owner = owner;
        this.isTurnBased = false;

        this.justApplied = fromMonster;

        this.name = NAME;
        this.ID = POWER_ID;
        this.type = POWER_TYPE;

        updateDescription();
    }

    public static boolean useful(AbstractMonster m) {
        switch(m.intent) {
            case ATTACK:
            case ATTACK_BUFF:
            case ATTACK_DEBUFF:
            case ATTACK_DEFEND:
                return !m.hasPower(POWER_ID);

            default:
                return m.hasPower(ReactivePower.POWER_ID) && !m.hasPower(POWER_ID);
        }
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (info.type == DamageInfo.DamageType.THORNS || info.type == DamageInfo.DamageType.HP_LOSS || info.owner != this.owner) {
            return;
        }
        this.owner.damage(new DamageInfo(this.owner, (info.output + 1) / 2, DamageInfo.DamageType.THORNS));
    }

    @Override
    public void atEndOfRound() {
        if(justApplied) {
            justApplied = false;
        } else {
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
        }
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
