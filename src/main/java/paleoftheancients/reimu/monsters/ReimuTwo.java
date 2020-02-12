package paleoftheancients.reimu.monsters;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.unique.RemoveDebuffsAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
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
    private static final byte STAB = 3;
    private static final byte SKYCONQUERINGWINDGODKICK = 4;
    private static final byte BUFFORBS = 5;

    public ReimuTwo() {
        this.moves.put(FANTASYSEAL, new ReimuMoveInfo(FANTASYSEAL, AbstractMonster.Intent.ATTACK_DEBUFF, calcAscensionNumber(35), 1, false, Reimu.ReimuAnimation.None));
        this.moves.put(FSBLINK, new ReimuMoveInfo(FSBLINK, AbstractMonster.Intent.ATTACK_BUFF, calcAscensionNumber(5), 8, true, Reimu.ReimuAnimation.None));
        this.moves.put(AMULET, new ReimuMoveInfo(AMULET, AbstractMonster.Intent.ATTACK, calcAscensionNumber(11), 3, true, Reimu.ReimuAnimation.MagicUp));
        this.moves.put(STAB, new ReimuMoveInfo(STAB, AbstractMonster.Intent.ATTACK_DEBUFF, calcAscensionNumber(25), 1, false, Reimu.ReimuAnimation.DashAttack, calcAscensionNumber(1.8F)));
        this.moves.put(SKYCONQUERINGWINDGODKICK, new ReimuMoveInfo(SKYCONQUERINGWINDGODKICK, AbstractMonster.Intent.ATTACK, calcAscensionNumber(35), 1, false, Reimu.ReimuAnimation.Flipkick));
        this.moves.put(BUFFORBS, new ReimuMoveInfo(BUFFORBS, AbstractMonster.Intent.DEFEND_BUFF, -1, -1, false, Reimu.ReimuAnimation.Spellcall, calcAscensionNumber(50)));
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
                AbstractDungeon.actionManager.addToBottom(new DamagingAction(() -> new HakureiAmuletVFX(AbstractDungeon.player, reimu, info, rmi.multiplier, 8)));
                break;
            case STAB:
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, reimu, new VulnerablePower(AbstractDungeon.player, rmi.magicNumber, true), rmi.magicNumber));
                break;
            case SKYCONQUERINGWINDGODKICK:
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                break;
            case BUFFORBS:
                for(final YinYangOrb[] orbarray : reimu.orbs) {
                    for(final YinYangOrb orb : orbarray) {
                        if(orb != null) {
                            AbstractDungeon.actionManager.addToBottom(new GainBlockAction(orb, reimu, rmi.magicNumber / 2));
                            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(orb, reimu, new StrengthPower(orb, rmi.magicNumber / 5), rmi.magicNumber / 5, true));
                            AbstractDungeon.actionManager.addToBottom(new RemoveDebuffsAction(orb));
                        }
                    }
                }
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(reimu, reimu, rmi.magicNumber));
                break;
        }
    }

    @Override
    protected void resetCycleTracker() {
        cycletracker.add(AMULET);
        cycletracker.add(STAB);
        cycletracker.add(SKYCONQUERINGWINDGODKICK);
        cycletracker.add(BUFFORBS);
    }

    @Override
    public void setBombIntent(Reimu reimu) {
        reimu.setMoveShortcut(FANTASYSEAL, (byte) (AbstractDungeon.ascensionLevel >= 9 ? 4 : 3));
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
