package paleoftheancients.bard.melodies;

import paleoftheancients.PaleMod;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.ArtifactPower;

public class ArtifactMelody extends AbstractMelody {
    public static final String ID = PaleMod.makeID("Artifact");

    public ArtifactMelody() {
        super(ID, AbstractCard.CardTarget.SELF);
        this.type = AbstractCard.CardType.SKILL;
        this.baseMagicNumber = this.magicNumber = AbstractMelody.m(1);
    }

    protected CustomCard.RegionName getRegionName() {
        return new CustomCard.RegionName("colorless/skill/panacea");
    }

    public void play(AbstractCreature target) {
        this.addToBottom(new ApplyPowerAction(target, target, new ArtifactPower(target, this.magicNumber), this.magicNumber));
    }

    public AbstractMelody makeCopy() {
        return new ArtifactMelody();
    }
}
