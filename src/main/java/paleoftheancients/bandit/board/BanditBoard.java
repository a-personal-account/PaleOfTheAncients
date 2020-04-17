package paleoftheancients.bandit.board;

import basemod.helpers.CardModifierManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.vfx.combat.HemokinesisEffect;
import paleoftheancients.PaleMod;
import paleoftheancients.bandit.actions.AddSpaceAction;
import paleoftheancients.bandit.actions.SetSpaceAction;
import paleoftheancients.bandit.actions.WeirdMoveGuyAction;
import paleoftheancients.bandit.board.rarespaces.*;
import paleoftheancients.bandit.board.spaces.AbstractSpace;
import paleoftheancients.bandit.board.spaces.BanditGoSpace;
import paleoftheancients.bandit.board.spaces.asymmetrical.GremlinSpace;
import paleoftheancients.bandit.board.spaces.asymmetrical.SpikeSpace;
import paleoftheancients.bandit.board.spaces.symmetrical.*;
import paleoftheancients.bandit.intent.EnumBuster;
import paleoftheancients.bandit.monsters.TheBandit;
import paleoftheancients.bandit.powers.ImprisonedPower;
import paleoftheancients.bandit.util.MoveModifier;
import paleoftheancients.helpers.AssetLoader;

import java.util.*;

public class BanditBoard extends AbstractBoard {

    public ArrayList<AbstractDrone> droneList = new ArrayList<>();

    public Texture dronePiece = AssetLoader.loadImage(PaleMod.assetPath("images/bandit/pieces/piecePlayer2.png"));

    public TheBandit owner;
    public Map<String, Integer> cards;
    public Set<AbstractCard> processedCards;
    public Map<Integer, Integer> renderNumbers;
    public BanditBoard(TheBandit owner) {
        this.owner = owner;
        this.cards = new HashMap<>();
        this.renderNumbers = new HashMap<>();
        this.processedCards = new HashSet<>();
    }

    public void init() {
        super.init();

        droneList.clear();
        ArrayList<Class<? extends AbstractSpace>> bruh = new ArrayList<>();
        for (int i = 0; i < 10; i++) bruh.add(getRandomCommonSquare());
        for (int i = 0; i < 2; i++) bruh.add(getRandomRareSquare());


        int kwab = 8;
        int x = (int) (Settings.WIDTH / 2F - squareOffset * (kwab + 1) / 2F);
        int y = (int) (Settings.HEIGHT / 1.75);
        final AbstractSpace s = new BanditGoSpace(this, x, y);
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
        if(!owner.phasetwo) {
            list.add(JailSpace.class);
        }

        return processHistory(list, rareHistory, rng);
    }

