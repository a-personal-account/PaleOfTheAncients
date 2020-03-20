package paleoftheancients.bandit.actions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import paleoftheancients.bandit.board.RelicBoard;
import paleoftheancients.bandit.board.spaces.AbstractSpace;

import java.util.HashMap;
import java.util.Map;

public class WeirdMoveBoardAction extends AbstractGameAction {
    private float cur;
    private float speed;

    private RelicBoard board;
    private Map<Integer, Integer> startingpositions;
    private Map<Integer, Integer> endpositions;

    public WeirdMoveBoardAction(RelicBoard board, float speed) {
        this.speed = speed;
        this.cur = 0.0F;
        this.board = board;
        this.startingpositions = new HashMap<>();
        this.endpositions = new HashMap<>();
    }

    public void update() {
        if(this.cur == 0F) {
            AbstractSpace space;
            int endposition, index, leftBoundary = board.leftBoundary();
            for(int i = board.player.position; i < board.player.position + RelicBoard.SIZE; i++) {
                index = i % board.squareList.size();
                space = board.squareList.get(index);
                endposition = (int)(leftBoundary + (i - board.player.position) * board.squareOffset);
                if(!(i >= board.player.position && i < board.player.position + board.DISPLAYEDSPACES)) {
                    endposition -= (int)(RelicBoard.SIZE * board.squareOffset);
                }

                startingpositions.put(index, space.x);
                endpositions.put(index, endposition);
                space.hb.move(space.hb.cX + (endposition - space.x), space.hb.cY);
            }
        }
        this.cur += Gdx.graphics.getDeltaTime();
        if (this.cur >= this.speed) {
            this.cur = this.speed;
            this.isDone = true;
        }
        for(int i = 0; i < board.squareList.size(); i++) {
            AbstractSpace space = board.squareList.get(i);
            space.x = (int)Interpolation.linear.apply(startingpositions.get(i), endpositions.get(i), this.cur / this.speed);
        }
    }
}