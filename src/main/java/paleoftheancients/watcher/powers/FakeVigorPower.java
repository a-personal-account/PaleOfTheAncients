package paleoftheancients.watcher.powers;

import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;

public class FakeVigorPower extends VigorPower {
    public FakeVigorPower(AbstractCreature owner, int amount) {
        super(owner, amount);
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {}
}