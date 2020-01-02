package paleoftheancients.reimu.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

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
        mo.setMove(mo.nextMove, AbstractMonster.Intent.NONE);
        mo.createIntent();
    }

    @Override
    public void atStartOfTurn() {
        ClearIntent();
    }
}