package paleoftheancients.finarubossu.monsters;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ChangeStateAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.vfx.CollectorStakeEffect;
import com.megacrit.cardcrawl.vfx.combat.HemokinesisEffect;
import com.megacrit.cardcrawl.vfx.combat.SmallLaserEffect;
import com.megacrit.cardcrawl.vfx.combat.SweepingBeamEffect;
import paleoftheancients.PaleMod;

import java.util.HashMap;
import java.util.Map;

public class EyeOfDisremembrance extends Eye {
    public static final String ID = PaleMod.makeID("EyeOfDisremembrance");
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;

    private static final byte LASER = 0;
    private static final byte SENTRYBEAM = 1;
    private static final byte HEMOKINESIS = 2;
    private static final byte NAILS = 3;

    public EyeOfDisremembrance(Texture t, float drawX, float drawY) {
        super(NAME, ID, 300, 0, 30, null, drawX, drawY, t);

        this.moves.put(LASER, new EnemyMoveInfo(LASER, Intent.ATTACK, 15, -1, false));
        this.moves.put(SENTRYBEAM, new EnemyMoveInfo(SENTRYBEAM, Intent.ATTACK_DEBUFF, 9, -1, false));
        this.moves.put(HEMOKINESIS, new EnemyMoveInfo(HEMOKINESIS, Intent.ATTACK, 22, -1, false));
        this.moves.put(NAILS, new EnemyMoveInfo(NAILS, Intent.ATTACK, 4, 3, true));
    }

    @Override
    public void takeEyeTurn() {
        DamageInfo info = null;
        if(this.moves.containsKey(this.nextMove)) {
            info = new DamageInfo(this, this.moves.get(this.nextMove).baseDamage, DamageInfo.DamageType.NORMAL);
            if (info.base > -1) {
                info.applyPowers(this, AbstractDungeon.player);
            }
        }
        switch(this.nextMove) {
            case REVIVAL_CONST:
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, REVIVAL_STATE));
                break;

            case LASER:
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new SweepingBeamEffect(this.hb.cX, this.hb.cY, true), 0.5F));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.NONE));
                break;
            case SENTRYBEAM:
                AbstractDungeon.actionManager.addToBottom(new SFXAction("ATTACK_MAGIC_BEAM_SHORT", 0.5F));
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new SmallLaserEffect(this.hb.cX, this.hb.cY, AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY), 0.1F));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.NONE));
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(new Dazed(), AbstractDungeon.ascensionLevel >= 9 ? 2 : 1, true, false));
                break;
            case HEMOKINESIS:
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new HemokinesisEffect(this.hb.cX, this.hb.cY, AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY), 0.5F));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.NONE));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(this, new DamageInfo(this, this.maxHealth / 10, DamageInfo.DamageType.HP_LOSS), AbstractGameAction.AttackEffect.NONE));
                break;
            case NAILS: {
                int multiplier = this.moves.get(this.nextMove).multiplier;
                for (int i = 0; i < multiplier; i++) {
                    AbstractDungeon.actionManager.addToBottom(new VFXAction(new CollectorStakeEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY)));
                }
                for (int i = 0; i < multiplier; i++) {
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.NONE));
                }
                break;
            }
        }

        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(int num) {
        Map<Byte, EnemyMoveInfo> stuff = new HashMap<>();
        stuff.putAll(this.moves);
        for(int i = 1; i < 3 && i < this.moveHistory.size(); i++) {
            stuff.remove(this.moveHistory.get(this.moveHistory.size() - i));
        }
        this.setMoveShortcut((Byte)stuff.keySet().toArray()[AbstractDungeon.monsterRng.random(stuff.size() - 1)]);
    }
    @Override
    public void rollMove() {
        this.getMove(0);
    }


    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
        NAME = monsterStrings.NAME;
        MOVES = monsterStrings.MOVES;
        DIALOG = monsterStrings.DIALOG;
    }
}
