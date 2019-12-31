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

import java.util.HashMap;
import java.util.Map;

public abstract class ReimuPhase {
    public Map<Byte, ReimuMoveInfo> moves;
    protected SpellCardBackgroundVFX backgroundVFX;

    public ReimuPhase() {
        moves = new HashMap<>();
        backgroundVFX = null;
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
