package paleoftheancients.events;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.events.shrines.Duplicator;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.*;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.ui.DialogWord;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import paleoftheancients.PaleMod;
import paleoftheancients.relics.Timepiece;
import vexMod.relics.*;

import java.util.ArrayList;
import java.util.Collections;

public class Recollection extends AbstractImageEvent {
    public static final String ID = PaleMod.makeID("Recollection");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;

    private static int BLOCKLIMIT = 5;
    private static int AOELIMIT = 2;
    private static int POWERLIMIT = 2;

    private boolean openmap;
    private byte markOfTheBloom;

    private Texture emptyPixel;

    ArrayList<DescriptedList<String>> possibilities;

    public Recollection() {
        super(NAME, DESCRIPTIONS[0], PaleMod.assetPath("images/misc/emptypixel.png"));

        openmap = false;
        possibilities = new ArrayList<>();
        possibilities.add(new DescriptedList<>());

        this.addToPossibilities(blockCheck(), 3, 5, PaleMod.assetPath("images/events/exordiumdeath.jpg"), Anchor.ID, Orichalcum.ID, OddlySmoothStone.ID, MeatOnTheBone.ID);
        this.addToPossibilities(energyCheck(), 2, 10, PaleMod.assetPath("images/events/heartevent.jpg"), VelvetChoker.ID, Sozu.ID, CoffeeDripper.ID, IceCream.ID);
        this.addToPossibilities(aoeCheck(), 1, 5, PaleMod.assetPath("images/events/nob.jpg"), GremlinHorn.ID, LetterOpener.ID, BronzeScales.ID, MercuryHourglass.ID);
        if(possibilities.size() <= 2) {
            DescriptedList<String> stuff = new DescriptedList<>();
            if(Loader.isModLoaded("vexMod")) {
                ArrayList<String> vexStuff = vexRelics();
                Collections.shuffle(vexStuff);
                for(int i = 0; i < vexStuff.size(); i++) {
                    stuff.add(vexStuff.get(i));
                }
            }
            if(!AbstractDungeon.player.hasRelic(CultistMask.ID)) {
                stuff.add(CultistMask.ID);
            }
            if(!AbstractDungeon.player.hasRelic(SpiritPoop.ID)) {
                stuff.add(SpiritPoop.ID);
            }

            stuff.description = DESCRIPTIONS[4];
            stuff.maxHpIncrease = 0;
            stuff.imagePath = "images/events/facelessTrader.jpg";

            if(!stuff.isEmpty()) {
                possibilities.add(stuff);
            }
        }


        imageEventText.setDialogOption(OPTIONS[0]);

        markOfTheBloom = (byte)(AbstractDungeon.player.hasRelic(MarkOfTheBloom.ID) ? 2 : 0);
    }

    @Override
    public void onEnterRoom() {
        if(AbstractDungeon.player.hasRelic(Timepiece.ID)) {
            AbstractRelic timepiece = AbstractDungeon.player.getRelic(Timepiece.ID);
            ((Timepiece) timepiece).reset();
            timepiece.flash();
        } else {
            (new Timepiece()).instantObtain();
        }
    }

