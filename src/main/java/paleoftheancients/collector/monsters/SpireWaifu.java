package paleoftheancients.collector.monsters;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.HideHealthBarAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.utility.TextAboveCreatureAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.city.BronzeAutomaton;
import com.megacrit.cardcrawl.monsters.city.TheCollector;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.vfx.CollectorCurseEffect;
import com.megacrit.cardcrawl.vfx.combat.InflameEffect;
import com.megacrit.cardcrawl.vfx.combat.LaserBeamEffect;
import paleoftheancients.PaleMod;
import paleoftheancients.collector.vfx.BloodyChair;

import java.util.HashMap;
import java.util.Map;

public class SpireWaifu extends TheCollector {
    public static final String ID = PaleMod.makeID("SpireWaifu");

    private int hyperbeamcounter;

    public SpireWaifu() {
        super();
        this.id = ID;
        this.setHp(this.maxHealth * 2);
        this.hyperbeamcounter = 0;
    }

    @Override
    public void usePreBattleAction() {
        AbstractDungeon.effectList.add(new BloodyChair());
    }

    @Override
    public void takeTurn() {
        int key;
        switch(this.nextMove) {
            case 1: {
                HashMap<Integer, AbstractMonster> es = (HashMap) ReflectionHacks.getPrivate(this, TheCollector.class, "enemySlots");
                float spx = (float) ReflectionHacks.getPrivate(this, TheCollector.class, "spawnX");
                for (key = 1; key < 3; ++key) {
                    MiniChamp mc = new MiniChamp(spx + -185.0F * (float) key, MathUtils.random(-5.0F, 25.0F));
                    AbstractDungeon.actionManager.addToBottom(new SFXAction("MONSTER_COLLECTOR_SUMMON"));
                    AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(mc, true));
                    es.put(key, mc);
                }

                ReflectionHacks.setPrivate(this, TheCollector.class, "initialSpawn", false);
                break;
            }
            case 2:
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, (DamageInfo)this.damage.get(0), AbstractGameAction.AttackEffect.FIRE));
                break;
            case 3: {
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this, 13 + AbstractDungeon.ascensionLevel));

                int strAmt = 3 + AbstractDungeon.ascensionLevel / 6;
                for (final AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
                    if (!m.isDead && !m.isDying && !m.isEscaping) {
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, this, new StrengthPower(m, strAmt), strAmt));
                    }
                }
                break;
            }
            case 4: {
                int megaDebuffAmt = (int)ReflectionHacks.getPrivate(this, TheCollector.class, "megaDebuffAmt");
                AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[0]));
                AbstractDungeon.actionManager.addToBottom(new SFXAction("MONSTER_COLLECTOR_DEBUFF"));
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new CollectorCurseEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY), 2.0F));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new WeakPower(AbstractDungeon.player, megaDebuffAmt, true), megaDebuffAmt));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new VulnerablePower(AbstractDungeon.player, megaDebuffAmt, true), megaDebuffAmt));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new FrailPower(AbstractDungeon.player, megaDebuffAmt, true), megaDebuffAmt));
                ReflectionHacks.setPrivate(this, TheCollector.class, "ultUsed", true);
                break;
            }
            case 5: {
                HashMap<Integer, AbstractMonster> es = (HashMap) ReflectionHacks.getPrivate(this, TheCollector.class, "enemySlots");
                float spx = (float) ReflectionHacks.getPrivate(this, TheCollector.class, "spawnX");

                for (final Map.Entry<Integer, AbstractMonster> m : ((HashMap<Integer, AbstractMonster>) ReflectionHacks.getPrivate(this, TheCollector.class, "enemySlots")).entrySet()) {
                    if (m.getValue().isDying) {
                        key = m.getKey();
                        MiniChamp mc = new MiniChamp(spx + -185.0F * (float) key, MathUtils.random(-5.0F, 25.0F));
                        es.put(key, mc);
                        AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(mc, true));
                    }
                }
                break;
            }

            case 6:
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new LaserBeamEffect(this.hb.cX, this.hb.cY + 60.0F * Settings.scale), 1.5F));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, new DamageInfo(this, this.getIntentDmg(), DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.NONE));
                hyperbeamcounter = 5;
                break;

            case 7:
                AbstractDungeon.actionManager.addToBottom(new TextAboveCreatureAction(this, TextAboveCreatureAction.TextType.STUNNED));
                break;
        }


        ReflectionHacks.setPrivate(this, TheCollector.class, "turnsTaken",
                (int)ReflectionHacks.getPrivate(this, TheCollector.class, "turnsTaken") + 1);

        int bronzeorbs = 0;
        for(final AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if(m.id == BronzeOrbThing.ID && !m.isDeadOrEscaped()) {
                bronzeorbs++;
            }
        }
        if(bronzeorbs < 2 && !this.isDead) {
            AbstractMonster bronzeorb = new BronzeOrbThing(0, 0, MathUtils.random(0, 1));
            do {
                boolean overlapping = false;
                bronzeorb.drawX = Settings.WIDTH * 0.75F + MathUtils.random(-500F, -100F) * Settings.scale;
                bronzeorb.drawY = AbstractDungeon.floorY + (300 + MathUtils.random(-60F, 30F)) * Settings.scale;
                bronzeorb.hb.move(bronzeorb.drawX, bronzeorb.drawY + bronzeorb.hb.height / 2);

                for(final AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
                    if(!mo.isDeadOrEscaped() && mo.hb.intersects(bronzeorb.hb)) {
                        overlapping = true;
                        break;
                    }
                }

                if(!overlapping) {
                    break;
                }
            } while(true);
            AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(bronzeorb, true));
        }

        hyperbeamcounter--;

        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    public void getMove(int num) {
        if(this.currentHealth < this.maxHealth * 0.7F && this.hyperbeamcounter <= 0) {
            this.setMove((String)ReflectionHacks.getPrivateStatic(BronzeAutomaton.class, "BEAM_NAME"), (byte)6, Intent.ATTACK, 40 + AbstractDungeon.ascensionLevel, 0, false);
        } else if(this.lastMove((byte)6)) {
            this.setMove((byte)7, Intent.STUN);
        } else {
            super.getMove(num);
        }
    }

    @Override
    public void die() {
        for(final AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (!m.isDead && !m.isDying) {
                AbstractDungeon.actionManager.addToTop(new HideHealthBarAction(m));
                AbstractDungeon.actionManager.addToTop(new SuicideAction(m));
                AbstractDungeon.actionManager.addToTop(new VFXAction(m, new InflameEffect(m), 0.2F));
            }
        }
        this.die(true);
    }

    @Override
    public void render(SpriteBatch sb) {
        super.render(sb);
    }
}
