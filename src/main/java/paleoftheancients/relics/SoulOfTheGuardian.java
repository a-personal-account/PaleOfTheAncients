package paleoftheancients.relics;

import com.evacipated.cardcrawl.mod.stslib.relics.OnReceivePowerRelic;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ThornsPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import paleoftheancients.PaleMod;
import paleoftheancients.helpers.AssetLoader;

public class SoulOfTheGuardian extends PaleRelic implements OnReceivePowerRelic {
    public static final String ID = PaleMod.makeID("SoulOfTheGuardian");

    private static final RelicTier TIER = RelicTier.SPECIAL;
    private static final LandingSound SOUND = LandingSound.FLAT;

    private boolean fromThisRelic = true;
    private final int THORNS = 1;

    public SoulOfTheGuardian() {
        super(ID, AssetLoader.loadImage(PaleMod.assetPath("images/relics/guardian.png")), AssetLoader.loadImage(PaleMod.assetPath("images/relics/outline/guardian.png")), TIER, SOUND);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + THORNS + DESCRIPTIONS[1];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new SoulOfTheGuardian();
    }

    public void onUseCard(AbstractCard card, UseCardAction action) {
        if(card.type == AbstractCard.CardType.SKILL) {
            AbstractPower pow = AbstractDungeon.player.getPower(ThornsPower.POWER_ID);
            if(pow != null && pow.amount < 1000000000) {
                this.flash();
                this.counter += pow.amount;
                pow.amount *= 2;
                pow.updateDescription();
                pow.flash();
            }
        }
    }

    @Override
    public void atTurnStart() {
        if(this.counter > 0) {
            AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(AbstractDungeon.player, AbstractDungeon.player, ThornsPower.POWER_ID, this.counter));
        }

        this.counter = 0;
    }

    @Override
    public void atBattleStart() {
        this.flash();
        AbstractDungeon.actionManager.addToBottom(new SetFromRelicAction(true));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new ThornsPower(AbstractDungeon.player, THORNS), THORNS));
        AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
            @Override
            public void update() {
                this.isDone = true;
                AbstractDungeon.actionManager.addToBottom(new SetFromRelicAction(false));
            }
        });
    }

    @Override
    public boolean onReceivePower(AbstractPower abstractPower, AbstractCreature abstractCreature) {
        if(!this.fromThisRelic && PaleMod.skillsOnThorns && abstractPower.ID == ThornsPower.POWER_ID) {
            for(int i = 0; i < 3; ++i) {
                AbstractCard card = AbstractDungeon.returnTrulyRandomCardInCombat(AbstractCard.CardType.SKILL).makeCopy();
                if (card.cost > 0) {
                    card.cost = 0;
                    card.costForTurn = 0;
                    card.isCostModified = true;
                }

                this.addToBot(new MakeTempCardInHandAction(card, 1));
            }
        }
        return true;
    }

    @Override
    public void onVictory() {
        this.counter = -1;
    }

    class SetFromRelicAction extends AbstractGameAction {
        private boolean target;
        public SetFromRelicAction() {
            this(!fromThisRelic);
        }
        public SetFromRelicAction(boolean target) {
            this.target = target;
        }
        @Override
        public void update() {
            this.isDone = true;
            fromThisRelic = target;
        }
    }
}
