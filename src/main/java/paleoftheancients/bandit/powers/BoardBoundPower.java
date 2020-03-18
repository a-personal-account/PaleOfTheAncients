package paleoftheancients.bandit.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import paleoftheancients.NRPower;
import paleoftheancients.PaleMod;
import paleoftheancients.bandit.board.AbstractBoard;
import paleoftheancients.finarubossu.actions.GuaranteePowerApplicationAction;

public abstract class BoardBoundPower extends NRPower {
    public static final String POWER_ID = PaleMod.makeID("BoardBoundPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    protected AbstractBoard board;

    public BoardBoundPower(AbstractCreature owner, AbstractBoard board) {
        super("banditboard.png");
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;

        type = AbstractPower.PowerType.BUFF;
        isTurnBased = false;
        this.priority--;

        this.board = board;
    }

    @Override
    public void onRemove() {
        addToBot(new GuaranteePowerApplicationAction(owner, owner, this));
    }
}
