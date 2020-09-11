package paleoftheancients.reimu.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.stslib.powers.abstracts.TwoAmountPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.StrengthPower;
import paleoftheancients.PaleMod;
import paleoftheancients.helpers.AssetLoader;
import paleoftheancients.thevixen.actions.ApplyTempLoseStrengthPowerAction;


public class SpellcardResistancePower extends TwoAmountPower {
    public static final String POWER_ID = PaleMod.makeID("SpellcardResistancePower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private final int damagePerStrength;

    public SpellcardResistancePower(AbstractCreature owner) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;

        this.amount = 50;
        if(AbstractDungeon.ascensionLevel >= 4) {
            this.amount += 10;
        }
        if(AbstractDungeon.ascensionLevel >= 9) {
            this.amount += 10;
        }

        type = PowerType.BUFF;
        isTurnBased = false;
        this.canGoNegative = true;

        this.region128 = new TextureAtlas.AtlasRegion(AssetLoader.loadImage(PaleMod.assetPath("images/reimu/powers/SpellCard84.png")), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(AssetLoader.loadImage(PaleMod.assetPath("images/reimu/powers/SpellCard32.png")), 0, 0, 32, 32);

        this.damagePerStrength = 10;
        this.amount2 = this.damagePerStrength;

        updateDescription();
    }

    public float atDamageReceive(float damage, DamageInfo.DamageType type) {
        return damage / (100F / (100 - this.amount));
    }

    @Override
    public void atEndOfRound() {
        this.amount = this.amount - (AbstractDungeon.ascensionLevel >= 19 ? 10 : 20);
        addToBot(new ApplyPowerAction(this.owner, this.owner, new StrengthPower(this.owner, 3), 3));
        this.updateDescription();
    }


    public int onAttacked(DamageInfo info, int damageAmount) {
        if (damageAmount > 0) {
            int residue = damageAmount % this.damagePerStrength;
            int increaseBy = damageAmount / this.damagePerStrength;
            this.amount2 -= residue;
            if(this.amount2 <= 0) {
                increaseBy++;
                this.amount2 += this.damagePerStrength;
            }
            if(increaseBy > 0) {
                AbstractDungeon.actionManager.addToBottom(new ApplyTempLoseStrengthPowerAction(this.owner, this.owner, increaseBy));
            }
        }

        return damageAmount;
    }

    @Override
    public void updateDescription() {
        StringBuilder sb = new StringBuilder(DESCRIPTIONS[0]);
        if(this.amount >= 0) {
            sb.append(DESCRIPTIONS[1]).append(DESCRIPTIONS[3]).append(amount);
        } else {
            sb.append(DESCRIPTIONS[2]).append(DESCRIPTIONS[3]).append(-amount);
        }
        sb.append(DESCRIPTIONS[4]).append(this.damagePerStrength).append(DESCRIPTIONS[5]);
        description = sb.toString();
    }
}