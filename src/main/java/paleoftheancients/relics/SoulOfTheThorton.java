package paleoftheancients.relics;

import basemod.ReflectionHacks;
import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.ui.panels.TopPanel;
import paleoftheancients.PaleMod;

public class SoulOfTheThorton extends CustomRelic {
    public static final String ID = PaleMod.makeID("SoulOfTheThorton");

    private static final RelicTier TIER = RelicTier.SPECIAL;
    private static final LandingSound SOUND = LandingSound.MAGICAL;
    private boolean triggered = false;

    public SoulOfTheThorton() {
        super(ID, ImageMaster.loadImage(PaleMod.assetPath("images/relics/thorton.png")), ImageMaster.loadImage(PaleMod.assetPath("images/relics/outline/thorton.png")), TIER, SOUND);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void onEquip() {
        if(!this.triggered) {
            this.triggered = true;
            ReflectionHacks.setPrivate(AbstractDungeon.topPanel, TopPanel.class, "title", DESCRIPTIONS[1]);
        }
    }

    @Override
    public void justEnteredRoom(AbstractRoom room) {
        this.onEquip();
    }

    @Override
    public AbstractRelic makeCopy() {
        return new SoulOfTheThorton();
    }
}
