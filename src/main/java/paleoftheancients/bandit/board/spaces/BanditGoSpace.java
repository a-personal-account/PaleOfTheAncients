package paleoftheancients.bandit.board.spaces;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.FocusPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import paleoftheancients.bandit.board.AbstractBoard;
import paleoftheancients.bandit.board.BanditBoard;
import paleoftheancients.bandit.powers.KeyFinisherPower;
import paleoftheancients.bandit.powers.RacingSpeedPower;

public class BanditGoSpace extends GoSpace {
    private BanditBoard board;

    public BanditGoSpace(AbstractBoard board, int x, int y) {
        super(board, x, y);
        this.board = (BanditBoard) board;
    }

    @Override
    public void uponLand(AbstractCreature actor) {
        super.uponLand(actor);
        splat();
        att(new DamageAction(AbstractDungeon.player, new DamageInfo(board.owner, BASEDAMAGE, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.FIRE));
    }

    @Override
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

        att(new ApplyPowerAction(board.owner, actor, new KeyFinisherPower(board.owner, 1), 1));
    }

    @Override
    public void playVFX(AbstractCreature actor) {
        super.playVFX(board.owner);
    }

    public String getBodyText() {
        return TEXT[1] + BASETEXT[1] + TEXT[2] + BASEDAMAGE + TEXT[3];
    }
}