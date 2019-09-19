package paleoftheancients.events;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.curses.AscendersBane;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.characters.AnimatedNpc;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.neow.NeowEvent;
import com.megacrit.cardcrawl.neow.NeowReward;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.potions.PotionSlot;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.scenes.AbstractScene;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import com.megacrit.cardcrawl.screens.DungeonTransitionScreen;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.FadeWipeParticle;
import com.megacrit.cardcrawl.vfx.InfiniteSpeechBubble;
import com.megacrit.cardcrawl.vfx.ObtainKeyEffect;
import com.megacrit.cardcrawl.vfx.combat.IntenseZoomEffect;
import com.megacrit.cardcrawl.vfx.combat.TimeWarpTurnEndEffect;
import com.megacrit.cardcrawl.vfx.scene.LevelTransitionTextOverlayEffect;
import paleoftheancients.PaleMod;
import paleoftheancients.finarubossu.vfx.RestoreCardEffect;
import paleoftheancients.finarubossu.vfx.RestoreRelicsVFX;
import paleoftheancients.helpers.FakeDeathScreen;
import paleoftheancients.helpers.PreloadNpc;
import paleoftheancients.relics.Timepiece;
import paleoftheancients.scenes.PreloadBottomScene;
import paleoftheancients.vfx.TimepieceTrigger;

import java.util.ArrayList;

public class DejaVu extends AbstractEvent {
    public static final String ID = PaleMod.makeID("DejaVu");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;

    private AnimatedNpc npc;

    private float DIALOG_X, DIALOG_Y;
    private InfiniteSpeechBubble bubble = null;
    private FakeDeathScreen ds = null;

    private int step;
    private int gold;
    private int maxhp;
    private int curhp;
    private boolean[] keys;
    private ArrayList<AbstractPotion> potions;
    private int potionslots;
    private Texture playerimg;

    private ArrayList<AbstractGameEffect> age;

    public DejaVu() {
        if (this.npc == null) {
            this.npc = new PreloadNpc(1534.0F * Settings.scale, 280.0F * Settings.scale, "images/npcs/neow/skeleton.atlas", "images/npcs/neow/skeleton.json", "idle");
        }

        this.body = "";
        CardCrawlGame.sound.play("VO_NEOW_" + MathUtils.random(1, 2) + (MathUtils.randomBoolean() ? 'A' : 'B'));

        DIALOG_X = (float)ReflectionHacks.getPrivateStatic(NeowEvent.class, "DIALOG_X");
        DIALOG_Y = (float)ReflectionHacks.getPrivateStatic(NeowEvent.class, "DIALOG_Y");

        talk(NeowEvent.TEXT[MathUtils.random(1, 3)]);
        this.step = 0;

        AbstractDungeon.topLevelEffects.add(new LevelTransitionTextOverlayEffect(DungeonTransitionScreen.TEXT[3], DungeonTransitionScreen.TEXT[2], true));

        this.roomEventText.addDialogOption(NeowEvent.OPTIONS[1]);
        this.hasDialog = true;
        this.hasFocus = true;

        age = new ArrayList<>();
    }

    private ArrayList<AbstractRelic> relics;
    private CardGroup cards;
    private ArrayList<AbstractBlight> blights;
    private int floornum;
    private AbstractScene scene;
    @Override
    public void onEnterRoom() {
        CardCrawlGame.music.silenceBGMInstantly();

        scene = AbstractDungeon.scene;
        AbstractDungeon.scene = new PreloadBottomScene();

        rob();
    }

    private void talk(String msg) {
        stopTalking();
        AbstractDungeon.effectList.add(bubble = new InfiniteSpeechBubble(DIALOG_X, DIALOG_Y, msg));
    }
    private void stopTalking() {
        if(this.bubble != null) {
            AbstractGameEffect text = (AbstractGameEffect) ReflectionHacks.getPrivate(bubble, InfiniteSpeechBubble.class, "textEffect");
            text.isDone = true;
            AbstractDungeon.effectList.remove(bubble);
            bubble = null;
        }
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
        switch(step) {
            case 0:
                this.talk(NeowEvent.TEXT[MathUtils.random(6, 7)]);
                step++;
                NeowEvent.rng = new Random(Settings.seed);
                this.roomEventText.clear();
                for(int i = 0; i < 4; i++) {
                    this.roomEventText.addDialogOption((new NeowReward(i)).optionLabel);
                }
                NeowEvent.rng = null;
                break;
            case 1:
                this.roomEventText.clear();
                this.stopTalking();
                this.talk(DESCRIPTIONS[0]);
                AbstractDungeon.player.currentHealth = 0;

                boolean isDemo = Settings.isDemo;
                Settings.isDemo = true;
                ds = new FakeDeathScreen(AbstractDungeon.getMonsters());
                this.resetScene();
                Settings.isDemo = isDemo;
                break;

            case 6:
                roomTransition();
                break;
        }
    }

