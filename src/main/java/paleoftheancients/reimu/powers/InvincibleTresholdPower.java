package paleoftheancients.reimu.powers;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ChangeStateAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.InvinciblePower;
import paleoftheancients.PaleMod;
import paleoftheancients.helpers.AssetLoader;
import paleoftheancients.reimu.monsters.Reimu;


public class InvincibleTresholdPower extends InvinciblePower {
    public static final String POWER_ID = PaleMod.makeID("InvincibleTresholdPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public InvincibleTresholdPower(AbstractCreature owner) {
        super(owner, owner.maxHealth * 2 / 3);
        name = NAME;
        ID = POWER_ID;
    }

    @Override
    public void atStartOfTurn() {}

    @Override
    public void atEndOfRound() {
        ReflectionHacks.setPrivate(this, InvinciblePower.class, "maxAmt", this.owner.maxHealth * 2 / 3 - (this.owner.maxHealth - this.owner.currentHealth));
        super.atStartOfTurn();
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }
}