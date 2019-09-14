package paleoftheancients.slimeboss.monsters;

import com.megacrit.cardcrawl.actions.animations.AnimateShakeAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.SpawnMonsterAction;
import com.megacrit.cardcrawl.actions.common.SuicideAction;
import com.megacrit.cardcrawl.actions.utility.HideHealthBarAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.InvinciblePower;
import paleoftheancients.slimeboss.powers.SlimeSplitPower;
import paleoftheancients.slimeboss.powers.SlimeUnityPower;

public class SlimeHelper {

    public static void usePreBattleAction(AbstractMonster mo) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(mo, mo, new SlimeSplitPower(mo, SlimeBossest.SPLIT_AMOUNT / 2), SlimeBossest.SPLIT_AMOUNT / 2));
        if(mo.hasPower(InvinciblePower.POWER_ID)) {
            mo.getPower(InvinciblePower.POWER_ID).amount = mo.maxHealth;
        } else {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(mo, mo, new InvinciblePower(mo, mo.maxHealth), mo.maxHealth));
        }
    }

    public static void spawnEnemies(AbstractMonster source, AbstractMonster... newmonsters) {
        for(final AbstractMonster mo : newmonsters) {
            AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(mo, false, AbstractDungeon.getCurrRoom().monsters.monsters.size()));
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(mo, source, new SlimeUnityPower(mo, source)));
        }
    }

    public static void splitShake(AbstractMonster mo) {
        AbstractDungeon.actionManager.addToBottom(new AnimateShakeAction(mo, 1.0F, 0.1F));
        AbstractDungeon.actionManager.addToBottom(new HideHealthBarAction(mo));
        AbstractDungeon.actionManager.addToBottom(new SuicideAction(mo, false));
        AbstractDungeon.actionManager.addToBottom(new WaitAction(1.0F));
        AbstractDungeon.actionManager.addToBottom(new SFXAction("SLIME_SPLIT"));
    }
}
