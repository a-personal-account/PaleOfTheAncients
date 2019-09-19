package paleoftheancients.thedefect.powers;

import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.OnReceivePowerPower;
import paleoftheancients.PaleMod;
import paleoftheancients.thedefect.monsters.orbs.AbstractBossOrb;
import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;


public class BossOrbPower extends AbstractPower implements InvisiblePower, OnReceivePowerPower {
    public static final String POWER_ID = PaleMod.makeID("BossOrbPower");
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;

    public static PowerType POWER_TYPE = PowerType.BUFF;

    public BossOrbPower(AbstractCreature owner) {
        this.owner = owner;
        this.isTurnBased = false;

        this.name = NAME;
        this.ID = POWER_ID;
        this.type = POWER_TYPE;

        this.loadRegion("mastery");

        updateDescription();
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        //If the owner is hit by a single target attack, replicate the effect on the Defect.
        if(card.type == AbstractCard.CardType.ATTACK && action.target == this.owner && (!(boolean)ReflectionHacks.getPrivate(card, AbstractCard.class, "isMultiDamage"))) {
            if(this.owner instanceof AbstractBossOrb) {
                card = card.makeStatEquivalentCopy();
                card.calculateCardDamage(((AbstractBossOrb)this.owner).owner);
                card.use(AbstractDungeon.player, ((AbstractBossOrb)this.owner).owner);
            }
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

    @Override
    public boolean onReceivePower(AbstractPower abstractPower, AbstractCreature abstractCreature, AbstractCreature abstractCreature1) {
        return false;
    }
}
