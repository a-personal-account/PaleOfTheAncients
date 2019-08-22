package paleoftheancients.thevixen.cards.attack;

import paleoftheancients.thevixen.helpers.RandomPoint;
import paleoftheancients.thevixen.vfx.RandomAnimatedSlashEffect;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class Facade {
    public static void VFX(AbstractCreature target) {
        AbstractDungeon.actionManager.addToBottom(new VFXAction(new RandomAnimatedSlashEffect(RandomPoint.x(target.hb), RandomPoint.y(target.hb))));
    }
}