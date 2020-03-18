package paleoftheancients.bandit.board.spaces.symmetrical;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import paleoftheancients.PaleMod;
import paleoftheancients.bandit.board.AbstractBoard;
import paleoftheancients.bandit.board.spaces.AbstractSpace;
import paleoftheancients.bandit.powers.EnergyDownNextTurnPower;
import paleoftheancients.helpers.AssetLoader;

public class EnergySpace extends AbstractSpace {
    private static String[] TEXT = CardCrawlGame.languagePack.getUIString(PaleMod.makeID("EnergySpace")).TEXT;

    public EnergySpace(AbstractBoard board, int x, int y) {
        super(board, x, y);
        this.tex = AssetLoader.loadImage(PaleMod.assetPath("images/bandit/spaces/EnergySquare" + AbstractBoard.artStyle + ".png"));
        this.goodness = GOODNESS.GOOD;
    }

    public void onLanded(AbstractCreature actor) {
        if(actor == board.owner) {
            att(new ApplyPowerAction(AbstractDungeon.player, actor, new EnergyDownNextTurnPower(AbstractDungeon.player, 1), 1));
        } else {
            att(new GainEnergyAction(1));
        }
    }

    public String getHeaderText() {
        return TEXT[0];
    }
    public String getBodyText() {
        return TEXT[1];
    }
}