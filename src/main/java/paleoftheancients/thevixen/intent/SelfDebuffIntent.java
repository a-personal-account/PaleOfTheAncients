package paleoftheancients.thevixen.intent;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import paleoftheancients.PaleMod;
import paleoftheancients.thevixen.TheVixenMod;
import paleoftheancients.thevixen.enums.VixenIntentEnum;

public class SelfDebuffIntent extends SunnyIntent {

    public static final String ID = PaleMod.makeID("selfdebuffintent");

    private static final UIStrings uiStrings;
    private static final String[] TEXT;


    public SelfDebuffIntent() {
        super(VixenIntentEnum.ATTACK_SELFDEBUFF, TEXT[0],
                TheVixenMod.getResourcePath("ui/intent/selfdebuff_L.png"),
                TheVixenMod.getResourcePath("ui/intent/selfdebuff.png"));
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
