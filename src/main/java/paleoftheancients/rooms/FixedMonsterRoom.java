package paleoftheancients.rooms;

import actlikeit.patches.AbstractRoomUpdateIncrementElitesPatch;
import basemod.BaseMod;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import paleoftheancients.bandit.monsters.TheBandit;
import paleoftheancients.bard.monsters.BardBoss;
import paleoftheancients.collector.monsters.SpireWaifu;
import paleoftheancients.donudeca.monsters.DonuDeca;
import paleoftheancients.guardian.monsters.Guardianest;
import paleoftheancients.helpers.AssetLoader;
import paleoftheancients.hexaghost.monsters.HexaghostPrime;
import paleoftheancients.ironcluck.monsters.IronCluck;
import paleoftheancients.reimu.monsters.Reimu;
import paleoftheancients.relics.*;
import paleoftheancients.thedefect.monsters.TheDefectBoss;
import paleoftheancients.theshowman.monsters.TheShowmanBoss;
import paleoftheancients.thesilent.monsters.TheSilentBoss;
import paleoftheancients.thevixen.monsters.TheVixenBoss;
import paleoftheancients.watcher.monsters.TheWatcher;
import paleoftheancients.wokeone.monsters.WokeOne;

import java.util.HashMap;
import java.util.Map;

public class FixedMonsterRoom extends MonsterRoom {
    private String encounterID;
    protected AbstractRelic relic;

    protected static Map<String, String> relics = new HashMap<>();

    public FixedMonsterRoom(String encounterID, String mapImg, String mapOutlineImg) {
        this.encounterID = encounterID;
        this.setMapImg(AssetLoader.loadImage(mapImg), AssetLoader.loadImage(mapOutlineImg));

        this.relic = RelicLibrary.getRelic(relics.get(encounterID));
    }

    public static void initialize() {
        relics.put(TheBandit.ID, SoulOfTheBandit.ID);
        relics.put(BardBoss.ID, SoulOfTheBard.ID);
        relics.put(SpireWaifu.ID, SoulOfTheCollector.ID);
        relics.put(DonuDeca.ID, SoulOfTheShapes.ID);
        relics.put(Guardianest.ID, SoulOfTheGuardian.ID);
        relics.put(HexaghostPrime.ID, SoulOfTheHexaghost.ID);
        relics.put(IronCluck.ID, SoulOfTheIroncluck.ID);
        relics.put(Reimu.ID, SoulOfTheShrineMaiden.ID);
        relics.put(TheDefectBoss.ID, SoulOfTheDefect.ID);
        relics.put(TheShowmanBoss.ID, SoulOfTheShowman.ID);
        relics.put(TheSilentBoss.ID, SoulOfTheSilent.ID);
        relics.put(TheVixenBoss.ID, SoulOfTheVixen.ID);
        relics.put(TheWatcher.ID, SoulOfTheWatcher.ID);
        relics.put(WokeOne.ID, SoulOfTheWokeBloke.ID);
    }

    @Override
    public void onPlayerEntry() {
        this.playBGM(null);
        this.monsters = BaseMod.getMonster(encounterID);
        this.monsters.init();
        waitTimer = MonsterRoom.COMBAT_WAIT_TIME;
    }

    @Override
    public void endBattle() {
        super.endBattle();
        AbstractRoomUpdateIncrementElitesPatch.Insert(null);
    }

    public void relicTips(SpriteBatch sb, float x, float y) {
        if(relic.isSeen) {
            TipHelper.queuePowerTips(x, y, relic.tips);
        }
    }
}
