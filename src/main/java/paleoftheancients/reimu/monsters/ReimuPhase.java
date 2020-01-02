package paleoftheancients.reimu.monsters;

import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import paleoftheancients.PaleMod;
import paleoftheancients.finarubossu.actions.GuaranteePowerApplicationAction;
import paleoftheancients.reimu.actions.EndSpellAction;
import paleoftheancients.reimu.powers.ShotTypePower;
import paleoftheancients.reimu.vfx.SpellCardBackgroundVFX;
import paleoftheancients.reimu.vfx.SpellCardDeclarationVFX;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class ReimuPhase {
    public Map<Byte, ReimuMoveInfo> moves;
    protected SpellCardBackgroundVFX backgroundVFX;
    protected ArrayList<Byte> cycletracker;
    public static byte BOMBREFILL = Byte.MAX_VALUE;

    public ReimuPhase() {
        moves = new HashMap<>();
        this.moves.put(BOMBREFILL, new ReimuMoveInfo(BOMBREFILL, AbstractMonster.Intent.BUFF, -1, 0, false, Reimu.ReimuAnimation.Spellcall));
        backgroundVFX = null;
        cycletracker = new ArrayList<>();
        resetCycleTracker();
    }

    public abstract void takeTurn(Reimu reimu, ReimuMoveInfo rmi, DamageInfo info);

    public void getMove(Reimu reimu, int num) {
        getMoveFromCycletracker(reimu);
    }

    public void getMoveFromCycletracker(Reimu reimu) {
        getMoveFromCycletracker(reimu, 1);
    }
    public void getMoveFromCycletracker(Reimu reimu, int resetAt) {
        if(cycletracker.size() <= resetAt) {
            cycletracker.clear();
            resetCycleTracker();
            if(reimu.rui.bombs > 0) {
                useBombSkill(reimu);
                setBombIntent(reimu);
            } else {
                reimu.setMoveShortcut(BOMBREFILL);
            }
            return;
        }
        int index;
        if(cycletracker.size() == 1) {
            index = 0;
        } else {
            index = AbstractDungeon.aiRng.random(cycletracker.size() - 1);
        }
        reimu.setMoveShortcut(cycletracker.get(index));
        cycletracker.remove(index);
    }
    protected abstract void setBombIntent(Reimu reimu);
    protected abstract void resetCycleTracker();
    public void setDeathbombIntent(Reimu reimu) {}

    protected void useBombSkill(Reimu reimu) {
        //Want to be able to prevent this.
        reimu.startSpellAnimation(false);
    }

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

    public void die(Reimu reimu) {}

    protected void reassignShottype(String previousPowerID, ShotTypePower stp) {
        for(final AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if(mo.id.equals(YinYangOrb.ID) && !mo.isDeadOrEscaped()) {
                AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(mo, mo, previousPowerID));
                AbstractDungeon.actionManager.addToBottom(new GuaranteePowerApplicationAction(mo, mo, stp.makeCopy(mo)));
            }
        }
    }

    protected void DeclareSpellcard(Reimu reimu, int image, int bombs) {
        AbstractDungeon.actionManager.addToBottom(new SFXAction(PaleMod.makeID("touhou_spellcard")));
        AbstractDungeon.actionManager.addToBottom(new VFXAction(backgroundVFX = new SpellCardBackgroundVFX(), 0.5F));
        AbstractDungeon.actionManager.addToBottom(new VFXAction(new SpellCardDeclarationVFX(reimu, image)));
        reimu.rui.bombs = Math.max(0, reimu.rui.bombs - bombs);
    }
    protected void EndSpellcard(Reimu reimu) {
        AbstractDungeon.actionManager.addToBottom(new EndSpellAction(reimu, backgroundVFX));
    }



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