    private void rob() {
        floornum = AbstractDungeon.floorNum;
        AbstractDungeon.floorNum = 0;

        cards = AbstractDungeon.player.masterDeck;
        AbstractDungeon.player.masterDeck = new CardGroup(CardGroup.CardGroupType.MASTER_DECK);
        for(final String s : AbstractDungeon.player.getStartingDeck()) {
            AbstractDungeon.player.masterDeck.addToBottom(CardLibrary.getCard(s).makeCopy());
        }
        if(AbstractDungeon.ascensionLevel >= 10) {
            AbstractDungeon.player.masterDeck.addToTop(CardLibrary.getCard(AscendersBane.ID).makeCopy());
        }

        relics = AbstractDungeon.player.relics;
        AbstractDungeon.player.relics = new ArrayList<>();
        for(final String s : AbstractDungeon.player.getStartingRelics()) {
            setRelicStuff(RelicLibrary.getRelic(s).makeCopy());
        }
        Timepiece tmp = (Timepiece)RelicLibrary.getRelic(Timepiece.ID).makeCopy();
        setRelicStuff(tmp);
        tmp.primeForEvent();

        blights = AbstractDungeon.player.blights;
        AbstractDungeon.player.blights = new ArrayList<>();

        playerimg = AbstractDungeon.player.img;
        maxhp = AbstractDungeon.player.maxHealth;
        curhp = AbstractDungeon.player.currentHealth;
        gold = AbstractDungeon.player.gold;
        potions = AbstractDungeon.player.potions;
        potionslots = AbstractDungeon.player.potionSlots;

        CharSelectInfo csi = AbstractDungeon.player.getLoadout();
        AbstractDungeon.player.maxHealth = csi.maxHp;
        AbstractDungeon.player.currentHealth = csi.currentHp;
        AbstractDungeon.player.gold = csi.gold;
        AbstractDungeon.player.displayGold = csi.gold;
        AbstractDungeon.player.potionSlots = AbstractDungeon.ascensionLevel >= 11 ? 2 : 3;
        AbstractDungeon.player.potions = new ArrayList<>();
        for(int i = 0; i < AbstractDungeon.player.potionSlots; i++) {
            AbstractDungeon.player.potions.add(new PotionSlot(i));
        }

        keys = new boolean[]{Settings.hasEmeraldKey, Settings.hasRubyKey, Settings.hasSapphireKey};
        Settings.hasEmeraldKey = false;
        Settings.hasRubyKey = false;
        Settings.hasSapphireKey = false;
    }

    public void triggeredClock() {
        step = 2;
    }
    private void clockEffect() {
        this.stopTalking();
        AbstractDungeon.overlayMenu.hideBlackScreen();
        AbstractDungeon.dynamicBanner.hide();
        ds = null;

        AbstractDungeon.effectList.add(new IntenseZoomEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, false));
        AbstractGameEffect tmp = new TimeWarpTurnEndEffect();
        AbstractDungeon.effectList.add(tmp);
        CardCrawlGame.sound.play("POWER_TIME_WARP", 0.05F);
        age.add(tmp);

        AbstractDungeon.floorNum = floornum;
        this.roomEventText.clear();

