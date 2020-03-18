package paleoftheancients.thevixen.intent;

import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import paleoftheancients.PaleMod;
import paleoftheancients.thevixen.TheVixenMod;
import paleoftheancients.thevixen.enums.VixenIntentEnum;

public class FireSpinIntent extends SunnyIntent {

    public static final String ID = PaleMod.makeID("firespin");

    private static final UIStrings uiStrings;
    private static final String[] TEXT;


    public FireSpinIntent() {
        super(VixenIntentEnum.ATTACK_FIRESPIN, TEXT[0],
                TheVixenMod.getResourcePath("ui/intent/firespin_L.png"),
                TheVixenMod.getResourcePath("ui/intent/firespin.png"));
    }

    @Override
    public String description(AbstractMonster mo) {
        String result = TEXT[1];
        result += mo.getIntentDmg();
        result += TEXT[2];
        result += ReflectionHacks.getPrivate(mo, AbstractMonster.class, "intentMultiAmt");
        result += TEXT[3];

        return result;
    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString(ID);
        TEXT = uiStrings.TEXT;
    }
}
