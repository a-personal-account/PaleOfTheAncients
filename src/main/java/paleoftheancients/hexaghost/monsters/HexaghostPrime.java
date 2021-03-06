package paleoftheancients.hexaghost.monsters;

import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.SpawnMonsterAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.InvinciblePower;
import paleoftheancients.PaleMod;
import paleoftheancients.dungeons.PaleOfTheAncients;
import paleoftheancients.hexaghost.powers.HexaghostPower;
import paleoftheancients.relics.SoulOfTheHexaghost;

import java.util.ArrayList;

public class HexaghostPrime extends CustomMonster {

    public static final String ID = PaleMod.makeID("HexaghostPrime");

    private ArrayList<HexaghostFamiliar> familiars;
    private int spawnedFamiliars = 0;

    public HexaghostPrime() {
        super("", ID, 1200, 20.0F, 0.0F, 150.0F, 0.0F, PaleMod.assetPath("images/misc/emptypixel.png"));
        this.drawY += 140F;
        this.type = EnemyType.BOSS;
        if(AbstractDungeon.ascensionLevel >= 9) {
            this.setHp(this.maxHealth + 300);
        }
        if(AbstractDungeon.ascensionLevel >= 19) {
            this.setHp(this.maxHealth + 300);
        }
    }

    @Override
    public void usePreBattleAction() {
        CardCrawlGame.music.precacheTempBgm("BOSS_BOTTOM");
        this.createFamiliars();
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new InvinciblePower(this, 0), 0));
        this.halfDead = true;
    }

    private void createFamiliars() {
        familiars = new ArrayList<>();
        HexaghostFamiliar tmp;
        Vector2[] points = HexaghostPositions();
        for(int i = 0; i < points.length; i++) {
            tmp = new HexaghostFamiliar(this, this.drawX + points[i].x * 1.3F * Settings.scale, this.drawY + points[i].y * 1.3F * Settings.scale);
            familiars.add(tmp);
            tmp.powers.add(new HexaghostPower(tmp));
        }
        AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(familiars.get(0), true, 0));
        spawnedFamiliars++;
    }

    public static Vector2[] HexaghostPositions() {
        return new Vector2[]{
                new Vector2(-90.0F , 130.0F),
                new Vector2(90.0F, 130.0F),
                new Vector2(+160.0F, 0),
                new Vector2(90.0F, -130.0F),
                new Vector2(-90.0F,  -130.0F),
                new Vector2( -160.0F, 0)
        };
    }

    @Override
    public void takeTurn() {
        if(spawnedFamiliars < 6) {
            AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(familiars.get(spawnedFamiliars++),true, AbstractDungeon.getCurrRoom().monsters.monsters.size() - 1));
        }
    }
    @Override
    public void getMove(int num) {
        this.setMove((byte)0, Intent.NONE);
    }

    @Override
    public void die(boolean triggerRelics) {
        PaleOfTheAncients.addRelicReward(SoulOfTheHexaghost.ID);
        this.useFastShakeAnimation(5.0F);
        CardCrawlGame.screenShake.rumble(4.0F);
        super.die(triggerRelics);

        for(final HexaghostFamiliar familiar : familiars) {
            familiar.die(true);
            familiar.hideHealthBar();
        }
    }
}