        AbstractDungeon.effectList.add(new TimepieceTrigger(AbstractDungeon.player, false));
    }

    private void restorePlayer() {
        AbstractGameEffect tmp;
        AbstractDungeon.player.relics = relics;
        AbstractDungeon.effectsQueue.add(tmp = new RestoreRelicsVFX());
        age.add(tmp);
        AbstractDungeon.player.blights = blights;
        ReflectionHacks.setPrivate(AbstractDungeon.player, AbstractPlayer.class, "renderCorpse", false);
        AbstractDungeon.player.img = playerimg;
        if(keys[0]) {
            AbstractDungeon.effectList.add(new ObtainKeyEffect(ObtainKeyEffect.KeyColor.GREEN));
        }
        if(keys[1]) {
            AbstractDungeon.effectList.add(new ObtainKeyEffect(ObtainKeyEffect.KeyColor.RED));
        }
        if(keys[2]) {
            AbstractDungeon.effectList.add(new ObtainKeyEffect(ObtainKeyEffect.KeyColor.BLUE));
        }

        AbstractDungeon.player.maxHealth = maxhp;
        AbstractDungeon.player.currentHealth = curhp;
        AbstractDungeon.player.gold = gold;
        AbstractDungeon.player.potions = potions;
        AbstractDungeon.player.potionSlots = potionslots;
    }
    private void restoreMasterdeck() {
        AbstractGameEffect tmp;
        AbstractDungeon.player.masterDeck.clear();
        for(AbstractCard card : cards.group) {
            card = card.makeStatEquivalentCopy();
            AbstractCard.CardRarity originalRarity = card.rarity;
            card.rarity = AbstractCard.CardRarity.BASIC;
            AbstractDungeon.effectsQueue.add(tmp = new RestoreCardEffect(card, MathUtils.random(0, Settings.WIDTH), MathUtils.random(0, Settings.HEIGHT), originalRarity));
            age.add(tmp);
        }
    }

    private void resetScene() {
        if(scene != AbstractDungeon.scene) {
            AbstractDungeon.scene.dispose();
        }
        AbstractDungeon.scene = scene;
    }

    @Override
    public void update() {
        switch(step) {
            case 2:
                step++;
                clockEffect();
                break;

            case 3:
            case 4:
            case 5:
                boolean done = true;
                for(int i = age.size() - 1; i >= 0; i--) {
                    if(age.get(i).isDone) {
                        age.remove(i);
                    } else {
                        done = false;
                    }
                }
                if(done) {
                    switch(step) {
                        case 3:
                            restorePlayer();
                            break;
                        case 4:
                            restoreMasterdeck();
                            break;
                        case 5:
                            talk(DESCRIPTIONS[MathUtils.random(1, 3)]);
                            roomEventText.clear();
                            for(int i = 0; i < 4; i++) {
                                roomEventText.addDialogOption(OPTIONS[i]);
                            }
                            break;
                    }
                    step++;
                }
                break;

        }
        super.update();
        if(ds != null) {
            ds.update();
        }
    }
    @Override
    public void render(SpriteBatch sb) {
        super.render(sb);
        npc.render(sb);
    }
    @Override
    public void renderAboveTopPanel(SpriteBatch sb) {
        renderText(sb);
        if(ds != null) {
            ds.render(sb);
        }
    }
    @Override
    public void dispose() {
        resetScene();
        super.dispose();
    }

    private void roomTransition() {
        this.resetScene();

        AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
        MapRoomNode node = new MapRoomNode(-1, 15);
        node.room = new MonsterRoomBoss();
        node.room.rewardAllowed = false;
        AbstractDungeon.nextRoom = node;
        CardCrawlGame.music.fadeOutTempBGM();
        AbstractDungeon.pathX.add(1);
        AbstractDungeon.pathY.add(15);
        AbstractDungeon.topLevelEffects.add(new FadeWipeParticle());
        AbstractDungeon.nextRoomTransitionStart();
    }

    private void setRelicStuff(AbstractRelic relic) {
        final float START_X = (float) ReflectionHacks.getPrivateStatic(AbstractRelic.class, "START_X");
        final float START_Y = (float) ReflectionHacks.getPrivateStatic(AbstractRelic.class, "START_Y");

        relic.isDone = true;
        relic.isObtained = true;
        relic.currentX = START_X + AbstractDungeon.player.relics.size() * AbstractRelic.PAD_X;
        relic.currentY = START_Y;
        relic.targetX = relic.currentX;
        relic.targetY = relic.currentY;
        relic.hb.move(relic.currentX, relic.currentY);

        AbstractDungeon.player.relics.add(relic);
    }
}
