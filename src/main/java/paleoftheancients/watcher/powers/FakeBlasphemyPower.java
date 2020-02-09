package paleoftheancients.watcher.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.watcher.EndTurnDeathPower;

public class FakeBlasphemyPower extends EndTurnDeathPower {
    private boolean sameTurn = true;

    public FakeBlasphemyPower(AbstractCreature owner) {
        super(owner);
    }

    @Override
    public void atStartOfTurn() {
    }

    @Override
    public void atEndOfRound() {
        if (sameTurn) {
            sameTurn = false;
        } else {
            super.atStartOfTurn();
        }
    }
}