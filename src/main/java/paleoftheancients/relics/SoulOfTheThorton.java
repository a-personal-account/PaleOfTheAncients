package paleoftheancients.relics;

import basemod.ReflectionHacks;
import basemod.abstracts.CustomRelic;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.ui.panels.TopPanel;
import paleoftheancients.PaleMod;
import paleoftheancients.helpers.AssetLoader;
import theThorton.characters.TheThorton;

public class SoulOfTheThorton extends CustomRelic {
    public static final String ID = PaleMod.makeID("SoulOfTheThorton");

    private static final RelicTier TIER = RelicTier.SPECIAL;
    private static final LandingSound SOUND = LandingSound.MAGICAL;
    private boolean triggered = false;

    public SoulOfTheThorton() {
        super(ID, AssetLoader.loadImage(PaleMod.assetPath("images/relics/thorton.png")), AssetLoader.loadImage(PaleMod.assetPath("images/relics/outline/thorton.png")), TIER, SOUND);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public void onEquip() {
        if(!this.triggered) {
            this.triggered = true;
            if(!Loader.isModLoaded("thorton") || !(AbstractDungeon.player instanceof TheThorton)) {
                ReflectionHacks.setPrivate(AbstractDungeon.topPanel, TopPanel.class, "title", DESCRIPTIONS[1]);
            }
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
