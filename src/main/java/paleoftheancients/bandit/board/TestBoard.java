package paleoftheancients.bandit.board;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.vfx.combat.HemokinesisEffect;
import paleoftheancients.bandit.actions.AddSpaceAction;
import paleoftheancients.bandit.actions.FoldBoardAction;
import paleoftheancients.bandit.actions.SetSpaceAction;
import paleoftheancients.bandit.actions.WeirdMoveGuyAction;
import paleoftheancients.bandit.board.rarespaces.*;
import paleoftheancients.bandit.board.spaces.AbstractSpace;
import paleoftheancients.bandit.board.spaces.RelicGoSpace;
import paleoftheancients.bandit.board.spaces.asymmetrical.GremlinSpace;
import paleoftheancients.bandit.board.spaces.asymmetrical.SpikeSpace;
import paleoftheancients.bandit.board.spaces.symmetrical.*;

import java.util.ArrayList;

public class TestBoard extends AbstractBoard {

    public TestBoard() {
    }

    public void init() {
        super.init();

        ArrayList<Class<? extends AbstractSpace>> bruh = new ArrayList<>();
        for (int i = 0; i < 10; i++) bruh.add(getRandomCommonSquare());
        for (int i = 0; i < 2; i++) bruh.add(getRandomRareSquare());


        int kwab = 8;
        int x = (int) (Settings.WIDTH / 2F - squareOffset * (kwab + 1) / 2F);
        int y = (int) (Settings.HEIGHT / 1.75);
        final AbstractSpace s = new RelicGoSpace(this, x, y);
        AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
            @Override
            public void update() {
                this.tickDuration();
                if(this.isDone) {
                    squareList.add(s);
                }
            }
        });
        player.location.move((int) s.hb.cX, (int) s.hb.cY + Settings.HEIGHT);

        ArrayList<Integer> slots = new ArrayList<>();
        for (int q = 0; q < kwab; q++) {
            x += squareOffset;
            AbstractDungeon.actionManager.addToBottom(new AddSpaceAction(this, EmptySpace.class, x, y));
            slots.add(slots.size() + 1);
        }
        for (int q = 0; q < 4; q++) {
            y += squareOffset;
            AbstractDungeon.actionManager.addToBottom(new AddSpaceAction(this, EmptySpace.class, x, y));
            slots.add(slots.size() + 1);
        }
        for (int q = 0; q < kwab; q++) {
            x = (int) (Math.ceil(x - squareOffset));
            AbstractDungeon.actionManager.addToBottom(new AddSpaceAction(this, EmptySpace.class, x, y));
            slots.add(slots.size() + 1);
        }
        for (int q = 0; q < 3; q++) {
            y = (int) (Math.ceil(y - squareOffset));
            AbstractDungeon.actionManager.addToBottom(new AddSpaceAction(this, EmptySpace.class, x, y));
            slots.add(slots.size() + 1);
        }

        do {
            AbstractDungeon.actionManager.addToBottom(new SetSpaceAction(this, bruh.remove(AbstractDungeon.monsterRng.random(bruh.size() - 1)), slots.remove(AbstractDungeon.monsterRng.random(slots.size() - 1))));
        } while(!bruh.isEmpty());

        AbstractDungeon.actionManager.addToBottom(new FoldBoardAction(this, 0F, 90F, kwab));
        AbstractDungeon.actionManager.addToBottom(new FoldBoardAction(this, 90F, 180F, kwab + 4));
        AbstractDungeon.actionManager.addToBottom(new FoldBoardAction(this, 180F, 270F, kwab * 2 + 4));

        AbstractDungeon.actionManager.addToBottom(new WeirdMoveGuyAction(this, player.location.x, player.location.y - Settings.HEIGHT, 0.5F, player));
        AbstractDungeon.actionManager.addToBottom(new VFXAction(new HemokinesisEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, player.location.x, player.location.y - Settings.HEIGHT)));

        shouldRender = true;
    }

    private ArrayList<Class<? extends AbstractSpace>> commonHistory = new ArrayList<>();
    public Class<? extends AbstractSpace> getRandomCommonSquare() {
        return this.getRandomCommonSquare(AbstractDungeon.monsterRng);
    }
    public Class<? extends AbstractSpace> getRandomCommonSquare(Random rng) {
        ArrayList<Class<? extends AbstractSpace>> list = new ArrayList<>();
        list.add(DamageSpace.class);
        list.add(BlockSpace.class);
        list.add(EnergySpace.class);
        list.add(SpikeSpace.class);
        list.add(GremlinSpace.class);
        list.add(DebilitateSpace.class);
        list.add(StrengthSpace.class);

        return processHistory(list, commonHistory, rng);
    }

    private ArrayList<Class<? extends AbstractSpace>> rareHistory = new ArrayList<>();
    public void resetRareHistory() {
        rareHistory.clear();
    }
    public Class<? extends AbstractSpace> getRandomRareSquare() {
        return this.getRandomRareSquare(AbstractDungeon.monsterRng);
    }
    public Class<? extends AbstractSpace> getRandomRareSquare(Random rng) {
        ArrayList<Class<? extends AbstractSpace>> list = new ArrayList<>();
        list.add(NobSpace.class);
        list.add(BufferSpace.class);
        list.add(CursedSpace.class);
        list.add(ArtifactSpace.class);
        list.add(DoomSpace.class);
        //Want to encourage movement in the later phase.

        return processHistory(list, rareHistory, rng);
    }

    @Override
    public void updateBoardSpecifics() {
    }

    @Override
    protected void movePieces(int jumpdistance, int x, int y, float speed, AbstractDrone piece) {
        AbstractDungeon.actionManager.addToTop(new WeirdMoveGuyAction(this, x, y, speed, piece));
    }
}