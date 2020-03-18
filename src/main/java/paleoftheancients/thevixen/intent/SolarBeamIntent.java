package paleoftheancients.thevixen.intent;

import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import paleoftheancients.PaleMod;
import paleoftheancients.thevixen.TheVixenMod;
import paleoftheancients.thevixen.enums.VixenIntentEnum;

import java.util.ArrayList;

public class SolarBeamIntent extends SunnyIntent {

    public static final String ID = PaleMod.makeID("sunnyattackbig");

    private static final UIStrings uiStrings;
    private static final String[] TEXT;


    public SolarBeamIntent() {
        super(VixenIntentEnum.ATTACK_SOLARBEAM, TEXT[0],
                TheVixenMod.getResourcePath("ui/intent/sunnyattackbig_L.png"),
                TheVixenMod.getResourcePath("ui/intent/sunnyattackbig.png"));
    }

    @Override
    public String description(AbstractMonster mo) {
        String result = TEXT[1];
        result += mo.getIntentDmg();
        result += TEXT[2];

        return result;
    }

    @Override
    public float updateVFXInInterval(AbstractMonster mo, ArrayList<AbstractGameEffect> intentVfx) {
        int count = (int) ReflectionHacks.getPrivate(mo, AbstractMonster.class, "intentMultiAmt");
        if (count > 1) {
            super.updateVFXInInterval(mo, intentVfx);
            return 1F / count;
        }
        return 1.0F;
    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString(ID);
        TEXT = uiStrings.TEXT;
    }
}
