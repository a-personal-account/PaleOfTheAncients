package paleoftheancients.thevixen.intent;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import paleoftheancients.PaleMod;
import paleoftheancients.helpers.FlashingIntent;
import paleoftheancients.thevixen.TheVixenMod;
import paleoftheancients.thevixen.enums.VixenIntentEnum;

public class FacadeIntent extends FlashingIntent {

    public static final String ID = PaleMod.makeID("facadeintent");

    private static final UIStrings uiStrings;
    private static final String[] TEXT;


    public FacadeIntent() {
        super(VixenIntentEnum.ATTACK_FACADE, TEXT[0],
                TheVixenMod.getResourcePath("ui/intent/facadeintent_L.png"),
                TheVixenMod.getResourcePath("ui/intent/facadeintent.png"));
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
