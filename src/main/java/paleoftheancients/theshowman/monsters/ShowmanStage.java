package paleoftheancients.theshowman.monsters;

import paleoftheancients.PaleMod;
import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ShowmanStage extends CustomMonster {

    private static final Logger logger = LogManager.getLogger(ShowmanStage.class.getName());
    public static final String ID = PaleMod.makeID("ShowmanStage");
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;
    public static final int BASEHP = 50;

    private TheShowmanBoss owner;

    public ShowmanStage(int hp, TheShowmanBoss owner) {
        //super(NAME, ID, BASEHP + hp, 0.0F, -15.0F, 300.0F, 230.0F, NeowsRealmMod.assetPath("images/misc/emptypixel.png"), 0.0F, 0.0F);
        super(NAME, ID, BASEHP + hp, 0.0F, -15.0F, 300.0F, 230.0F, PaleMod.assetPath("images/misc/emptypixel.png"), -300.0F, 0.0F);

        this.drawX = Settings.WIDTH / 2;
        this.refreshHitboxLocation();

        //this.loadAnimation(NeowsRealmMod.assetPath("images/ironcluck/ironcluck.atlas"), NeowsRealmMod.assetPath("images/ironcluck/ironcluck.json"), 6F);


        this.type = EnemyType.NORMAL;
        this.owner = owner;
    }

    public void usePreBattleAction() {}

    public void takeTurn() {
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    protected void getMove(int num) {
        this.setMove((byte)0, Intent.NONE);
    }

    public void damage(DamageInfo info) {
        if ((info.type != DamageInfo.DamageType.THORNS) && (info.output > this.currentBlock)) {

        }
        super.damage(info);
    }

    public void die() {
        super.die();
        this.owner.stageStun();
    }

    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
        NAME = monsterStrings.NAME;
        MOVES = monsterStrings.MOVES;
        DIALOG = monsterStrings.DIALOG;
    }
}