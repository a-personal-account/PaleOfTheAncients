package paleoftheancients.reimu.monsters;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.VoidCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.vfx.combat.GoldenSlashEffect;
import com.megacrit.cardcrawl.vfx.combat.WeightyImpactEffect;
import paleoftheancients.PaleMod;
import paleoftheancients.reimu.actions.DamagingAction;
import paleoftheancients.reimu.actions.PersuasionNeedleAction;
import paleoftheancients.reimu.vfx.EvilSealingCircleVFX;
import paleoftheancients.reimu.vfx.ExterminationVFX;
import paleoftheancients.reimu.vfx.HakureiBarrierVFX;
import paleoftheancients.reimu.vfx.SpellCardDeclarationVFX;

public class ReimuThree extends ReimuPhase {
    private static final byte EVILSEALINGCIRCLE = 0;
    private static final byte HAKUREIDANMAKUBARRIER = 1;
    private static final byte PERSUASIONNEEDLE = 2;
    private static final byte EXTERMINATION = 3;
    private static final byte COMBO = 4;
    private static final byte THWACK = 5;

    public ReimuThree() {
        this.moves.put(EVILSEALINGCIRCLE, new ReimuMoveInfo(EVILSEALINGCIRCLE, AbstractMonster.Intent.ATTACK_BUFF, calcAscensionNumber(14), 3, true, Reimu.ReimuAnimation.None, calcAscensionNumber(1.7F)));
        this.moves.put(HAKUREIDANMAKUBARRIER, new ReimuMoveInfo(HAKUREIDANMAKUBARRIER, AbstractMonster.Intent.ATTACK_DEFEND, calcAscensionNumber(3), calcAscensionNumber(13), true, Reimu.ReimuAnimation.None, calcAscensionNumber(50F)));
        this.moves.put(COMBO, new ReimuMoveInfo(COMBO, AbstractMonster.Intent.ATTACK_DEBUFF, calcAscensionNumber(12), 2, true, Reimu.ReimuAnimation.None, calcAscensionNumber(2F)));
        this.moves.put(THWACK, new ReimuMoveInfo(THWACK, AbstractMonster.Intent.ATTACK_DEFEND, calcAscensionNumber(20), 1, false, Reimu.ReimuAnimation.ForwardOccult));
        this.moves.put(EXTERMINATION, new ReimuMoveInfo(EXTERMINATION, AbstractMonster.Intent.ATTACK_DEBUFF, calcAscensionNumber(8), 3, true, Reimu.ReimuAnimation.MagicUp, calcAscensionNumber(2.4F)));
        this.moves.put(PERSUASIONNEEDLE, new ReimuMoveInfo(PERSUASIONNEEDLE, AbstractMonster.Intent.ATTACK, calcAscensionNumber(5), 6, true, Reimu.ReimuAnimation.MagicForward));
    }

    @Override
    public void takeTurn(Reimu reimu, ReimuMoveInfo rmi, DamageInfo info) {
        switch (reimu.nextMove) {
            case EVILSEALINGCIRCLE:
                DeclareSpellcard(reimu, 3, 1);
                AbstractDungeon.actionManager.addToBottom(new DamagingAction(() -> new EvilSealingCircleVFX(AbstractDungeon.player, reimu, info, rmi.multiplier)));
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(new VoidCard(), calcAscensionNumber(rmi.magicNumber)));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, reimu, new StrengthPower(AbstractDungeon.player, -rmi.magicNumber), -rmi.magicNumber));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, reimu, new DexterityPower(AbstractDungeon.player, -rmi.magicNumber), -rmi.magicNumber));
                EndSpellcard(reimu);
                break;
            case HAKUREIDANMAKUBARRIER:
                AbstractDungeon.actionManager.addToBottom(new SFXAction(PaleMod.makeID("touhou_spellcard")));
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new SpellCardDeclarationVFX(reimu, -1)));
                AbstractDungeon.actionManager.addToBottom(new DamagingAction(() -> new HakureiBarrierVFX(AbstractDungeon.player, reimu, info, rmi.multiplier)));
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(reimu, reimu, rmi.magicNumber));
                break;

            case PERSUASIONNEEDLE:
                AbstractDungeon.actionManager.addToBottom(new PersuasionNeedleAction(AbstractDungeon.player, reimu, info, rmi.multiplier));
                break;
            case EXTERMINATION:
                AbstractDungeon.actionManager.addToBottom(new DamagingAction(() -> new ExterminationVFX(AbstractDungeon.player, reimu, info, rmi.multiplier)));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, reimu, new VulnerablePower(AbstractDungeon.player, rmi.magicNumber, true), rmi.magicNumber));
                break;
            case THWACK:
                reimu.runAnim(Reimu.ReimuAnimation.ForwardOccult);
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new WeightyImpactEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY), 0.8F));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.NONE));
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(reimu, reimu, info.output));
                break;
            case COMBO:
                reimu.runAnim(Reimu.ReimuAnimation.Kick, Reimu.ReimuAnimation.CloseAttack, Reimu.ReimuAnimation.Idle);
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new GoldenSlashEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, true)));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.NONE));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, reimu, new FrailPower(AbstractDungeon.player, rmi.magicNumber, true), rmi.magicNumber));
                break;
        }
    }

    @Override
    protected void resetCycleTracker() {
        cycletracker.add(PERSUASIONNEEDLE);
        cycletracker.add(EXTERMINATION);
        cycletracker.add(THWACK);
        cycletracker.add(COMBO);
    }

    @Override
    public void setBombIntent(Reimu reimu) {
        reimu.setMoveShortcut(EVILSEALINGCIRCLE, (byte) (AbstractDungeon.ascensionLevel >= 9 ? 7 : 6));
    }
    @Override
    public void setDeathbombIntent(Reimu reimu) {
        reimu.setMoveShortcut(HAKUREIDANMAKUBARRIER, 8);
        DeclareSpellcard(reimu, 3, 0);
    }
}
