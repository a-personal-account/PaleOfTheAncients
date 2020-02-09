package paleoftheancients.relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.watcher.MantraPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import paleoftheancients.PaleMod;
import paleoftheancients.helpers.AssetLoader;

public class SoulOfTheWatcher extends CustomRelic {
    public static final String ID = PaleMod.makeID("SoulOfTheWatcher");

    private static final RelicTier TIER = RelicTier.SPECIAL;
    private static final LandingSound SOUND = LandingSound.MAGICAL;

    private static final byte MANTRAGAIN = 2;
    private boolean active;

    public SoulOfTheWatcher() {
        super(ID, AssetLoader.loadImage(PaleMod.assetPath("images/relics/watcher.png")), AssetLoader.loadImage(PaleMod.assetPath("images/relics/outline/watcher.png")), TIER, SOUND);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + MANTRAGAIN + DESCRIPTIONS[1];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new SoulOfTheWatcher();
    }

    @Override
    public void atTurnStart() {
        active = false;
    }

    @Override
    public void atTurnStartPostDraw() {
        this.addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                this.isDone = true;
                active = true;
            }
        });
    }

    @Override
    public void onCardDraw(AbstractCard drawnCard) {
        if(active) {
            this.addToBot(new ApplyPowerAction(AbstractDungeon.player, null, new MantraPower(AbstractDungeon.player, MANTRAGAIN), MANTRAGAIN));
        }
    }
}
