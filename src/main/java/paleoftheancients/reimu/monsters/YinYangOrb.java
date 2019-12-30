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
import paleoftheancients.reimu.actions.YinYangMoveAction;
import paleoftheancients.reimu.powers.*;
import paleoftheancients.reimu.vfx.BasicNeedleVFX;
import paleoftheancients.reimu.vfx.HakureiAmuletVFX;

public class YinYangOrb extends CustomMonster {
    public static final String ID = PaleMod.makeID("YinYangOrb");
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String[] DIALOG = monsterStrings.DIALOG;
    private static final int HP = 50;
    private static final int HP_VARIANCE = 10;
    private static final byte MOVE = 1;
    private static final byte ATTACK = 2;
    public int delay;
    public int position;
    private float movement = Reimu.orbOffset;
    private Reimu master;

    public YinYangOrb(float x, float y, int type, int position, int delay, Reimu master) {
        super(NAME, ID, HP, 0.0F, 0.0F, 140.0F, 120.0F, null, x, y);

        this.loadAnimation(PaleMod.assetPath("images/reimu/monsters/YinYangOrb/YinYangOrb.atlas"), PaleMod.assetPath("images/reimu/monsters/YinYangOrb/YinYangOrb.json"), 0.6F);
        this.state.setAnimation(0, "Reverse", true);

        this.type = EnemyType.NORMAL;

        this.setHp(HP + HP_VARIANCE * type);
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
        if(master.rui.extralives < 2) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, p));
        }
    }

    private void move() {
        master.orbs[delay - 1][position - 1].remove(this);
        AbstractDungeon.actionManager.addToBottom(new VFXAction(new YinYangMoveAction(this, this.drawX, this.drawX - movement)));
        delay--;
        if (delay > 0) {
            AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(this, this, MonsterPosition.POWER_ID, 1));
            master.orbs[delay - 1][position - 1].add(this);
        }
    }

    @Override
    public void die(boolean triggerRelics) {
        super.die(false);
        if (delay > 0) {
            master.orbs[delay - 1][position - 1].remove(this);
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
                    case 0:
                        if(Position.playerPosition() == this.position) {
                            AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_HEAVY, true));
                        }
                        break;
                    case 1: {
                        ShotTypeAmuletPower stp = (ShotTypeAmuletPower)getPower(ShotTypeAmuletPower.POWER_ID);
                        if(stp == null || stp.homing) {
                            AbstractDungeon.actionManager.addToBottom(new VFXAction(new HakureiAmuletVFX(AbstractDungeon.player, this, this.damage.get(0), 1)));
                        }
                        break;
                    }
                    case 2:
                        if(Position.playerPosition() == this.position) {
                            AbstractDungeon.actionManager.addToBottom(new VFXAction(new BasicNeedleVFX(AbstractDungeon.player, this, this.damage.get(0), 1)));
                        }
                        break;
                }
                move();
                if(delay < 1) {
                    AbstractDungeon.actionManager.addToBottom(new SuicideAction(this));
                }
                break;
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(int num) {
        boolean set = false;
        switch(master.rui.extralives) {
            case 2:
            case 1:
                if (delay <= 1) {
                    set = true;
                    this.setMove(ATTACK, Intent.ATTACK, this.damage.get(0).base);
                }
                break;

            case 0:
                set = true;
                this.setMove(ATTACK, Intent.ATTACK, this.damage.get(0).base);
                break;
        }
        if(!set) {
            this.setMove(MOVE, Intent.NONE); //Setting intent to none makes their turn go by much faster
        }
    }
}
