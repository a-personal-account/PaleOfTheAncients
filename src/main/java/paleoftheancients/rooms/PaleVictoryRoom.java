package paleoftheancients.rooms;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.GameCursor;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import paleoftheancients.screens.PaleVictoryScreen;

public class PaleVictoryRoom extends AbstractRoom {
    private PaleVictoryScreen screen;

    public PaleVictoryRoom() {
        this.phase = RoomPhase.INCOMPLETE;
        this.screen = new PaleVictoryScreen();
        AbstractDungeon.overlayMenu.proceedButton.hideInstantly();
    }

    public void onPlayerEntry() {
        AbstractDungeon.isScreenUp = true;
        AbstractDungeon.overlayMenu.proceedButton.hide();
        GameCursor.hidden = true;
        AbstractDungeon.screen = AbstractDungeon.CurrentScreen.NO_INTERACT;
        this.screen.open();
    }

    public void update() {
        super.update();
        this.screen.update();
    }

    public void render(SpriteBatch sb) {
        super.render(sb);
        this.screen.render(sb);
    }

    public void renderAboveTopPanel(SpriteBatch sb) {
        super.renderAboveTopPanel(sb);
    }

    public void dispose() {
        super.dispose();
    }
}
