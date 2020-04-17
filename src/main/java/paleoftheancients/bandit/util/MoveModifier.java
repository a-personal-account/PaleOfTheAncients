package paleoftheancients.bandit.util;

import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.cards.AbstractCard;
import paleoftheancients.PaleMod;
import paleoftheancients.bandit.powers.BoardBoundPower;

public class MoveModifier extends AbstractCardModifier {
    public static String ID = PaleMod.makeID(MoveModifier.class.getSimpleName());
    private int move;

    public MoveModifier(int move) {
        super();
        this.move = move;
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        return rawDescription + " NL " + "[#FF6666]" + BoardBoundPower.DESCRIPTIONS[0] + " " + move + "[]";
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new MoveModifier(move);
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }
}