package paleoftheancients.bard.powers;

import paleoftheancients.PaleMod;
import paleoftheancients.bard.actions.QueueNoteAction;
import paleoftheancients.bard.helpers.CardNoteAllocator;
import paleoftheancients.bard.hooks.OnNoteQueuedHook;
import paleoftheancients.bard.monsters.BardBoss;
import paleoftheancients.bard.notes.AbstractNote;
import paleoftheancients.bard.notes.WildCardNote;
import paleoftheancients.bard.relics.MagicTuningFork;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class QueueNotePower extends AbstractPower implements InvisiblePower, OnNoteQueuedHook {
    public static final String POWER_ID = PaleMod.makeID("QueueNotePower");

    private MagicTuningFork mtf;

    public QueueNotePower(BardBoss owner, int melodyPotency) {
        this.name = "";
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = melodyPotency;
        this.type = PowerType.BUFF;
        this.description = "";
        this.region48 = new TextureAtlas.AtlasRegion(new Texture(new Pixmap(1, 1, Pixmap.Format.Alpha)), 0, 0, 1, 1);
        this.region128 = this.region48;
        this.mtf = null;
    }

    @Override
    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
        for(final AbstractNote note : CardNoteAllocator.getNotes(card)) {
            AbstractDungeon.actionManager.addToBottom(new QueueNoteAction(((BardBoss)this.owner).notequeue, note));
        }
    }

    @Override
    public void atEndOfRound() {
        if(this.mtf == null) {
            this.mtf = new MagicTuningFork();
        }
        AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(this.owner, this.mtf));
        AbstractDungeon.actionManager.addToBottom(new QueueNoteAction(((BardBoss)this.owner).notequeue, WildCardNote.get()));

        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.owner, this.owner, new MelodyPotencyPower((BardBoss)this.owner, this.amount), this.amount));
    }

    @Override
    public AbstractNote onNoteQueued(AbstractNote var1) {
        AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
            @Override
            public void update() {
                this.isDone = true;
                ((BardBoss)owner).calcDervishDance();
            }
        });
        return var1;
    }
}