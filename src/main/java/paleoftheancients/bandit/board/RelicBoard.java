package paleoftheancients.bandit.board;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import paleoftheancients.bandit.actions.WeirdMoveBoardAction;
import paleoftheancients.bandit.actions.WeirdMoveGuyAction;
import paleoftheancients.bandit.board.rarespaces.ArtifactSpace;
import paleoftheancients.bandit.board.rarespaces.BufferSpace;
import paleoftheancients.bandit.board.spaces.AbstractSpace;
import paleoftheancients.bandit.board.spaces.RelicGoSpace;
import paleoftheancients.bandit.board.spaces.symmetrical.*;

import java.util.ArrayList;

public class RelicBoard extends AbstractBoard {
    public static final int SIZE = 20;
    public final int DISPLAYEDSPACES = 5;

    public void init() {
        super.init();

        ArrayList<Class<? extends AbstractSpace>> bruh = new ArrayList<>();
        for (int i = 0; i < SIZE - 1; i++) bruh.add(getRandomGoodSquare());

        int x = leftBoundary();
        int y = (int) (AbstractDungeon.player.drawY + AbstractDungeon.player.hb.height + squareOffset);
        final AbstractSpace s = new RelicGoSpace(this, x, y);
        squareList.add(s);
        player.location.move((int) s.hb.cX, (int) s.hb.cY + Settings.HEIGHT);

        do {
            x += squareOffset;
            squareList.add(swapSquare(bruh.remove(AbstractDungeon.cardRng.random(bruh.size() - 1)), x, y));
            if(squareList.size() == DISPLAYEDSPACES) {
                x -= squareOffset * SIZE;
            }
        } while(!bruh.isEmpty());

        AbstractDungeon.actionManager.addToBottom(new WeirdMoveGuyAction(this, player.location.x, player.location.y - Settings.HEIGHT, 0.5F, player));
        AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
            @Override
            public void update() {
                finishedSetup = true;
                this.isDone = true;
            }
        });

        shouldRender = true;
    }

    public int leftBoundary() {
        return (int)(AbstractDungeon.player.hb.cX - squareOffset * (DISPLAYEDSPACES / 2F));
    }

    private ArrayList<Class<? extends AbstractSpace>> goodHistory = new ArrayList<>();
    public Class<? extends AbstractSpace> getRandomGoodSquare() {
        ArrayList<Class<? extends AbstractSpace>> list = new ArrayList<>();
        list.add(DamageSpace.class);
        list.add(BlockSpace.class);
        list.add(EnergySpace.class);
        list.add(DebilitateSpace.class);
        list.add(StrengthSpace.class);
        list.add(BufferSpace.class);
        list.add(ArtifactSpace.class);

        return processHistory(list, goodHistory, AbstractDungeon.cardRandomRng);
    }

    @Override
    protected boolean renderSpecificSpace(AbstractSpace space) {
        return space.x + space.hb.width >= player.location.x;
    }

    @Override
    protected void renderBoardNumbers(SpriteBatch sb) {
        if (AbstractDungeon.player.hoveredCard != null) {
            int motion = this.getCardMotion(AbstractDungeon.player.hoveredCard, false);
            if(motion > 0) {
                this.renderNumbersFromPosition(sb, player.position, motion, null, Color.GOLD, Color.WHITE);
            }
        }
    }

    @Override
    protected void movePieces(int jumpdistance, int x, int y, float speed, AbstractDrone piece) {
        AbstractDungeon.actionManager.addToTop(new WeirdMoveBoardAction(this, speed));
    }

    @Override
    public void update() {
        super.update();
        for(final AbstractSpace space : squareList) {
            space.updateAlpha();
        }
    }

    public int getCardMotion(AbstractCard card, boolean played) {
        if(!card.freeToPlay()) {
            int motion = card.isCostModifiedForTurn ? card.costForTurn : card.cost;
            if(motion > 0) {
                return motion;
            } else if(motion == -1) {
                if(played) {
                    return card.energyOnUse;
                } else {
                    return EnergyPanel.totalCount;
                }
            }
        }
        return 0;
    }
}