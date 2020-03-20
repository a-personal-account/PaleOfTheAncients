package paleoftheancients.bandit.board;

import java.awt.Point;

public class AbstractDrone {
    public int position;

    public Point location;

    public AbstractDrone(int position2, int x, int y) {
        this.position = position2;
        this.location = new Point(x, y);
    }
}