package paleoftheancients.donudeca.monsters;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.powers.watcher.EndTurnDeathPower;
import paleoftheancients.donudeca.actions.SlideCreatureEffect;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.SpawnMonsterAction;
import com.megacrit.cardcrawl.actions.common.SuicideAction;
import com.megacrit.cardcrawl.actions.unique.CanLoseAction;
import com.megacrit.cardcrawl.actions.utility.HideHealthBarAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.beyond.Deca;
import com.megacrit.cardcrawl.monsters.beyond.Donu;
import com.megacrit.cardcrawl.powers.PainfulStabsPower;

public class Decaer extends Deca {

    private boolean spawnedthing;

    public Decaer() {
        super();
        this.setHp(this.maxHealth / 2);

        this.spawnedthing = false;
    }

    @Override
    public void usePreBattleAction() {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new PainfulStabsPower(this)));
        AbstractDungeon.getCurrRoom().cannotLose = true;
    }

    @Override
    public void takeTurn() {
        if(!spawnedthing) {
            if (this.halfDead) {
                Donuer donu = (Donuer) AbstractDungeon.getCurrRoom().monsters.getMonster(Donu.ID);
                AbstractDungeon.actionManager.addToBottom(new HideHealthBarAction(this));
                if (donu != null) {
                    AbstractDungeon.actionManager.addToBottom(new HideHealthBarAction(donu));
                    AbstractDungeon.actionManager.addToBottom(new VFXAction(new SlideCreatureEffect(donu, -225 * Settings.scale, 0)));
                }
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new SlideCreatureEffect(this, 225 * Settings.scale, 0), SlideCreatureEffect.DURATION));
                AbstractDungeon.actionManager.addToBottom(new SuicideAction(this));
                if (donu != null) {
                    AbstractDungeon.actionManager.addToBottom(new SuicideAction(donu));
                }
                AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(new DonuDeca(this, donu), false));
                AbstractDungeon.actionManager.addToBottom(new CanLoseAction());
                AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(AbstractDungeon.player, this, EndTurnDeathPower.POWER_ID));
                this.spawnedthing = true;
            } else {
                super.takeTurn();
            }
        }
    }

    @Override
    public void die() {
        if(!this.halfDead) {
            this.halfDead = true;
            this.setMove((byte) 0, Intent.UNKNOWN);
            this.createIntent();
            this.hideHealthBar();

            AbstractMonster mo = AbstractDungeon.getCurrRoom().monsters.getMonster(Donu.ID);
            if (mo != null) {
                mo.die();
            } else {
                AbstractDungeon.getCurrRoom().cannotLose = false;
                super.die(true);
            }
        }
    }
}
