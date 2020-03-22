package paleoftheancients.relics;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import paleoftheancients.PaleMod;
import paleoftheancients.bandit.actions.BoardMoveAction;
import paleoftheancients.bandit.board.RelicBoard;
import paleoftheancients.helpers.AssetLoader;

public class SoulOfTheBandit extends PaleRelic {
    public static final String ID = PaleMod.makeID("SoulOfTheBandit");

    private static final RelicTier TIER = RelicTier.SPECIAL;
    private static final LandingSound SOUND = LandingSound.CLINK;

    private RelicBoard board = null;

    public SoulOfTheBandit() {
        super(ID, AssetLoader.loadImage(PaleMod.assetPath("images/relics/bandit.png")), AssetLoader.loadImage(PaleMod.assetPath("images/relics/outline/bandit.png")), TIER, SOUND);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + RelicBoard.SIZE + DESCRIPTIONS[1];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new SoulOfTheBandit();
    }

    @Override
    public void atBattleStart() {
        board = new RelicBoard();
        board.init();
    }

    @Override
    public void onVictory() {
        board = null;
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        final int motion = board.getCardMotion(card, true);
        if(motion > 0) {
            AbstractDungeon.actionManager.addToBottom(new BoardMoveAction(AbstractDungeon.player, board, motion));
        }
    }

    @Override
    public void update() {
        super.update();
        if(this.board != null) {
            this.board.update();
        }
    }

    public void renderBoard(SpriteBatch sb) {
        if(this.board != null && !AbstractDungeon.isScreenUp) {
            this.board.render(sb);
        }
    }
}