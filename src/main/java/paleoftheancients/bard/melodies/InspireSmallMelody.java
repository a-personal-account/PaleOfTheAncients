package paleoftheancients.bard.melodies;

import paleoftheancients.PaleMod;
import paleoftheancients.bard.powers.InspirationPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class InspireSmallMelody extends AbstractMelody {
    public static final String ID = PaleMod.makeID("InspireSmall");

    public InspireSmallMelody() {
        super(ID, AbstractCard.CardTarget.ALL_ENEMY);
        this.baseMagicNumber = this.magicNumber = 2;
    }

    public void play(AbstractCreature source) {
        this.addToBottom(new ApplyPowerAction(source, source, new InspirationPower(AbstractDungeon.player, this.magicNumber, 50), this.magicNumber));
    }

    public AbstractMelody makeCopy() {
        return new InspireSmallMelody();
    }
}
