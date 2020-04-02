package paleoftheancients.bandit.board;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.random.Random;
import org.apache.commons.lang3.ArrayUtils;
import paleoftheancients.PaleMod;
import paleoftheancients.bandit.board.spaces.AbstractSpace;
import paleoftheancients.bandit.board.spaces.GoSpace;
import paleoftheancients.bandit.board.spaces.symmetrical.EmptySpace;
import paleoftheancients.bandit.powers.ImprisonedPower;
import paleoftheancients.bandit.powers.KeyFinisherPower;
import paleoftheancients.helpers.AssetLoader;
import paleoftheancients.vfx.FlashingIntentVFX;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class AbstractBoard {
    public ArrayList<AbstractSpace> squareList = new ArrayList<>();

    public Texture playerPiece = AssetLoader.loadImage(PaleMod.assetPath("images/bandit/pieces/piecePlayer1.png"));

    public AbstractDrone player = new AbstractDrone(0, 0, 0);

    public boolean shouldRender = false;

    public final int artStyle = 1;

    public Texture goodOutline = AssetLoader.loadImage(PaleMod.assetPath("images/bandit/outlines/GoodOutline" + 2 + ".png"));
    public Texture neutralOutline = AssetLoader.loadImage(PaleMod.assetPath("images/bandit/outlines/NeutralOutline" + 2 + ".png"));
    public Texture badOutline = AssetLoader.loadImage(PaleMod.assetPath("images/bandit/outlines/BadOutline" + 2 + ".png"));
    public Texture activatesWhenPassedOutline = AssetLoader.loadImage(PaleMod.assetPath("images/bandit/outlines/ActivatesWhenPassedOutline" + 2 + ".png"));
    public Texture deathOutline = AssetLoader.loadImage(PaleMod.assetPath("images/bandit/outlines/DeathOutline" + 2 + ".png"));
    public Texture onOutline = AssetLoader.loadImage(PaleMod.assetPath("images/bandit/outlines/OnOutline" + 2 + ".png"));

    public final float squareOffset = 64F * Settings.scale;

    protected Map<Integer, SquareRenderingInfo> squareRenderingInfoMap = new HashMap<>();
    protected boolean finishedSetup;
    protected float pulseTimer;
    public AbstractBoard() {
        this.finishedSetup = false;
        this.pulseTimer = 0F;
    }

    public void init() {
        player.position = 0;

        squareList.clear();
    }

    protected Class<? extends AbstractSpace> processHistory(ArrayList<Class<? extends AbstractSpace>> possibilities, ArrayList<Class<? extends AbstractSpace>> history, Random rng) {
        int count = possibilities.size();

        for(Class<? extends AbstractSpace> clz : history) {
            possibilities.remove(clz);
        }
        Class<? extends AbstractSpace> choice = possibilities.get(rng.random(possibilities.size() - 1));
        history.add(choice);
        if(history.size() == count) {
            history.clear();
        }

        return choice;
    }

    public AbstractSpace swapSquare(Class<? extends AbstractSpace> square, int x, int y) {
        try {
            return square.getConstructor(AbstractBoard.class, int.class, int.class).newInstance(this, x, y);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return new EmptySpace(this, x, y);
    }

    public void update() {
        squareRenderingInfoMap.clear();
        for (AbstractSpace s : squareList) {
            s.hb.update();
        }

        this.updateBoardSpecifics();

        this.pulseTimer -= Gdx.graphics.getDeltaTime();
        if(this.pulseTimer <= 0F) {
            this.pulseTimer = 0.2F;

            for(final Map.Entry<Integer, SquareRenderingInfo> sqi : squareRenderingInfoMap.entrySet()) {
                if(sqi.getValue().pulsing) {
                    AbstractSpace space = this.squareList.get(sqi.getKey());
                    AbstractDungeon.effectList.add(new FlashingIntentVFX(space.getOutline(), space.hb.cX, space.hb.cY, 2F, 0.7F, 0.6F));
                }
            }
        }
    }
    protected abstract void updateBoardSpecifics();

    public void render(SpriteBatch sb) {
        if (shouldRender) {
            sb.setColor(Color.WHITE.cpy());
            for (AbstractSpace s : squareList) {
                if(this.renderSpecificSpace(s)) {
                    s.render(sb);
                    if (s.hb.hovered) {
                        String q = s.getBodyText();
                        if (!(s instanceof GoSpace)) {
                            if (s.triggersWhenPassed) {
                                q = AbstractSpace.BASETEXT[1] + q;
                            } else {
                                q = AbstractSpace.BASETEXT[0] + q;
                            }
                        }
                        if ((float) InputHelper.mX < 1400.0F * Settings.scale) {
                            TipHelper.renderGenericTip(
                                    (float) InputHelper.mX + 60.0F * Settings.scale, (float) InputHelper.mY - 50.0F * Settings.scale,
                                    s.getHeaderText(),
                                    q
                            );
                        } else {
                            TipHelper.renderGenericTip((float) InputHelper.mX - 350.0F * Settings.scale, (float) InputHelper.mY - 50.0F * Settings.scale,
                                    s.getHeaderText(),
                                    q);
                        }
                    }
                }
            }

            //Draw the numbers on the tiles.
            if(finishedSetup) {
                AbstractSpace space;
                for(final Map.Entry<Integer, SquareRenderingInfo> sqi : squareRenderingInfoMap.entrySet()) {
                    space = squareList.get(sqi.getKey());
                    SquareRenderingInfo sri = sqi.getValue();
                    if(sri.color != null) {
                        this.renderSpaceNumber(sb, sri.number, space.x, space.y, sri.color);
                    }
                }
            }
            sb.setColor(Color.WHITE);
            sb.draw(playerPiece, player.location.x - ((playerPiece.getWidth() * Settings.scale) / 2F), player.location.y - ((playerPiece.getHeight() * Settings.scale) / 2F), playerPiece.getWidth() / 2F, playerPiece.getHeight() / 2F, playerPiece.getWidth(), playerPiece.getHeight(), Settings.scale, Settings.scale, 0, 0, 0, playerPiece.getWidth(), playerPiece.getHeight(), false, false);
            this.renderExtraPieces();
        }
    }
    protected boolean renderSpecificSpace(AbstractSpace space) {
        return true;
    }

    protected void renderSpaceNumber(SpriteBatch sb, int number, float x, float y, Color color) {
        FontHelper.renderFontCentered(sb, FontHelper.energyNumFontRed, String.valueOf(number), x + 34.0F, y + 32.0F, color);
    }

    protected void renderNumbersFromPosition(SpriteBatch sb, int curPosition, int jump, Set<Integer> takenNumbers, Color endColor) {
        this.renderNumbersFromPosition(sb, curPosition, jump, takenNumbers, endColor, null);
    }
    protected void renderNumbersFromPosition(SpriteBatch sb, int curPosition, int jump, Set<Integer> takenNumbers, Color endColor, Color passingColor) {
        int index;
        AbstractSpace space;
        if (passingColor != null) {
            for (int j = 1; j < jump; j++) {
                index = (curPosition + j) % squareList.size();
                if(takenNumbers == null || !takenNumbers.contains(index)) {
                    space = squareList.get(index);
                    this.renderSpaceNumber(sb, j, space.x, space.y, passingColor);
                }
            }
        }
        index = (curPosition + jump) % squareList.size();
        if(takenNumbers != null) {
            takenNumbers.add(index);
        }
        space = squareList.get(index);
        this.renderSpaceNumber(sb, jump, space.x, space.y, endColor);
    }

    protected void renderExtraPieces() {}

    public void transform(AbstractSpace oldSquare, AbstractSpace newSquare) {
        int i = squareList.indexOf(oldSquare);
        transform(i, newSquare);
    }

    public void transform(int index, AbstractSpace newSquare) {
        if (!(squareList.get(index) instanceof GoSpace)) {
            squareList.set(index, newSquare);
            newSquare.splat();
        }
    }

    public void move(AbstractCreature actor, AbstractSpace square) {
        move(actor, ((squareList.indexOf(square) - player.position) + squareList.size()) % squareList.size());
    }
    public void move(AbstractCreature actor, int amount) {
        if(actor.hasPower(ImprisonedPower.POWER_ID)) {
            return;
        }

        AbstractSpace cursquare;
        int count = 1;
        AbstractPower pow = actor.getPower(KeyFinisherPower.POWER_ID);
        if(pow != null) {
            count += pow.amount;
        }

        AbstractDrone[] pieces = getPieces();
        ArrayUtils.reverse(pieces);
        for (AbstractDrone r : pieces) {
            for (int i = 1; i < amount; i++) {
                //for(int j = 0; j < count; j++) {
                squareList.get((r.position + i) % squareList.size()).onPassed(actor);
                //}
            }
            r.position = (r.position + amount) % squareList.size();
            cursquare = squareList.get(r.position);
            for (int i = 1; i < count; i++) {
                cursquare.onLanded(actor);
            }
            if (!(cursquare instanceof EmptySpace)) {
                cursquare.splat();
            }
            cursquare.uponLand(actor);

            if (amount != 0) {
                movePieces(amount, (int) cursquare.hb.cX, (int) cursquare.hb.cY, Math.min(amount / 10F, 0.66F), r);
            }
        }
    }
    public AbstractDrone[] getPieces() {
        return new AbstractDrone[]{player};
    }
    protected abstract void movePieces(int jumpdistance, int x, int y, float speed, AbstractDrone piece);

    protected static class SquareRenderingInfo {
        int number;
        Color color;
        boolean pulsing;

        public SquareRenderingInfo(int number, Color color, boolean pulsing) {
            this.number = number;
            this.color = color;
            this.pulsing = pulsing;
        }
    }
}