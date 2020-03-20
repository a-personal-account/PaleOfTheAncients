package paleoftheancients.bandit.intent;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import paleoftheancients.PaleMod;
import paleoftheancients.helpers.FlashingIntent;

public class DeadlyDashIntent extends FlashingIntent {
    public static final String ID = PaleMod.makeID("DeadlyDashIntent");

    private static final UIStrings uiStrings;
    private static final String[] TEXT;

    public static final byte MOVE = 1;

    public DeadlyDashIntent() {
        super(EnumBuster.DeadlyDashIntent, TEXT[0],
                PaleMod.assetPath("images/bandit/intent/deadlydash_L.png"),
                PaleMod.assetPath("images/bandit/intent/deadlydash.png"));
    }

    @Override
    public String description(AbstractMonster mo) {
        return TEXT[1] + mo.getIntentDmg() + TEXT[2] + MOVE + TEXT[3];
    }

    @Override
    public String damageNumber(AbstractMonster mo) {
        return "(" + mo.getIntentDmg() + "->" + MOVE + ")x???";
    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString(ID);
        TEXT = uiStrings.TEXT;
    }
}
