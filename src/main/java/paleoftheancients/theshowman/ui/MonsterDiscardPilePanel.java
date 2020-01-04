package paleoftheancients.theshowman.ui;

import paleoftheancients.theshowman.misc.DummyCard;
import paleoftheancients.theshowman.monsters.TheShowmanBoss;
import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireOverride;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.ui.panels.DiscardPilePanel;
import com.megacrit.cardcrawl.vfx.BobEffect;
import com.megacrit.cardcrawl.vfx.DiscardGlowEffect;

import java.util.ArrayList;
import java.util.Iterator;

public class MonsterDiscardPilePanel extends DiscardPilePanel {
    private TheShowmanBoss boss;
    public Hitbox privateHb;
    private GlyphLayout privateGl;
    private BobEffect privateBob;
    private final float COUNT_OFFSET_X;
    private final float COUNT_OFFSET_Y;
    private final float COUNT_X;
    private final float COUNT_Y;
    private final float DECK_X;
    private final float DECK_Y;
    private final float COUNT_CIRCLE_W;
    private float privateScale;

    private ArrayList<DiscardGlowEffect> privateVfxBelow;
    private ArrayList<DiscardGlowEffect> privateVfxAbove;

    public MonsterDiscardPilePanel(TheShowmanBoss boss) {
        super();
        this.boss = boss;
        this.privateHb = (Hitbox) ReflectionHacks.getPrivate(this, DiscardPilePanel.class, "hb");
        this.privateGl = (GlyphLayout) ReflectionHacks.getPrivate(this, DiscardPilePanel.class, "gl");
        this.privateBob = (BobEffect) ReflectionHacks.getPrivate(this, DiscardPilePanel.class, "bob");
        this.COUNT_OFFSET_X = (float) ReflectionHacks.getPrivateStatic(DiscardPilePanel.class, "COUNT_OFFSET_X");
        this.COUNT_OFFSET_Y = (float) ReflectionHacks.getPrivateStatic(DiscardPilePanel.class, "COUNT_OFFSET_Y");
        this.COUNT_X = (float) ReflectionHacks.getPrivateStatic(DiscardPilePanel.class, "COUNT_X");
        this.COUNT_Y = (float) ReflectionHacks.getPrivateStatic(DiscardPilePanel.class, "COUNT_Y");
        this.DECK_X = (float) ReflectionHacks.getPrivateStatic(DiscardPilePanel.class, "DECK_X");
        this.DECK_Y = (float) ReflectionHacks.getPrivateStatic(DiscardPilePanel.class, "DECK_Y");
        this.COUNT_CIRCLE_W = (float) ReflectionHacks.getPrivateStatic(DiscardPilePanel.class, "COUNT_CIRCLE_W");

        this.privateVfxBelow = (ArrayList<DiscardGlowEffect>) ReflectionHacks.getPrivate(this, DiscardPilePanel.class, "vfxBelow");
        this.privateVfxAbove = (ArrayList<DiscardGlowEffect>) ReflectionHacks.getPrivate(this, DiscardPilePanel.class, "vfxAbove");

        this.current_x = this.boss.drawX + this.boss.hb.width * 1 / 4;
        this.current_y = this.boss.drawY + this.boss.hb.height / 8;
        this.target_x = this.current_x;
        this.target_y = this.current_y;

        this.doneAnimating = true;
        this.isHidden = false;

        this.privateHb.move(this.current_x + this.privateHb.width * 6 / 4, this.current_y + this.privateHb.height * 3 / 5);
    }

    @Override
    public void updatePositions() {
        super.updatePositions();
        this.privateScale = (float) ReflectionHacks.getPrivate(this, DiscardPilePanel.class, "scale");
    }

    @SpireOverride
    protected void openDiscardPile() {
        if(!this.boss.discardpile.isEmpty()) {
            AbstractDungeon.dynamicBanner.hide();
            CardGroup tmp = new CardGroup(CardGroup.CardGroupType.DISCARD_PILE);
            for (final AbstractCard card : this.boss.discardpile.group) {
                tmp.addToTop(new DummyCard(card));
            }
            AbstractDungeon.gridSelectScreen.openConfirmationGrid(tmp, MonsterDrawPilePanel.PILESTRINGS.TEXT[1]);
            AbstractDungeon.gridSelectScreen.confirmButton.hideInstantly();
            AbstractDungeon.dynamicBanner.hide();
            AbstractDungeon.overlayMenu.cancelButton.showInstantly(MonsterDrawPilePanel.PILESTRINGS.TEXT[1]);
        }
        this.privateHb.hovered = false;
        InputHelper.justClickedLeft = false;
    }

    @Override
    public void render(SpriteBatch sb) {
        this.renderButton(sb);
        String msg = Integer.toString(this.boss.discardpile.size());
        this.privateGl.setText(FontHelper.eventBodyText, msg);
        sb.setColor(Color.WHITE);
        sb.draw(ImageMaster.DECK_COUNT_CIRCLE, this.current_x + COUNT_OFFSET_X, this.current_y + COUNT_OFFSET_Y, COUNT_CIRCLE_W, COUNT_CIRCLE_W);

        FontHelper.renderFontCentered(sb, FontHelper.eventBodyText, msg, this.current_x + COUNT_X, this.current_y + COUNT_Y);
        if (!this.isHidden) {
            this.privateHb.render(sb);
            if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.DISCARD_VIEW) {
                //this.bannerHb.render(sb);
            }
        }
    }

    private void renderButton(SpriteBatch sb) {
        if (this.privateHb.hovered) {
            this.privateScale = 1.2F * Settings.scale;
        }

        Iterator var2 = this.privateVfxBelow.iterator();

        DiscardGlowEffect e;
        while(var2.hasNext()) {
            e = (DiscardGlowEffect)var2.next();
            e.render(sb, this.current_x - 1664.0F * Settings.scale, this.current_y + this.privateBob.y * 0.5F);
        }

        sb.setColor(Color.WHITE);
        sb.draw(ImageMaster.DISCARD_BTN_BASE, this.current_x + DECK_X, this.current_y + DECK_Y + this.privateBob.y / 2.0F, 64.0F, 64.0F, 128.0F, 128.0F, this.privateScale, this.privateScale, 0.0F, 0, 0, 128, 128, false, false);
        var2 = this.privateVfxAbove.iterator();

        while(var2.hasNext()) {
            e = (DiscardGlowEffect)var2.next();
            e.render(sb, this.current_x - 1664.0F * Settings.scale, this.current_y + this.privateBob.y * 0.5F);
        }

    }
}
