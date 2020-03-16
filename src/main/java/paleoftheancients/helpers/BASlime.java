package paleoftheancients.helpers;

import actlikeit.dungeons.CustomDungeon;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.exordium.ApologySlime;
import com.megacrit.cardcrawl.vfx.SpeechBubble;
import paleoftheancients.PaleMod;
import paleoftheancients.relics.BlurryLens;

public class BASlime extends ApologySlime {
    public static final String ID = PaleMod.makeID("BASlime");
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String DIALOG = monsterStrings.DIALOG[0];

    public BASlime() {
        super();
        this.name = NAME;
    }

    @Override
    public void die(boolean triggerRelics) {
        CustomDungeon.addRelicReward(BlurryLens.ID);
        AbstractDungeon.effectList.add(new SpeechBubble(this.hb.cX + this.dialogX, this.hb.cY + this.dialogY, 2.5F, DIALOG, false));
        ++this.deathTimer;
        super.die(triggerRelics);
    }
}
