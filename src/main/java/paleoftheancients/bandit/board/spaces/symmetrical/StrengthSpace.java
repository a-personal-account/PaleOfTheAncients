package paleoftheancients.bandit.board.spaces.symmetrical;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.powers.StrengthPower;
import paleoftheancients.PaleMod;
import paleoftheancients.bandit.board.AbstractBoard;
import paleoftheancients.bandit.board.spaces.AbstractSpace;
import paleoftheancients.helpers.AssetLoader;

public class StrengthSpace extends AbstractSpace {
    private static final String[] TEXT = CardCrawlGame.languagePack.getUIString(PaleMod.makeID("StrengthSpace")).TEXT;
    private static final int STRENGTH = 1;

    public StrengthSpace(AbstractBoard board, int x, int y) {
        super(board, x, y);
        this.tex = AssetLoader.loadImage(PaleMod.assetPath("images/bandit/spaces/StrengthSquare.png"));
        this.goodness = GOODNESS.GOOD;
    }

    public void onLanded(AbstractCreature actor) {
        att(new ApplyPowerAction(actor, actor, new StrengthPower(actor, STRENGTH), STRENGTH));
    }

    @Override
    public String getHeaderText() {
        return TEXT[0];
    }
    public String getBodyText() {
        return TEXT[1] + STRENGTH + TEXT[2];
    }
}
