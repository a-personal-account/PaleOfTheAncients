package paleoftheancients.thevixen.intent;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import paleoftheancients.PaleMod;
import paleoftheancients.thevixen.TheVixenMod;
import paleoftheancients.thevixen.enums.VixenIntentEnum;

public class FlameWheelIntent extends SunnyIntent {

    public static final String ID = PaleMod.makeID("flamewheel");

    private static final UIStrings uiStrings;
    private static final String[] TEXT;


    public FlameWheelIntent() {
        super(VixenIntentEnum.ATTACK_FLAMEWHEEL, TEXT[0],
                TheVixenMod.getResourcePath("ui/intent/flamewheel_L.png"),
                TheVixenMod.getResourcePath("ui/intent/flamewheel.png"));
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
