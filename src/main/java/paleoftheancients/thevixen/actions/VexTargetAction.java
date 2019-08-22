package thevixen.actions;

import basemod.BaseMod;
import basemod.interfaces.PostUpdateSubscriber;
import basemod.interfaces.RenderSubscriber;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.GameCursor;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import thevixen.helpers.SingleTargetRelic;

public class VexTargetAction implements RenderSubscriber, PostUpdateSubscriber {


    private SingleTargetRelic r;
    private AbstractCreature hoveredCreature;
    private Vector2 controlPoint;
    private Vector2 origin;
    private float arrowScale;
    private float arrowScaleTimer;
    private Vector2[] points = new Vector2[20];
    private boolean isHidden;

    public VexTargetAction(SingleTargetRelic r, float originX, float originY) {
        this.r = r;
        this.origin = new Vector2(originX, originY);

        BaseMod.subscribe(this);
        this.isHidden = false;
        com.megacrit.cardcrawl.core.GameCursor.hidden = true;
        for (int i = 0; i < this.points.length; i++) {
            this.points[i] = new Vector2();
        }
    }
    public VexTargetAction(SingleTargetRelic r, Hitbox origin) {
        this(r, origin.cX, origin.cY);
    }
    public VexTargetAction(SingleTargetRelic r) {
        this(r, ((AbstractRelic)r).hb);
    }

    private void close() {
        this.isHidden = true;
        GameCursor.hidden = false;
        VexTargetAction vex = this;
        AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
            @Override
            public void update() {
                BaseMod.unsubscribe(vex);
                this.isDone = true;
            }
        });
    }

    private void updateTargetMode() {
        this.hoveredCreature = null;
        for (AbstractCreature m : AbstractDungeon.getMonsters().monsters) {
            if ((m.hb.hovered) && (!m.isDying) && !m.halfDead) {
                this.hoveredCreature = m;
                break;
            }
        }
        AbstractCreature m = AbstractDungeon.player;
        if ((m.hb.hovered) && (!m.isDying) && !m.halfDead) {
            this.hoveredCreature = m;
        }

        if (InputHelper.justClickedLeft || InputHelper.justReleasedClickRight) {
            if (this.hoveredCreature != null && this.hoveredCreature != AbstractDungeon.player) {
                r.onTargetChosen(this.hoveredCreature);
                close();
            } else if(InputHelper.justReleasedClickRight) {
                close();
            }
        }
    }

    @Override
    public void receiveRender(SpriteBatch sb) {
        render(sb);
    }

    public void render(SpriteBatch sb) {
        if (!this.isHidden) {
            renderTargetingUi(sb);
            if (this.hoveredCreature != null) {
                this.hoveredCreature.renderReticle(sb);
            }
        }
    }

    public void renderTargetingUi(SpriteBatch sb) {
        float x = InputHelper.mX;
        float y = InputHelper.mY;
        this.controlPoint = new Vector2(origin.x, y * 0.6F);
        if (this.hoveredCreature == null) {
            this.arrowScale = Settings.scale;
            this.arrowScaleTimer = 0.0F;
            sb.setColor(new Color(1.0F, 1.0F, 1.0F, 1.0F));
        } else {
            this.arrowScaleTimer += com.badlogic.gdx.Gdx.graphics.getDeltaTime();
            if (this.arrowScaleTimer > 1.0F) {
                this.arrowScaleTimer = 1.0F;
            }

            this.arrowScale = com.badlogic.gdx.math.Interpolation.elasticOut.apply(Settings.scale, Settings.scale * 1.2F, this.arrowScaleTimer);
            sb.setColor(new Color(1.0F, 0.2F, 0.3F, 1.0F));
        }
        Vector2 tmp = new Vector2(this.controlPoint.x - x, this.controlPoint.y - y);
        tmp.nor();

        drawCurvedLine(sb, origin, new Vector2(x, y), this.controlPoint);

        sb.draw(ImageMaster.TARGET_UI_ARROW, x - 128.0F, y - 128.0F, 128.0F, 128.0F, 256.0F, 256.0F, this.arrowScale, this.arrowScale, tmp.angle() + 90.0F, 0, 0, 256, 256, false, false);

    }

    private void drawCurvedLine(SpriteBatch sb, Vector2 start, Vector2 end, Vector2 control) {
        float radius = 7.0F * Settings.scale;

        for (int i = 0; i < this.points.length - 1; i++) {
            this.points[i] = com.badlogic.gdx.math.Bezier.quadratic(this.points[i], (i + 1) / 21.0F, start, control, end, new Vector2());
            radius += 0.4F * Settings.scale;

            Vector2 ref = i == 0 ? start : this.points[(i - 1)];

            float angle = ref.cpy().sub(this.points[i]).angle() + 90.0F;
            sb.draw(ImageMaster.TARGET_UI_CIRCLE, this.points[i].x - 64.0F, this.points[i].y - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, radius / 18.0F, radius / 18.0F, angle, 0, 0, 128, 128, false, false);
        }
    }

    /*     */
    @Override
    public void receivePostUpdate() {
        if (!this.isHidden) {
            updateTargetMode();
        }
    }
}
