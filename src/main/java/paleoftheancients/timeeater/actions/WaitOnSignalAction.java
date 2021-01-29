package paleoftheancients.timeeater.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import paleoftheancients.timeeater.vfx.AbstractSignalEffect;

public class WaitOnSignalAction extends AbstractGameAction {
    protected AbstractSignalEffect age;

    public WaitOnSignalAction(AbstractSignalEffect age) {
        this.age = age;
        this.duration = 0F;
        this.startDuration = 0F;
    }

    @Override
    public void update() {
        if(this.duration == this.startDuration) {
            AbstractDungeon.effectList.add(age);
            this.duration++;
        }
        if(this.age.ending) {
            this.isDone = true;
        }
    }
}
