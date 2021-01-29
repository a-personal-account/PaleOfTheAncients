package paleoftheancients.timeeater.intent;

import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import paleoftheancients.PaleMod;
import paleoftheancients.RazIntent.CustomIntent;
import paleoftheancients.timeeater.monsters.TimeEaterer;

public class DualApocalypseIntent extends CustomIntent {

    public DualApocalypseIntent() {
        super(TimeEatererIntentEnums.DualApocalypseIntent, TimeEaterer.MOVES[TimeEaterer.DUALAPOCALYPSE],
                PaleMod.assetPath("images/ui/intent/dualapocalypse_L.png"),
                PaleMod.assetPath("images/ui/intent/dualapocalypse.png"));
    }

    @Override
    public String description(AbstractMonster mo) {
        return SingularApocalypseIntent.TEXT[0] + mo.getIntentDmg() + SingularApocalypseIntent.TEXT[1] + SingularApocalypseIntent.TEXT[3] + ReflectionHacks.getPrivate(mo, AbstractMonster.class, "intentMultiAmt") + SingularApocalypseIntent.TEXT[4] + SingularApocalypseIntent.TEXT[2];
    }

    @Override
    public String damageNumber(AbstractMonster mo) {
        return mo.getIntentDmg() + "x" + ReflectionHacks.getPrivate(mo, AbstractMonster.class, "intentMultiAmt");
    }
}
