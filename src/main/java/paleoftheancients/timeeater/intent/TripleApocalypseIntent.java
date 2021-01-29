package paleoftheancients.timeeater.intent;

import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import paleoftheancients.PaleMod;
import paleoftheancients.RazIntent.CustomIntent;
import paleoftheancients.timeeater.monsters.TimeEaterer;

public class TripleApocalypseIntent extends CustomIntent {

    public TripleApocalypseIntent() {
        super(TimeEatererIntentEnums.TripleApocalypseIntent, TimeEaterer.MOVES[TimeEaterer.TRIPLEAPOCALYPSE],
                PaleMod.assetPath("images/ui/intent/tripleapocalypse_L.png"),
                PaleMod.assetPath("images/ui/intent/tripleapocalypse.png"));
    }

    @Override
    public String description(AbstractMonster mo) {
        return SingularApocalypseIntent.TEXT[0] + mo.getIntentDmg() + SingularApocalypseIntent.TEXT[1] + SingularApocalypseIntent.TEXT[3] + ReflectionHacks.getPrivate(mo, AbstractMonster.class, "intentMultiAmt") + SingularApocalypseIntent.TEXT[5];
    }

    @Override
    public String damageNumber(AbstractMonster mo) {
        return mo.getIntentDmg() + "x" + ReflectionHacks.getPrivate(mo, AbstractMonster.class, "intentMultiAmt");
    }
}
