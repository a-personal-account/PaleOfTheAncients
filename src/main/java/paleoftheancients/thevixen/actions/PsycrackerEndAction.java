package paleoftheancients.thevixen.actions;

import paleoftheancients.thevixen.vfx.PsycrackerOrb;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;
import java.util.Map;

public class PsycrackerEndAction extends AbstractGameAction {

    private DamageInfo info;

    private ArrayList<PsycrackerOrb> orbs;
    private ArrayList<AbstractCreature> targets;
    private Map<AbstractCreature, Integer> dmg;

    public PsycrackerEndAction(DamageInfo info, ArrayList<AbstractCreature> targets, ArrayList<PsycrackerOrb> orbs, Map<AbstractCreature, Integer> dmg) {
        this.info = info;

        this.orbs = orbs;
        this.targets = targets;
        this.dmg = dmg;
    }


    @Override
    public void update() {
        for(int i = 0; i < orbs.size(); i++) {
            if(orbs.get(i).isDone) {
                if(orbs.size() > 1) {
                    AbstractDungeon.actionManager.addToTop(new PsycrackerEndAction(info, targets, orbs, dmg));
                }

                AbstractDungeon.actionManager.addToTop(
                        new DamageAction(targets.get(i), new DamageInfo(this.info.owner, dmg.get(targets.get(i))), AttackEffect.NONE));
                targets.remove(i);
                orbs.remove(i);

                this.isDone = true;
                break;
            }
        }
    }
}
