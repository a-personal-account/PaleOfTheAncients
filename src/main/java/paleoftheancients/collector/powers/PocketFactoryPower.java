package paleoftheancients.collector.powers;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.SpawnMonsterAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import paleoftheancients.PaleMod;
import paleoftheancients.collector.monsters.BronzeOrbThing;


public class PocketFactoryPower extends AbstractPower {
    public static final String POWER_ID = PaleMod.makeID("PocketFactoryPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public PocketFactoryPower(AbstractCreature owner, int amount) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;

        type = PowerType.BUFF;
        isTurnBased = false;

        this.amount = amount;

        this.loadRegion("loop");

        updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }

    @Override
    public void atEndOfRound() {
        int bronzeorbs = 0;
        for(final AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if(m.id == BronzeOrbThing.ID && !m.isDeadOrEscaped()) {
                bronzeorbs++;
            }
        }
        if(bronzeorbs < this.amount && !this.owner.isDead) {
            AbstractMonster bronzeorb = new BronzeOrbThing(0, 0, MathUtils.random(0, 1));
            do {
                boolean overlapping = false;
                bronzeorb.drawX = Settings.WIDTH * 0.75F + MathUtils.random(-500F, -100F) * Settings.scale;
                bronzeorb.drawY = AbstractDungeon.floorY + (300 + MathUtils.random(-60F, 30F)) * Settings.scale;
                bronzeorb.hb.move(bronzeorb.drawX, bronzeorb.drawY + bronzeorb.hb.height / 2);

                for(final AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
                    if(!mo.isDeadOrEscaped() && mo.hb.intersects(bronzeorb.hb)) {
                        overlapping = true;
                        break;
                    }
                }

                if(!overlapping) {
                    break;
                }
            } while(true);
            AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(bronzeorb, true));
        }
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type) {
        if(type == DamageInfo.DamageType.NORMAL) {
            damage = damage * 2F / 3F;
        }
        return damage;
    }
}