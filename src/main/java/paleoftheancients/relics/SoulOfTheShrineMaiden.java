package paleoftheancients.relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import paleoftheancients.PaleMod;
import paleoftheancients.helpers.AssetLoader;
import paleoftheancients.thevixen.actions.ApplyTempGainStrengthPowerAction;

public class SoulOfTheShrineMaiden extends CustomRelic {
    public static final String ID = PaleMod.makeID("SoulOfTheShrineMaiden");

    private static final RelicTier TIER = RelicTier.SPECIAL;
    private static final LandingSound SOUND = LandingSound.MAGICAL;

    boolean bloodied;

    public SoulOfTheShrineMaiden() {
        super(ID, AssetLoader.loadImage(PaleMod.assetPath("images/relics/reimu.png")), AssetLoader.loadImage(PaleMod.assetPath("images/relics/outline/reimu.png")), TIER, SOUND);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new SoulOfTheShrineMaiden();
    }

    @Override
    public void atPreBattle() {
        bloodied = false;
    }

    @Override
    public void atTurnStart() {
        if(bloodied) {
            AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(1));
            this.flash();
        }
        bloodied = false;
    }

    @Override
    public void onLoseHp(int damageAmount) {
        bloodied = true;
    }

    @Override
    public int onAttackedToChangeDamage(DamageInfo info, int damageAmount) {
        if(info.output > damageAmount) {
            AbstractDungeon.actionManager.addToBottom(new ApplyTempGainStrengthPowerAction(AbstractDungeon.player, AbstractDungeon.player, info.output - damageAmount));
        }
        return damageAmount;
    }
}
