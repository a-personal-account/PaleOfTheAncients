package paleoftheancients.dungeons;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.map.MapEdge;
import com.megacrit.cardcrawl.map.MapGenerator;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.rooms.*;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import paleoftheancients.PaleMod;
import paleoftheancients.bard.monsters.BardBoss;
import paleoftheancients.collector.monsters.SpireWaifu;
import paleoftheancients.donudeca.monsters.DonuDeca;
import paleoftheancients.events.Recollection;
import paleoftheancients.guardian.monsters.Guardianest;
import paleoftheancients.hexaghost.monsters.HexaghostPrime;
import paleoftheancients.ironcluck.monsters.IronCluck;
import paleoftheancients.rooms.*;
import paleoftheancients.scenes.NeowsRealmScene;
import paleoftheancients.slimeboss.monsters.SlimeBossest;
import paleoftheancients.thedefect.monsters.TheDefectBoss;
import paleoftheancients.thevixen.monsters.TheVixenBoss;
import paleoftheancients.wokeone.monsters.WokeOne;

import java.util.ArrayList;

public class PaleOfTheAncients extends CustomDungeon {
    public static final String ID = PaleMod.makeID("PaleOfTheAncients");

    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ID);
    public static final String[] TEXT = uiStrings.TEXT;
    public static final String NAME = TEXT[0];

    public PaleOfTheAncients() {
        super(new NeowsRealmScene(), NAME, ID);
        this.onEnterEvent(Recollection.class);
        this.addTempMusic(PaleMod.makeID("undyne"), PaleMod.assetPath("music/undertale_undyne.ogg"));
        this.addTempMusic(PaleMod.makeID("dragonsnest"), PaleMod.assetPath("music/pokken-dragonsnest.ogg"));
        this.addTempMusic(PaleMod.makeID("tellurtownautumn"), PaleMod.assetPath("music/pokken-tellurtownautumn.ogg"));
        this.addTempMusic(PaleMod.makeID("phoenix"), PaleMod.assetPath("music/ffxiv-phoenix.ogg"));
    }

    public PaleOfTheAncients(CustomDungeon cd, AbstractPlayer p, ArrayList<String> emptyList) {
        super(cd, p, emptyList);
    }

    public PaleOfTheAncients(CustomDungeon cd, AbstractPlayer p, SaveFile saveFile) {
        super(cd, p, saveFile);
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

        toughEncounters.add(new MonsterRoomCreator(PaleMod.assetPath("images/ui/map/defect.png"), PaleMod.assetPath("images/ui/map/defectOutline.png"), TheDefectBoss.ID));
        toughEncounters.add(new MonsterRoomCreator(PaleMod.assetPath("images/ui/map/slime.png"), PaleMod.assetPath("images/ui/map/slimeOutline.png"), SlimeBossest.ID));
        toughEncounters.add(new MonsterRoomCreator(PaleMod.assetPath("images/ui/map/hexaghost.png"), PaleMod.assetPath("images/ui/map/hexaghostOutline.png"), HexaghostPrime.ID));
        toughEncounters.add(new MonsterRoomCreator(PaleMod.assetPath("images/ui/map/vixen.png"), PaleMod.assetPath("images/ui/map/vixenOutline.png"), TheVixenBoss.ID));

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

    public static void playTempMusic(String key) {
        CardCrawlGame.music.silenceTempBgmInstantly();
        CardCrawlGame.music.silenceBGMInstantly();
        AbstractDungeon.scene.fadeOutAmbiance();
        CardCrawlGame.music.playTempBgmInstantly(key, true);
    }
    public static void resumeMainMusic() {
        CardCrawlGame.music.silenceTempBgmInstantly();
        AbstractDungeon.scene.fadeInAmbiance();
        CardCrawlGame.music.unsilenceBGM();
    }
}
