package paleoftheancients.relics;

import basemod.abstracts.CustomBottleRelic;
import basemod.abstracts.CustomSavable;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import paleoftheancients.PaleMod;
import paleoftheancients.helpers.AssetLoader;
import paleoftheancients.patches.CollectorRelicField;

import java.util.ArrayList;
import java.util.UUID;
import java.util.function.Predicate;

public class SoulOfTheCollector extends PaleRelic implements CustomBottleRelic, CustomSavable<ArrayList<Integer>> {
    public static final String ID = PaleMod.makeID("SoulOfTheCollector");

    private static final RelicTier TIER = RelicTier.SPECIAL;
    private static final LandingSound SOUND = LandingSound.CLINK;

    private boolean cardSelected = true;
    public ArrayList<AbstractCard> cards = null;

    public SoulOfTheCollector() {
        super(ID, AssetLoader.loadImage(PaleMod.assetPath("images/relics/collector.png")), AssetLoader.loadImage(PaleMod.assetPath("images/relics/outline/collector.png")), TIER, SOUND);
    }

    @Override
    public Predicate<AbstractCard> isOnCard() {
        return CollectorRelicField.isBottled::get;
    }

    private boolean containsUUID(UUID uuid) {
        for(final AbstractCard c : this.cards) {
            if(c.uuid.equals(uuid)) {
                return true;
            }
        }
        return false;
    }
    private void setField() {
        for(final AbstractCard c : cards) {
            CollectorRelicField.isBottled.set(c, true);
        }
    }

    @Override
    public String getUpdatedDescription() {
        String desc = DESCRIPTIONS[0];

        if(this.cards != null && this.cards.size() == 2) {
            desc += DESCRIPTIONS[2];
            desc += FontHelper.colorString(this.cards.get(0).name, "y");
            desc += DESCRIPTIONS[3];
            desc += FontHelper.colorString(this.cards.get(1).name, "y");
            desc += LocalizedStrings.PERIOD;
        }

        return desc;
    }

    @Override
    public AbstractRelic makeCopy() {
        return new SoulOfTheCollector();
    }

    @Override
    public ArrayList<Integer> onSave() {
        if (cards != null && cards.size() == 2) {
            ArrayList<Integer> result = new ArrayList<>();
            for(int i = AbstractDungeon.player.masterDeck.group.size() - 1; i >= 0; i--) {
                if(containsUUID(AbstractDungeon.player.masterDeck.group.get(i).uuid)) {
                    result.add(i);
                }
            }
            return result;
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public void onLoad(ArrayList<Integer> cardIndeces) {
        this.cards = new ArrayList<>();
        if (cardIndeces == null || cardIndeces.isEmpty()) {
            return;
        }
        for(final Integer cardIndex : cardIndeces) {
            if (cardIndex >= 0 && cardIndex < AbstractDungeon.player.masterDeck.group.size()) {
                this.cards.add(AbstractDungeon.player.masterDeck.group.get(cardIndex));
            }
        }
        setField();
        setDescriptionAfterLoading();
    }


    @Override
    public void onEquip() {
        cardSelected = false;
        if (AbstractDungeon.isScreenUp) {
            AbstractDungeon.dynamicBanner.hide();
            AbstractDungeon.overlayMenu.cancelButton.hide();
            AbstractDungeon.previousScreen = AbstractDungeon.screen;
        }
        AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.INCOMPLETE;
        CardGroup group = CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck);
        for(int i = group.group.size() - 1; i >= 0; i--) {
            AbstractCard.CardType type = group.group.get(i).type;
            switch(type) {
                case POWER:
                case CURSE:
                case STATUS:
                    group.group.remove(i);
                    break;
            }
        }
        AbstractDungeon.gridSelectScreen.open(group, 2, DESCRIPTIONS[1], false, false, false, false);
    }

    @Override
    public void update() {
        super.update(); //Do all of the original update() method in AbstractRelic

        if (!cardSelected && AbstractDungeon.gridSelectScreen.selectedCards.size() == 2) {
            cardSelected = true;
            cards = new ArrayList<>(AbstractDungeon.gridSelectScreen.selectedCards);
            setField();

            AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            setDescriptionAfterLoading();
        }
    }

    @Override
    public void atPreBattle() {
        for(int i = 0; i < cards.size(); i++) {
            for(final AbstractCard c : AbstractDungeon.player.drawPile.group) {
                if(c.uuid.equals(cards.get(i).uuid)) {
                    cards.set(i, c);
                    break;
                }
            }
        }
    }


    public void onUseCard(AbstractCard targetCard, UseCardAction useCardAction) {
        if(!targetCard.purgeOnUse && cards.size() == 2 && CollectorRelicField.isBottled.get(targetCard)) {
            this.flash();

            int copyIndex = 0;
            if(targetCard.uuid == cards.get(0).uuid) {
                copyIndex++;
            }

            GameActionManager.queueExtraCard(cards.get(copyIndex), (useCardAction.target != null) ? (AbstractMonster) useCardAction.target : AbstractDungeon.getRandomMonster());
        }
    }

    private void setDescriptionAfterLoading() {
        this.description = this.getUpdatedDescription();
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        this.initializeTips();
    }
}
