package paleoftheancients.bandit.intent;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import paleoftheancients.PaleMod;
import paleoftheancients.bandit.monsters.TheBandit;
import paleoftheancients.helpers.AbstractBossMonster;
import paleoftheancients.helpers.FlashingIntent;

public class MoveAttackIntent extends FlashingIntent {
    public static final String ID = PaleMod.makeID("MoveAttackIntent");

    private static final UIStrings uiStrings;
    private static final String[] TEXT;

    public MoveAttackIntent() {
        super(EnumBuster.MoveAttackIntent, TEXT[0],
                PaleMod.assetPath("images/bandit/intent/moveattack_L.png"),
                PaleMod.assetPath("images/bandit/intent/moveattack.png"));
    }

    @Override
    public String description(AbstractMonster mo) {
        StringBuilder result = new StringBuilder();
        result.append(TEXT[1]);
        result.append(mo.getIntentDmg());
        result.append(TEXT[2]);

        EnemyMoveInfo info = ((AbstractBossMonster) mo).getMoveInfo(mo.nextMove);
        if(info instanceof TheBandit.BanditMoveInfo) {
            result.append(TEXT[3]).append(((TheBandit.BanditMoveInfo) info).move);
        }

        if(info.isMultiDamage) {
            result.append(TEXT[4]).append(info.multiplier).append(TEXT[5]);
        }

        return result.toString() + LocalizedStrings.PERIOD;
    }

    @Override
    public String damageNumber(AbstractMonster mo) {
        StringBuilder s = new StringBuilder();
        EnemyMoveInfo info = ((AbstractBossMonster) mo).getMoveInfo(mo.nextMove);
        if(info.isMultiDamage) {
            s.append("(");
        }
        s.append(mo.getIntentDmg());
        if(info instanceof TheBandit.BanditMoveInfo) {
            s.append("->").append(((TheBandit.BanditMoveInfo) info).move);
        }
        if(info.isMultiDamage) {
            s.append(")");
            s.append("x").append(info.multiplier);
        }
        return s.toString();
    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString(ID);
        TEXT = uiStrings.TEXT;
    }
}
