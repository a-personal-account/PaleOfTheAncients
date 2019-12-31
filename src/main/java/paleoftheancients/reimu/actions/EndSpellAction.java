package paleoftheancients.reimu.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import paleoftheancients.reimu.monsters.Reimu;
import paleoftheancients.reimu.vfx.SpellCardBackgroundVFX;

public class EndSpellAction extends AbstractGameAction {
    private Reimu reimu;
    private SpellCardBackgroundVFX background;

    public EndSpellAction(Reimu reimu, SpellCardBackgroundVFX background) {
        this.reimu = reimu;
        this.background = background;
    }

    @Override
    public void update() {
        reimu.endSpellAnimation();
        background.end();
        this.isDone = true;
    }
}
