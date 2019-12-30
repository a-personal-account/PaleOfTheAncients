package paleoftheancients.reimu.monsters;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
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

public class ReimuOne extends ReimuPhase {
    private static final byte FIRSTTURN = Byte.MAX_VALUE;

    private static final byte SEAL = 0;
    private static final byte BASICBLOCK = 1;
    private static final byte BASICATTACK = 2;
    private static final byte DEBUFFATTACK = 3;

    private static final int DEBUFF_COUNTER_THESHOLD = 3;
    private static final int MAX_DEBUFF = 3;

    private boolean firstMove = true;

    public ReimuOne() {
        this.moves.put(FIRSTTURN, new ReimuMoveInfo(FIRSTTURN, AbstractMonster.Intent.UNKNOWN, -1, 0, false, ReimuAnimation.Spellcall));
        this.moves.put(SEAL, new ReimuMoveInfo(SEAL, Intent.STRONG_DEBUFF, -1, 0, false, ReimuAnimation.Spellcall));
        this.moves.put(BASICATTACK, new ReimuMoveInfo(BASICATTACK, Intent.ATTACK, calcAscensionNumber(28), 0, false, ReimuAnimation.Closeattack, calcAscensionNumber(1.3F)));
        this.moves.put(DEBUFFATTACK, new ReimuMoveInfo(DEBUFFATTACK, Intent.ATTACK_DEBUFF, calcAscensionNumber(23), 0, false, ReimuAnimation.Closeattack, calcAscensionNumber(1.3F)));
        this.moves.put(BASICBLOCK, new ReimuMoveInfo(BASICBLOCK, Intent.DEFEND_DEBUFF, -1, calcAscensionNumber(30), false, ReimuAnimation.Guard, calcAscensionNumber(2F)));
    }

    @Override
    public void takeTurn(Reimu reimu, ReimuMoveInfo rmi, DamageInfo info) {
        switch (reimu.nextMove) {
            case FIRSTTURN: {
                AbstractDungeon.actionManager.addToBottom(new TalkAction(reimu, Reimu.DIALOG[0]));
                break;
            }
            case BASICATTACK: {
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(new Dazed(), rmi.magicNumber));
                break;
            }
            case DEBUFFATTACK: {
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(new Dazed(), rmi.magicNumber));
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
                reimu.turnsSinceBomb = -1;
                reimu.rui.useBomb();
                break;
            }
        }
    }

    @Override
    public void getMove(Reimu reimu, final int num) {
        if (this.firstMove) {
            reimu.setMove(Reimu.MOVES[0], FIRSTTURN, Intent.UNKNOWN);
            this.firstMove = false;
        } else {
            if (reimu.turnsSinceBomb >= DEBUFF_COUNTER_THESHOLD && canMegaDebuff() && reimu.rui.bombs > 0) { //use this every few turns until player has max stacks of the debuff
                reimu.setMoveShortcut(SEAL, 0);
            } else if (reimu.lastMove(SEAL) && reimu.lastMoveBefore(BASICBLOCK)) { //can't not attack for more than 2 turns
                if (num % 2 == 0) {
                    reimu.setMoveShortcut(DEBUFFATTACK);
                } else {
                    reimu.setMoveShortcut(BASICATTACK);
                }
            } else if (num < 34) {
                if (!reimu.lastMove(BASICATTACK)) {
                    reimu.setMoveShortcut(BASICATTACK);
                } else {
                    if (num % 2 == 0) {
                        reimu.setMoveShortcut(BASICBLOCK);
                    } else {
                        reimu.setMoveShortcut(DEBUFFATTACK);
                    }
                }
            } else if (num < 67) {
                if (!reimu.lastMove(DEBUFFATTACK)) {
                    reimu.setMoveShortcut(DEBUFFATTACK);
                } else {
                    if (num % 2 == 0) {
                        reimu.setMoveShortcut(BASICATTACK);
                    } else {
                        reimu.setMoveShortcut(BASICBLOCK);
                    }
                }
            } else if (num < 100) {
                if (!reimu.lastMove(BASICBLOCK)) {
                    reimu.setMoveShortcut(BASICBLOCK);
                } else {
                    if (num % 2 == 0) {
                        reimu.setMoveShortcut(DEBUFFATTACK);
                    } else {
                        reimu.setMoveShortcut(BASICATTACK);
                    }
                }
            }
        }
    }

    private boolean canMegaDebuff() {
        if (AbstractDungeon.player.hasPower(SealedPower.POWER_ID)) {
            if (AbstractDungeon.player.getPower(SealedPower.POWER_ID).amount >= MAX_DEBUFF) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void die() {
        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(AbstractDungeon.player, AbstractDungeon.player, SealedPower.POWER_ID));
    }
}
