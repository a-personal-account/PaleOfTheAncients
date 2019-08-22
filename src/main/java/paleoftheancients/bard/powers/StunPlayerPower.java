package paleoftheancients.bard.powers;

import com.evacipated.cardcrawl.mod.stslib.powers.StunMonsterPower;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.ExhaustCardEffect;

import java.util.Iterator;

public class StunPlayerPower extends StunMonsterPower {

    private int handsize;

    public StunPlayerPower() {
        super(null, -1);
        this.owner = AbstractDungeon.player;
    }


    public void atStartOfTurn() {
        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, this));

        AbstractDungeon.actionManager.cardQueue.clear();
        Iterator var3 = AbstractDungeon.player.limbo.group.iterator();

        while(var3.hasNext()) {
            AbstractCard c = (AbstractCard)var3.next();
            AbstractDungeon.effectList.add(new ExhaustCardEffect(c));
        }

        AbstractDungeon.player.limbo.group.clear();
        AbstractDungeon.player.releaseCard();
        AbstractDungeon.overlayMenu.endTurnButton.disable(true);
    }

    @Override
    public void onInitialApplication() {
        handsize = AbstractDungeon.player.gameHandSize;
        AbstractDungeon.player.gameHandSize = 0;
    }

    @Override
    public void atEndOfRound() {}

    @Override
    public void onRemove() {
        AbstractDungeon.player.gameHandSize += handsize;
    }
}
