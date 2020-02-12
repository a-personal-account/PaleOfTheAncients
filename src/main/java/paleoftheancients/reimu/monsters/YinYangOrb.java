package paleoftheancients.reimu.monsters;

import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import paleoftheancients.PaleMod;
import paleoftheancients.reimu.actions.DamagingAction;
import paleoftheancients.reimu.actions.PersuasionNeedleAction;
import paleoftheancients.reimu.actions.YinYangMoveAction;
import paleoftheancients.reimu.powers.*;
import paleoftheancients.reimu.vfx.HakureiAmuletVFX;

public class YinYangOrb extends CustomMonster {
    public static final String ID = PaleMod.makeID("YinYangOrb");
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String[] DIALOG = monsterStrings.DIALOG;
    private static final int HP = 30;
    private static final int HP_VARIANCE = 10;
    public static final byte MOVE = 1;
    private static final byte ATTACK = 2;
    public int delay;
    public int position;
    private float movement = Reimu.orbOffset;
    public Reimu master;

    public YinYangOrb(float x, float y, int type, int position, int delay, Reimu master) {
        super(NAME, ID, HP + HP_VARIANCE * type - (2 - master.rui.extralives) * 5, 0.0F, 0.0F, 140.0F, 120.0F, null, x, y);

        this.loadAnimation(PaleMod.assetPath("images/reimu/monsters/YinYangOrb/YinYangOrb.atlas"), PaleMod.assetPath("images/reimu/monsters/YinYangOrb/YinYangOrb.json"), 0.86F);
        forward();

        this.type = EnemyType.NORMAL;

        this.damage.add(new DamageInfo(this, HP / 2 + HP_VARIANCE * (4 - type)));

        this.delay = delay;
        this.position = position;
        this.master = master;
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new MonsterPosition(this, delay, position)));
        AbstractPower p = null;
        switch(master.rui.extralives) {
            case 0:
                p = new ShotTypeNeedlePower(this);
                break;
            case 1:
                p = new ShotTypeAmuletPower(this);
                break;
            case 2:
                p = new ShotTypeBasePower(this);
                break;
        }

        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, p, -1, true));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new BlockUponDeathPower(this, BlockUponDeathPower.BLOCK), 0, true));
    }

    private void move() {
        master.orbs[delay - 1][position - 1] = null;
        AbstractDungeon.actionManager.addToBottom(new VFXAction(new YinYangMoveAction(this, this.drawX, this.drawX - movement)));
        delay--;
        if (delay > 0) {
            AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(this, this, MonsterPosition.POWER_ID, 1));
            master.orbs[delay - 1][position - 1] = this;
        }
    }

    @Override
    public void die(boolean triggerRelics) {
        super.die(false);
        if (delay > 0) {
            master.orbs[delay - 1][position - 1] = null;
        }
    }

    @Override
    public void takeTurn() {
        switch(this.nextMove) {
            case MOVE:
                move();
                break;
            case ATTACK:
                switch(master.rui.extralives) {
                    case 2:
                        if(Position.playerPosition() == this.position) {
                            AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_HEAVY, true));
                        }
                        break;
                    case 1: {
                        ShotTypeAmuletPower stp = (ShotTypeAmuletPower)getPower(ShotTypeAmuletPower.POWER_ID);
                        if(stp == null || stp.homing) {
                            AbstractDungeon.actionManager.addToBottom(new DamagingAction(() -> new HakureiAmuletVFX(AbstractDungeon.player, this, this.damage.get(0), 1, 3)));
                        }
                        break;
                    }
                    case 0:
                        if(Position.playerPosition() == this.position) {
                            AbstractDungeon.actionManager.addToBottom(new PersuasionNeedleAction(AbstractDungeon.player, this, this.damage.get(0), 1));
                        }
                        break;
                }
                move();
                break;
        }
        if(delay < 1) {
            AbstractDungeon.actionManager.addToBottom(new SuicideAction(this));
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(int num) {
        if(3 - delay >= master.rui.extralives) {
            this.setMove(ATTACK, Intent.ATTACK, this.damage.get(0).base);
        } else {
            this.setMove(MOVE, Intent.NONE); //Setting intent to none makes their turn go by much faster
        }
    }

    @Override
    public void damage(DamageInfo info) {
        super.damage(info);
        if(this.currentHealth <= 0) {
            AbstractPower p = this.getPower(BlockUponDeathPower.POWER_ID);
            if(p != null) {
                ((BlockUponDeathPower) p).onTrigger();
            }
        }
    }

    public void forward() {
        this.state.setAnimation(0, "Reverse", true);
    }
    public void reverse() {
        this.state.setAnimation(0, "Idle", true);
    }
}
