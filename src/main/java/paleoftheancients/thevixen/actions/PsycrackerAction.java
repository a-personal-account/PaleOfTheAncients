package paleoftheancients.thevixen.actions;

import paleoftheancients.thevixen.vfx.PsycrackerOrb;
import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PsycrackerAction extends AbstractGameAction {
    private static float ORBTIME = 1F;

    private DamageInfo info;
    private boolean frameOne;

    private ArrayList<PsycrackerOrb> orbs;
    private ArrayList<AbstractCreature> targets;
    private Map<AbstractCreature, Integer> dmg;

    private int index;
    private float time;
    boolean ready;

    public PsycrackerAction(DamageInfo info, int numTimes) {
        this.info = info;
        this.amount = numTimes;

        this.orbs = new ArrayList<>();
        this.targets = new ArrayList<>();
        this.dmg = new HashMap<>();

        this.time = 0F;
        this.index = 0;
        this.frameOne = true;
    }


    @Override
    public void update() {
        if(frameOne) {
            this.frameOne = false;
            if(info.owner instanceof AbstractPlayer) {
                MonsterGroup mg = new MonsterGroup(new AbstractMonster[]{});
                AbstractMonster mo;
                Map<AbstractMonster, Integer> curhp = new HashMap<>();
                for (int i = 0; i < AbstractDungeon.getMonsters().monsters.size(); i++) {
                    mo = AbstractDungeon.getMonsters().monsters.get(i);
                    if (!mo.halfDead && !mo.isDying && !mo.isEscaping) {
                        mg.add(mo);
                        curhp.put(mo, mo.currentBlock + mo.currentHealth);
                    }
                }

                for (int i = 0; i < this.amount && mg.monsters.size() > 0; i++) {
                    mo = mg.getRandomMonster((AbstractMonster) null, false, AbstractDungeon.cardRandomRng);

                    if (!dmg.containsKey(mo)) {
                        this.info.applyPowers(this.info.owner, mo);
                        dmg.put(mo, this.info.output);
                    }

                    targets.add(mo);

                    if (curhp.get(mo) <= dmg.get(mo)) {
                        mg.monsters.remove(mo);
                    } else {
                        curhp.put(mo, curhp.get(mo) - dmg.get(mo));
                    }
                }
            } else {
                this.info.applyPowers(this.info.owner, AbstractDungeon.player);
                dmg.put(AbstractDungeon.player, this.info.output);
                for(int i = 0; i < this.amount; i++) {
                    targets.add(AbstractDungeon.player);
                }
            }
        }

        if(index < targets.size()) {
            this.time -= Gdx.graphics.getDeltaTime();
            if (this.time <= 0F) {
                this.time = ORBTIME;
                this.info.owner.useHopAnimation();
                if (index < 2) {
                    PsycrackerOrb po = new PsycrackerOrb(this.info.owner, index++);
                    orbs.add(po);
                    AbstractDungeon.effectList.add(po);
                } else {
                    for (; index < targets.size(); index++) {
                        PsycrackerOrb po = new PsycrackerOrb(this.info.owner, index);
                        orbs.add(po);
                        AbstractDungeon.effectList.add(po);
                    }
                }
            }
        } else if(!ready) {
            ready = true;
            for (index = 0; index < orbs.size(); index++) {
                ready &= orbs.get(index).idle();
            }
            if(ready) {
                time = ORBTIME;
            }
        } else {
            this.time -= Gdx.graphics.getDeltaTime();
            if(time < 0F) {
                this.info.owner.useFastAttackAnimation();
                for (index = 0; index < orbs.size(); index++) {
                    orbs.get(index).shoot(targets.get(index));
                }
                AbstractDungeon.actionManager.addToTop(new PsycrackerEndAction(info, targets, orbs, dmg));
                this.isDone = true;
            }
        }
    }
}
