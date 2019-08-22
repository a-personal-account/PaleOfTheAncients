package paleoftheancients.thevixen.intent;

import paleoftheancients.PaleMod;
import paleoftheancients.RazIntent.CustomIntent;
import paleoftheancients.thevixen.TheVixenMod;
import paleoftheancients.thevixen.enums.VixenIntentEnum;
import paleoftheancients.thevixen.vfx.ScytheParticleEffect;
import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

import java.util.ArrayList;

public class FacadeIntent extends CustomIntent {

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

    @Override
    public float updateVFXInInterval(AbstractMonster mo, ArrayList<AbstractGameEffect> intentVfx) {
        int count = (int)ReflectionHacks.getPrivate(mo, AbstractMonster.class, "intentMultiAmt");
        if(count > 1) {
            AbstractGameEffect sb = new ScytheParticleEffect(mo.intentHb.cX, mo.intentHb.cY);

            intentVfx.add(sb);

            return 1.0F / count;
        }
        return 0.2F;
    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString(ID);
        TEXT = uiStrings.TEXT;
    }
}
