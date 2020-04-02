package paleoftheancients.thevixen.intent;

import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import paleoftheancients.RazIntent.CustomIntent;
import paleoftheancients.helpers.AssetLoader;
import paleoftheancients.vfx.FlashingIntentVFX;
import paleoftheancients.thevixen.TheVixenMod;
import paleoftheancients.thevixen.powers.SunnyDayPower;

import java.util.ArrayList;

public abstract class SunnyIntent extends CustomIntent {
    public SunnyIntent(AbstractMonster.Intent intent, String header, String display, String tip) {
        super(intent, header, display, tip);
    }

    @Override
    public float updateVFXInInterval(AbstractMonster mo, ArrayList<AbstractGameEffect> intentVfx) {
        if(mo.hasPower(SunnyDayPower.POWER_ID)) {
            AbstractGameEffect sb = new FlashingIntentVFX(AssetLoader.loadImage(TheVixenMod.getResourcePath("vfx/sunnyday.png")), mo.intentHb.cX, mo.intentHb.cY);

            intentVfx.add(sb);
        }
        return 0.2F;
    }
}
