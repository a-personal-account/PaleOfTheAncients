package paleoftheancients.helpers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.powers.StunMonsterPower;
import com.megacrit.cardcrawl.actions.IntentFlashAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.RunicDome;
import com.megacrit.cardcrawl.vfx.combat.MoveNameEffect;
import paleoftheancients.theshowman.misc.DummyOrb;
import paleoftheancients.theshowman.monsters.DummyMonster;

import java.util.ArrayList;

public abstract class AbstractMultiIntentMonster extends AbstractBossMonster {
    protected ArrayList<DummyMonster> dummies = new ArrayList<>();

    public AbstractMultiIntentMonster(String name, String id, int maxHealth, float hb_x, float hb_y, float hb_w, float hb_h, String imgUrl, float offsetX, float offsetY) {
        super(name, id, maxHealth, hb_x, hb_y, hb_w, hb_h, imgUrl, offsetX, offsetY);
        this.setMove((byte)-1, Intent.NONE);
    }

    public AbstractMultiIntentMonster(String name, String id, int maxHealth, float hb_x, float hb_y, float hb_w, float hb_h, String imgUrl, float offsetX, float offsetY, boolean ignoreBlights) {
        super(name, id, maxHealth, hb_x, hb_y, hb_w, hb_h, imgUrl, offsetX, offsetY, ignoreBlights);
        this.setMove((byte)-1, Intent.NONE);
    }

    public AbstractMultiIntentMonster(String name, String id, int maxHealth, float hb_x, float hb_y, float hb_w, float hb_h, String imgUrl) {
        super(name, id, maxHealth, hb_x, hb_y, hb_w, hb_h, imgUrl);
        this.setMove((byte)-1, Intent.NONE);
    }

    @Override
    public void takeTurn() {
        AbstractMonster mo;
        for(int i = dummies.size() - 1; i >= 0; i--) {
            mo = dummies.get(i);
            this.nextMove = mo.nextMove;
            addToBot(new VFXAction(new MoveNameEffect(this.hb.cX - this.animX, this.hb.cY + this.hb.height / 2.0F, mo.moveName)));
            addToBot(new IntentFlashAction(mo));
            if(!this.damage.isEmpty()) {
                damageInfoSet = true;
            }
            super.takeTurn();
            //super.takeTurn adds a RollMoveAction that I have to remove.
            AbstractDungeon.actionManager.actions.remove(AbstractDungeon.actionManager.actions.size() - 1);
        }

        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    public void setIntentAmount(int length) {
        this.setMove((byte)-1, Intent.NONE);
        while(dummies.size() < length) {
            dummies.add(new DummyMonster(0, 0, 0, 0, null));
        }
        while(dummies.size() > length) {
            dummies.remove(0);
        }
        DummyOrb orb = new DummyOrb();
        DummyMonster monster;
        for(int i = 0; i < length; i++) {
            orb.setSlot(i, length);
            monster = dummies.get(i);
            monster.drawX = (orb.tX - AbstractDungeon.player.drawX) / 2F + this.drawX;
            monster.drawY = (orb.tY - AbstractDungeon.player.hb.cY) / 2F + this.hb.y + this.hb.height;
            monster.nextMove = -1;
            monster.refresh();
        }
        this.damage.clear();
    }
    public void setMoveShortcut(byte next, String text) {
        EnemyMoveInfo info = this.moves.get(next);
        int index = 0;
        AbstractMonster monster;
        for(; (monster = dummies.get(index)).nextMove != (byte)-1; index++);
        monster.setMove(text, next, info.intent, info.baseDamage, info.multiplier, info.isMultiDamage);
        monster.createIntent();
        if(info.baseDamage > this.getIntentBaseDmg()) {
            this.setIntentBaseDmg(info.baseDamage);
        }
        monster.damage.add(new DamageInfo(this, info.baseDamage));
    }

    @Override
    public void update() {
        super.update();
        for(final AbstractMonster mo : dummies) {
            mo.update();
        }
    }
    @Override
    public void render(SpriteBatch sb) {
        super.render(sb);
        for(final AbstractMonster mo : dummies) {
            mo.render(sb);
        }
    }

    @Override
    public void renderTip(SpriteBatch sb) {
        this.tips.clear();
        if (!AbstractDungeon.player.hasRelic(RunicDome.ID)) {
            for(final DummyMonster mo : dummies) {
                this.tips.add(mo.intentTip);
            }
        }
        for (AbstractPower p : this.powers) {
            if (p.region48 != null) {
                this.tips.add(new PowerTip(p.name, p.description, p.region48));
                continue;
            }
            this.tips.add(new PowerTip(p.name, p.description, p.img));
        }
        if (!this.tips.isEmpty()) {
            if (this.hb.cX + this.hb.width / 2.0F < TIP_X_THRESHOLD) {
                TipHelper.queuePowerTips(this.hb.cX + this.hb.width / 2.0F + TIP_OFFSET_R_X, this.hb.cY +

                        TipHelper.calculateAdditionalOffset(this.tips, this.hb.cY), this.tips);
            } else {
                TipHelper.queuePowerTips(this.hb.cX - this.hb.width / 2.0F + TIP_OFFSET_L_X, this.hb.cY +

                        TipHelper.calculateAdditionalOffset(this.tips, this.hb.cY), this.tips);
            }
        }
    }

    @Override
    public void addPower(AbstractPower power) {
        if(power instanceof StunMonsterPower) {
            dummies.clear();
        }
    }
}