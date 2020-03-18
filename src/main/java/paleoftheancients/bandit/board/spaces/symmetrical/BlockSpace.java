package paleoftheancients.bandit.board.spaces.symmetrical;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import paleoftheancients.PaleMod;
import paleoftheancients.bandit.board.AbstractBoard;
import paleoftheancients.bandit.board.spaces.AbstractSpace;
import paleoftheancients.helpers.AssetLoader;

public class BlockSpace extends AbstractSpace {
    private static String[] TEXT = CardCrawlGame.languagePack.getUIString(PaleMod.makeID("BlockSpace")).TEXT;
    private static int BASEBLOCK = 8;

    public BlockSpace(AbstractBoard board, int x, int y) {
        super(board, x, y);
        this.tex = AssetLoader.loadImage(PaleMod.assetPath("images/bandit/spaces/BlockSquare" + AbstractBoard.artStyle + ".png"));
        this.goodness = GOODNESS.GOOD;
    }

    public void onLanded(AbstractCreature actor) {
        att(new GainBlockAction(actor, actor, BASEBLOCK));
    }

    public String getHeaderText() {
        return TEXT[0];
    }
    public String getBodyText() {
        return TEXT[1] + BASEBLOCK + TEXT[2];
    }
}