package paleoftheancients.bandit.board.spaces.asymmetrical;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import paleoftheancients.PaleMod;
import paleoftheancients.bandit.actions.SummonGremlinAction;
import paleoftheancients.bandit.board.AbstractBoard;
import paleoftheancients.bandit.board.spaces.AbstractSpace;
import paleoftheancients.helpers.AssetLoader;

public class GremlinSpace extends AbstractSpace {
    private static String[] TEXT = CardCrawlGame.languagePack.getUIString(PaleMod.makeID("GremlinSpace")).TEXT;
    public static int ENEMYLIMIT = 5;

    public GremlinSpace(AbstractBoard board, int x, int y) {
        super(board, x, y);
        this.tex = AssetLoader.loadImage(PaleMod.assetPath("images/bandit/spaces/GremlinSquare.png"));
        this.goodness = GOODNESS.BAD;
    }

    public void onLanded(AbstractCreature actor) {
        att(new SummonGremlinAction());
    }

    public String getHeaderText() {
        return TEXT[0];
    }
    public String getBodyText() {
        return TEXT[1] + ENEMYLIMIT + TEXT[2];
    }
}