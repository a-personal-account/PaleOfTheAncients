package paleoftheancients.bandit.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import paleoftheancients.bandit.board.AbstractBoard;

public class BoardMoveAction extends AbstractGameAction {
    private AbstractBoard board;
    private AbstractCreature actor;
    private int distance;

    public BoardMoveAction(AbstractCreature actor, AbstractBoard board, int distance) {
        this.board = board;
        this.actor = actor;
        this.distance = distance;
    }

    @Override
    public void update() {
        board.move(board.owner, distance);
        this.isDone = true;
    }
}
