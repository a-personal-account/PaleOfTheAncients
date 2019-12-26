package paleoftheancients.reimu.monsters;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;

import java.util.HashMap;
import java.util.Map;

public abstract class ReimuPhase {
    public Map<Byte, ReimuMoveInfo> moves;

    public ReimuPhase() {
        moves = new HashMap<>();
    }

    public abstract void takeTurn(Reimu reimu, ReimuMoveInfo rmi, DamageInfo info);
    public abstract void getMove(Reimu reimu, int num);
    public void setDeathbombIntent(Reimu reimu) {}

    public int calcAscensionNumber(float base) {
        if(AbstractDungeon.ascensionLevel >= 19) {
            base *= 1.35F;
        } else if(AbstractDungeon.ascensionLevel >= 9) {
            base *= 1.20F;
        } else if(AbstractDungeon.ascensionLevel >= 4) {
            base *= 1.15F;
        }
        return Math.round(base);
    }

    public void die() {}

    class ReimuMoveInfo extends EnemyMoveInfo {
        public Reimu.ReimuAnimation animation;
        public int magicNumber;
        public ReimuMoveInfo(byte nextMove, AbstractMonster.Intent intent, int intentBaseDmg, int multiplier, boolean isMultiDamage, Reimu.ReimuAnimation animation) {
            this(nextMove, intent, intentBaseDmg, multiplier, isMultiDamage, animation, 0);
        }
        public ReimuMoveInfo(byte nextMove, AbstractMonster.Intent intent, int intentBaseDmg, int multiplier, boolean isMultiDamage, Reimu.ReimuAnimation animation, int magicNumber) {
            super(nextMove, intent, intentBaseDmg, multiplier, isMultiDamage);
            this.animation = animation;
            this.magicNumber = magicNumber;
        }
    }
}
