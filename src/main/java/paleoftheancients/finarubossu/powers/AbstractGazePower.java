package paleoftheancients.finarubossu.powers;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import paleoftheancients.NRPower;

import java.util.ArrayList;

public abstract class AbstractGazePower extends NRPower {
    public static PowerType POWER_TYPE = PowerType.BUFF;
    public static final String IMG = "neoweye.png";

    protected int onCardNumber;

    protected Color color;

    public AbstractGazePower(AbstractCreature owner, Color color, int onCardNumber) {
        super(IMG);
        this.color = color;
        this.type = POWER_TYPE;
        this.owner = owner;
        this.priority = onCardNumber - 5;
        this.onCardNumber = onCardNumber;
        this.amount = 0;

    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if(!card.purgeOnUse && (!this.owner.halfDead || AbstractDungeon.ascensionLevel >= 4)) {
            if(this.amount < this.onCardNumber) {
                if (++this.amount == this.onCardNumber) {
                    this.flashWithoutSound();
                    this.trigger(card);
                }
            }
        }
    }

    abstract void trigger(AbstractCard card);

    @Override
    public void atEndOfRound() {
        this.amount = 0;
    }

    @Override
    public void flash() {
        super.flash();
        ArrayList<AbstractGameEffect> stuff = (ArrayList) ReflectionHacks.getPrivate(this, AbstractPower.class, "effect");
        this.allocateColor((Color) ReflectionHacks.getPrivate(stuff.get(stuff.size() - 1), AbstractGameEffect.class, "color"));
    }
    @Override
    public void flashWithoutSound() {
        super.flashWithoutSound();
        ArrayList<AbstractGameEffect> stuff = (ArrayList) ReflectionHacks.getPrivate(this, AbstractPower.class, "effect");
        this.allocateColor((Color) ReflectionHacks.getPrivate(stuff.get(stuff.size() - 1), AbstractGameEffect.class, "color"));
    }
    private void allocateColor(Color c) {
        c.r = color.r;
        c.g = color.g;
        c.b = color.b;
    }

    @Override
    public void renderIcons(SpriteBatch sb, float x, float y, Color c) {
        super.renderIcons(sb, x, y, color);
    }
}
