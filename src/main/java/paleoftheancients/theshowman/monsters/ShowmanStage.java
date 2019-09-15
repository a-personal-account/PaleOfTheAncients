package paleoftheancients.theshowman.monsters;

import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.exordium.GoldenIdolEvent;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import paleoftheancients.PaleMod;

public class ShowmanStage extends CustomMonster {

    public static final String ID = PaleMod.makeID("ShowmanStage");
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final int BASEHP = 50;

    private TheShowmanBoss owner;

    private float duration;
    private TextureRegion CURTAIN_LEFT = null;
    private TextureRegion CURTAIN_RIGHT = null;
    private TextureRegion CURTAIN_MIDDLE = null;
    private float offSetLeftX;
    private float offSetRightX;
    private float offSetMiddleY;
    private float graphicsAnimation;
    private float scaleLeftWidth;
    private float scaleLeftHeight;
    private float scaleRightWidth;
    private float scaleRightHeight;
    private float scaleMiddleWidth;
    private float scaleMiddleHeight;
    private Color color;

    private float leftRotation, rightRotation, leftRotVelocity, rightRotVelocity, crumblingY, crumblingVelocity;

    public ShowmanStage(int hp, TheShowmanBoss owner) {
        //super(NAME, ID, BASEHP + hp, 0.0F, -15.0F, 300.0F, 330.0F, NeowsRealmMod.assetPath("images/misc/emptypixel.png"), 0.0F, 0.0F);
        super(NAME, ID, BASEHP + hp, 0.0F, -15.0F, 300.0F, 230.0F, PaleMod.assetPath("images/misc/emptypixel.png"), -300.0F, 0.0F);

        this.drawX = Settings.WIDTH / 2;
        this.refreshHitboxLocation();


        this.type = EnemyType.NORMAL;
        this.owner = owner;


        this.CURTAIN_LEFT = new TextureRegion(new Texture(PaleMod.assetPath("images/TheShowman/vfx/curtain/curtain_left.png")));
        this.CURTAIN_RIGHT = new TextureRegion(new Texture(PaleMod.assetPath("images/TheShowman/vfx/curtain/curtain_right.png")));
        this.CURTAIN_MIDDLE = new TextureRegion(new Texture(PaleMod.assetPath("images/TheShowman/vfx/curtain/curtain_main_long.png")));
        this.setStage();
    }

    public void usePreBattleAction() {}

    public void takeTurn() {
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    protected void getMove(int num) {
        this.setMove((byte)0, Intent.NONE);
    }

    public void die() {
        this.halfDead = true;
        this.hideHealthBar();
        this.healthBarUpdatedEvent();
        this.owner.stageStun();
        this.crumblingVelocity = 3F;
        this.leftRotVelocity = MathUtils.random(10F, 30F);
        this.rightRotVelocity = MathUtils.random(-30F, -10F);
    }

    public void setStage() {
        this.halfDead = false;
        this.duration = 2F;
        this.graphicsAnimation = 0.0F;
        this.offSetRightX = this.CURTAIN_RIGHT.getRegionWidth();
        this.offSetLeftX = -this.CURTAIN_LEFT.getRegionWidth();
        this.offSetMiddleY = this.CURTAIN_MIDDLE.getRegionHeight();
        this.scaleLeftWidth = Settings.scale;
        this.scaleLeftHeight = Settings.scale;
        this.scaleRightWidth = Settings.scale;
        this.scaleRightHeight = Settings.scale;
        this.scaleMiddleWidth = Settings.scale;
        this.scaleMiddleHeight = Settings.scale;
        this.color = Color.WHITE.cpy();
        this.leftRotation = 0F;
        this.rightRotation = 0F;
        this.showHealthBar();
        this.healthBarUpdatedEvent();
        this.crumblingY = 0F;
    }

    @Override
    public void update() {
        super.update();

        if(this.halfDead) {
            this.leftRotation -= this.leftRotVelocity * Gdx.graphics.getDeltaTime();
            this.rightRotation -= this.rightRotVelocity * Gdx.graphics.getDeltaTime();
            this.crumblingY -= this.crumblingVelocity * Gdx.graphics.getDeltaTime();
            this.crumblingVelocity *= 1.2;
        } else if(this.duration > 0F) {
            this.duration -= Gdx.graphics.getDeltaTime();
            this.graphicsAnimation += Gdx.graphics.getDeltaTime();

            if (this.graphicsAnimation <= 0.5F) {
                this.offSetLeftX = Interpolation.fade.apply(-this.CURTAIN_LEFT.getRegionWidth(), 0F, this.graphicsAnimation / 0.5F);
                this.offSetRightX = Interpolation.fade.apply(this.CURTAIN_RIGHT.getRegionWidth(), 0F, this.graphicsAnimation / 0.5F);
                this.offSetMiddleY = Interpolation.fade.apply(this.CURTAIN_MIDDLE.getRegionHeight(), 0F, this.graphicsAnimation / 0.5F);
            } else if (this.graphicsAnimation > 0.5F && this.graphicsAnimation <= 2.0F) {
                this.offSetMiddleY = Interpolation.fade.apply(0F, this.CURTAIN_MIDDLE.getRegionHeight() * 5 / 6, (this.graphicsAnimation - 0.5F) / 1.5F);
            }
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        super.render(sb);
        sb.setColor(this.color);
        sb.draw(this.CURTAIN_MIDDLE, 0.0F, 0.0F + this.offSetMiddleY * this.scaleMiddleHeight + crumblingY, 0.0F, 0.0F, (float)this.CURTAIN_MIDDLE.getRegionWidth(), (float)this.CURTAIN_MIDDLE.getRegionHeight(), this.scaleMiddleWidth, this.scaleMiddleHeight, 0F);
        sb.draw(this.CURTAIN_LEFT, 0.0F + this.offSetLeftX * this.scaleLeftWidth, crumblingY, this.CURTAIN_LEFT.getRegionWidth() / 2, this.CURTAIN_LEFT.getRegionHeight() / 4, (float)this.CURTAIN_LEFT.getRegionWidth(), (float)this.CURTAIN_LEFT.getRegionHeight(), this.scaleLeftWidth, this.scaleLeftHeight, leftRotation);
        sb.draw(this.CURTAIN_RIGHT, Settings.WIDTH - this.CURTAIN_RIGHT.getRegionWidth() * this.scaleRightWidth + this.offSetRightX * this.scaleRightWidth, crumblingY, this.CURTAIN_RIGHT.getRegionWidth() / 2, this.CURTAIN_RIGHT.getRegionHeight() / 4, (float)this.CURTAIN_RIGHT.getRegionWidth(), (float)this.CURTAIN_RIGHT.getRegionHeight(), this.scaleRightWidth, this.scaleRightHeight, rightRotation);
    }

    @Override
    public void dispose() {
        this.CURTAIN_LEFT.getTexture().dispose();
        this.CURTAIN_RIGHT.getTexture().dispose();
        this.CURTAIN_MIDDLE.getTexture().dispose();
    }

    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
        NAME = monsterStrings.NAME;
    }
}