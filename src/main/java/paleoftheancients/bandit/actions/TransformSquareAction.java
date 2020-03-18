package paleoftheancients.bandit.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import paleoftheancients.bandit.board.AbstractBoard;
import paleoftheancients.bandit.board.spaces.AbstractSpace;

public class TransformSquareAction extends AbstractGameAction {
    private AbstractSpace byeByeSquare;
    private AbstractBoard board;

    public Class<? extends AbstractSpace> swapSquare;

    public TransformSquareAction(AbstractBoard board, AbstractSpace bruh, Class<? extends AbstractSpace> wah) {
        this.board = board;
        this.byeByeSquare = bruh;
        this.swapSquare = wah;
    }

    public void update() {
        board.transform(board.squareList.indexOf(this.byeByeSquare), board.swapSquare(this.swapSquare, this.byeByeSquare.x, this.byeByeSquare.y));
        this.isDone = true;
    }
}