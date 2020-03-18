package paleoftheancients.bandit.board;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DescriptionLine;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.HemokinesisEffect;
import org.apache.commons.lang3.ArrayUtils;
import paleoftheancients.PaleMod;
import paleoftheancients.bandit.actions.AddSpaceAction;
import paleoftheancients.bandit.actions.SetSpaceAction;
import paleoftheancients.bandit.actions.WeirdMoveGuyAction;
import paleoftheancients.bandit.board.rarespaces.*;
import paleoftheancients.bandit.board.spaces.AbstractSpace;
import paleoftheancients.bandit.board.spaces.GoSpace;
import paleoftheancients.bandit.board.spaces.asymmetrical.GremlinSpace;
import paleoftheancients.bandit.board.spaces.asymmetrical.SpikeSpace;
import paleoftheancients.bandit.board.spaces.symmetrical.*;
import paleoftheancients.bandit.intent.EnumBuster;
import paleoftheancients.bandit.monsters.TheBandit;
import paleoftheancients.bandit.powers.BoardBoundPower;
import paleoftheancients.bandit.powers.ImprisonedPower;
import paleoftheancients.bandit.powers.KeyFinisherPower;
import paleoftheancients.helpers.AssetLoader;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class AbstractBoard {
    public ArrayList<AbstractSpace> squareList = new ArrayList<>();

    public Texture playerPiece = AssetLoader.loadImage(PaleMod.assetPath("images/bandit/pieces/piecePlayer1.png"));

    public Texture dronePiece = AssetLoader.loadImage(PaleMod.assetPath("images/bandit/pieces/pieceDrone.png"));


    public AbstractDrone player = new AbstractDrone(0, 0, 0);

    public boolean shouldRender = false;

    public ArrayList<AbstractDrone> droneList = new ArrayList<>();

    public static int artStyle = 1;

    public Texture goodOutline = AssetLoader.loadImage(PaleMod.assetPath("images/bandit/outlines/GoodOutline" + 2 + ".png"));
    public Texture neutralOutline = AssetLoader.loadImage(PaleMod.assetPath("images/bandit/outlines/NeutralOutline" + 2 + ".png"));
    public Texture badOutline = AssetLoader.loadImage(PaleMod.assetPath("images/bandit/outlines/BadOutline" + 2 + ".png"));
    public Texture activatesWhenPassedOutline = AssetLoader.loadImage(PaleMod.assetPath("images/bandit/outlines/ActivatesWhenPassedOutline" + 2 + ".png"));
    public Texture deathOutline = AssetLoader.loadImage(PaleMod.assetPath("images/bandit/outlines/DeathOutline" + 2 + ".png"));
    public Texture onOutline = AssetLoader.loadImage(PaleMod.assetPath("images/bandit/outlines/OnOutline" + 2 + ".png"));

    public TheBandit owner;
    public Map<String, Integer> cards;
    public Set<AbstractCard> processedCards;
    public Map<Integer, Integer> renderNumbers;
    private boolean finishedSetup;
    public AbstractBoard(TheBandit owner) {
        this.owner = owner;
        this.cards = new HashMap<>();
        this.renderNumbers = new HashMap<>();
        this.processedCards = new HashSet<>();
        this.finishedSetup = false;
    }

    public void init() {
        player.position = 0;
        artStyle = 1;

        squareList.clear();
        droneList.clear();
        ArrayList<Class<? extends AbstractSpace>> bruh = new ArrayList<>();
        for (int i = 0; i < 10; i++) bruh.add(getRandomCommonSquare());
        for (int i = 0; i < 2; i++) bruh.add(getRandomRareSquare());


        int x = (int) (Settings.WIDTH / 3.25);
        int y = (int) (Settings.HEIGHT / 1.75);
        final AbstractSpace s = new GoSpace(this, x, y);
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
        int kwab = 8;

        float squareOffset = 64F * Settings.scale;
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

        AbstractDungeon.actionManager.addToBottom(new WeirdMoveGuyAction(this, player.location.x, player.location.y - Settings.HEIGHT, 0.5F, player));
        AbstractDungeon.actionManager.addToBottom(new VFXAction(new HemokinesisEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, player.location.x, player.location.y - Settings.HEIGHT)));
        AbstractDungeon.actionManager.addToBottom(new VFXAction(new HemokinesisEffect(this.owner.hb.cX, this.owner.hb.cY, player.location.x, player.location.y - Settings.HEIGHT)));
        AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
            @Override
            public void update() {
                finishedSetup = true;
                this.isDone = true;
            }
        });

        shouldRender = true;
    }

    public static Class<? extends AbstractSpace> getRandomCommonSquare() {
        ArrayList<Class<? extends AbstractSpace>> list = new ArrayList<>();
        list.add(DamageSpace.class);
        list.add(BlockSpace.class);
        list.add(EnergySpace.class);
        list.add(SpikeSpace.class);
        list.add(GremlinSpace.class);
        list.add(DebilitateSpace.class);
        return list.get(AbstractDungeon.monsterRng.random(list.size() - 1));
    }

    private ArrayList<Class<? extends AbstractSpace>> rareHistory = new ArrayList<>();
    public Class<? extends AbstractSpace> getRandomRareSquare() {
        ArrayList<Class<? extends AbstractSpace>> list = new ArrayList<>();
        list.add(NobSpace.class);
        list.add(BufferSpace.class);
        list.add(CursedSpace.class);
        list.add(ArtifactSpace.class);
        list.add(DoomSpace.class);
        list.add(JailSpace.class);
        int count = list.size();

        for(Class<? extends AbstractSpace> clz : rareHistory) {
            list.remove(clz);
        }
        Class<? extends AbstractSpace> choice = list.get(AbstractDungeon.monsterRng.random(list.size() - 1));
        rareHistory.add(choice);
        if(rareHistory.size() == count) {
            rareHistory.clear();
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
        for (AbstractSpace s : squareList) {
            s.hb.update();
        }
    }

    public void render(SpriteBatch sb) {
        if (shouldRender) {
            sb.setColor(Color.WHITE.cpy());
            for (AbstractSpace s : squareList) {
                s.render(sb);
                if (s.hb.hovered) {
                    String q = s.getBodyText();
                    if (!(s instanceof GoSpace)) {
                        if(s.triggersWhenPassed) {
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

            //Draw the numbers on the tiles.
            if(finishedSetup && owner.displayNumbers) {
                int curOffset = 0;
                AbstractSpace space;
                int[] motion = new int[2];
                if (AbstractDungeon.player.hoveredCard != null) {
                    motion[0] = cards.getOrDefault(AbstractDungeon.player.hoveredCard.cardID, 1);
                }
                motion[1] = owner.getDisplayMotion();
                AbstractDrone[] pieceList = getPieces();
                ArrayUtils.reverse(pieceList);
                if (motion[0] > 0 || motion[1] > 0) {
                    Color[] colors = new Color[]{
                            Color.WHITE, Color.GOLD,
                            Color.RED, Color.PURPLE
                    };
                    if (motion[0] > 0 && motion[1] > 0) {//If both are displayed at the same time, make the enemy one a bit translucent.
                        for (int i = 2; i < colors.length; i++) {
                            colors[i] = colors[i].cpy();
                            colors[i].a = 0.6F;
                        }
                    }
                    Set<Integer> takenNumbers = new HashSet<>();
                    int index;
                    for (int i = 0; i < motion.length; i++) {
                        if (motion[i] > 0) {
                            boolean playerpiece = true;
                            for (final AbstractDrone piece : pieceList) {
                                if (playerpiece) {
                                    for (int j = 1; j < motion[i]; j++) {
                                        index = (piece.position + j + curOffset) % squareList.size();
                                        if(!takenNumbers.contains(index)) {
                                            space = squareList.get(index);
                                            this.renderSpaceNumber(sb, j, space.x, space.y, colors[i * 2]);
                                        }
                                    }
                                }
                                index = (piece.position + motion[i] + curOffset) % squareList.size();
                                takenNumbers.add(index);
                                space = squareList.get(index);
                                this.renderSpaceNumber(sb, motion[i], space.x, space.y, colors[1 + i * 2]);
                                playerpiece = false;
                            }
                            curOffset = motion[0];
                        }
                    }
                }
                if(owner.intent == EnumBuster.HappyHitIntent) {
                    int origAmount = owner.getMoveInfo().multiplier;
                    for(final AbstractDrone drone : pieceList) {
                        int amount = origAmount;
                        for (int i = 1; amount > 0; i++) {
                            space = squareList.get((drone.position + i) % squareList.size());
                            if(!(space instanceof EmptySpace)) {
                                amount--;
                                this.renderSpaceNumber(sb, 0, space.x, space.y, Color.RED);
                            }
                        }
                    }
                }
            }

            sb.draw(playerPiece, player.location.x - ((playerPiece.getWidth() * Settings.scale) / 2F), player.location.y - ((playerPiece.getHeight() * Settings.scale) / 2F), playerPiece.getWidth() / 2F, playerPiece.getHeight() / 2F, playerPiece.getWidth(), playerPiece.getHeight(), Settings.scale, Settings.scale, 0, 0, 0, playerPiece.getWidth(), playerPiece.getHeight(), false, false);
            if (!droneList.isEmpty()) {
                for (AbstractDrone r : droneList) {
                    sb.draw(dronePiece, r.location.x - ((dronePiece.getWidth() * Settings.scale) / 2F), r.location.y - ((dronePiece.getHeight() * Settings.scale) / 2F), dronePiece.getWidth() / 2F, dronePiece.getHeight() / 2F, dronePiece.getWidth(), dronePiece.getHeight(), Settings.scale, Settings.scale, 0, 0, 0, dronePiece.getWidth(), dronePiece.getHeight(), false, false);
                }
            }
        }
    }
    private void renderSpaceNumber(SpriteBatch sb, int number, float x, float y, Color color) {
        FontHelper.renderFontCentered(sb, FontHelper.energyNumFontRed, String.valueOf(number), x + 34.0F, y + 32.0F, color);
    }
    public AbstractDrone[] getPieces() {
        AbstractDrone[] list = new AbstractDrone[droneList.size() + 1];
        int count = 1;
        for(final AbstractDrone d : droneList) {
            list[count++] = d;
        }
        list[0] = player;
        return list;
    }

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
                squareList.get((r.position + i) % squareList.size()).onPassed(actor);
            }
            r.position = (r.position + amount) % squareList.size();
            cursquare = squareList.get(r.position);
            boolean splat = !(cursquare instanceof EmptySpace);
            cursquare.uponLand(actor);
            for (int i = 1; i < count; i++) {
                if (splat) {
                    cursquare.splat();
                }
                cursquare.onLanded(actor);
            }

            if (amount != 0) {
                AbstractDungeon.actionManager.addToTop(new WeirdMoveGuyAction(this, (int) cursquare.hb.cX, (int) cursquare.hb.cY, Math.min(amount / 10F, 0.66F), r));
            }
        }
    }

    public void processCard(AbstractCard card) {
        if(!cards.containsKey(card.cardID)) {
            int motion = AbstractDungeon.cardRandomRng.random(card.cost * 2) + AbstractDungeon.cardRandomRng.random(2);
            cards.put(card.cardID, motion);
        }
        this.addDescription(card);
    }
    public void addDescription(AbstractCard card) {
        if(!processedCards.contains(card)) {
            processedCards.add(card);
            card.initializeDescription();
            card.description.add(new DescriptionLine("[#FF6666]" + BoardBoundPower.DESCRIPTIONS[0] + " " + cards.get(card.cardID) + "[]", (BoardBoundPower.DESCRIPTIONS[0].length() + 2) * 12F));
        }
    }

    public void triggerNextSpaces(AbstractCreature actor, int amountOfSpaces, int amountOfTriggers) {
        AbstractSpace space;
        for (final AbstractDrone piece : getPieces()) {
            for (int found = 0; found < amountOfSpaces; ) {
                for (int i = 1; found < amountOfSpaces; i++) {
                    space = squareList.get(piece.position + i % squareList.size());
                    if (!(space instanceof EmptySpace)) {
                        found++;
                        space.uponLand(actor);
                        for(int j = 1; j < amountOfTriggers; j++) {
                            space.onLanded(actor);
                        }
                    }
                }
            }
        }
    }
}