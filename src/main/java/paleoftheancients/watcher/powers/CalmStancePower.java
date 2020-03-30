package paleoftheancients.watcher.powers;

import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.localization.StanceStrings;
import com.megacrit.cardcrawl.stances.CalmStance;

public class CalmStancePower extends AbstractStancePower {
    private static final StanceStrings stanceString = (StanceStrings) ReflectionHacks.getPrivateStatic(CalmStance.class, "stanceString");

    public CalmStancePower(AbstractCreature owner) {
        super(owner, stanceString);
        this.description = "-";
    }
}