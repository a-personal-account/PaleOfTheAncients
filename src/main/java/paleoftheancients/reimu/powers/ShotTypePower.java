package paleoftheancients.reimu.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import paleoftheancients.reimu.monsters.YinYangOrb;

public abstract class ShotTypePower extends AbstractPower {

    public ShotTypePower(AbstractCreature owner, String ID, String name) {
        this.name = name;
        this.ID = ID;

        this.owner = owner;

        type = PowerType.BUFF;
        isTurnBased = false;

        updateDescription();
    }

    public abstract ShotTypePower makeCopy(AbstractMonster mo);

    protected void ClearIntent() {
        AbstractMonster mo = (AbstractMonster) this.owner;
        mo.setMove(YinYangOrb.MOVE, AbstractMonster.Intent.NONE);
        mo.createIntent();
    }

    @Override
    public void atStartOfTurn() {
        YinYangOrb yyo = (YinYangOrb) this.owner;
        if(yyo.getIntentBaseDmg() > 0 && yyo.position != Position.playerPosition()) {
            ClearIntent();
        }
    }
}