package paleoftheancients.helpers;

import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractBossMonster extends CustomMonster {

    protected Map<Byte, EnemyMoveInfo> moves;
    private boolean damageInfoSet = false;

    public AbstractBossMonster(String name, String id, int maxHealth, float hb_x, float hb_y, float hb_w, float hb_h, String imgUrl, float offsetX, float offsetY) {
        super(name, id, maxHealth, hb_x, hb_y, hb_w, hb_h, imgUrl, offsetX, offsetY);
        setupMisc();
    }

    public AbstractBossMonster(String name, String id, int maxHealth, float hb_x, float hb_y, float hb_w, float hb_h, String imgUrl, float offsetX, float offsetY, boolean ignoreBlights) {
        super(name, id, maxHealth, hb_x, hb_y, hb_w, hb_h, imgUrl, offsetX, offsetY, ignoreBlights);
        setupMisc();
    }

    public AbstractBossMonster(String name, String id, int maxHealth, float hb_x, float hb_y, float hb_w, float hb_h, String imgUrl) {
        super(name, id, maxHealth, hb_x, hb_y, hb_w, hb_h, imgUrl);
        setupMisc();
    }
    private void setupMisc() {
        this.type = EnemyType.BOSS;
        this.moves = new HashMap<>();
    }

    protected void addMove(byte moveCode, Intent intent) {
        this.addMove(moveCode, intent, -1);
    }
    protected void addMove(byte moveCode, Intent intent, int baseDamage) {
        this.addMove(moveCode, intent, baseDamage, 0);
    }
    protected void addMove(byte moveCode, Intent intent, int baseDamage, int multiplier) {
        this.addMove(moveCode, intent, baseDamage, multiplier, false);
    }
    protected void addMove(byte moveCode, Intent intent, int baseDamage, int multiplier, boolean isMultiDamage) {
        this.moves.put(moveCode, new EnemyMoveInfo(moveCode, intent, baseDamage, multiplier, isMultiDamage));
    }

    @Override
    public void takeTurn() {
        DamageInfo info = null;
        int multiplier = 0;
        if(moves.containsKey(this.nextMove)) {
            EnemyMoveInfo emi = moves.get(this.nextMove);
            info = new DamageInfo(this, emi.baseDamage, DamageInfo.DamageType.NORMAL);
            multiplier = emi.multiplier;
        } else {
            info = new DamageInfo(this, 0, DamageInfo.DamageType.NORMAL);
        }
        if(damageInfoSet) {
            info = this.damage.get(0);
            this.damage.remove(0);
            damageInfoSet = false;
        } else {
            info.applyPowers(this, AbstractDungeon.player);
        }
        takeCustomTurn(info, multiplier);

        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }
    public abstract void takeCustomTurn(DamageInfo info, int multiplier);

    @Override
    protected void getMove(int num) {
        ArrayList<Byte> possibilities = new ArrayList<>(this.moves.keySet());
        for(int i = this.moveHistory.size() - 1, found = 0; i >= 0 && found < moves.size() - 2; i--) {
            boolean foundThisCycle = false;
            int before;
            do {
                before = possibilities.size();
                possibilities.remove(this.moveHistory.get(i));
                if(!foundThisCycle && possibilities.size() != before) {
                    found++;
                    foundThisCycle = true;
                }
            } while(before != possibilities.size());
        }
        this.setMoveShortcut(possibilities.get(AbstractDungeon.monsterRng.random(possibilities.size() - 1)));
    }

    public void setMoveShortcut(byte next, String text) {
        EnemyMoveInfo info = this.moves.get(next);
        this.setMove(text, next, info.intent, info.baseDamage, info.multiplier, info.isMultiDamage);
        setDamageInfo(info.baseDamage);
    }
    public void setMoveShortcut(byte next) {
        EnemyMoveInfo info = this.moves.get(next);
        this.setMove(next, info.intent, info.baseDamage, info.multiplier, info.isMultiDamage);
        setDamageInfo(info.baseDamage);
    }
    private void setDamageInfo(int baseDamage) {
        if(!damageInfoSet) {
            this.damage.add(0, new DamageInfo(this, 0, DamageInfo.DamageType.NORMAL));
            damageInfoSet = true;
        }
        this.damage.get(0).base = baseDamage;
    }
}
