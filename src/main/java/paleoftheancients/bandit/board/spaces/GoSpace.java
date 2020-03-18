package paleoftheancients.bandit.board.spaces;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.FocusPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.combat.SweepingBeamEffect;
import paleoftheancients.PaleMod;
import paleoftheancients.bandit.board.AbstractBoard;
import paleoftheancients.bandit.powers.KeyFinisherPower;
import paleoftheancients.bandit.powers.RacingSpeedPower;
import paleoftheancients.helpers.AssetLoader;

public class GoSpace extends AbstractSpace {
    private static String[] TEXT = CardCrawlGame.languagePack.getUIString(PaleMod.makeID("GoSpace")).TEXT;
    private static int BASEDAMAGE = 25;
    public GoSpace(AbstractBoard board, int x, int y) {
        super(board, x, y);
        this.tex = AssetLoader.loadImage(PaleMod.assetPath("images/bandit/spaces/GoSquare" + AbstractBoard.artStyle + ".png"));
        this.goodness = AbstractSpace.GOODNESS.GOOD;
        this.triggersWhenPassed = true;
    }

    public void onLanded(AbstractCreature actor) {
        AbstractPower pow = board.owner.getPower(RacingSpeedPower.POWER_ID);
        if (pow != null) {
            att(new ReducePowerAction(AbstractDungeon.player, board.owner, DexterityPower.POWER_ID, pow.amount / 2));
            att(new ReducePowerAction(AbstractDungeon.player, board.owner, StrengthPower.POWER_ID, pow.amount / 2));
            if(!AbstractDungeon.player.orbs.isEmpty()) {
                att(new ReducePowerAction(AbstractDungeon.player, board.owner, FocusPower.POWER_ID, pow.amount / 2));
            }
            att(new ApplyPowerAction(board.owner, board.owner, new StrengthPower(board.owner, pow.amount), pow.amount));
        }
        int bruh = BASEDAMAGE;
        splat();
        att(new DamageAction(AbstractDungeon.player, new DamageInfo(board.owner, bruh, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.FIRE));
        att(new VFXAction(board.owner, new SweepingBeamEffect(board.owner.hb.cX, board.owner.hb.cY, board.owner.flipHorizontal), 0.4F));
        att(new SFXAction("ATTACK_DEFECT_BEAM"));

        att(new ApplyPowerAction(board.owner, actor, new KeyFinisherPower(board.owner, 1), 1));
    }

    public String getHeaderText() {
        return TEXT[0];
    }
    public String getBodyText() {
        return TEXT[1] + BASETEXT[1] + TEXT[2] + BASEDAMAGE + TEXT[3];
    }
}