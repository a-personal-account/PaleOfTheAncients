package paleoftheancients.helpers;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.exordium.ApologySlime;
import paleoftheancients.PaleMod;
import paleoftheancients.dungeons.PaleOfTheAncients;
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
        PaleOfTheAncients.addRewardRelic(BlurryLens.ID);
        super.die(triggerRelics);
    }
}
