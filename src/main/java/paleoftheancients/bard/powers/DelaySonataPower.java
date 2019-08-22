package paleoftheancients.bard.powers;

import paleoftheancients.PaleMod;
import paleoftheancients.bard.monsters.BardBoss;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class DelaySonataPower extends AbstractPower implements InvisiblePower {
    public static final String POWER_ID = PaleMod.makeID("DelaySonataPower");

    public DelaySonataPower(BardBoss owner) {
        this.name = "";
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = PowerType.BUFF;
        this.description = "";
        this.region48 = new TextureAtlas.AtlasRegion(new Texture(new Pixmap(1, 1, Pixmap.Format.Alpha)), 0, 0, 1, 1);
        this.region128 = this.region48;
    }

    @Override
    public void atEndOfRound() {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this.owner, this.owner, new SonataPower((BardBoss)this.owner)));
        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, this));
    }
}