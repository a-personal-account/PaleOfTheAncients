package paleoftheancients.reimu.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ChangeStateAction;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import paleoftheancients.PaleMod;
import paleoftheancients.helpers.AssetLoader;
import paleoftheancients.reimu.monsters.Reimu;


public class DeathBombPower extends AbstractPower {
    public static final String POWER_ID = PaleMod.makeID("DeathBombPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private boolean triggered = false;

    public DeathBombPower(AbstractCreature owner) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;

        type = PowerType.BUFF;
        isTurnBased = false;

        this.region128 = new TextureAtlas.AtlasRegion(AssetLoader.loadImage(PaleMod.assetPath("images/reimu/powers/SealingCircle84.png")), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(AssetLoader.loadImage(PaleMod.assetPath("images/reimu/powers/SealingCircle32.png")), 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public int onLoseHp(int damageAmount) {
        if(!triggered && damageAmount >= this.owner.currentHealth) {
            damageAmount = 0;
            AbstractDungeon.actionManager.addToTop(new ChangeStateAction((AbstractMonster) this.owner, Reimu.Deathbomb));
            triggered = true;
        }
        return damageAmount;
    }

    @Override
    public void atEndOfRound() {
        triggered = false;
        if(((Reimu) this.owner).rui.bombs <= 0) {
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, this));
        }
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }
}