package paleoftheancients.thevixen.powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import paleoftheancients.PaleMod;
import paleoftheancients.thevixen.hooks.OnReceivePower;
import paleoftheancients.thevixen.monsters.TheVixenBoss;
import paleoftheancients.thevixen.vfx.DefiantFlameVFX;


public class DefiantPower extends AbstractTheVixenPower implements OnReceivePower {
    public static final String POWER_ID = PaleMod.makeID("DefiantPower");
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;

    public static PowerType POWER_TYPE = PowerType.BUFF;
    public static final String IMG = "defiant.png";

    private int triggered;
    public DefiantFlameVFX dfv;

    public DefiantPower(AbstractCreature owner, int amount) {
        super(IMG);
        this.owner = owner;
        this.amount = amount;
        this.isTurnBased = false;

        this.name = NAME;
        this.ID = POWER_ID;
        this.type = POWER_TYPE;

        updateDescription();

        this.triggered = 0;

        dfv = null;
    }

    @Override
    public void updateDescription() {
        this.description = (DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1]);
    }

    @Override
    public void atEndOfRound() {
        if(triggered > 0) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.owner, this.owner, new SunnyDayPower(this.owner, this.triggered), this.triggered));
            this.triggered = 0;
            if(this.dfv != null) {
                this.dfv.end();
                this.dfv = null;
            }
            AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
                @Override
                public void update() {
                    this.isDone = true;
                    if(((AbstractMonster) owner).nextMove == TheVixenBoss.SOLARBEAM_CONST) {
                        ((TheVixenBoss) owner).calcDamage(TheVixenBoss.SOLARBEAM_CONST, true);
                    }
                }
            });
        }
    }

    @Override
    public void onReceivePower(AbstractPower power) {
        if(power.type == PowerType.DEBUFF && power.ID != LoseStrengthPower.POWER_ID && power.ID != StrengthPower.POWER_ID) {
            if(this.triggered < this.amount) {
                this.triggered++;
                if(this.dfv == null) {
                    this.dfv = new DefiantFlameVFX(this.owner);
                    AbstractDungeon.effectList.add(this.dfv);
                } else {
                    this.dfv.increaseAmount();
                }
                this.flash();
            }
        }
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    }
}
