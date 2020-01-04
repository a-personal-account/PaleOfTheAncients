package paleoftheancients.thevixen.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import paleoftheancients.thevixen.cards.status.BossBurn;

public class ShuffleBossBurnsAction extends AbstractGameAction {
    private int intensity, amount;

    public ShuffleBossBurnsAction() {
        this(0);
    }
    public ShuffleBossBurnsAction(int intensity) {
        this(intensity, 1);
    }
    public ShuffleBossBurnsAction(int intensity, int amount) {
        this.intensity = intensity;
        this.amount = amount;
    }

    @Override
    public void update() {
        AbstractDungeon.actionManager.addToTop(new MakeTempCardInDiscardAction(new BossBurn(intensity), amount));
        SetPlayerBurnAction.addToBottom();
        this.isDone = true;
    }
}
