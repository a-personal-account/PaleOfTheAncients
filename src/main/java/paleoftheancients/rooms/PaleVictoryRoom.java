package paleoftheancients.rooms;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.GameCursor;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import paleoftheancients.PaleMod;
import paleoftheancients.screens.DefaultPaleVictoryScreen;
import paleoftheancients.screens.PaleVictoryScreen;
import paleoftheancients.screens.ShowmanVictoryScreen;
import paleoftheancients.theshowman.helpers.Cards;
import paleoftheancients.theshowman.monsters.DummyMonster;

public class PaleVictoryRoom extends AbstractRoom {
    private PaleVictoryScreen screen;

    public PaleVictoryRoom() {
        this.phase = RoomPhase.INCOMPLETE;
        if(AbstractDungeon.player.masterDeck.findCardById("theShowman:Columbify") != null) {
            Cards.preload();
            this.screen = new ShowmanVictoryScreen();
            this.monsters = new MonsterGroup(new DummyMonster(0, 0, 0, 0, ImageMaster.loadImage(PaleMod.assetPath("images/misc/emptypixel.png"))));
        } else {
            this.screen = new DefaultPaleVictoryScreen();
        }
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
