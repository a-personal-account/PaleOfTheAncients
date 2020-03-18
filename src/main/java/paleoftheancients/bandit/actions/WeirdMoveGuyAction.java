package paleoftheancients.bandit.actions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Interpolation;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import paleoftheancients.bandit.board.AbstractBoard;
import paleoftheancients.bandit.board.AbstractDrone;

public class WeirdMoveGuyAction extends AbstractGameAction {
    private float startX;
    private float startY;
    private float endX;
    private float endY;
    private float cur;
    private float speed;

    private AbstractBoard board;
    private AbstractDrone drone;

    public WeirdMoveGuyAction(AbstractBoard board, float x, float y, float speed, AbstractDrone drone) {
        this.endX = x;
        this.endY = y;
        this.speed = speed;
        this.cur = 0.0F;
        this.drone = drone;
        this.board = board;
    }

    public void update() {
        if (this.startX == 0.0F) {
            this.startX = this.drone.location.x;
            this.startY = this.drone.location.y;
        }
        this.cur += Gdx.graphics.getDeltaTime();
        if (this.cur > this.speed) {
            this.cur = this.speed;
        }
        this.drone.location.x = (int)Interpolation.linear.apply(this.startX, this.endX, this.cur / this.speed);
        this.drone.location.y = (int)Interpolation.linear.apply(this.startY, this.endY, this.cur / this.speed);
        if (this.cur == this.speed) {
            this.isDone = true;
        }
    }
}