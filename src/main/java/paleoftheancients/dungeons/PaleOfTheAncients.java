package paleoftheancients.dungeons;

import actlikeit.dungeons.CustomDungeon;
import basemod.BaseMod;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.map.MapEdge;
import com.megacrit.cardcrawl.map.MapGenerator;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterInfo;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.Circlet;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.*;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import com.megacrit.cardcrawl.scenes.AbstractScene;
import paleoftheancients.PaleMod;
import paleoftheancients.bandit.monsters.TheBandit;
import paleoftheancients.bard.monsters.BardBoss;
import paleoftheancients.collector.monsters.SpireWaifu;
import paleoftheancients.donudeca.monsters.DonuDeca;
import paleoftheancients.events.Recollection;
import paleoftheancients.guardian.monsters.Guardianest;
import paleoftheancients.helpers.PaleRewardItem;
import paleoftheancients.hexaghost.monsters.HexaghostPrime;
import paleoftheancients.ironcluck.monsters.IronCluck;
import paleoftheancients.reimu.monsters.Reimu;
import paleoftheancients.rooms.*;
import paleoftheancients.scenes.PaleScene;
import paleoftheancients.thedefect.monsters.TheDefectBoss;
import paleoftheancients.theshowman.monsters.TheShowmanBoss;
import paleoftheancients.thesilent.monsters.TheSilentBoss;
import paleoftheancients.thevixen.monsters.TheVixenBoss;
import paleoftheancients.watcher.monsters.TheWatcher;
import paleoftheancients.wokeone.monsters.WokeOne;

import java.util.ArrayList;
import java.util.Iterator;

