package paleoftheancients.helpers;

import actlikeit.dungeons.CustomDungeon;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.exordium.ApologySlime;
import paleoftheancients.PaleMod;
import paleoftheancients.relics.BlurryLens;

public class BASlime extends ApologySlime {
    public static final String ID = PaleMod.makeID("BASlime");
    public static final String NAME = CardCrawlGame.languagePack.getMonsterStrings(ID).NAME;

    public BASlime() {
        super();
        this.name = NAME;
    }

    @Override
    public void die(boolean triggerRelics) {
        CustomDungeon.addRelicReward(BlurryLens.ID);
        super.die(triggerRelics);
    }
}
