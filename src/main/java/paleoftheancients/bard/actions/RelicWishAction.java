package paleoftheancients.bard.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.defect.SeekAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;

import java.util.*;

public class RelicWishAction extends AbstractGameAction {
    public RelicWishAction() {
        this.duration = Settings.ACTION_DUR_MED;
    }

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_MED) {
            AbstractCard tmpcard;

            Map<String, Boolean> cardIsBasegame = new HashMap<>();
            List<AbstractCard.CardColor> basegameCardColors = Arrays.asList(AbstractCard.CardColor.values());
            CardGroup tmp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            for(final AbstractCard card : CardLibrary.getAllCards()) {
                if(card.rarity == AbstractCard.CardRarity.RARE && card.type != AbstractCard.CardType.POWER) {
                    tmpcard = card.makeCopy();
                    tmpcard.upgrade();
                    if(tmpcard.cost > 0) {
                        tmpcard.updateCost(-1);
                    }
                    tmp.group.add(tmpcard);
                    cardIsBasegame.put(tmpcard.cardID, basegameCardColors.contains(tmpcard.color));
                }
            }
            Collections.sort(tmp.group, (a, b) -> {
                boolean aIsBase = cardIsBasegame.get(a.cardID);
                boolean bIsBase = cardIsBasegame.get(b.cardID);
                if(aIsBase && !bIsBase) {
                    return 1;
                } else if(!aIsBase && bIsBase) {
                    return -1;
                } else if(aIsBase && bIsBase) {
                    if(a.color == b.color) {
                        return a.name.compareTo(b.name);
                    } else {
                        return basegameCardColors.indexOf(a.color) - basegameCardColors.indexOf(b.color);
                    }
                } else {
                    if(a.color == b.color) {
                        return a.name.compareTo(b.name);
                    } else {
                        return a.color.name().compareTo(b.color.name());
                    }
                }
            });
            AbstractDungeon.gridSelectScreen.open(tmp, 1, SeekAction.TEXT[0], false);
            this.tickDuration();
        } else {
            if (AbstractDungeon.gridSelectScreen.selectedCards.size() != 0) {
                AbstractDungeon.player.hand.addToBottom(AbstractDungeon.gridSelectScreen.selectedCards.get(0));
                AbstractDungeon.gridSelectScreen.selectedCards.clear();
                AbstractDungeon.player.hand.refreshHandLayout();
                this.isDone = true;
            }
        }
    }
}
