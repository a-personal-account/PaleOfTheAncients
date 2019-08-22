package paleoftheancients.bard.melodies;

import paleoftheancients.PaleMod;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.IntangiblePower;

public class DivineProtectionMelody extends AbstractMelody {
    public static final String ID = PaleMod.makeID("DivineProtection");

    public DivineProtectionMelody() {
        super(ID, AbstractCard.CardTarget.ALL_ENEMY);
        this.type = AbstractCard.CardType.SKILL;
        this.baseMagicNumber = this.magicNumber = 1;
    }

    protected CustomCard.RegionName getRegionName() {
        return new CustomCard.RegionName("colorless/skill/apparition");
    }

    public void play(AbstractCreature source) {
        this.addToBottom(new ApplyPowerAction(source, source, new IntangiblePower(source, this.magicNumber), this.magicNumber));
    }

    public AbstractMelody makeCopy() {
        return new DivineProtectionMelody();
    }
}
