package paleoftheancients.helpers;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import paleoftheancients.RazIntent.CustomIntent;
import paleoftheancients.vfx.FlashingIntentVFX;

import java.util.ArrayList;

public class FlashingIntent extends CustomIntent {
    protected Texture img;
    public FlashingIntent(AbstractMonster.Intent intent, String header, String display, String tip) {
        super(intent, header, display, tip);
    }
    public FlashingIntent(AbstractMonster.Intent intent, String header, Texture display, Texture tip) {
        super(intent, header, display, tip);
    }

    @Override
    public float updateVFXInInterval(AbstractMonster mo, ArrayList<AbstractGameEffect> intentVfx) {
        AbstractGameEffect sb = new FlashingIntentVFX(tip, mo.intentHb.cX, mo.intentHb.cY);
        intentVfx.add(sb);
        return 0.2F;
    }
}
