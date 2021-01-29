package paleoftheancients.timeeater.intent;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import paleoftheancients.PaleMod;
import paleoftheancients.RazIntent.CustomIntent;
import paleoftheancients.timeeater.monsters.TimeEaterer;

public class SingularApocalypseIntent extends CustomIntent {
    public static final String ID = PaleMod.makeID("SingularApocalypseIntent");
    private static final UIStrings uiStrings;
    public static final String[] TEXT;

    public SingularApocalypseIntent() {
        super(TimeEatererIntentEnums.SingularApocalypseIntent, TimeEaterer.MOVES[TimeEaterer.SINGULARAPOCALYPSE],
                PaleMod.assetPath("images/ui/intent/singularapocalypse_L.png"),
                PaleMod.assetPath("images/ui/intent/singularapocalypse.png"));
    }

    @Override
    public String description(AbstractMonster mo) {
        return TEXT[0] + mo.getIntentDmg() + TEXT[1] + LocalizedStrings.PERIOD + TEXT[2];
    }

    @Override
    public String damageNumber(AbstractMonster mo) {
        return Integer.toString(mo.getIntentDmg());
    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString(ID);
        TEXT = uiStrings.TEXT;
    }
}