public class PaleOfTheAncients extends CustomDungeon {
    public static final String ID = PaleMod.makeID("PaleOfTheAncients");

    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ID);
    private static final EventStrings forkStrings = CardCrawlGame.languagePack.getEventString(PaleMod.makeID("ForkInTheRoad"));
    public static final String[] TEXT = uiStrings.TEXT;
    public static final String NAME = TEXT[0];

    public PaleOfTheAncients() {
        super(NAME, ID);
        this.onEnterEvent(Recollection.class);
        this.addTempMusic(PaleMod.makeID("undyne"), PaleMod.assetPath("music/undertale_undyne.ogg"));
        this.addTempMusic(PaleMod.makeID("dragonsnest"), PaleMod.assetPath("music/pokken-dragonsnest_loop.ogg"));
        this.addTempMusic(PaleMod.makeID("tellurtownautumn"), PaleMod.assetPath("music/pokken-tellurtownautumn_loop.ogg"));
        this.addTempMusic(PaleMod.makeID("phoenix"), PaleMod.assetPath("music/ffxiv-phoenix_loop.ogg"));
        this.addTempMusic(PaleMod.makeID("finaldream"), PaleMod.assetPath("music/final-dream.ogg"));
        this.addTempMusic(PaleMod.makeID("thunderrolls"), PaleMod.assetPath("music/ffxiv-thunder_rolls.ogg"));
    }

    public PaleOfTheAncients(CustomDungeon cd, AbstractPlayer p, ArrayList<String> emptyList) {
        super(cd, p, emptyList);
    }

    public PaleOfTheAncients(CustomDungeon cd, AbstractPlayer p, SaveFile saveFile) {
        super(cd, p, saveFile);
    }

    @Override
    public AbstractScene DungeonScene() {
        return new PaleScene();
    }

    @Override
    public String getAfterSelectText() {
        return forkStrings.DESCRIPTIONS[forkStrings.DESCRIPTIONS.length - 1];
    }
    @Override
    public String getOptionText() {
        return forkStrings.OPTIONS[0];
    }
    @Override
    public String getBodyText() {
        return forkStrings.DESCRIPTIONS[forkStrings.DESCRIPTIONS.length - 2].replace(" NL", "");
    }

    private String Format(String subject, String prefix, String suffix) {
        subject = subject.replaceAll("(?<!\\sNL)(?!\\sNL)\\s+", suffix + ' ' + prefix);
        subject = subject.replaceAll("NL\\s+", "NL " + prefix);
        subject = subject.replaceAll("\\s+NL", suffix + " NL");
        return prefix + subject + suffix;
    }

    @Override
    protected void initializeLevelSpecificChances() {
        shopRoomChance = 0.00F;
        restRoomChance = 0.00F;
        treasureRoomChance = 0.0F;
        eventRoomChance = 1.00F;
        eliteRoomChance = 0.00F;

        smallChestChance = 30;
        mediumChestChance = 35;
        largeChestChance = 35;

        commonRelicChance = 50;
        uncommonRelicChance = 33;
        rareRelicChance = 17;

        colorlessRareChance = 0.3F;
        if (AbstractDungeon.ascensionLevel >= 12) {
            cardUpgradedChance = 0.325F;
        } else {
            cardUpgradedChance = 0.45F;
        }
    }

    @Override
    protected void initializeEventImg() {
        if (eventBackgroundImg != null) {
            eventBackgroundImg.dispose();
            eventBackgroundImg = null;
        }
        eventBackgroundImg = ImageMaster.loadImage("images/ui/event/panel.png");
    }

    @Override
    protected void initializeShrineList() {
        shrineList.add("Golden Shrine");
        shrineList.add("Transmorgrifier");
        shrineList.add("Purifier");
        shrineList.add("Upgrade Shrine");
    }

    @Override
    protected void makeMap() {

        ArrayList<MonsterRoomCreator> toughEncounters = new ArrayList();
        ArrayList<MonsterRoomCreator> easyishEncounters = new ArrayList();

        easyishEncounters.add(new MonsterRoomCreator(PaleMod.assetPath("images/ui/map/wokeone.png"), PaleMod.assetPath("images/ui/map/wokeoneOutline.png"), WokeOne.ID));
        easyishEncounters.add(new MonsterRoomCreator(PaleMod.assetPath("images/ui/map/guardian.png"), PaleMod.assetPath("images/ui/map/guardianOutline.png"), Guardianest.ID));
        easyishEncounters.add(new MonsterRoomCreator(PaleMod.assetPath("images/ui/map/collector.png"), PaleMod.assetPath("images/ui/map/collectorOutline.png"), SpireWaifu.ID));
        easyishEncounters.add(new MonsterRoomCreator(PaleMod.assetPath("images/ui/map/ironcluck.png"), PaleMod.assetPath("images/ui/map/ironcluckOutline.png"), IronCluck.ID));
        easyishEncounters.add(new MonsterRoomCreator(PaleMod.assetPath("images/ui/map/donudeca.png"), PaleMod.assetPath("images/ui/map/donudecaOutline.png"), DonuDeca.ID));
        easyishEncounters.add(new MonsterRoomCreator(PaleMod.assetPath("images/ui/map/bard.png"), PaleMod.assetPath("images/ui/map/bardOutline.png"), BardBoss.ID));
        easyishEncounters.add(new MonsterRoomCreator(PaleMod.assetPath("images/ui/map/showman.png"), PaleMod.assetPath("images/ui/map/showmanOutline.png"), TheShowmanBoss.ID));
        easyishEncounters.add(new MonsterRoomCreator(PaleMod.assetPath("images/ui/map/silent.png"), PaleMod.assetPath("images/ui/map/silentOutline.png"), TheSilentBoss.ID));
        easyishEncounters.add(new MonsterRoomCreator(PaleMod.assetPath("images/ui/map/watcher.png"), PaleMod.assetPath("images/ui/map/watcherOutline.png"), TheWatcher.ID));
        if(!Loader.isModLoaded("TheBandit") || !(AbstractDungeon.player instanceof theWario.TheBandit)) {
            easyishEncounters.add(new MonsterRoomCreator(PaleMod.assetPath("images/ui/map/bandit.png"), PaleMod.assetPath("images/ui/map/banditOutline.png"), TheBandit.ID));
        }

        toughEncounters.add(new MonsterRoomCreator(PaleMod.assetPath("images/ui/map/defect.png"), PaleMod.assetPath("images/ui/map/defectOutline.png"), TheDefectBoss.ID));
        //toughEncounters.add(new MonsterRoomCreator(PaleMod.assetPath("images/ui/map/slime.png"), PaleMod.assetPath("images/ui/map/slimeOutline.png"), SlimeBossest.ID));
        toughEncounters.add(new MonsterRoomCreator(PaleMod.assetPath("images/ui/map/hexaghost.png"), PaleMod.assetPath("images/ui/map/hexaghostOutline.png"), HexaghostPrime.ID));
        toughEncounters.add(new MonsterRoomCreator(PaleMod.assetPath("images/ui/map/vixen.png"), PaleMod.assetPath("images/ui/map/vixenOutline.png"), TheVixenBoss.ID));
        toughEncounters.add(new MonsterRoomCreator(PaleMod.assetPath("images/ui/map/reimu.png"), PaleMod.assetPath("images/ui/map/reimuOutline.png"), Reimu.ID));

        map = new ArrayList();

        int index = 0;
        map.add(populate(easyishEncounters, index++));
        map.add(populate(easyishEncounters, index++));
        map.add(singleNodeArea(new EventRoom(), index++));
        map.add(doubleNodeArea(new TreasureRoom(), new ShopRoom(), index++));
        map.add(singleNodeArea(new RestRoom(), index++));

        //map.add(populate(easyishEncounters, index++));
        map.add(populate(toughEncounters, index++));

        map.add(singleNodeArea(new RestRoom(), index++));
        map.add(doubleNodeArea(new PreloadTreasureRoom(), new PreloadShopRoom(), index++));
        map.add(singleNodeArea(new DejaVuRoom(), index++));
        map.add(singleNodeArea(new MonsterRoomBoss(), index++));
        map.add(singleNodeArea(new PaleVictoryRoom(), index++, false));

        logger.info("Generated the following dungeon map:");
        logger.info(MapGenerator.toString(map, true));

        firstRoomChosen = false;
        fadeIn();
    }

    @Override
    public void Ending() {
        CardCrawlGame.music.fadeOutBGM();
        MapRoomNode node = new MapRoomNode(3, 4);
        node.room = new PaleVictoryRoom();
        AbstractDungeon.nextRoom = node;
        AbstractDungeon.closeCurrentScreen();
        AbstractDungeon.nextRoomTransitionStart();
    }

    private void connectNode(MapRoomNode src, MapRoomNode dst) {
        src.addEdge(new MapEdge(src.x, src.y, src.offsetX, src.offsetY, dst.x, dst.y, dst.offsetX, dst.offsetY, false));
    }

    private ArrayList<MapRoomNode> populate(ArrayList<MonsterRoomCreator> possibilities, int index) {
        ArrayList<MapRoomNode> result = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            MapRoomNode mrn = new MapRoomNode(i, index);
            if (i % 2 == 1) {
                int rnd = AbstractDungeon.mapRng.random(possibilities.size() - 1);
                mrn.room = possibilities.get(rnd).get();
                possibilities.remove(rnd);
            }
            result.add(mrn);
        }

        if (index > 0) {
            ArrayList<MapRoomNode> mapcontent = map.get(index - 1);
            for (int i = 0; i < mapcontent.size(); i++) {
                if (mapcontent.get(i).room != null) {
                    for (int j = 0; j < result.size(); j++) {
                        if (result.get(j).room != null) {
                            if (Math.abs(i - j) < 4) {
                                this.connectNode(mapcontent.get(i), result.get(j));
                            }
                        }
                    }
                }
            }
        }

        return result;
    }

    private ArrayList<MapRoomNode> doubleNodeArea(AbstractRoom roomOne, AbstractRoom roomTwo, int index) {
        ArrayList<MapRoomNode> result = new ArrayList<>();
        MapRoomNode mrn;
        result.add(new MapRoomNode(0, index));
        result.add(new MapRoomNode(1, index));
        mrn = new MapRoomNode(2, index);
        mrn.room = roomOne;
        result.add(mrn);
        result.add(new MapRoomNode(3, index));
        mrn = new MapRoomNode(4, index);
        mrn.room = roomTwo;
        result.add(mrn);
        result.add(new MapRoomNode(5, index));
        result.add(new MapRoomNode(6, index));

        linkNonMonsterAreas(result);

        return result;
    }

    private ArrayList<MapRoomNode> singleNodeArea(AbstractRoom room, int index) {
        return singleNodeArea(room, index, true);
    }

    private ArrayList<MapRoomNode> singleNodeArea(AbstractRoom room, int index, boolean connected) {
        ArrayList<MapRoomNode> result = new ArrayList<>();
        MapRoomNode mrn;
        result.add(new MapRoomNode(0, index));
        result.add(new MapRoomNode(1, index));
        result.add(new MapRoomNode(2, index));
        mrn = new MapRoomNode(3, index);
        mrn.room = room;
        result.add(mrn);
        result.add(new MapRoomNode(4, index));
        result.add(new MapRoomNode(5, index));
        result.add(new MapRoomNode(6, index));

        if (connected) {
            linkNonMonsterAreas(result);
        }

        return result;
    }

    private void linkNonMonsterAreas(ArrayList<MapRoomNode> result) {
        if (!map.isEmpty()) {
            ArrayList<MapRoomNode> mapcontent = map.get(map.size() - 1);
            for (int i = 0; i < mapcontent.size(); i++) {
                if (mapcontent.get(i).room != null) {
                    for (int j = 0; j < result.size(); j++) {
                        if (result.get(j).room != null) {
                            this.connectNode(mapcontent.get(i), result.get(j));
                        }
                    }
                }
            }
        }
    }

    class MonsterRoomCreator {
        String image, outline, encounterID;

        public MonsterRoomCreator(String image, String outline, String encounterID) {
            this.image = image;
            this.outline = outline;
            this.encounterID = encounterID;
        }

        public MonsterRoom get() {
            return new FixedMonsterRoom(encounterID, image, outline);
        }
    }

    public static void deathTriggers(AbstractMonster mo) {
        for(int i = mo.powers.size() - 1; i >= 0; i--) {
            AbstractPower pow = mo.powers.get(i);
            pow.onDeath();
        }
        for(final AbstractRelic relic : AbstractDungeon.player.relics) {
            relic.onMonsterDeath(mo);
        }
    }

    public static void addEventMonster(String id, AbstractMonster mo) {
        for(int i = 1; i <= 3; i++) {
            BaseMod.addMonster(id + i, () -> mo);
            BaseMod.addMonsterEncounter(ID, new MonsterInfo(id + i, 1F));
            BaseMod.addStrongMonsterEncounter(ID, new MonsterInfo(id + i, 1F));
            BaseMod.addEliteEncounter(ID, new MonsterInfo(id + i, 1F));
        }
    }

    public static void addRelicReward(String relicID) {
        if (AbstractDungeon.player.hasRelic(relicID) && !relicID.equals(Circlet.ID)) {
            AbstractDungeon.getCurrRoom().rewards.add(new RewardItem(RelicLibrary.getRelic(Circlet.ID).makeCopy()));
        } else {
            boolean found = false;

            Iterator var2 = AbstractDungeon.getCurrRoom().rewards.iterator();

            while (var2.hasNext()) {
                RewardItem ri = (RewardItem) var2.next();
                if (ri.type == RewardItem.RewardType.RELIC && ri.relic != null && ri.relic.relicId.equals(relicID)) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                AbstractDungeon.getCurrRoom().rewards.add(new PaleRewardItem(RelicLibrary.getRelic(relicID).makeCopy()));
            }
        }
    }
}
