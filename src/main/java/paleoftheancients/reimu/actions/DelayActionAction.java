package paleoftheancients.reimu.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;

public class DelayActionAction extends AbstractGameAction {
    public DelayActionAction() {
        this.duration = 5F;
    }

    @Override
    public void update() {
        this.tickDuration();
    }

    public void end() {
        this.isDone = true;
    }
}
