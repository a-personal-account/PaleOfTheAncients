package paleoftheancients.reimu.monsters;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.VoidCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import paleoftheancients.reimu.actions.DamagingAction;
import paleoftheancients.reimu.actions.PersuasionNeedleAction;
import paleoftheancients.reimu.vfx.EvilSealingCircleVFX;
import paleoftheancients.reimu.vfx.ExterminationVFX;
import paleoftheancients.reimu.vfx.HakureiBarrierVFX;

public class ReimuThree extends ReimuPhase {
    private static final byte EVILSEALINGCIRCLE = 0;
    private static final byte HAKUREIDANMAKUBARRIER = 1;
    private static final byte PERSUASIONNEEDLE = 2;
    private static final byte EXTERMINATION = 3;
    private static final byte SKYCONQUERINGWINDGODKICK = 4;
    private static final byte THWACK = 4;

    public ReimuThree() {
        this.moves.put(EVILSEALINGCIRCLE, new ReimuMoveInfo(EVILSEALINGCIRCLE, AbstractMonster.Intent.ATTACK, calcAscensionNumber(16), 3, true, Reimu.ReimuAnimation.None, calcAscensionNumber(2.7F)));
        this.moves.put(HAKUREIDANMAKUBARRIER, new ReimuMoveInfo(HAKUREIDANMAKUBARRIER, AbstractMonster.Intent.ATTACK_BUFF, calcAscensionNumber(4), calcAscensionNumber(15), true, Reimu.ReimuAnimation.None));
    }

    @Override
    public void takeTurn(Reimu reimu, ReimuMoveInfo rmi, DamageInfo info) {
        switch (reimu.nextMove) {
            case EVILSEALINGCIRCLE:
                DeclareSpellcard(reimu, 3, 1);
                AbstractDungeon.actionManager.addToBottom(new DamagingAction(() -> new EvilSealingCircleVFX(AbstractDungeon.player, reimu, info, rmi.multiplier)));
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(new VoidCard(), calcAscensionNumber(rmi.magicNumber)));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, reimu, new WeakPower(AbstractDungeon.player, rmi.magicNumber, true), rmi.magicNumber));
                EndSpellcard(reimu);
                break;
            case HAKUREIDANMAKUBARRIER:
                DeclareSpellcard(reimu, 3, 2);
                AbstractDungeon.actionManager.addToBottom(new DamagingAction(() -> new HakureiBarrierVFX(AbstractDungeon.player, reimu, info, rmi.multiplier)));
                EndSpellcard(reimu);
                break;

            case PERSUASIONNEEDLE:
                reimu.runAnim(Reimu.ReimuAnimation.MagicForward);
                AbstractDungeon.actionManager.addToBottom(new PersuasionNeedleAction(AbstractDungeon.player, reimu, info, rmi.multiplier));
                break;
            case EXTERMINATION:
                reimu.runAnim(Reimu.ReimuAnimation.MagicForward);
                AbstractDungeon.actionManager.addToBottom(new DamagingAction(() -> new ExterminationVFX(AbstractDungeon.player, reimu, info, rmi.multiplier)));
                break;
        }
    }

    @Override
    public void getMove(Reimu reimu, final int num) {
        reimu.setMoveShortcut(EVILSEALINGCIRCLE, (byte) (AbstractDungeon.ascensionLevel >= 9 ? 7 : 6));
    }

    @Override
    public void setDeathbombIntent(Reimu reimu) {
        reimu.setMoveShortcut(HAKUREIDANMAKUBARRIER, 8);
    }
}
