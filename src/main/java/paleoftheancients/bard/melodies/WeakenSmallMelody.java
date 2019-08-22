package paleoftheancients.bard.melodies;

import paleoftheancients.PaleMod;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.WeakPower;

public class WeakenSmallMelody extends AbstractMelody {
    public static final String ID = PaleMod.makeID("WeakenSmall");

    public WeakenSmallMelody() {
        super(ID, AbstractCard.CardTarget.SELF);
        this.type = AbstractCard.CardType.SKILL;
        this.baseMagicNumber = this.magicNumber = AbstractMelody.m(1);
    }

    protected CustomCard.RegionName getRegionName() {
        return new CustomCard.RegionName("colorless/skill/blind");
    }

    public void play(AbstractCreature source) {
        this.addToBottom(new ApplyPowerAction(AbstractDungeon.player, source, new WeakPower(AbstractDungeon.player, this.magicNumber, true), this.magicNumber));
    }

    public AbstractMelody makeCopy() {
        return new WeakenSmallMelody();
    }
}
