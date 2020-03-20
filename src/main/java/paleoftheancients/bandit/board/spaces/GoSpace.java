package paleoftheancients.bandit.board.spaces;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import paleoftheancients.PaleMod;
import paleoftheancients.bandit.board.AbstractBoard;
import paleoftheancients.helpers.AssetLoader;

public abstract class GoSpace extends AbstractSpace {
    protected static String[] TEXT = CardCrawlGame.languagePack.getUIString(PaleMod.makeID("GoSpace")).TEXT;
    protected static int BASEDAMAGE = 25;

    public GoSpace(AbstractBoard board, int x, int y) {
        super(board, x, y);
        this.tex = AssetLoader.loadImage(PaleMod.assetPath("images/bandit/spaces/GoSquare" + board.artStyle + ".png"));
        this.goodness = AbstractSpace.GOODNESS.GOOD;
        this.triggersWhenPassed = true;
    }

    public String getHeaderText() {
        return TEXT[0];
    }
}