package paleoftheancients.theshowman.ui;

import paleoftheancients.theshowman.misc.DummyCard;
import paleoftheancients.theshowman.monsters.TheShowmanBoss;
import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.ui.panels.ExhaustPanel;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.ExhaustPileParticle;

import java.util.ArrayList;

public class MonsterExhaustPanel extends ExhaustPanel {
    private TheShowmanBoss boss;
    private Hitbox privateHb;
    private GlyphLayout privateGl;
    private float scale;
    private ArrayList<AbstractGameEffect> particles;

    private float COUNT_CIRCLE_W;
    public MonsterExhaustPanel(TheShowmanBoss boss) {
        super();
        this.boss = boss;
        this.particles = new ArrayList<>();
        this.privateHb = (Hitbox) ReflectionHacks.getPrivate(this, ExhaustPanel.class, "hb");
        this.privateGl = (GlyphLayout) ReflectionHacks.getPrivate(this, ExhaustPanel.class, "gl");
        this.COUNT_CIRCLE_W = (float) ReflectionHacks.getPrivateStatic(ExhaustPanel.class, "COUNT_CIRCLE_W");

        this.current_x = this.boss.drawX + this.boss.hb.width;
        this.current_y = this.boss.drawY + this.boss.hb.height;

        this.scale = 0.5F;
    }

    @Override
    public void updatePositions() {
        if (!this.isHidden && !this.boss.exhaustpile.isEmpty()) {
            this.privateHb.update();
            this.updateVfx();
        }
        for(int i = this.particles.size() - 1; i >= 0; i--) {
            this.particles.get(i).update();
            if(this.particles.get(i).isDone) {
                this.particles.remove(i);
            }
        }

        this.privateHb.move(this.current_x, this.current_y);
        if (this.privateHb.hovered && (!AbstractDungeon.isScreenUp || AbstractDungeon.screen == AbstractDungeon.CurrentScreen.EXHAUST_VIEW || AbstractDungeon.screen == AbstractDungeon.CurrentScreen.HAND_SELECT || AbstractDungeon.screen == AbstractDungeon.CurrentScreen.CARD_REWARD && AbstractDungeon.overlayMenu.combatPanelsShown)) {
            AbstractDungeon.overlayMenu.hoveredTip = true;
            if (InputHelper.justClickedLeft) {
                this.privateHb.clickStarted = true;
            }
        }

        if (this.privateHb.clicked && AbstractDungeon.screen == AbstractDungeon.CurrentScreen.EXHAUST_VIEW) {
            this.privateHb.clicked = false;
            this.privateHb.hovered = false;
            CardCrawlGame.sound.play("DECK_CLOSE");
            AbstractDungeon.closeCurrentScreen();
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
        } else {
            if (this.privateHb.clicked && AbstractDungeon.overlayMenu.combatPanelsShown && !this.boss.isDead && !this.boss.exhaustpile.isEmpty()) {
                this.privateHb.clicked = false;
                this.privateHb.hovered = false;
                if (AbstractDungeon.isScreenUp) {
                    if (AbstractDungeon.previousScreen == null) {
                        AbstractDungeon.previousScreen = AbstractDungeon.screen;
                    }
                } else {
                    AbstractDungeon.previousScreen = null;
                }

                this.openMonsterExhaustPile();
            }
        }
    }

    private void updateVfx() {
        energyVfxTimer -= Gdx.graphics.getDeltaTime();
        if (energyVfxTimer < 0.0F && !Settings.hideLowerElements) {
            this.particles.add(new ExhaustPileParticle(this.current_x, this.current_y));
            energyVfxTimer = 0.05F;
        }
    }

    private void openMonsterExhaustPile() {
        AbstractDungeon.dynamicBanner.hide();
        CardGroup tmp = new CardGroup(CardGroup.CardGroupType.EXHAUST_PILE);
        for(final AbstractCard card : this.boss.exhaustpile.group) {
            tmp.addToTop(new DummyCard(card));
        }
        AbstractDungeon.gridSelectScreen.openConfirmationGrid(tmp, MonsterDrawPilePanel.PILESTRINGS.TEXT[2]);
        AbstractDungeon.gridSelectScreen.confirmButton.hideInstantly();
        AbstractDungeon.dynamicBanner.hide();
        AbstractDungeon.overlayMenu.cancelButton.showInstantly(MonsterDrawPilePanel.PILESTRINGS.TEXT[2]);
        this.privateHb.hovered = false;
        InputHelper.justClickedLeft = false;
    }

    @Override
    public void render(SpriteBatch sb) {
        if (!this.boss.exhaustpile.isEmpty()) {
            for(final AbstractGameEffect age : particles) {
                age.render(sb);
            }

            String msg = Integer.toString(this.boss.exhaustpile.size());
            this.privateGl.setText(FontHelper.eventBodyText, msg);
            sb.setColor(new Color(0.0F, 0.0F, 0.0F, 0.8F));
            sb.draw(ImageMaster.DECK_COUNT_CIRCLE, this.current_x - COUNT_CIRCLE_W / 2.0F, this.current_y - COUNT_CIRCLE_W / 2.0F, COUNT_CIRCLE_W, COUNT_CIRCLE_W);
            FontHelper.renderFontCentered(sb, FontHelper.eventBodyText, msg, this.current_x, this.current_y + 2.0F * Settings.scale, Settings.PURPLE_COLOR.cpy());
            if (Settings.isControllerMode) {
                sb.setColor(Color.WHITE);
                sb.draw(CInputActionSet.pageRightViewExhaust.getKeyImg(), this.current_x - 32.0F + 30.0F * Settings.scale, this.current_y - 32.0F - 30.0F * Settings.scale, 32.0F, 32.0F, 64.0F, 64.0F, Settings.scale * 0.75F, Settings.scale * 0.75F, 0.0F, 0, 0, 64, 64, false, false);
            }

            this.privateHb.render(sb);
            if (this.privateHb.hovered && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && !AbstractDungeon.isScreenUp) {
                if (Settings.isConsoleBuild) {
                    TipHelper.renderGenericTip(1550.0F * Settings.scale, 450.0F * Settings.scale, LABEL[0], MSG[1]);
                } else {
                    TipHelper.renderGenericTip(1550.0F * Settings.scale, 450.0F * Settings.scale, LABEL[0], MSG[0]);
                }
            }
        }

    }
}