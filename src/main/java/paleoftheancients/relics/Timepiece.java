package paleoftheancients.relics;

import basemod.abstracts.CustomRelic;
import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.common.SuicideAction;
import com.megacrit.cardcrawl.actions.unique.CanLoseAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import paleoftheancients.PaleMod;
import paleoftheancients.dungeons.PaleOfTheAncients;
import paleoftheancients.finarubossu.monsters.Eye;
import paleoftheancients.finarubossu.monsters.N;
import paleoftheancients.helpers.AssetLoader;
import paleoftheancients.patches.AbstractRoomUpdateIncrementElitesPatch;
import paleoftheancients.rooms.DejaVuRoom;
import paleoftheancients.savefields.ElitesSlain;

public class Timepiece extends CustomRelic implements ClickableRelic {
    public static final String ID = PaleMod.makeID("Timepiece");

    private static final AbstractRelic.RelicTier TIER = RelicTier.SPECIAL;
    private static final LandingSound SOUND = LandingSound.CLINK;

    public Timepiece() {
        super(ID, AssetLoader.loadImage(PaleMod.assetPath("images/relics/timepiece.png")), AssetLoader.loadImage(PaleMod.assetPath("images/relics/outline/timepiece.png")), TIER, SOUND);
    }

    public void onTrigger() {
        this.flash();
        int healAmt = AbstractDungeon.player.maxHealth;
        if (healAmt < 1) {
            healAmt = 1;
        }

        AbstractDungeon.player.heal(healAmt, true);
        this.setCounter(this.counter - 1);

        for(final AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if(mo.id != N.ID && !(mo instanceof Eye) && (!mo.isDeadOrEscaped() || mo.halfDead)) {
                AbstractDungeon.actionManager.addToTop(new SuicideAction(mo));
            }
        }
        AbstractDungeon.actionManager.addToTop(new CanLoseAction());
        AbstractDungeon.actionManager.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        AbstractDungeon.getCurrRoom().rewards.clear();

        if(AbstractDungeon.id == PaleOfTheAncients.ID) {
            AbstractRoomUpdateIncrementElitesPatch.Insert(null);
            ElitesSlain.getKilledElites().put(AbstractDungeon.actNum, ElitesSlain.getKilledElites().get(AbstractDungeon.actNum) - 2);
        }
    }

    @Override
    public void setCounter(int counter) {
        if (counter == -2) {
            this.usedUp();
        }
        super.setCounter(counter);
    }

    public void reset() {
        this.description = this.getUpdatedDescription();
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        this.setCounter(-1);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new Timepiece();
    }

    @Override
    public void onVictory() {
        if(AbstractDungeon.id == PaleOfTheAncients.ID) {
            int healAmt = (AbstractDungeon.player.maxHealth - AbstractDungeon.player.currentHealth + 1) / 2;
            if (healAmt > 0) {
                this.flash();
                AbstractDungeon.player.heal(healAmt, true);
            }
        }
    }

    @Override
    public void onRightClick() {
        if(this.pulse && AbstractDungeon.getCurrRoom() instanceof DejaVuRoom) {
            this.flash();
            this.stopPulse();
            this.getUpdatedDescription();
            this.setCounter(this.counter);
            ((DejaVuRoom) AbstractDungeon.getCurrRoom()).triggeredClock();
        }
    }

    public void primeForEvent() {
        this.beginLongPulse();
        this.description = DESCRIPTIONS[1];
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        this.initializeTips();
    }
}
