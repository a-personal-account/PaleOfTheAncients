package paleoftheancients.theshowman.bosscards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import paleoftheancients.theshowman.actions.ExhaustShowmanCardAction;
import paleoftheancients.theshowman.monsters.TheShowmanBoss;

import java.util.ArrayList;

public abstract class AbstractShowmanExhaustingCard extends AbstractShowmanCard {
    public AbstractCard toExhaust;

    public AbstractShowmanExhaustingCard(String id, String name, String img, int cost, String rawDescription, CardType type, CardRarity rarity, CardTarget target, TheShowmanBoss owner, AbstractMonster.Intent intent) {
        super(id, name, img, cost, rawDescription, type, rarity, target, owner, intent);
    }

    public AbstractShowmanExhaustingCard(String id, String name, RegionName img, int cost, String rawDescription, CardType type, CardRarity rarity, CardTarget target, TheShowmanBoss owner, AbstractMonster.Intent intent) {
        super(id, name, img, cost, rawDescription, type, rarity, target, owner, intent);
    }

    @Override
    public void use(ArrayList<AbstractCard> availableExhaustCards, AbstractPlayer p, AbstractMonster mo) {
        this.triggerExhaust(availableExhaustCards);
        AbstractDungeon.actionManager.addToBottom(new DamageAction(p, new DamageInfo(mo, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.NONE));
    }

    protected void triggerExhaust(ArrayList<AbstractCard> availableExhaustCards) {
        this.highestExhaustPriority(availableExhaustCards);
        AbstractDungeon.actionManager.addToBottom(new ExhaustShowmanCardAction(this.owner, this.toExhaust));
    }

    @Override
    public int getPriority(ArrayList<AbstractCard> availableExhaustCards, int availableEnergy, int byrdHits) {
        int priority = super.getPriority(availableExhaustCards, availableEnergy, byrdHits);

        priority += this.highestExhaustPriority(availableExhaustCards);

        return priority;
    }

    public int highestExhaustPriority(ArrayList<AbstractCard> availableExhaustCards) {
        int highestExhaustPriority = Integer.MIN_VALUE / 2;
        for(final AbstractCard card : availableExhaustCards) {
            if(card.costForTurn > -2 && card != this) {
                AbstractShowmanCard asc = (AbstractShowmanCard) card;
                if(highestExhaustPriority < asc.exhaustPriority) {
                    highestExhaustPriority = asc.exhaustPriority;
                    this.toExhaust = asc;
                }
            }
        }
        return highestExhaustPriority;
    }
}
