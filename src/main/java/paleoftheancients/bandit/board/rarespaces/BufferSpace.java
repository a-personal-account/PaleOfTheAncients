package paleoftheancients.bandit.board.rarespaces;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.powers.BufferPower;
import paleoftheancients.PaleMod;
import paleoftheancients.bandit.board.AbstractBoard;
import paleoftheancients.bandit.board.spaces.AbstractSpace;
import paleoftheancients.helpers.AssetLoader;

public class BufferSpace extends AbstractSpace {
    private static String[] TEXT = CardCrawlGame.languagePack.getUIString(PaleMod.makeID("BufferSpace")).TEXT;

    public BufferSpace(AbstractBoard board, int x, int y) {
        super(board, x, y);
        this.tex = AssetLoader.loadImage(PaleMod.assetPath("images/bandit/spaces/BufferSquare" + AbstractBoard.artStyle + ".png"));
        this.goodness = GOODNESS.GOOD;
        this.explodeOnUse = true;
    }

    public void onLanded(AbstractCreature actor) {
        att(new ApplyPowerAction(actor, actor, new BufferPower(actor, 1), 1));
    }

    public String getHeaderText() {
        return TEXT[0];
    }
    public String getBodyText() {
        return TEXT[1] + BASETEXT[2];
    }
}