package paleoftheancients.reimu.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class WaitOnVFXAction extends AbstractGameAction {
    private AbstractGameEffect age;

    public WaitOnVFXAction(AbstractGameEffect age) {
        this.age = age;
        AbstractDungeon.effectList.add(age);
    }

    @Override
    public void update() {
        if(this.age.isDone) {
            this.isDone = true;
        }
    }
}
