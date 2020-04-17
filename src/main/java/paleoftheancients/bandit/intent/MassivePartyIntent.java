package paleoftheancients.bandit.intent;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import paleoftheancients.PaleMod;
import paleoftheancients.bandit.board.spaces.AbstractSpace;
import paleoftheancients.bandit.board.spaces.GoSpace;
import paleoftheancients.bandit.board.spaces.symmetrical.DamageSpace;
import paleoftheancients.bandit.monsters.TheBandit;
import paleoftheancients.helpers.FlashingIntent;

public class MassivePartyIntent extends FlashingIntent {
    public static final String ID = PaleMod.makeID("MassivePartyIntent");

    private static final UIStrings uiStrings;
    private static final String[] TEXT;

    public MassivePartyIntent() {
        super(EnumBuster.MassivePartyIntent, TEXT[0],
                PaleMod.assetPath("images/bandit/intent/massiveparty_L.png"),
                PaleMod.assetPath("images/bandit/intent/massiveparty.png"));
    }

    @Override
    public String description(AbstractMonster mo) {
        return TEXT[1];
    }

    @Override
    public String damageNumber(AbstractMonster mo) {
        if(mo instanceof TheBandit && ((TheBandit) mo).board != null) {
            int dmg = GoSpace.BASEDAMAGE;
            for(final AbstractSpace space : ((TheBandit) mo).board.squareList) {
                if(space instanceof DamageSpace) {
                    dmg += DamageSpace.BASEDAMAGE;
                }
            }
            return Integer.toString(dmg);
        }
        return null;
    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString(ID);
        TEXT = uiStrings.TEXT;
    }
}
