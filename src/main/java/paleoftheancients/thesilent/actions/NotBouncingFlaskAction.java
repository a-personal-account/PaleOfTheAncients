package paleoftheancients.thesilent.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.PotionBounceEffect;
import paleoftheancients.thesilent.powers.CorrosionPower;

public class NotBouncingFlaskAction extends AbstractGameAction {
    private static final float DURATION = 0.01F;
    private static final float POST_ATTACK_WAIT_DUR = 0.1F;
    private int numTimes;
    private int amount;

    public NotBouncingFlaskAction(AbstractCreature source, int amount, int numTimes) {
        this.source = source;
        this.actionType = ActionType.DEBUFF;
        this.duration = 0.01F;
        this.numTimes = numTimes;
        this.amount = amount;
    }

    public void update() {
        --this.numTimes;
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this.source, new CorrosionPower(AbstractDungeon.player, this.amount), this.amount, true, AttackEffect.POISON));
        AbstractDungeon.actionManager.addToTop(new WaitAction(0.1F));

        if (this.numTimes > 1) {
            AbstractDungeon.actionManager.addToBottom(new VFXAction(new PotionBounceEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY), 0.4F));
            AbstractDungeon.actionManager.addToBottom(new NotBouncingFlaskAction(AbstractDungeon.player, this.amount, this.numTimes));
        }

        this.isDone = true;
    }
}
