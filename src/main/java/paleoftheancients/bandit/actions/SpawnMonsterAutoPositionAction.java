package paleoftheancients.bandit.actions;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.daily.mods.Lethality;
import com.megacrit.cardcrawl.daily.mods.TimeDilation;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.MinionPower;
import com.megacrit.cardcrawl.powers.RitualPower;
import com.megacrit.cardcrawl.powers.SlowPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.PhilosopherStone;

import java.util.HashSet;
import java.util.Set;

public class SpawnMonsterAutoPositionAction extends AbstractGameAction {
    private static final float DURATION = 0.1F;
    private int signum;

    private AbstractMonster m;

    private boolean minion;

    private float intendedPosX;

    public SpawnMonsterAutoPositionAction(AbstractMonster m, boolean isMinion, float x) {
        this(m, isMinion, x, -1);
    }
    public SpawnMonsterAutoPositionAction(AbstractMonster m, boolean isMinion, float x, int signum) {
        this.actionType = AbstractGameAction.ActionType.SPECIAL;
        this.duration = DURATION;
        this.m = m;
        this.minion = isMinion;
        this.intendedPosX = x;
        m.addPower(new RitualPower(m, 1 + AbstractDungeon.ascensionLevel / 9, false));
        if (AbstractDungeon.player.hasRelic(PhilosopherStone.ID)) {
            m.addPower(new StrengthPower(m, 1));
            AbstractDungeon.onModifyPower();
        }
        this.signum = Integer.signum(signum);
    }

    public void update() {
        if (this.duration == DURATION) {
            this.m.maxHealth *= 3;// :P
            this.m.currentHealth = this.m.maxHealth;
            this.m.init();
            this.m.applyPowers();
            int position = 0;
            boolean overlapping;
            Set<AbstractMonster> ineligible = new HashSet<>();
            this.m.hb.y += MathUtils.random(-30, 30) * Settings.scale;
            this.m.drawY = this.m.hb.y;
            do {
                overlapping = false;
                for (final AbstractMonster mo : (AbstractDungeon.getCurrRoom()).monsters.monsters) {
                    if (!mo.isDeadOrEscaped() && mo.hb.intersects(this.m.hb)) {
                        overlapping = true;
                        break;
                    }
                }
                if(overlapping) {
                    boolean changed = false;
                    for (final AbstractMonster mo : (AbstractDungeon.getCurrRoom()).monsters.monsters) {
                        if (!mo.isDeadOrEscaped() && !ineligible.contains(mo)) {
                            ineligible.add(mo);
                            this.m.drawX = mo.hb.cX + (this.m.hb.width * 1.5F + mo.hb.width) * signum / 2F;
                            this.m.hb.move(this.m.drawX, this.m.hb.cY);
                            changed = true;
                            break;
                        }
                    }
                    if(!changed) {
                        if(signum > 0 && this.m.hb.x + this.m.hb.width > Settings.WIDTH) {
                            signum *= -1;
                            ineligible.clear();
                        } else {
                            this.m.drawX += this.m.hb.width * signum;
                            break;
                        }
                    }
                }
            } while(overlapping);
            this.intendedPosX = this.m.drawX;
            for (AbstractMonster mo : (AbstractDungeon.getCurrRoom()).monsters.monsters) {
                if (!mo.isDeadOrEscaped() && this.intendedPosX > mo.drawX)
                    position++;
            }
            (AbstractDungeon.getCurrRoom()).monsters.addMonster(position, this.m);
            this.m.showHealthBar();
            if (ModHelper.isModEnabled(Lethality.ID))
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.m, this.m, new StrengthPower(this.m, 3), 3));
            if (ModHelper.isModEnabled(TimeDilation.ID))
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.m, this.m, new SlowPower(this.m, 0)));
            if (this.minion)
                AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(this.m, this.m, new MinionPower(this.m)));
        }
        tickDuration();
    }
}