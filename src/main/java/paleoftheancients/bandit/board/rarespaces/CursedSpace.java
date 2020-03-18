package paleoftheancients.bandit.board.rarespaces;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.curses.Necronomicurse;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.FontHelper;
import paleoftheancients.PaleMod;
import paleoftheancients.bandit.board.AbstractBoard;
import paleoftheancients.bandit.board.spaces.AbstractSpace;
import paleoftheancients.helpers.AssetLoader;

public class CursedSpace extends AbstractSpace {
    private static String[] TEXT = CardCrawlGame.languagePack.getUIString(PaleMod.makeID("CursedSpace")).TEXT;
    private int amount;
    private boolean ontop;

    public CursedSpace(AbstractBoard board, int x, int y) {
        super(board, x, y);
        this.tex = AssetLoader.loadImage(PaleMod.assetPath("images/bandit/spaces/curse.png"));
        this.goodness = GOODNESS.BAD;
        this.explodeOnUse = true;

        this.amount = 1;
        if(AbstractDungeon.ascensionLevel >= 4) {
            this.amount++;
        }
        if(AbstractDungeon.ascensionLevel >= 19) {
            this.amount++;
        }
        ontop = AbstractDungeon.ascensionLevel >= 9;
    }

    public void onLanded(AbstractCreature actor) {
        for(int i = 0; i < amount; i++) {
            att(new MakeTempCardInDrawPileAction(CardLibrary.getCurse(CardLibrary.getCard(Necronomicurse.ID), AbstractDungeon.monsterRng).makeCopy(), 1, !ontop, !ontop));
        }
    }

    public String getHeaderText() {
        return TEXT[0];
    }
    public String getBodyText() {
        String result = "";
        result += TEXT[ontop ? 1 : 2];
        if(amount > 1) {
            result += FontHelper.colorString(Integer.toString(amount), "b") + TEXT[4];
        } else {
            result += TEXT[3];
        }
        result += TEXT[ontop ? 5 : 6] + TEXT[7];
        return result + BASETEXT[2];
    }
}