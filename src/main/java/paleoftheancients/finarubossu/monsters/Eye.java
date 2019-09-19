package paleoftheancients.finarubossu.monsters;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import paleoftheancients.finarubossu.powers.AbstractGazePower;

import java.util.HashMap;
import java.util.Map;

public abstract class Eye extends AbstractMonster {

    protected Map<Byte, EnemyMoveInfo> moves = new HashMap<>();

    public Eye(String name, String id, int maxHealth, float hb_x, float hb_y, String imgUrl, float offsetX, float offsetY, Texture t) {
        super(name, id, maxHealth, hb_x, hb_y, 64 * 3 / 2, 64 * 3 / 2, imgUrl, offsetX, offsetY);
        this.drawX = offsetX;
        this.drawY = offsetY;
        this.img = t;

        if(AbstractDungeon.ascensionLevel >= 19) {
            this.setHp(this.maxHealth * 3 / 2);
        }
        if(AbstractDungeon.ascensionLevel >= 4) {
            this.setHp(this.maxHealth * 3 / 2);
        }
    }


    protected void setMoveShortcut(byte next) {
        EnemyMoveInfo info = this.moves.get(next);
        this.setMove(next, info.intent, info.baseDamage, info.multiplier, info.isMultiDamage);
    }

    @Override
    public void takeTurn() {
        if (this.nextMove == Byte.MIN_VALUE) {
            return;
        }
        this.takeEyeTurn();
    }
    abstract void takeEyeTurn();

    @Override
    public void damage(DamageInfo info) {
        super.damage(info);

        if(this.currentHealth <= 0 && this.nextMove != Byte.MIN_VALUE) {
            this.halfDead = true;
            this.hideHealthBar();
            this.setMove(Byte.MIN_VALUE, Intent.NONE);
            this.createIntent();

            boolean eyesded = true;
            for (final AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
                if (mo instanceof Eye && !mo.halfDead) {
                    eyesded = false;
                    break;
                }
            }
            if (eyesded) {
                N n = (N) AbstractDungeon.getCurrRoom().monsters.getMonster(N.ID);
                if (n != null) {
                    n.awaken();
                }
            } else {
                for(int i = this.powers.size() - 1; i >= 0; i--) {
                    AbstractPower pow = this.powers.get(i);
                    pow.onDeath();
                    if(!(pow instanceof AbstractGazePower)) {
                        this.powers.remove(i);
                    }
                }
                for(final AbstractRelic relic : AbstractDungeon.player.relics) {
                    relic.onMonsterDeath(this);
                }
            }
        }
    }

    @Override
    public void die(){}

    @Override
    public void dispose() {

    }

    protected static final byte REVIVAL_CONST = Byte.MIN_VALUE + 1;
    protected static final String REVIVAL_STATE = "REVIVE";
    @Override
    public void changeState(String state) {
        switch(state) {
            case REVIVAL_STATE:
                this.halfDead = false;
                this.showHealthBar();
                break;
        }
    }

    public void refresh() {
        this.refreshHitboxLocation();
        this.refreshIntentHbLocation();
    }
}
