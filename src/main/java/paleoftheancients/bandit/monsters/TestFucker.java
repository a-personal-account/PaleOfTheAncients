package paleoftheancients.bandit.monsters;

import basemod.animations.SpriterAnimation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.Settings;
import paleoftheancients.PaleMod;
import paleoftheancients.bandit.board.TestBoard;
import paleoftheancients.helpers.AbstractBossMonster;

public class TestFucker extends AbstractBossMonster {
    public static final String ID = PaleMod.makeID("TestFucker");

    private TestBoard board = null;

    public TestFucker() {
        super("Testfucker", ID, 15000,0.0F, -15.0F, 240.0F, 320.0F, null, 0, -20);
        this.animation = new SpriterAnimation(PaleMod.assetPath("images/bandit/spriter/bandit_resized_hat.scml"));
        this.animation.setFlip(true, false);

        this.setHp(calcAscensionNumber(this.maxHealth));
        this.flipHorizontal = true;
        this.dialogX -= 30F * Settings.scale;
        this.dialogY += 60F * Settings.scale;

        addMove((byte)0, Intent.UNKNOWN);
    }

    @Override
    public void takeCustomTurn(DamageInfo info, int multiplier) {
        if(board == null) {
            board = new TestBoard();
            board.init();
        } else {
            board.squareList.clear();
            board = null;
        }
    }

    @Override
    public void getMove(int num) {
        setMoveShortcut((byte)0);
    }
    @Override
    public void update() {
        super.update();
        if(board != null) {
            board.update();
        }
    }
    @Override
    public void render(SpriteBatch sb) {
        super.render(sb);
        if(board != null) {
            board.render(sb);
        }
    }
}