package paleoftheancients.reimu.monsters;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.AbstractMonster.Intent;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import paleoftheancients.reimu.monsters.Reimu.ReimuAnimation;
import paleoftheancients.reimu.powers.SealedPower;
import paleoftheancients.reimu.powers.ShotTypeAmuletPower;
import paleoftheancients.reimu.powers.ShotTypeBasePower;

public class ReimuOne extends ReimuPhase {
    private static final byte FIRSTTURN = 4;

    private static final byte SEAL = 0;
    private static final byte BASICBLOCK = 1;
    private static final byte BASICATTACK = 2;
    private static final byte DEBUFFATTACK = 3;

    public ReimuOne() {
        this.moves.put(FIRSTTURN, new ReimuMoveInfo(FIRSTTURN, AbstractMonster.Intent.UNKNOWN, -1, 0, false, ReimuAnimation.Spellcall));
        this.moves.put(SEAL, new ReimuMoveInfo(SEAL, Intent.STRONG_DEBUFF, -1, 0, false, ReimuAnimation.Spellcall));
        this.moves.put(BASICATTACK, new ReimuMoveInfo(BASICATTACK, Intent.ATTACK, calcAscensionNumber(28), 0, false, ReimuAnimation.CloseAttack, calcAscensionNumber(1.3F)));
        this.moves.put(DEBUFFATTACK, new ReimuMoveInfo(DEBUFFATTACK, Intent.ATTACK_DEBUFF, calcAscensionNumber(23), 0, false, ReimuAnimation.Kick, calcAscensionNumber(2.3F)));
        this.moves.put(BASICBLOCK, new ReimuMoveInfo(BASICBLOCK, Intent.DEFEND_DEBUFF, -1, calcAscensionNumber(30), false, ReimuAnimation.MagicForward, calcAscensionNumber(2F)));
        cycletracker.clear();
        cycletracker.add(FIRSTTURN);
    }

    @Override
    public void takeTurn(Reimu reimu, ReimuMoveInfo rmi, DamageInfo info) {
        switch (reimu.nextMove) {
            case FIRSTTURN:
                resetCycleTracker();
                cycletracker.remove(MathUtils.random(cycletracker.size() - 1));
                break;
            case BASICATTACK: {
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(new Dazed(), rmi.magicNumber));
                break;
            }
            case DEBUFFATTACK: {
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, reimu, new WeakPower(AbstractDungeon.player, rmi.magicNumber, true), rmi.magicNumber));
                break;
            }
            case BASICBLOCK: {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, reimu, new VulnerablePower(AbstractDungeon.player, rmi.magicNumber, true), rmi.magicNumber));
                for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
                    if (!mo.isDying) {
                        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(mo, reimu, rmi.multiplier));
                    }
                }
                break;
            }
            case SEAL: {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, reimu, new SealedPower(AbstractDungeon.player, 1), 1));
                reimu.rui.bombs--;
                break;
            }
        }
    }

    @Override
    public void getMove(Reimu reimu, int num) {
        getMoveFromCycletracker(reimu, 0);
    }

    @Override
    protected void setBombIntent(Reimu reimu) {
        reimu.setMoveShortcut(SEAL);
    }
    @Override
    protected void resetCycleTracker() {
        cycletracker.add(BASICATTACK);
        cycletracker.add(BASICBLOCK);
        cycletracker.add(DEBUFFATTACK);
    }

    public void getMoveFromCycletracker(Reimu reimu) {
        getMoveFromCycletracker(reimu, 0);
    }

    @Override
    protected void useBombSkill(Reimu reimu) {}

    @Override
    public void die(Reimu reimu) {
        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(AbstractDungeon.player, AbstractDungeon.player, SealedPower.POWER_ID));
        reimu.phase = new ReimuTwo();
        reassignShottype(ShotTypeBasePower.POWER_ID, new ShotTypeAmuletPower(null));
    }
}
