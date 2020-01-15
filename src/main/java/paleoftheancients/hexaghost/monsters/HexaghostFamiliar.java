package paleoftheancients.hexaghost.monsters;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.evacipated.cardcrawl.modthespire.lib.SpireOverride;
import com.evacipated.cardcrawl.modthespire.lib.SpireSuper;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.exordium.Hexaghost;
import com.megacrit.cardcrawl.monsters.exordium.HexaghostOrb;
import com.megacrit.cardcrawl.powers.CorpseExplosionPower;
import paleoftheancients.dungeons.PaleOfTheAncients;

import java.util.ArrayList;

public class HexaghostFamiliar extends Hexaghost {

    private HexaghostPrime owner;
    private int deactivateOnDeath;
    public float before;//for the render patch. I don't like using static variables.

    public HexaghostFamiliar(HexaghostPrime owner, float cX, float cY) {
        super();
        this.hb.height /= 2;
        this.hb.width /= 2;
        this.hb_w /= 2;
        this.hb_h /= 2;

        this.drawX = cX;
        this.drawY = cY;

        this.setHp(60);
        this.type = EnemyType.NORMAL;
        this.owner = owner;
        this.deactivateOnDeath = 2;
    }

    @Override
    public void usePreBattleAction() {}

    @SpireOverride
    protected void createOrbs() {
        Vector2[] points = HexaghostPrime.HexaghostPositions();
        ArrayList<HexaghostOrb> grargh = (ArrayList<HexaghostOrb>) ReflectionHacks.getPrivate(this, Hexaghost.class, "orbs");

        for(int i = 0; i < points.length; i++) {
            grargh.add(new HexaghostOrb(points[i].x / 2F, points[i].y / 2F + 256F / 2F, grargh.size()));
        }
    }

    @Override
    public void die() {
        this.die(true);
    }
    @Override
    public void die(boolean triggerRelics) {
        if(this.owner.currentHealth > 0) {
            if(AbstractDungeon.ascensionLevel >= 19) {
                this.maxHealth += 10;
            }
            if(AbstractDungeon.ascensionLevel >= 9) {
                this.maxHealth += 10;
            }
            if(AbstractDungeon.ascensionLevel >= 4) {
                this.maxHealth += 10;
            }
            AbstractDungeon.actionManager.addToTop(new HealAction(this, this, this.maxHealth));
            this.owner.damage(new DamageInfo(this, this.maxHealth, DamageInfo.DamageType.HP_LOSS));

            PaleOfTheAncients.deathTriggers(this);
            AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(this, this, CorpseExplosionPower.POWER_ID));

            if (this.intent != Intent.UNKNOWN) {
                int prevOrbActiveCount = (int) ReflectionHacks.getPrivate(this, Hexaghost.class, "orbActiveCount");
                ArrayList<HexaghostOrb> grargh = (ArrayList<HexaghostOrb>) ReflectionHacks.getPrivate(this, Hexaghost.class, "orbs");
                for (int i = grargh.size() - 1, deactivated = deactivateOnDeath; i >= 0 && deactivated > 0; i--) {
                    if (grargh.get(i).activated) {
                        deactivated--;
                        prevOrbActiveCount--;
                        grargh.get(i).deactivate();
                    }
                }
                ReflectionHacks.setPrivate(this, Hexaghost.class, "orbActiveCount", prevOrbActiveCount);
                this.getMove(0);
                this.createIntent();
            }
        } else {
            super.die(triggerRelics);
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        float scale = Settings.scale;
        Settings.scale /= 2F;
        super.render(sb);
        Settings.scale = scale;
    }

    @Override
    public void renderHealth(SpriteBatch sb) {
        Settings.scale *= 2F;
        super.renderHealth(sb);
        Settings.scale /= 2F;
    }
    @SpireOverride
    protected void renderIntentVfxBehind(SpriteBatch sb) {
        Settings.scale *= 2F;
        SpireSuper.call(sb);
    }
    @SpireOverride
    protected void renderDamageRange(SpriteBatch sb) {
        SpireSuper.call(sb);
        Settings.scale /= 2F;
    }
}
