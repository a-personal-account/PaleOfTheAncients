package paleoftheancients.finarubossu.powers;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import paleoftheancients.PaleMod;
import paleoftheancients.finarubossu.vfx.PoofRelicEffect;

import java.util.ArrayList;

public class GazeThree extends AbstractGazePower {
    public static final String POWER_ID = PaleMod.makeID("GazeThree");
    private static final PowerStrings powerStrings;
    public static final String NAME;
    public static final String[] DESCRIPTIONS;

    private ArrayList<AbstractRelic> relics;
    private ArrayList<Integer> slots;

    private int strength;

    public GazeThree(AbstractCreature owner, ArrayList<AbstractRelic> relicsreference, ArrayList<Integer> slotsreference) {
        super(owner, Color.RED, 3);
        this.ID = POWER_ID;
        this.name = NAME;
        this.isTurnBased = false;

        this.relics = relicsreference;
        this.slots = slotsreference;

        this.strength = AbstractDungeon.ascensionLevel >= 19 ? 2 : 1;

        this.updateDescription();
    }


    @Override
    protected void trigger(AbstractCard card) {
        for(int i = 0; i < AbstractDungeon.player.relics.size(); i++) {
            switch(AbstractDungeon.player.relics.get(i).tier) {
                case BOSS:
                case STARTER:
                    break;

                default:
                    AbstractRelic relic = AbstractDungeon.player.relics.get(i);
                    this.relics.add(relic);
                    this.slots.add(i);
                    AbstractDungeon.player.loseRelic(relic.relicId);
                    AbstractDungeon.effectList.add(new PoofRelicEffect(relic));

                    if(AbstractDungeon.ascensionLevel >= 4) {
                        for(final AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
                            if(!mo.isDeadOrEscaped()) {
                                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(mo, this.owner, new StrengthPower(mo, this.strength), this.strength));
                            }
                        }
                    }
                    break;
            }
        }
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
        if(AbstractDungeon.ascensionLevel >= 4) {
            this.description += DESCRIPTIONS[1] + this.strength + DESCRIPTIONS[2];
        }
        this.description += DESCRIPTIONS[3] + this.demonform + DESCRIPTIONS[4];
    }

    static {
        powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        NAME = powerStrings.NAME;
        DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    }
}
