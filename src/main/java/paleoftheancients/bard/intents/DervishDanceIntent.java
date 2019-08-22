package paleoftheancients.bard.intents;

import paleoftheancients.PaleMod;
import paleoftheancients.RazIntent.CustomIntent;
import paleoftheancients.bard.notes.AttackNote;
import paleoftheancients.bard.vfx.RenderNoteVFX;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

import java.util.ArrayList;

public class DervishDanceIntent extends CustomIntent {

    public static final String ID = PaleMod.makeID("DervishDanceIntent");

    private static final UIStrings uiStrings;
    private static final String[] TEXT;

    public DervishDanceIntent() {
        super(BardIntentEnum.ATTACK_DERVISH_DANCE, TEXT[0],
                "images/ui/intent/attack/attack_intent_3.png",
                "images/ui/intent/tip/3.png");
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
        intentVfx.add(new RenderNoteVFX(AttackNote.get(), mo.intentHb.cX, mo.intentHb.cY, 0.21F));

        return 0.2F;
    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString(ID);
        TEXT = uiStrings.TEXT;
    }
}
