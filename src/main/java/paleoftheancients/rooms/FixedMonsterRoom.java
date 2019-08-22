package paleoftheancients.rooms;

import basemod.BaseMod;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.rooms.MonsterRoom;

public class FixedMonsterRoom extends MonsterRoom {
    private String encounterID;

    public FixedMonsterRoom(String encounterID, String mapImg, String mapOutlineImg) {
        this.encounterID = encounterID;
        this.setMapImg(ImageMaster.loadImage(mapImg), ImageMaster.loadImage(mapOutlineImg));
    }

    @Override
    public void onPlayerEntry() {
        this.playBGM(null);
        this.monsters = BaseMod.getMonster(encounterID);
        this.monsters.init();
        waitTimer = MonsterRoom.COMBAT_WAIT_TIME;
    }
}
