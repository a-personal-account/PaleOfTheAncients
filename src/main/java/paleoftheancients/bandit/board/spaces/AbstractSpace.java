package paleoftheancients.bandit.board.spaces;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import paleoftheancients.PaleMod;
import paleoftheancients.bandit.actions.TransformSquareAction;
import paleoftheancients.bandit.board.AbstractBoard;
import paleoftheancients.bandit.board.AbstractDrone;
import paleoftheancients.bandit.board.BanditBoard;
import paleoftheancients.bandit.board.spaces.symmetrical.EmptySpace;
import paleoftheancients.finarubossu.vfx.DamageCurvy;
import paleoftheancients.finarubossu.vfx.DamageLine;

public abstract class AbstractSpace {
    public static String[] BASETEXT = CardCrawlGame.languagePack.getUIString(PaleMod.makeID("AbstractSpace")).TEXT;
    protected AbstractBoard board;

    public AbstractSpace(AbstractBoard board, int x2, int y2) {
        this.board = board;
        x = x2;
        y = y2;
        hb = new Hitbox(x2, y2, 64 * Settings.scale, 64 * Settings.scale);
        hb.translate(x2, y2);
        this.alpha = 1.0F;
    }

    protected int maxLines = 36;
    protected int stride = 360 / maxLines;
    protected float offset = MathUtils.random(-180.0F, 180.0F);

    public boolean triggersWhenPassed = false;

    public boolean explodeOnUse = false;

    public GOODNESS goodness;

    public boolean hover = false;

    public Hitbox hb;

    protected Texture tex;

    public int x;
    public int y;

    private float alpha;

    public void render(SpriteBatch sb) {
        Color c = Color.WHITE.cpy();
        c.a = alpha;
        sb.setColor(c);
        sb.draw(tex, x, y, tex.getWidth() / 2F, tex.getHeight() / 2F, tex.getWidth(), tex.getHeight(), Settings.scale, Settings.scale, 0, 0, 0, tex.getWidth(), tex.getHeight(), false, false);
        renderOutline(sb);
    }

    public void renderOutline(SpriteBatch sb) {
        if (this.hover)
            FontHelper.renderFontCentered(sb, FontHelper.energyNumFontRed, String.valueOf(((board.squareList.indexOf(this) - board.player.position) + board.squareList.size()) % board.squareList.size()), x + ((64 / 2F)), y + ((64 / 2F)), Color.WHITE);

        Texture toRender;
        if (board.squareList.get(board.player.position) == this || droneOnThis()) {
            toRender = board.onOutline;
        } else if (this.triggersWhenPassed) {
            toRender = board.activatesWhenPassedOutline;
        } else {
            switch(this.goodness) {
                case REALLYBAD:
                    toRender = board.deathOutline;
                    break;
                case GOOD:
                    toRender = board.goodOutline;
                    break;
                case BAD:
                    toRender = board.badOutline;
                    break;
                default:
                    toRender = board.neutralOutline;
                    break;
            }
        }
        sb.draw(toRender, x, y, toRender.getWidth() / 2F, toRender.getHeight() / 2F, toRender.getWidth(), toRender.getHeight(), Settings.scale, Settings.scale, 0, 0, 0, toRender.getWidth(), toRender.getHeight(), false, false);
    }

    public boolean droneOnThis() {
        if(board instanceof BanditBoard) {
            for (AbstractDrone r : ((BanditBoard) board).droneList) {
                if (board.squareList.get(r.position) == this)
                    return true;
            }
        }
        return false;
    }

    public void uponLand(AbstractCreature actor) {
        if (explodeOnUse) {
            AbstractDungeon.actionManager.addToTop(new TransformSquareAction(board, this, EmptySpace.class));
        }
        onLanded(actor);
    }

    public abstract void onLanded(AbstractCreature actor);

    public void onPassed(AbstractCreature actor) {
        if (triggersWhenPassed) {
            splat();
            uponLand(actor);
        }
    }

    public abstract String getHeaderText();
    public abstract String getBodyText();

    protected void att(AbstractGameAction action) {
        AbstractDungeon.actionManager.addToTop(action);
    }

    protected void atb(AbstractGameAction action) {
        AbstractDungeon.actionManager.addToTop(action);
    }

    public void splat() {
        for (int i = 0; i < maxLines; i++) {
            AbstractDungeon.effectList.add(new DamageLine(this.hb.cX, this.hb.cY, new Color(MathUtils.random(), MathUtils.random(), MathUtils.random(), 1), ((stride * i) + MathUtils.random(-stride, stride) + offset)));
            if (i % 2 == 0) {
                AbstractDungeon.effectList.add(new DamageCurvy(this.hb.cX, this.hb.cY, new Color(MathUtils.random(), MathUtils.random(), MathUtils.random(), 1)));
            }
        }
    }

    public void updateAlpha() {
        if(this.x + this.hb.width < board.player.location.x && this.alpha > 0F) {
            this.alpha -= Gdx.graphics.getDeltaTime();
            if(this.alpha < 0F) {
                this.alpha = 0F;
            }
        } else if(this.x >= board.player.location.x && this.alpha < 1F) {
            this.alpha += Gdx.graphics.getDeltaTime();
            if(this.alpha > 1F) {
                this.alpha = 1F;
            }
        }
    }

    public enum GOODNESS {
        GOOD,
        OKAY,
        BAD,
        REALLYBAD
    }
}