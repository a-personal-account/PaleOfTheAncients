package paleoftheancients.reimu.powers;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import paleoftheancients.PaleMod;

public class ShotTypeNeedlePower extends ShotTypePower {
    public static final String POWER_ID = PaleMod.makeID("ShotTypeNeedlePower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public ShotTypeNeedlePower(AbstractCreature owner) {
        super(owner, POWER_ID, NAME);

        if(owner != null) {
            this.loadRegion("doubleDamage");
        }

        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type) {
        if(type == DamageInfo.DamageType.NORMAL) {
            damage = damage * 2F / 3F;
        }
        return damage;
    }

    @Override
    public ShotTypePower makeCopy(AbstractMonster mo) {
        return new ShotTypeNeedlePower(mo);
    }
}