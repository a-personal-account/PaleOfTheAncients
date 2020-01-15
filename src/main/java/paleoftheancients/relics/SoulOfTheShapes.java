package paleoftheancients.relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.LoseDexterityPower;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import paleoftheancients.PaleMod;
import paleoftheancients.donudeca.monsters.DonuDeca;
import paleoftheancients.helpers.AssetLoader;

public class SoulOfTheShapes extends CustomRelic {
    public static final String ID = PaleMod.makeID("SoulOfTheShapes");

    private static final RelicTier TIER = RelicTier.SPECIAL;
    private static final LandingSound SOUND = LandingSound.MAGICAL;

    private DonuDeca.Mode mode;
    private final int STRENGTH = 3, DEX = 3;

    public SoulOfTheShapes() {
        super(ID, AssetLoader.loadImage(PaleMod.assetPath("images/relics/donudeca_deca.png")), AssetLoader.loadImage(PaleMod.assetPath("images/relics/outline/donudeca_deca.png")), TIER, SOUND);
        mode = DonuDeca.Mode.Deca;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + DESCRIPTIONS[mode == DonuDeca.Mode.Donu ? 1 : 2];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new SoulOfTheShapes();
    }

    public void onUseCard(AbstractCard card, UseCardAction action) {
        if(mode == DonuDeca.Mode.Deca) {
            mode = DonuDeca.Mode.Donu;
            if(card.type == AbstractCard.CardType.SKILL) {
                this.flash();
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, STRENGTH), STRENGTH));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new LoseStrengthPower(AbstractDungeon.player, STRENGTH), STRENGTH));
            }
        } else {
            mode = DonuDeca.Mode.Deca;
            if(card.type == AbstractCard.CardType.ATTACK) {
                this.flash();
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new DexterityPower(AbstractDungeon.player, DEX), DEX));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new LoseDexterityPower(AbstractDungeon.player, DEX), DEX));
            }
        }
        setImage();
    }

    private void setImage() {
        String tmp = mode.name().toLowerCase();
        img = AssetLoader.loadImage(PaleMod.assetPath("images/relics/donudeca_" + tmp + ".png"));
        outlineImg = AssetLoader.loadImage(PaleMod.assetPath("images/relics/outline/donudeca_" + tmp + ".png"));
        this.description = this.getUpdatedDescription();
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        this.initializeTips();
    }

    @Override
    public void onVictory() {
        if(mode == DonuDeca.Mode.Donu) {
            mode = DonuDeca.Mode.Deca;
            setImage();
        }
    }
}