    private void addToPossibilities(boolean doit, int descindex, int maxHpIncrease, String imagePath, String... relics) {
        if(doit) {
            DescriptedList<String> tmp = new DescriptedList<>();
            tmp.description = DESCRIPTIONS[descindex];
            tmp.maxHpIncrease = maxHpIncrease;
            tmp.imagePath = imagePath;

            for(int i = 0; i < relics.length; i++) {
                if (!AbstractDungeon.player.hasRelic(relics[i]))
                    tmp.add(relics[i]);
            }
            if(!tmp.isEmpty()) {
                possibilities.add(tmp);
            }
        }
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
        if(!possibilities.isEmpty()) {
            if (buttonPressed < possibilities.get(0).size()) {
                RelicLibrary.getRelic(possibilities.get(0).get(buttonPressed)).makeCopy().instantObtain();
            } else if (!possibilities.get(0).isEmpty()) {
                AbstractDungeon.player.increaseMaxHp(maxHpIncrease(possibilities.get(0).maxHpIncrease), false);
            }
            possibilities.remove(0);
        }

        imageEventText.clearAllDialogs();
        if(!possibilities.isEmpty()) {
            imageEventText.updateBodyText(possibilities.get(0).description, DialogWord.AppearEffect.FADE_IN);
            for (final String relicid : possibilities.get(0)) {
                AbstractRelic relic = RelicLibrary.getRelic(relicid);
                imageEventText.setDialogOption('[' + OPTIONS[1] + FontHelper.colorString(relic.name, "y") + ']', relic);
            }
            if(possibilities.get(0).maxHpIncrease > 0) {
                imageEventText.setDialogOption('[' + OPTIONS[2] + maxHpIncrease(possibilities.get(0).maxHpIncrease) + OPTIONS[3] + ']' + OPTIONS[4]);
            } else {
                imageEventText.setDialogOption(OPTIONS[0]);
            }
            imageEventText.loadImage(possibilities.get(0).imagePath);
        } else if(markOfTheBloom == 2) {
            imageEventText.updateBodyText(DESCRIPTIONS[DESCRIPTIONS.length - 2], DialogWord.AppearEffect.FADE_IN);
            imageEventText.setDialogOption("[" + OPTIONS[7] + FontHelper.colorString(RelicLibrary.getRelic(MarkOfTheBloom.ID).name, "r") + "] " + OPTIONS[5]);
            imageEventText.setDialogOption('[' + OPTIONS[8] + "] "  + OPTIONS[6]);
            markOfTheBloom--;
            imageEventText.loadImage("images/events/mindBloom.jpg");
        } else if(!openmap) {
            if(markOfTheBloom == 1) {
                if(buttonPressed == 0) {
                    AbstractDungeon.player.loseRelic(MarkOfTheBloom.ID);
                    AbstractDungeon.player.heal(AbstractDungeon.player.maxHealth, false);
                } else {
                    AbstractDungeon.gridSelectScreen.open(AbstractDungeon.player.masterDeck, 1, Duplicator.OPTIONS[2], false, false, false, false);
                }
            }
            AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
            imageEventText.updateBodyText(DESCRIPTIONS[DESCRIPTIONS.length - 1], DialogWord.AppearEffect.NONE);
            imageEventText.setDialogOption(OPTIONS[OPTIONS.length - 1]);
            openmap = true;
            imageEventText.loadImage("images/misc/emptypixel.png");
        } else {
            imageEventText.setDialogOption(OPTIONS[OPTIONS.length - 1]);
            openMap();
        }
    }

    @Override
    public void update() {
        super.update();
        if (!AbstractDungeon.isScreenUp && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty() && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            AbstractCard c = ((AbstractCard)AbstractDungeon.gridSelectScreen.selectedCards.get(0)).makeStatEquivalentCopy();
            logMetricObtainCard(NAME, "Copied", c);
            c.inBottleFlame = false;
            c.inBottleLightning = false;
            c.inBottleTornado = false;
            AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(c, (float) Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
        }
    }

    private int maxHpIncrease(int percentage) {
        return AbstractDungeon.player.maxHealth * percentage / 100;
    }

    private boolean blockCheck() {
        int count = 0;
        for(final AbstractCard card : AbstractDungeon.player.masterDeck.group) {
            if(card.baseBlock > 0) {
                count++;
            }
        }
        return count < BLOCKLIMIT;
    }
    private boolean aoeCheck() {
        int count = 0;
        for(final AbstractCard card : AbstractDungeon.player.masterDeck.group) {
            if(card.baseDamage > 0 && (boolean) ReflectionHacks.getPrivate(card, AbstractCard.class, "isMultiDamage")) {
                count++;
            }
        }
        return count < AOELIMIT;
    }
    private boolean energyCheck() {
        return AbstractDungeon.player.energy.energyMaster <= 3;
    }

    class DescriptedList<T> extends ArrayList<T> {
        public String description;
        public int maxHpIncrease;
        public String imagePath;
    }


    private ArrayList<String> vexRelics() {
        ArrayList<String> IDs = new ArrayList<>();
        IDs.add(VoiceBox.ID);
        IDs.add(NotEnergy.ID);
        IDs.add(NewsTicker.ID);
        IDs.add(StoryBook.ID);
        IDs.add(Pepega.ID);
        IDs.add(SpireShuffle.ID);
        IDs.add(HealthChanger.ID);
        IDs.add(Bottle.ID);
        IDs.add(Incredibleness.ID);
        IDs.add(RealismEngine.ID);
        IDs.add(Rainbarrow.ID);
        if (!AbstractDungeon.player.hasRelic(PopTire.ID) && !AbstractDungeon.player.hasRelic(JugglerBalls.ID) && !AbstractDungeon.player.hasRelic(MiniSolarSystem.ID) && !AbstractDungeon.player.hasRelic(TheWave.ID)) {
            IDs.add(TheWave.ID);
            IDs.add(PopTire.ID);
            IDs.add(JugglerBalls.ID);
            IDs.add(MiniSolarSystem.ID);
        }
        for(int i = IDs.size() - 1; i >= 0; i--) {
            if(AbstractDungeon.player.hasRelic(IDs.get(i))) {
                IDs.remove(i);
            }
        }
        return IDs;
    }
}