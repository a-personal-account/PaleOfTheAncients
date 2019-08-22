package paleoftheancients.bard.melodies;

import paleoftheancients.PaleMod;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.MetallicizePower;

public class DefenseUpSmallMelody extends AbstractMelody {
    public static final String ID = PaleMod.makeID("DefenseUpSmall");

    public DefenseUpSmallMelody() {
        super(ID, AbstractCard.CardTarget.ALL_ENEMY);
        this.baseMagicNumber = this.magicNumber = AbstractMelody.m(1);
    }

    protected CustomCard.RegionName getRegionName() {
        return new CustomCard.RegionName("green/power/footwork");
    }

    public void play(AbstractCreature source) {
        this.addToBottom(new ApplyPowerAction(source, source, new MetallicizePower(source, this.magicNumber), this.magicNumber));
    }

    public AbstractMelody makeCopy() {
        return new DefenseUpSmallMelody();
    }
}
