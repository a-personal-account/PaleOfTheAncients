package paleoftheancients.thevixen.intent;

import paleoftheancients.PaleMod;
import paleoftheancients.RazIntent.CustomIntent;
import paleoftheancients.thevixen.TheVixenMod;
import paleoftheancients.thevixen.enums.VixenIntentEnum;
import paleoftheancients.thevixen.powers.SunnyDayPower;
import paleoftheancients.thevixen.vfx.SunParticleEffect;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

import java.util.ArrayList;

public class SolarBeamIntent extends CustomIntent {

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
        if(mo.hasPower(SunnyDayPower.POWER_ID)) {
            AbstractGameEffect sb = new SunParticleEffect(mo.intentHb.cX, mo.intentHb.cY);

            intentVfx.add(sb);
        }
        return 0.2F;
    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString(ID);
        TEXT = uiStrings.TEXT;
    }
}
