package paleoftheancients.thevixen.powers;

import paleoftheancients.PaleMod;
import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.TextAboveCreatureEffect;


public class BlazePower extends AbstractTheVixenPower {
    public static final String POWER_ID = PaleMod.makeID("BlazePower");
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;

    public static PowerType POWER_TYPE = PowerType.BUFF;
    public static final String IMG = "blaze.png";

    public BlazePower(AbstractCreature owner, int amount) {
        super(IMG);
        this.owner = owner;
        this.amount = amount;
        this.isTurnBased = false;

        this.name = NAME;
        this.ID = POWER_ID;
        this.type = POWER_TYPE;

        updateDescription();

        this.priority = 6;
        this.setGreenColor(this.owner.currentHealth);
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + (this.owner.maxHealth / 2) + DESCRIPTIONS[1] + this.amount + DESCRIPTIONS[2];
    }

    @Override
    public int onLoseHp(int damageAmount) {
        if(this.owner.currentHealth > this.owner.maxHealth / 2 && this.owner.currentHealth - damageAmount <= this.owner.maxHealth / 2) {
            AbstractDungeon.effectList.add(new TextAboveCreatureEffect(this.owner.hb.cX - this.owner.animX, this.owner.hb.cY + this.owner.hb.height / 2.0F, DESCRIPTIONS[3], Color.RED.cpy()));
            this.flash();
            this.setGreenColor(this.owner.currentHealth - damageAmount);
        }
        return damageAmount;
    }
    @Override
    public int onHeal(int healAmount) {
        if(this.owner.currentHealth <= this.owner.maxHealth / 2 && this.owner.currentHealth + healAmount > this.owner.maxHealth / 2) {
            this.setGreenColor(this.owner.currentHealth + healAmount);
        }
        return healAmount;
    }

    @Override
    public void renderIcons(SpriteBatch sb, float x, float y, Color c) {
        if(this.owner.currentHealth > this.owner.maxHealth / 2) {
            c = Color.DARK_GRAY.cpy();
        }
        super.renderIcons(sb, x, y, c);
    }

    private void setGreenColor(int hp) {
        Color c;
        if(hp > this.owner.maxHealth / 2) {
            c = Color.DARK_GRAY.cpy();
        } else {
            c = new Color(0.0F, 1.0F, 0.0F, 1.0F);
        }
        ReflectionHacks.setPrivate(this, AbstractPower.class, "greenColor", c);
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    }
}
