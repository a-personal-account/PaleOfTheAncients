package paleoftheancients.donudeca.monsters;

import paleoftheancients.donudeca.powers.LifestealPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.beyond.Deca;
import com.megacrit.cardcrawl.monsters.beyond.Donu;

public class Donuer extends Donu {

    public Donuer() {
        super();
        this.setHp(this.maxHealth / 2);
    }

    @Override
    public void usePreBattleAction() {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new LifestealPower(this)));
    }

    @Override
    public void takeTurn() {
        if(!this.halfDead) {
            super.takeTurn();
        }
    }

    @Override
    public void die() {
        if(!this.halfDead) {
            this.halfDead = true;
            this.hideHealthBar();
            this.setMove((byte) 0, Intent.UNKNOWN);
            this.createIntent();
            AbstractMonster mo = AbstractDungeon.getCurrRoom().monsters.getMonster(Deca.ID);
            if (mo != null) {
                mo.die();
            } else {
                AbstractDungeon.getCurrRoom().cannotLose = false;
                super.die(true);
            }
        }
    }
}
