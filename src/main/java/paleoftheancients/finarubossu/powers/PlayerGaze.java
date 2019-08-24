package paleoftheancients.finarubossu.powers;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.MarkOfTheBloom;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import paleoftheancients.NRPower;
import paleoftheancients.PaleMod;
import paleoftheancients.finarubossu.vfx.DamageCurvy;
import paleoftheancients.finarubossu.vfx.DamageLine;

import java.util.ArrayList;

public class PlayerGaze extends NRPower {
    public static final String POWER_ID = PaleMod.makeID("PlayerGaze");
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;

    private AbstractCreature source;
    private Color color;
    private AbstractRelic markofthebloom;

    public PlayerGaze(AbstractCreature owner, AbstractCreature source) {
        super("neoweye.png");
        this.ID = POWER_ID;
        this.name = NAME;
        this.isTurnBased = false;

        this.owner = owner;
        this.source = source;
        this.amount = 0;

        this.color = Color.PURPLE.cpy();

        markofthebloom = AbstractDungeon.player.getRelic(MarkOfTheBloom.ID);

        this.updateDescription();
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        this.flash();
        if(this.amount > 0) {
            for (int i = 0; i < 20; i++) {
                if (MathUtils.randomBoolean()) {
                    AbstractDungeon.effectList.add(new DamageCurvy(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, Color.PURPLE));
                } else {
                    AbstractDungeon.effectList.add(new DamageLine(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, Color.PURPLE, MathUtils.random((float) Math.PI * 2)));
                }
            }
            AbstractDungeon.actionManager.addToBottom(new DamageAction(this.owner, new DamageInfo(this.source, this.amount, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.NONE));
        }
    }

    @Override
    public void updateDescription() {
        if(markofthebloom == null) {
            this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];

            if (AbstractDungeon.ascensionLevel < 9) {
                this.description += DESCRIPTIONS[2];
            }

            this.description += DESCRIPTIONS[3];
        } else {
            this.description = DESCRIPTIONS[4];
            if (AbstractDungeon.ascensionLevel < 9) {
                this.description += DESCRIPTIONS[5];
            }
        }
    }

    @Override
    public void onRemove() {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.owner, this.owner, this));
        this.amount = 0;
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster m) {
        if(!card.purgeOnUse) {
            if(markofthebloom == null) {
                this.amount++;
            }
            this.flashWithoutSound();
            this.updateDescription();
            if (AbstractDungeon.ascensionLevel < 9) {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.owner, this.owner, new StrengthPower(this.owner, 1), 1));
            }
        }
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

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    }
}
