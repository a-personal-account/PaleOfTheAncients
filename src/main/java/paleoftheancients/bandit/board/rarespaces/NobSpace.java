package paleoftheancients.bandit.board.rarespaces;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.exordium.GremlinNob;
import paleoftheancients.PaleMod;
import paleoftheancients.bandit.actions.SpawnMonsterAutoPositionAction;
import paleoftheancients.bandit.actions.SummonGremlinAction;
import paleoftheancients.bandit.board.AbstractBoard;
import paleoftheancients.bandit.board.spaces.AbstractSpace;
import paleoftheancients.helpers.AssetLoader;

public class NobSpace extends AbstractSpace {
    private static String[] TEXT = CardCrawlGame.languagePack.getUIString(PaleMod.makeID("NobSpace")).TEXT;

    public NobSpace(AbstractBoard board, int x, int y) {
        super(board, x, y);
        this.tex = AssetLoader.loadImage(PaleMod.assetPath("images/bandit/spaces/nob.png"));
        this.goodness = GOODNESS.BAD;
        this.explodeOnUse = true;
    }

    public void onLanded(AbstractCreature actor) {
        att(new SpawnMonsterAutoPositionAction(new GremlinNob(0F, 0F), false, SummonGremlinAction.leftMostCoordinate()));
    }

    public String getHeaderText() {
        return TEXT[0];
    }
    public String getBodyText() {
        return TEXT[1] + BASETEXT[2];
    }
}