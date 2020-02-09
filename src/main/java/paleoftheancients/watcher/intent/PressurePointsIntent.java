package paleoftheancients.watcher.intent;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.DebuffParticleEffect;
import paleoftheancients.PaleMod;
import paleoftheancients.RazIntent.CustomIntent;
import paleoftheancients.watcher.monsters.TheWatcher;

import java.util.ArrayList;

public class PressurePointsIntent extends CustomIntent {

    public static final String ID = PaleMod.makeID("PressurePointsIntent");

    private static final UIStrings uiStrings;
    private static final String[] TEXT;

    public PressurePointsIntent() {
        super(WatcherIntentEnums.PressurePointsIntent, TheWatcher.MOVES[TheWatcher.PRESSUREPOINTS],
                ImageMaster.INTENT_DEBUFF_L,
                ImageMaster.INTENT_DEBUFF);
    }

    @Override
    public String description(AbstractMonster mo) {
        String result = TEXT[0];
        result += mo.getIntentBaseDmg();
        result += TEXT[1];

        return result;
    }

    @Override
    public float updateVFXInInterval(AbstractMonster mo, ArrayList<AbstractGameEffect> intentVfx) {
        intentVfx.add(new DebuffParticleEffect(mo.intentHb.cX, mo.intentHb.cY));
        return 1F;
    }

    @Override
    public String damageNumber(AbstractMonster am) {
        return Integer.toString(TheWatcher.getPressurePointsDamage(am));
    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString(ID);
        TEXT = uiStrings.TEXT;
    }
}
