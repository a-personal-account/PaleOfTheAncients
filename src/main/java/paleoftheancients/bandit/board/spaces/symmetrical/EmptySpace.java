package paleoftheancients.bandit.board.spaces.symmetrical;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.powers.AbstractPower;
import paleoftheancients.PaleMod;
import paleoftheancients.bandit.board.AbstractBoard;
import paleoftheancients.bandit.board.BanditBoard;
import paleoftheancients.bandit.board.spaces.AbstractSpace;
import paleoftheancients.bandit.powers.EmptyMindPower;
import paleoftheancients.helpers.AssetLoader;

public class EmptySpace extends AbstractSpace {
    private static String[] TEXT = CardCrawlGame.languagePack.getUIString(PaleMod.makeID("EmptySpace")).TEXT;
    public EmptySpace(AbstractBoard board, int x, int y) {
        super(board, x, y);
        this.tex = AssetLoader.loadImage(PaleMod.assetPath("images/bandit/spaces/EmptySquare" + board.artStyle + ".png"));
        this.goodness = AbstractSpace.GOODNESS.OKAY;
    }

    public void onLanded(AbstractCreature actor) {
        if(board instanceof BanditBoard) {
            AbstractPower pow = ((BanditBoard) board).owner.getPower(EmptyMindPower.POWER_ID);
            if (pow != null) {
                atb(new GainBlockAction(((BanditBoard) board).owner, actor, pow.amount));
            }
        }
    }

    public String getHeaderText() {
        return TEXT[0];
    }
    public String getBodyText() {
        return TEXT[1];
    }
}