package paleoftheancients.reimu.actions;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import paleoftheancients.reimu.monsters.Reimu;

public class WaitOnFantasyHeavenAction extends WaitOnVFXAction {

    private Reimu reimu;

    public WaitOnFantasyHeavenAction(FantasyHeavenAction fha) {
        super(fha);
        this.reimu = fha.reimu;
    }

    @Override
    public void update() {
        for(int i = ((FantasyHeavenAction) age).toRemove.size() - 1; i >= 0; i--) {
            ((FantasyHeavenAction) age).toRemove.remove(i).isDone = true;
            int before = AbstractDungeon.player.currentHealth;
            AbstractDungeon.player.damage(new DamageInfo(AbstractDungeon.player, 1, DamageInfo.DamageType.HP_LOSS));
            if(before <= AbstractDungeon.player.currentHealth) {
                AbstractDungeon.player.currentHealth--;
                AbstractDungeon.player.healthBarUpdatedEvent();
                if(AbstractDungeon.player.currentHealth <= 0) {
                    AbstractDungeon.player.damage(new DamageInfo(AbstractDungeon.player, MathUtils.random(10000, 999999), DamageInfo.DamageType.HP_LOSS));
                }
            }
        }
        super.update();
        if(age.isDone) {
            reimu.killShrineMaiden(true);
        }
    }
}
