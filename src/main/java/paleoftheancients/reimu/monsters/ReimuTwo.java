package paleoftheancients.reimu.monsters;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import paleoftheancients.reimu.actions.DamagingAction;
import paleoftheancients.reimu.actions.EndSpellAction;
import paleoftheancients.reimu.powers.ShotTypeAmuletPower;
import paleoftheancients.reimu.powers.ShotTypeNeedlePower;
import paleoftheancients.reimu.vfx.FSBlinkVFX;
import paleoftheancients.reimu.vfx.FantasySealVFX;
import paleoftheancients.reimu.vfx.HakureiAmuletVFX;

public class ReimuTwo extends ReimuPhase {
    private static final byte FANTASYSEAL = 0;
    private static final byte FSBLINK = 1;
    private static final byte AMULET = 2;

    public ReimuTwo() {
        this.moves.put(FANTASYSEAL, new ReimuMoveInfo(FANTASYSEAL, AbstractMonster.Intent.ATTACK_DEBUFF, calcAscensionNumber(40), 1, false, Reimu.ReimuAnimation.None));
        this.moves.put(FSBLINK, new ReimuMoveInfo(FSBLINK, AbstractMonster.Intent.ATTACK, calcAscensionNumber(7), 8, true, Reimu.ReimuAnimation.None));
        this.moves.put(AMULET, new ReimuMoveInfo(AMULET, AbstractMonster.Intent.ATTACK, calcAscensionNumber(13), 3, true, Reimu.ReimuAnimation.MagicUp));
    }

    @Override
    public void takeTurn(Reimu reimu, ReimuPhase.ReimuMoveInfo rmi, DamageInfo info) {
        switch (reimu.nextMove) {
            case FANTASYSEAL:
                DeclareSpellcard(reimu, 1, 1);
                AbstractDungeon.actionManager.addToBottom(new DamagingAction(() -> new FantasySealVFX(AbstractDungeon.player, reimu, info, rmi.multiplier)));
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(new Burn(), calcAscensionNumber(3)));
                EndSpellcard(reimu);
                break;
            case FSBLINK:
                DeclareSpellcard(reimu, 2, 2);
                AbstractDungeon.actionManager.addToBottom(new DamagingAction(() -> new FSBlinkVFX(AbstractDungeon.player, reimu, info, rmi.multiplier)));
                AbstractDungeon.actionManager.addToBottom(new EndSpellAction(reimu, backgroundVFX));
                EndSpellcard(reimu);
                break;

            case AMULET:
                reimu.runAnim(Reimu.ReimuAnimation.MagicUp);
                AbstractDungeon.actionManager.addToBottom(new DamagingAction(() -> new HakureiAmuletVFX(AbstractDungeon.player, reimu, info, rmi.multiplier, 8)));
                break;
        }
    }

    @Override
    public void getMove(Reimu reimu, final int num) {
        //reimu.setMoveShortcut(FANTASYSEAL, (byte) (AbstractDungeon.ascensionLevel >= 9 ? 4 : 3));
        reimu.setMoveShortcut(AMULET);
    }

    @Override
    public void setDeathbombIntent(Reimu reimu) {
        reimu.setMoveShortcut(FSBLINK, 5);
    }

    @Override
    public void die(Reimu reimu) {
        reimu.phase = new ReimuThree();
        reassignShottype(ShotTypeAmuletPower.POWER_ID, new ShotTypeNeedlePower(null));
    }
}
