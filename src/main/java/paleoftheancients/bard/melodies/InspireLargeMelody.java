package paleoftheancients.bard.melodies;

import paleoftheancients.PaleMod;
import paleoftheancients.bard.powers.InspirationPower;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class InspireLargeMelody extends AbstractMelody {
    public static final String ID = PaleMod.makeID("InspireLarge");

    public InspireLargeMelody() {
        super(ID, AbstractCard.CardTarget.ALL_ENEMY);
        this.baseMagicNumber = this.magicNumber = 2;
    }

    protected CustomCard.RegionName getRegionName() {
        return new CustomCard.RegionName("bard/power/splendidForm");
    }

    public void play(AbstractCreature source) {
        this.addToBottom(new ApplyPowerAction(source, source, new InspirationPower(source, this.magicNumber, 100), this.magicNumber));
    }

    public AbstractMelody makeCopy() {
        return new InspireLargeMelody();
    }
}
