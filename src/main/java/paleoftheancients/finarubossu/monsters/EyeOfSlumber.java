package paleoftheancients.finarubossu.monsters;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.unique.VampireDamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.VoidCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.vfx.combat.*;
import paleoftheancients.PaleMod;
import paleoftheancients.bard.vfx.LifeDrainEffect;
import paleoftheancients.thevixen.actions.ShuffleBossBurnsAction;

public class EyeOfSlumber extends Eye {
    public static final String ID = PaleMod.makeID("EyeOfSlumber");
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;

    private static final byte WEAKEN = 0;
    private static final byte VOID = 1;
    private static final byte FRAIL = 2;
    private static final byte VULN = 3;
    private static final byte BURN = 4;
    private static final byte DRAIN = 5;

    private int intensity;
    private int turncounter;

    public EyeOfSlumber(Texture t, float drawX, float drawY) {
        super(NAME, ID, 200, 0, 30, null, drawX, drawY, t);

        this.moves.put(WEAKEN, new EnemyMoveInfo(WEAKEN, Intent.ATTACK_DEBUFF, 5, 2, true));
        this.moves.put(VOID, new EnemyMoveInfo(VOID, Intent.DEBUFF, -1, -1, false));
        this.moves.put(VULN, new EnemyMoveInfo(VULN, Intent.ATTACK_DEBUFF, 8, -1, false));
        this.moves.put(FRAIL, new EnemyMoveInfo(FRAIL, Intent.ATTACK_DEBUFF, 12, -1, false));
        this.moves.put(BURN, new EnemyMoveInfo(BURN, Intent.STRONG_DEBUFF, -1, -1, false));
        this.moves.put(DRAIN, new EnemyMoveInfo(DRAIN, Intent.ATTACK_BUFF, 10, 2, true));

        this.intensity = AbstractDungeon.ascensionLevel >= 9 ? 3 : 2;
        this.turncounter = AbstractDungeon.monsterRng.random(this.moves.size() - 1);
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

            case VULN:
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new BloodShotEffect(this.hb.cX, this.hb.cY, AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, 1)));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.NONE));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new VulnerablePower(AbstractDungeon.player, intensity, true), intensity));
                break;
            case WEAKEN:
                for(int i = 0; i < this.moves.get(this.nextMove).multiplier; i++) {
                    AbstractDungeon.actionManager.addToBottom(new VFXAction(new ScrapeEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY)));
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.NONE, true));
                }
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new WeakPower(AbstractDungeon.player, intensity, true), intensity));
                break;
            case FRAIL:
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new WeightyImpactEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY), 0.8F));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.NONE, true));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new FrailPower(AbstractDungeon.player, intensity - 1, true), intensity - 1));
                break;
            case BURN:
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new FireballEffect(this.hb.cX, this.hb.cY, AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY), 0.5F));
                AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
                    @Override
                    public void update() {
                        this.isDone = true;
                        CardCrawlGame.sound.play("ATTACK_FIRE");
                    }
                });
                AbstractDungeon.actionManager.addToBottom(new ShuffleBossBurnsAction(intensity));
                break;
            case VOID:
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new HeartMegaDebuffEffect()));
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new ShockWaveEffect(this.hb.cX, this.hb.cY, Color.PURPLE, ShockWaveEffect.ShockWaveType.ADDITIVE)));
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(new VoidCard(), intensity - 1));
                break;
            case DRAIN:
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new LifeDrainEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, this.hb.cX, this.hb.cY)));
                for(int i = 0; i < this.moves.get(this.nextMove).multiplier; i++) {
                    AbstractDungeon.actionManager.addToBottom(new VampireDamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.NONE));
                }
                break;
        }

        turncounter++;
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(int i) {
        this.setMoveShortcut((byte)(i % this.moves.size()));
    }
    @Override
    public void rollMove() {
        this.getMove(turncounter);
    }


    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
        NAME = monsterStrings.NAME;
        MOVES = monsterStrings.MOVES;
        DIALOG = monsterStrings.DIALOG;
    }
}