    @Override
    public void updateBoardSpecifics() {
        if(owner.displayNumbers) {
            int curOffset = 0;
            boolean playerDisplayed = false;
            AbstractSpace space;
            ArrayList<Integer> motion = new ArrayList<>();
            if (AbstractDungeon.player.hoveredCard != null && !AbstractDungeon.player.hasPower(ImprisonedPower.POWER_ID)) {
                motion.add(cards.getOrDefault(AbstractDungeon.player.hoveredCard.cardID, 1));
                playerDisplayed = true;
            }

            motion.addAll(owner.getDisplayMotion());

            AbstractDrone[] pieceList = getPieces();
            //ArrayUtils.reverse(pieceList);

            Color[] colors = new Color[]{
                    Color.WHITE, Color.GOLD,
                    Color.RED, Color.PURPLE
            };
            if (playerDisplayed) {//If both are displayed at the same time, make the enemy one a bit translucent.
                for (int i = 2; i < colors.length; i++) {
                    colors[i] = colors[i].cpy();
                    colors[i].a = 0.6F;
                }
            } else {
                colors[0] = colors[2];
                colors[1] = colors[3];
            }

            for (int i = 0; i < motion.size(); i++) {
                for (final AbstractDrone piece : pieceList) {
                    for(int number = motion.get(i); (number > 0 && piece == player) || number == motion.get(i); number--) {
                        squareRenderingInfoMap.put((piece.position + curOffset + number) % squareList.size(),
                                new SquareRenderingInfo((i == 0 || owner.intent != EnumBuster.HappyHitIntent) ? number : 0, colors[Math.min(1, i) * 2 + (number == motion.get(i) ? 1 : 0)], number == motion.get(i)));
                    }
                }
                curOffset += motion.get(i);
            }

            if (owner.intent == EnumBuster.HappyHitIntent) {
                int origAmount = owner.getMoveInfo().multiplier;
                for (final AbstractDrone drone : pieceList) {
                    int amount = origAmount;
                    for (int i = 1; amount > 0; i++) {
                        space = squareList.get((drone.position + curOffset + i) % squareList.size());
                        if (!(space instanceof EmptySpace)) {
                            amount--;
                            squareRenderingInfoMap.put((drone.position + curOffset + i) % squareList.size(), new SquareRenderingInfo(0, Color.RED, true));
                        }
                    }
                }
            } else if(owner.intent == EnumBuster.MassivePartyIntent) {
                for (int i = 0; i < squareList.size(); i++) {
                    if(!(squareList.get(i) instanceof EmptySpace) && !squareRenderingInfoMap.containsKey(i)) {
                        squareRenderingInfoMap.put(i, new SquareRenderingInfo(0, null, true));
                    }
                }
            } else if(owner.nextMove == TheBandit.DEADLYDASH) {
                for(int i = 0; i < squareList.size(); i++) {
                    if(!(squareList.get((player.position + curOffset + i) % squareList.size()) instanceof EmptySpace)) {
                        for (final AbstractDrone piece : pieceList) {
                            if(!squareRenderingInfoMap.containsKey((piece.position + curOffset + i + 1) % squareList.size())) {
                                squareRenderingInfoMap.put((piece.position + curOffset + i + 1) % squareList.size(),
                                        new SquareRenderingInfo(1, colors[3], true));
                            }
                        }
                    } else {
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        if (shouldRender) {
            super.render(sb);
            if (!droneList.isEmpty()) {
                for (AbstractDrone r : droneList) {
                    sb.draw(dronePiece, r.location.x - ((dronePiece.getWidth() * Settings.scale) / 2F), r.location.y - ((dronePiece.getHeight() * Settings.scale) / 2F), dronePiece.getWidth() / 2F, dronePiece.getHeight() / 2F, dronePiece.getWidth(), dronePiece.getHeight(), Settings.scale, Settings.scale, 0, 0, 0, dronePiece.getWidth(), dronePiece.getHeight(), false, false);
                }
            }
        }
    }

    @Override
    public AbstractDrone[] getPieces() {
        AbstractDrone[] list = new AbstractDrone[droneList.size() + 1];
        int count = 1;
        for(final AbstractDrone d : droneList) {
            list[count++] = d;
        }
        list[0] = player;
        return list;
    }

    @Override
    protected void movePieces(int jumpdistance, int x, int y, float speed, AbstractDrone piece) {
        AbstractDungeon.actionManager.addToTop(new WeirdMoveGuyAction(this, x, y, speed, piece));
    }


    public void processCard(AbstractCard card) {
        if(!cards.containsKey(card.cardID)) {
            int motion = AbstractDungeon.cardRandomRng.random(2);
            if(card.cost >= 0) {
                motion += AbstractDungeon.cardRandomRng.random(card.cost * 2);
            }
            cards.put(card.cardID, motion);
        }
        this.addDescription(card);
    }
    public void addDescription(AbstractCard card) {
        if(!CardModifierManager.hasModifier(card, MoveModifier.ID)) {
            processedCards.add(card);
            CardModifierManager.addModifier(card, new MoveModifier(cards.get(card.cardID)));
        }
    }

    public void triggerNextSpaces(AbstractCreature actor, int amountOfSpaces, int amountOfTriggers) {
        AbstractSpace space;
        for (final AbstractDrone piece : getPieces()) {
            for (int found = 0; found < amountOfSpaces; ) {
                for (int i = 1; found < amountOfSpaces; i++) {
                    space = squareList.get((piece.position + i) % squareList.size());
                    if (!(space instanceof EmptySpace)) {
                        found++;
                        for(int j = 1; j < amountOfTriggers; j++) {
                            space.onLanded(actor);
                        }
                        space.uponLand(actor);
                    }
                }
            }
        }
    }
}