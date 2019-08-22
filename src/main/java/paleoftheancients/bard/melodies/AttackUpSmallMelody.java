package paleoftheancients.bard.melodies;

import paleoftheancients.PaleMod;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class AttackUpSmallMelody extends AbstractMelody {
    public static final String ID = PaleMod.makeID("AttackUpSmall");

    public AttackUpSmallMelody() {
        super(ID, AbstractCard.CardTarget.ALL_ENEMY);
        this.baseMagicNumber = this.magicNumber = AbstractMelody.m(1);
    }

    protected CustomCard.RegionName getRegionName() {
        return new CustomCard.RegionName("red/power/inflam");
    }

    public void play(AbstractCreature source) {
        this.addToBottom(new ApplyPowerAction(source, source, new StrengthPower(source, this.magicNumber), this.magicNumber));
    }

    public AbstractMelody makeCopy() {
        return new AttackUpSmallMelody();
    }
}
