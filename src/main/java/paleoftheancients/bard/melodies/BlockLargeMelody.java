package paleoftheancients.bard.melodies;

import paleoftheancients.PaleMod;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;

public class BlockLargeMelody extends AbstractMelody {
    public static final String ID = PaleMod.makeID("BlockLarge");

    public BlockLargeMelody() {
        super(ID, AbstractCard.CardTarget.ALL_ENEMY);
        this.type = AbstractCard.CardType.SKILL;
        this.baseMagicNumber = this.magicNumber = AbstractMelody.m(15);
    }

    protected CustomCard.RegionName getRegionName() {
        return new CustomCard.RegionName("bard/skill/guardPoint");
    }

    public void play(AbstractCreature source) {
        this.addToBottom(new GainBlockAction(source, source, this.magicNumber));
    }

    public AbstractMelody makeCopy() {
        return new BlockLargeMelody();
    }
}