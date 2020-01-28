package paleoftheancients.relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import paleoftheancients.PaleMod;
import paleoftheancients.helpers.AssetLoader;

public class SoulOfTheHexaghost extends CustomRelic {
    public static final String ID = PaleMod.makeID("SoulOfTheHexaghost");

    private static final RelicTier TIER = RelicTier.SPECIAL;
    private static final LandingSound SOUND = LandingSound.MAGICAL;

    public SoulOfTheHexaghost() {
        super(ID, AssetLoader.loadImage(PaleMod.assetPath("images/relics/hexaghost.png")), AssetLoader.loadImage(PaleMod.assetPath("images/relics/outline/hexaghost.png")), TIER, SOUND);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new SoulOfTheHexaghost();
    }

    public void onUseCard(AbstractCard card, UseCardAction action) {
        counter++;
        if(counter == 36) {
            counter = 0;
            this.flash();
            this.stopPulse();
            for(int i = 1; i < 6; i++) {
                GameActionManager.queueExtraCard(card, (AbstractMonster) action.target);
            }
        } else if(counter == 35) {
            this.beginLongPulse();
        }
    }

    @Override
    public void atBattleStart() {
        this.counter = 0;
    }

    @Override
    public void onVictory() {
        this.stopPulse();
        this.counter = -1;
    }
}
