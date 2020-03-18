package paleoftheancients.bandit.intent;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import paleoftheancients.PaleMod;
import paleoftheancients.helpers.AbstractBossMonster;
import paleoftheancients.helpers.FlashingIntent;

public class HappyHitIntent extends FlashingIntent {
    public static final String ID = PaleMod.makeID("HappyHitIntent");

    private static final UIStrings uiStrings;
    private static final String[] TEXT;

    public HappyHitIntent() {
        super(EnumBuster.HappyHitIntent, TEXT[0],
                PaleMod.assetPath("images/bandit/intent/happyhit_L.png"),
                PaleMod.assetPath("images/bandit/intent/happyhit.png"));
    }

    @Override
    public String description(AbstractMonster mo) {
        StringBuilder result = new StringBuilder();
        result.append(TEXT[1]);
        result.append(mo.getIntentDmg());
        result.append(TEXT[2]);

        EnemyMoveInfo info = ((AbstractBossMonster) mo).getMoveInfo();
        if(info.multiplier > 1) {
            result.append("#b" + info.multiplier + " ");
        }
        result.append(TEXT[3]);
        result.append(TEXT[info.multiplier > 1 ? 4 : 5]);
        result.append(TEXT[6]);

        return result.toString();
    }

    @Override
    public String damageNumber(AbstractMonster mo) {
        StringBuilder builder = new StringBuilder();
        EnemyMoveInfo info = ((AbstractBossMonster) mo).getMoveInfo();

        builder.append(mo.getIntentDmg());
        if(info.isMultiDamage) {
            builder.append("x");
            builder.append(info.multiplier);
        }
        builder.append(" :) ");
        builder.append(info.multiplier);
        return builder.toString();
    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString(ID);
        TEXT = uiStrings.TEXT;
    }
}
