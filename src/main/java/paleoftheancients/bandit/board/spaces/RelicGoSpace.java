package paleoftheancients.bandit.board.spaces;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import paleoftheancients.bandit.board.AbstractBoard;
import paleoftheancients.bandit.powers.KeyFinisherPower;

public class RelicGoSpace extends GoSpace {

    public RelicGoSpace(AbstractBoard board, int x, int y) {
        super(board, x, y);
    }

    @Override
    public void uponLand(AbstractCreature actor) {
        super.uponLand(actor);
        att(new ApplyPowerAction(actor, actor, new KeyFinisherPower(actor, 1), 1));
    }

    @Override
    public void onLanded(AbstractCreature actor) {
        splat();
        att(new DamageAllEnemiesAction(AbstractDungeon.player, DamageInfo.createDamageMatrix(BASEDAMAGE, true), DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.FIRE));
    }

    public String getBodyText() {
        return BASETEXT[1] + TEXT[4] + BASEDAMAGE + TEXT[5];
    }
}