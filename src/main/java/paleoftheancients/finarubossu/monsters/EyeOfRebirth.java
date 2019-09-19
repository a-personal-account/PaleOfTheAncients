package paleoftheancients.finarubossu.monsters;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ChangeStateAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.actions.unique.RemoveDebuffsAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.powers.AbstractPower;
import paleoftheancients.PaleMod;
import paleoftheancients.thevixen.helpers.RandomPoint;
import paleoftheancients.thevixen.vfx.RandomAnimatedSlashEffect;

public class EyeOfRebirth extends Eye {
    public static final String ID = PaleMod.makeID("EyeOfRebirth");
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;

    private static final byte HEALBECAUSEOFCOURSE = 0;
    private static final byte REVIVE = 1;
    private static final byte REMOVEDEBUFFS = 2;
    private static final byte STRUGGLE1 = 3;
    private static final byte STRUGGLE2 = 4;
    private static final byte STRUGGLE3 = 5;

    public EyeOfRebirth(Texture t, float drawX, float drawY) {
        super(NAME, ID, 150, 0, 30, null, drawX, drawY, t);

        this.moves.put(HEALBECAUSEOFCOURSE, new EnemyMoveInfo(HEALBECAUSEOFCOURSE, Intent.BUFF, -1, -1, false));
        this.moves.put(REVIVE, new EnemyMoveInfo(REVIVE, Intent.UNKNOWN, -1, -1, false));
        this.moves.put(REMOVEDEBUFFS, new EnemyMoveInfo(REMOVEDEBUFFS, Intent.MAGIC, -1, -1, false));

        this.moves.put(STRUGGLE1, new EnemyMoveInfo(STRUGGLE1, Intent.ATTACK, 2, 3, true));
        this.moves.put(STRUGGLE2, new EnemyMoveInfo(STRUGGLE2, Intent.ATTACK, 4, 2, true));
        this.moves.put(STRUGGLE3, new EnemyMoveInfo(STRUGGLE3, Intent.ATTACK, 10, -1, false));
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

            case REVIVE:
                for(final AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
                    if(mo instanceof Eye && mo.halfDead) {
                        AbstractDungeon.actionManager.addToBottom(new HealAction(mo, this, mo.maxHealth / (AbstractDungeon.ascensionLevel >= 4 ? 3 : 4)));
                        mo.setMove(REVIVAL_CONST, Intent.NONE);
                        mo.createIntent();
                        break;
                    }
                }
                break;
            case HEALBECAUSEOFCOURSE:
                for(final AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
                    if(mo instanceof Eye && !mo.isDeadOrEscaped()) {
                        AbstractDungeon.actionManager.addToBottom(new HealAction(mo, this, mo.maxHealth / 10));
                    }
                }
                break;
            case REMOVEDEBUFFS:
                for(final AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
                    if(mo instanceof Eye && !mo.isDeadOrEscaped()) {
                        AbstractDungeon.actionManager.addToBottom(new RemoveDebuffsAction(mo));
                    }
                }
                break;

            case STRUGGLE1:
            case STRUGGLE2:
            case STRUGGLE3: {
                int multiplier = this.moves.get(this.nextMove).isMultiDamage ? this.moves.get(this.nextMove).multiplier : 1;
                for(int i = 0; i < multiplier; i++) {
                    AbstractDungeon.actionManager.addToBottom(new VFXAction(new RandomAnimatedSlashEffect(RandomPoint.x(AbstractDungeon.player.hb), RandomPoint.y(AbstractDungeon.player.hb))));
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.NONE));
                }
            }

        }

        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(int i) {
        boolean revive = this.reviveNecessary();
        boolean heal = this.healNecessary();
        boolean cleanse = this.cleanseNecessary();

        if(revive) {
            this.setMoveShortcut(REVIVE);
        } else if(cleanse) {
            this.setMoveShortcut(REMOVEDEBUFFS);
        } else if(heal && i % 3 != 0) {
            this.setMoveShortcut(HEALBECAUSEOFCOURSE);
        } else {
            this.setMoveShortcut((byte)(STRUGGLE1 + i % 3));
        }
    }

    private boolean reviveNecessary() {
        if(!this.moveHistory.isEmpty() && this.moveHistory.get(this.moveHistory.size() - 1) == REVIVE) {
            return false;
        }
        for(final AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if(mo instanceof Eye && mo.halfDead) {
                return true;
            }
        }
        return false;
    }
    private boolean healNecessary() {
        for(final AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if(mo instanceof Eye && !(mo.isDeadOrEscaped()) && mo.currentHealth < mo.maxHealth * 4 / 5) {
                return true;
            }
        }
        return false;
    }
    private boolean cleanseNecessary() {
        int debuffcount = 0;
        for(final AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if(mo instanceof Eye && !(mo.isDeadOrEscaped())) {
                for(final AbstractPower pow : mo.powers) {
                    if(pow.type == AbstractPower.PowerType.DEBUFF) {
                        debuffcount++;
                    }
                }
            }
        }
        return debuffcount > 3;
    }

    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
        NAME = monsterStrings.NAME;
        MOVES = monsterStrings.MOVES;
        DIALOG = monsterStrings.DIALOG;
    }
}
