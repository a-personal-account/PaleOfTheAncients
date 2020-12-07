package paleoftheancients.theshowman.ui;

import paleoftheancients.PaleMod;
import paleoftheancients.theshowman.misc.DummyCard;
import paleoftheancients.theshowman.monsters.TheShowmanBoss;
import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireOverride;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.relics.FrozenEye;
import com.megacrit.cardcrawl.ui.panels.DrawPilePanel;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.BobEffect;
import com.megacrit.cardcrawl.vfx.GameDeckGlowEffect;

import java.util.ArrayList;
import java.util.Iterator;

public class MonsterDrawPilePanel extends DrawPilePanel {
    public static final UIStrings PILESTRINGS = CardCrawlGame.languagePack.getUIString(PaleMod.makeID("MonsterPiles"));
    private TheShowmanBoss boss;
    public Hitbox privateHb;
    private GlyphLayout privateGl;
    private ArrayList<AbstractGameEffect> particles;
    private BobEffect privateBob;
    private final float COUNT_OFFSET_X;
    private final float COUNT_OFFSET_Y;
    private final float COUNT_X;
    private final float COUNT_Y;
    private final float DECK_X;
    private final float DECK_Y;
    private final float COUNT_CIRCLE_W;
    private float privateScale;
    private ArrayList<GameDeckGlowEffect> privateVfxBelow;

    public MonsterDrawPilePanel(TheShowmanBoss boss) {
        super();
        this.boss = boss;
        this.particles = new ArrayList<>();
        this.privateHb = (Hitbox) ReflectionHacks.getPrivate(this, DrawPilePanel.class, "hb");
        this.privateGl = (GlyphLayout) ReflectionHacks.getPrivate(this, DrawPilePanel.class, "gl");
        this.privateBob = (BobEffect) ReflectionHacks.getPrivate(this, DrawPilePanel.class, "bob");
        this.COUNT_OFFSET_X = (float) ReflectionHacks.getPrivateStatic(DrawPilePanel.class, "COUNT_OFFSET_X");
        this.COUNT_OFFSET_Y = (float) ReflectionHacks.getPrivateStatic(DrawPilePanel.class, "COUNT_OFFSET_Y");
        this.COUNT_X = (float) ReflectionHacks.getPrivateStatic(DrawPilePanel.class, "COUNT_X");
        this.COUNT_Y = (float) ReflectionHacks.getPrivateStatic(DrawPilePanel.class, "COUNT_Y");
        this.DECK_X = (float) ReflectionHacks.getPrivateStatic(DrawPilePanel.class, "DECK_X");
        this.DECK_Y = (float) ReflectionHacks.getPrivateStatic(DrawPilePanel.class, "DECK_Y");
        this.COUNT_CIRCLE_W = (float) ReflectionHacks.getPrivateStatic(DrawPilePanel.class, "COUNT_CIRCLE_W");
        this.privateVfxBelow = (ArrayList<GameDeckGlowEffect>) ReflectionHacks.getPrivate(this, DrawPilePanel.class, "vfxBelow");

        this.current_x = this.boss.drawX - this.boss.hb.width * 12F / 11;
        this.current_y = this.boss.drawY + this.boss.hb.height / 8;
        this.target_x = this.current_x;
        this.target_y = this.current_y;

        this.doneAnimating = true;

        this.privateHb.move(this.current_x + this.privateHb.width * 3 / 5, this.current_y + this.privateHb.height * 3 / 5);
        this.isHidden = false;
    }

    @Override
    public void updatePositions() {
        super.updatePositions();
        this.privateScale = (float) ReflectionHacks.getPrivate(this, DrawPilePanel.class, "scale");
    }

    @SpireOverride
    protected void openDrawPile() {
        if(!(InputActionSet.drawPile.isJustPressed() || CInputActionSet.drawPile.isJustPressed())) {
            if (!this.boss.drawpile.isEmpty()) {
                CardGroup tmp = new CardGroup(CardGroup.CardGroupType.DRAW_PILE);
                boolean frozenEye = AbstractDungeon.player.hasRelic(FrozenEye.ID);
                AbstractCard dummy;
                for (final AbstractCard card : this.boss.drawpile.group) {
                    tmp.addToTop(dummy = new DummyCard(card));
                    dummy.isFlipped = !frozenEye;
                }
                AbstractDungeon.gridSelectScreen.openConfirmationGrid(tmp, PILESTRINGS.TEXT[0]);
                AbstractDungeon.dynamicBanner.hide();
                AbstractDungeon.gridSelectScreen.confirmButton.hideInstantly();
                AbstractDungeon.overlayMenu.cancelButton.showInstantly(PILESTRINGS.TEXT[0]);
            }
            this.privateHb.hovered = false;
            InputHelper.justClickedLeft = false;
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        if (this.privateHb.hovered) {
            this.privateScale = 1.2F * Settings.scale;
        }

        Iterator var2 = this.privateVfxBelow.iterator();

        while(var2.hasNext()) {
            GameDeckGlowEffect e = (GameDeckGlowEffect)var2.next();
            e.render(sb, this.current_x, this.current_y + this.privateBob.y * 0.5F);
        }

        sb.setColor(Color.WHITE);
        sb.draw(ImageMaster.DECK_BTN_BASE, this.current_x + DECK_X, this.current_y + DECK_Y + this.privateBob.y / 2.0F, 64.0F, 64.0F, 128.0F, 128.0F, this.privateScale, this.privateScale, 0.0F, 0, 0, 128, 128, false, false);
        String msg = Integer.toString(this.boss.drawpile.size());
        this.privateGl.setText(FontHelper.panelNameFont, msg);
        sb.setColor(Color.WHITE);
        sb.draw(ImageMaster.DECK_COUNT_CIRCLE, this.current_x + COUNT_OFFSET_X, this.current_y + COUNT_OFFSET_Y, COUNT_CIRCLE_W, COUNT_CIRCLE_W);
        if (Settings.isControllerMode) {
            sb.draw(CInputActionSet.drawPile.getKeyImg(), this.current_x - 32.0F + 30.0F * Settings.scale, this.current_y - 32.0F + 40.0F * Settings.scale, 32.0F, 32.0F, 64.0F, 64.0F, Settings.scale * 0.75F, Settings.scale * 0.75F, 0.0F, 0, 0, 64, 64, false, false);
        }

        FontHelper.renderFontCentered(sb, FontHelper.panelNameFont, msg, this.current_x + COUNT_X, this.current_y + COUNT_Y);
        if (!this.isHidden) {
            this.privateHb.render(sb);
            if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.GAME_DECK_VIEW) {
                //this.bannerHb.render(sb);
            }
        }
    }
}
