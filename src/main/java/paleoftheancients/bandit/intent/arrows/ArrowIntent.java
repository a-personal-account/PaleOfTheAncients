package paleoftheancients.bandit.intent.arrows;

import com.megacrit.cardcrawl.monsters.AbstractMonster;
import paleoftheancients.RazIntent.CustomIntent;
import paleoftheancients.helpers.AssetLoader;

public abstract class ArrowIntent extends CustomIntent {

    public ArrowIntent(AbstractMonster.Intent intent, String header, String display) {
        super(intent, header, AssetLoader.loadImage(display), AssetLoader.emptyPixel());
    }

    @Override
    public String description(AbstractMonster mo) {
        return "";
    }

    @Override
    public String damageNumber(AbstractMonster mo) {
        return Integer.toString(mo.getIntentBaseDmg());
    }
}
