package paleoftheancients.bard.melodies;

import paleoftheancients.PaleMod;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.DrawReductionPower;

public class DrawMelody extends AbstractMelody {
    public static final String ID = PaleMod.makeID("Draw");

    public DrawMelody() {
        super(ID, AbstractCard.CardTarget.SELF);
        this.type = AbstractCard.CardType.SKILL;
        this.baseMagicNumber = this.magicNumber = 1;
    }

    protected CustomCard.RegionName getRegionName() {
        return new CustomCard.RegionName("blue/skill/skim");
    }

    public void play(AbstractCreature source) {
        this.addToBottom(new ApplyPowerAction(AbstractDungeon.player, source, new DrawReductionPower(AbstractDungeon.player, this.magicNumber), this.magicNumber));
    }

    public AbstractMelody makeCopy() {
        return new DrawMelody();
    }
}
