package paleoftheancients.wokeone.monsters;

import actlikeit.dungeons.CustomDungeon;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.beyond.AwakenedOne;
import com.megacrit.cardcrawl.powers.CuriosityPower;
import com.megacrit.cardcrawl.powers.RegenerateMonsterPower;
import paleoftheancients.PaleMod;
import paleoftheancients.relics.SoulOfTheWokeBloke;

public class WokeOne extends AwakenedOne {
    public static final String ID = PaleMod.makeID("WokeOne");

    public WokeOne(float x, float y) {
        super(x, y);
    }

    public void usePreBattleAction() {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new RegenerateMonsterPower(this, 10)));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new CuriosityPower(this, 2)));
    }

    @Override
    public void die() {
        CustomDungeon.addRelicReward(SoulOfTheWokeBloke.ID);
        super.die(true);
    }
}
