package paleoftheancients.reimu.monsters;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import paleoftheancients.finarubossu.actions.GuaranteePowerApplicationAction;
import paleoftheancients.reimu.powers.ShotTypeAmuletPower;
import paleoftheancients.reimu.powers.ShotTypeBasePower;

public class ReimuTwo extends ReimuPhase {
    private static final byte FANTASYSEAL = 0;
    private static final byte FSBLINK = 1;

    public ReimuTwo() {
        for(final AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if(mo.id.equals(YinYangOrb.ID) && !mo.isDeadOrEscaped()) {
                AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(mo, mo, ShotTypeBasePower.POWER_ID));
                AbstractDungeon.actionManager.addToBottom(new GuaranteePowerApplicationAction(mo, mo, new ShotTypeAmuletPower(mo)));
            }
        }
    }

    @Override
    public void takeTurn(Reimu reimu, ReimuPhase.ReimuMoveInfo rmi, DamageInfo info) {
        switch (reimu.nextMove) {

        }
    }

    @Override
    public void getMove(Reimu reimu, final int num) {

    }

    @Override
    public void setDeathbombIntent(Reimu reimu) {
        reimu.setMoveShortcut(FSBLINK);
    }
}
