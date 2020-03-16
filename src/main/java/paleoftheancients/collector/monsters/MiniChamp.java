package paleoftheancients.collector.monsters;

import com.evacipated.cardcrawl.modthespire.lib.SpireOverride;
import com.evacipated.cardcrawl.modthespire.lib.SpireSuper;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.city.Champ;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.WeakPower;
import paleoftheancients.PaleMod;
import paleoftheancients.collector.powers.VulnNextTurnPower;

public class MiniChamp extends Champ {
    public static final String ID = PaleMod.makeID("MiniChamp");
    private static float divisor = 2.3F;

    public MiniChamp(float x, float y) {
        super();
        this.id = ID;
        this.drawX = (float) Settings.WIDTH * 0.75F + x * Settings.scale;
        this.drawY = AbstractDungeon.floorY + y * Settings.scale;
        this.hb_w /= divisor;
        this.hb_h /= divisor;
        this.hb.width /= divisor;
        this.hb.height /= divisor;

        this.setHp(this.maxHealth / 4);

        this.powers.add(new StrengthPower(this, -5));
    }

    @Override
    protected void loadAnimation(String atlasUrl, String skeletonUrl, float scale) {
        super.loadAnimation(atlasUrl, skeletonUrl, scale * divisor);
    }

    @Override
    public void takeTurn() {
        switch(this.nextMove) {
            case 4:
                AbstractDungeon.actionManager.addToBottom(new SFXAction("MONSTER_CHAMP_SLAP"));
                AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, (DamageInfo) this.damage.get(2), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new FrailPower(AbstractDungeon.player, 2, true), 2));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new VulnNextTurnPower(AbstractDungeon.player, 2), 2));
                break;// 214
            case 6:
                AbstractDungeon.actionManager.addToBottom(new SFXAction("VO_CHAMP_2A"));
                AbstractDungeon.actionManager.addToBottom(new TalkAction(this, this.getTaunt()));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new WeakPower(AbstractDungeon.player, 2, true), 2));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new VulnNextTurnPower(AbstractDungeon.player, 2), 2));
                break;

            default:
                super.takeTurn();
        }
    }

    @Override
    public void damage(DamageInfo info) {
        super.damage(info);
    }

    @Override
    public void die() {
        super.die(true);
    }
    @Override
    public void onBossVictoryLogic() {}

    @SpireOverride
    protected String getTaunt() {
        return SpireSuper.call();
    }
}
