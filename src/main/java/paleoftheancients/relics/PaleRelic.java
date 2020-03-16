package paleoftheancients.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import paleoftheancients.PaleMod;

public abstract class PaleRelic extends CustomRelic {
    private static String[] TEXT = CardCrawlGame.languagePack.getUIString(PaleMod.makeID("PaleRelicReward")).TEXT;

    public PaleRelic(String id, Texture texture, RelicTier tier, LandingSound sfx) {
        super(id, texture, tier, sfx);
        addTip();
    }

    public PaleRelic(String id, Texture texture, Texture outline, RelicTier tier, LandingSound sfx) {
        super(id, texture, outline, tier, sfx);
        addTip();
    }

    public PaleRelic(String id, String imgName, RelicTier tier, LandingSound sfx) {
        super(id, imgName, tier, sfx);
        addTip();
    }

    private void addTip() {
        this.tips.add(new PowerTip(TEXT[0], TEXT[1]));
        this.initializeTips();
    }

    @Override
    public void instantObtain() {
        PaleRelic p = getPaleRelic();
        if(p == null) {
            super.instantObtain();
        } else {
            p.flash();
        }
    }

    public static boolean takeAble() {
        return getPaleRelic() == null;
    }
    private static PaleRelic getPaleRelic() {
        for(final AbstractRelic r : AbstractDungeon.player.relics) {
            if(r instanceof PaleRelic) {
                return (PaleRelic) r;
            }
        }
        return null;
    }
}