package paleoftheancients.relics;

import basemod.ReflectionHacks;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.FocusPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.*;
import paleoftheancients.PaleMod;
import paleoftheancients.helpers.AssetLoader;

public class KeyRelic extends CustomRelic {
    public static final String ID = PaleMod.makeID("KeyRelic");

    private static final RelicTier TIER = RelicTier.SPECIAL;
    private static final LandingSound SOUND = LandingSound.CLINK;

    private boolean firstTurn;
    private final float offsetX = (float) ReflectionHacks.getPrivateStatic(AbstractRelic.class, "offsetX");

    public KeyRelic() {
        super(ID, AssetLoader.loadImage(PaleMod.assetPath("images/relics/keyrelic.png")), AssetLoader.loadImage(PaleMod.assetPath("images/relics/outline/keyrelic.png")), TIER, SOUND);
    }

    public void updateDesc() {
        this.description = this.getUpdatedDescription();
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
    }

    @Override
    public String getUpdatedDescription() {
        StringBuilder result = new StringBuilder();
        result.append(DESCRIPTIONS[0]);
        if(Settings.hasRubyKey)
            result.append(DESCRIPTIONS[2]).append(DESCRIPTIONS[1]);
        if(Settings.hasEmeraldKey)
            result.append(DESCRIPTIONS[3]).append(DESCRIPTIONS[1]);
        if(Settings.hasSapphireKey)
            result.append(DESCRIPTIONS[4]).append(DESCRIPTIONS[1]);

        if(Settings.hasEmeraldKey) {
            result.append(DESCRIPTIONS[6]).append(DESCRIPTIONS[1]);
            result.append(DESCRIPTIONS[7]).append(DESCRIPTIONS[1]);
        }
        if(Settings.hasRubyKey)
            result.append(DESCRIPTIONS[5]).append(DESCRIPTIONS[1]);
        if(Settings.hasSapphireKey)
            result.append(DESCRIPTIONS[8]).append(DESCRIPTIONS[1]);

        if(Settings.hasRubyKey || Settings.hasSapphireKey) {
            result.append(DESCRIPTIONS[9].substring(1)).append(DESCRIPTIONS[10]);
            if(Settings.hasRubyKey) {
                result.append(DESCRIPTIONS[11]);
                if(Settings.hasSapphireKey)
                    result.append(DESCRIPTIONS[9]);
            }
            if(Settings.hasSapphireKey)
                result.append(DESCRIPTIONS[12]);
            result.append(DESCRIPTIONS[13]);
        }
        result.append(LocalizedStrings.PERIOD);
        return result.toString();
    }

    @Override
    public AbstractRelic makeCopy() {
        return new KeyRelic();
    }

    @Override
    public void atBattleStart() {
        this.flash();

        if(Settings.hasRubyKey) {
            this.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, 1), 1));
            this.addToTop(new HealAction(AbstractDungeon.player, AbstractDungeon.player, 2, 0.0F));
            RelicLibrary.getRelic(RedMask.ID).atBattleStart();
        }

        if(Settings.hasEmeraldKey) {
            this.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new DexterityPower(AbstractDungeon.player, 1), 1));
            RelicLibrary.getRelic(BagOfPreparation.ID).atBattleStart();
        }

        if(Settings.hasSapphireKey) {
            this.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new FocusPower(AbstractDungeon.player, 1), 1));
            RelicLibrary.getRelic(BagOfMarbles.ID).atBattleStart();
            RelicLibrary.getRelic(Anchor.ID).atBattleStart();
        }

        this.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
    }

    public void atPreBattle() {
        this.firstTurn = true;
    }

    public void atTurnStart() {
        if (this.firstTurn) {
            this.firstTurn = false;
            if(Settings.hasEmeraldKey) {
                this.addToTop(new GainEnergyAction(1));
            }
        }
    }

    @Override
    public void renderInTopPanel(SpriteBatch sb) {
        if (Settings.hideRelics)
            return;
        renderOutline(sb, true);
        sb.setColor(Color.WHITE);
        sb.draw(this.img, this.currentX - 64.0F + offsetX, this.currentY - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, this.scale, this.scale, 0, 0, 0, 128, 128, false, false);

        if(Settings.hasRubyKey)
            sb.draw(ImageMaster.RUBY_KEY, this.currentX - 32.0F + offsetX, this.currentY - 32.0F, 32.0F, 32.0F, 64.0F, 64.0F, this.scale, this.scale, 0, 0, 0, 64, 64, false, false);
        if(Settings.hasEmeraldKey)
            sb.draw(ImageMaster.EMERALD_KEY, this.currentX - 32.0F + offsetX, this.currentY - 32.0F, 32.0F, 32.0F, 64.0F, 64.0F, this.scale, this.scale, 0, 0, 0, 64, 64, false, false);
        if(Settings.hasSapphireKey)
            sb.draw(ImageMaster.SAPPHIRE_KEY, this.currentX - 32.0F + offsetX, this.currentY - 32.0F, 32.0F, 32.0F, 64.0F, 64.0F, this.scale, this.scale, 0, 0, 0, 64, 64, false, false);

        renderFlash(sb, true);
        this.hb.render(sb);
    }
}
