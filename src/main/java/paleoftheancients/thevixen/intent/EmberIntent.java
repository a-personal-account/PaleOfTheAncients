package paleoftheancients.thevixen.intent;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import paleoftheancients.PaleMod;
import paleoftheancients.thevixen.TheVixenMod;
import paleoftheancients.thevixen.enums.VixenIntentEnum;

public class EmberIntent extends SunnyIntent {

    public static final String ID = PaleMod.makeID("sunnyattack");

    private static final UIStrings uiStrings;
    private static final String[] TEXT;


    public EmberIntent() {
        super(VixenIntentEnum.ATTACK_SUNNY, TEXT[0],
                TheVixenMod.getResourcePath("ui/intent/sunnyattack_L.png"),
                TheVixenMod.getResourcePath("ui/intent/sunnyattack.png"));
    }

    @Override
    public String description(AbstractMonster mo) {
        String result = TEXT[1];
        result += mo.getIntentDmg();
        result += TEXT[2];

        return result;
    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString(ID);
        TEXT = uiStrings.TEXT;
    }
}
