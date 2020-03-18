package paleoftheancients.bandit.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import paleoftheancients.bandit.board.AbstractBoard;
import paleoftheancients.bandit.board.spaces.symmetrical.EmptySpace;
import paleoftheancients.bandit.intent.DeadlyDashIntent;
import paleoftheancients.bandit.powers.ImprisonedPower;

public class DeadlyDashAction extends AbstractGameAction {
    private AbstractBoard board;
    private DamageInfo info;

    public DeadlyDashAction(AbstractBoard board, DamageInfo info) {
        this.board = board;
        this.info = info;
    }

    @Override
    public void update() {
        boolean repeat = !(board.squareList.get((board.player.position + 1) % board.squareList.size()) instanceof EmptySpace) && !board.owner.hasPower(ImprisonedPower.POWER_ID);

        board.owner.useFastAttackAnimation();
        this.addToBot(new DamageAction(AbstractDungeon.player, info, AttackEffect.BLUNT_LIGHT));
        this.addToBot(new BoardMoveAction(board.owner, board, DeadlyDashIntent.MOVE));

        if(repeat) {
            this.addToBot(new DeadlyDashAction(board, info));
        }
        this.isDone = true;
    }
}
