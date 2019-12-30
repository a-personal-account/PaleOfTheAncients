package paleoftheancients.reimu.monsters;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import paleoftheancients.finarubossu.actions.GuaranteePowerApplicationAction;
import paleoftheancients.reimu.powers.ShotTypeAmuletPower;
import paleoftheancients.reimu.powers.ShotTypeNeedlePower;

public class ReimuThree extends ReimuPhase {
    private static final byte EVILSEALINGCIRCLE = 0;
    private static final byte HAKUREIDANMAKUBARRIER = 1;
    private static final byte PERSUASIONNEEDLE = 2;
    private static final byte EXTERMINATION = 3;
    private static final byte SKYCONQUERINGWINDGODKICK = 4;
    private static final byte THWACK = 4;

    public ReimuThree() {
        for(final AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if(mo.id.equals(YinYangOrb.ID) && !mo.isDeadOrEscaped()) {
                AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(mo, mo, ShotTypeAmuletPower.POWER_ID));
                AbstractDungeon.actionManager.addToBottom(new GuaranteePowerApplicationAction(mo, mo, new ShotTypeNeedlePower(mo)));
            }
        }
    }

    @Override
    public void takeTurn(Reimu reimu, ReimuMoveInfo rmi, DamageInfo info) {
        switch (reimu.nextMove) {

        }
    }

    @Override
    public void getMove(Reimu reimu, final int num) {

    }

    @Override
    public void setDeathbombIntent(Reimu reimu) {
        reimu.setMoveShortcut(HAKUREIDANMAKUBARRIER);
    }
}
