package thevixen.helpers;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.Iterator;

public class FixedDamageInfo extends DamageInfo {


    public FixedDamageInfo(AbstractCreature damageSource, int base, DamageInfo.DamageType type) {
        super(damageSource, base, type);
    }

    public FixedDamageInfo(AbstractCreature owner, int base) {
        super(owner, base, DamageInfo.DamageType.NORMAL);
    }


    @Override
    public void applyEnemyPowersOnly(AbstractCreature target) {
        this.output = this.base;
        this.isModified = false;
        float tmp = (float)this.output;
        Iterator var3 = target.powers.iterator();

        AbstractPower p;
        while(var3.hasNext()) {
            p = (AbstractPower)var3.next();
            tmp = p.atDamageReceive(tmp, this.type);
        }

        var3 = target.powers.iterator();

        while(var3.hasNext()) {
            p = (AbstractPower)var3.next();
            tmp = p.atDamageFinalReceive(tmp, this.type);
        }

        if (tmp < 0.0F) {
            tmp = 0.0F;
        }

        this.output = MathUtils.floor(tmp);
        if (this.base != this.output) {
            this.isModified = true;
        }
    }
}
