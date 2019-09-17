package paleoftheancients.theshowman.powers;

import paleoftheancients.theshowman.cards.*;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.city.Byrd;
import com.megacrit.cardcrawl.powers.BlurPower;
import com.megacrit.cardcrawl.powers.FlightPower;

import java.util.ArrayList;
import java.util.Collections;


public class ColumbifyPower extends DummyColumbifyPower {

    private CardGroup hand;
    private int handsize;
    public static Byrd byrd = null;
    private boolean removeFlight;
    private boolean firstRound;
    private boolean extended;

    public ColumbifyPower(AbstractCreature owner) {
        super(owner);
    }

    @Override
    public void onInitialApplication() {
        handsize = AbstractDungeon.player.gameHandSize;
        AbstractDungeon.player.gameHandSize = 0;
        hand = AbstractDungeon.player.hand;

        AbstractDungeon.player.hand = new CardGroup(CardGroup.CardGroupType.HAND);

        byrd = new Byrd(0, 0);
        byrd.flipHorizontal = true;
        byrd.isPlayer = true;
        byrd.drawX = this.owner.drawX;
        byrd.drawY = this.owner.drawY;
        if(removeFlight = !this.owner.hasPower(FlightPower.POWER_ID)) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.owner, this.owner, new FlightPower(this.owner, 3), 3));
        }
        firstRound = true;
        extended = false;
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.owner, this.owner, new BlurPower(this.owner, 1), 1));
    }
    @Override
    public void onRemove() {
        if(removeFlight && this.owner.hasPower(FlightPower.POWER_ID)) {
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, FlightPower.POWER_ID));
        }
        byrd = null;
        if(handsize > -1) {
            restoreHand();
        }
    }
    @Override
    public void onVictory() {
        this.onRemove();
    }

    @Override
    public void atStartOfTurnPostDraw() {
        if(handsize > -1) {
            if(!extended) {
                int[] possibilities = new int[]{0, 0, 0, 0, 0, 0, 1, 1, 1, 2};
                for (int i = 0; i < handsize; i++) {
                    ByrdCard card = null;
                    switch (possibilities[AbstractDungeon.cardRandomRng.random(0, possibilities.length - 1)]) {
                        case 0:
                            card = new Peck();
                            break;
                        case 1:
                            card = new Swoop();
                            break;
                        case 2:
                            card = new CawCaw();
                            break;
                    }
                    AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(card));
                }
            } else {
                ArrayList<AbstractCard> cards = new ArrayList<>();
                cards.add(new BeSad());
                for(int i = 0; i < 2; i++) {
                    cards.add(new Headbutt());
                }
                Collections.shuffle(cards);
                for(final AbstractCard card : cards) {
                    AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(card));
                }
            }
        }
    }
    @Override
    public void atStartOfTurn() {
        if(firstRound) {
            firstRound = false;
        } else {
            restoreHand();
            AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, this));
        }
    }

    public void extend() {
        if(!extended) {
            firstRound = true;
            extended = true;
        }
    }

    @Override
    public int onLoseHp(int damageAmount) {
        byrd.useStaggerAnimation();
        return damageAmount;
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster m) {
        if(card.type == AbstractCard.CardType.ATTACK) {
            if (Settings.FAST_MODE) {
                byrd.useFastAttackAnimation();
            } else {
                byrd.useSlowAttackAnimation();
            }
        }
    }

    private void restoreHand() {
        AbstractDungeon.player.gameHandSize += handsize;
        AbstractDungeon.player.hand.group.addAll(hand.group);
        AbstractDungeon.player.hand.refreshHandLayout();
        handsize = -1;
    }
}
