package paleoftheancients.bandit.board.rarespaces;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import paleoftheancients.PaleMod;
import paleoftheancients.bandit.board.AbstractBoard;
import paleoftheancients.bandit.board.BanditBoard;
import paleoftheancients.bandit.board.spaces.AbstractSpace;
import paleoftheancients.bandit.powers.ImprisonedPower;
import paleoftheancients.helpers.AssetLoader;

public class JailSpace extends AbstractSpace {
    private static String[] TEXT = CardCrawlGame.languagePack.getUIString(PaleMod.makeID("JailSpace")).TEXT;

    public JailSpace(AbstractBoard board, int x, int y) {
        super(board, x, y);
        this.tex = AssetLoader.loadImage(PaleMod.assetPath("images/bandit/spaces/JailSquare" + board.artStyle + ".png"));
        this.goodness = GOODNESS.GOOD;
        this.explodeOnUse = true;
    }

    public void onLanded(AbstractCreature actor) {
        AbstractCreature target;
        if(actor == ((BanditBoard) board).owner) {
            target = AbstractDungeon.player;
        } else {
            target = ((BanditBoard) board).owner;
        }
        att(new ApplyPowerAction(target, actor, new ImprisonedPower(target)));
    }

    public String getHeaderText() {
        return TEXT[0];
    }
    public String getBodyText() {
        return TEXT[1] + BASETEXT[2];
    }
}