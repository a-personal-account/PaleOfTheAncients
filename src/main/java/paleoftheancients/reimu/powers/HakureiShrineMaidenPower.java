package paleoftheancients.reimu.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import paleoftheancients.PaleMod;
import paleoftheancients.helpers.AssetLoader;
import paleoftheancients.reimu.actions.SpawnOrbAction;
import paleoftheancients.reimu.monsters.Reimu;


public class HakureiShrineMaidenPower extends AbstractPower {

    public static final String POWER_ID = PaleMod.makeID("HakureiShrineMaidenPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public HakureiShrineMaidenPower(AbstractCreature owner, int amount) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = amount;

        type = PowerType.BUFF;
        isTurnBased = false;

        this.region128 = new TextureAtlas.AtlasRegion(AssetLoader.loadImage(PaleMod.assetPath("images/reimu/powers/Hakurei84.png")), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(AssetLoader.loadImage(PaleMod.assetPath("images/reimu/powers/Hakurei32.png")), 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void duringTurn() { //we use this instead of end of turn so the spawned orbs roll their moves for the next turn correctly
        Reimu reimu = (Reimu) owner;
        if (reimu.orbNum() == 0) {
            AbstractDungeon.actionManager.addToBottom(new SpawnOrbAction(reimu, 1));
            AbstractDungeon.actionManager.addToBottom(new SpawnOrbAction(reimu, 2));
            AbstractDungeon.actionManager.addToBottom(new SpawnOrbAction(reimu, 3));
        } else {
            AbstractDungeon.actionManager.addToBottom(new SpawnOrbAction(reimu, 2));
            AbstractDungeon.actionManager.addToBottom(new SpawnOrbAction(reimu, 3));
        }
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1] + (amount * 2) + DESCRIPTIONS[2];
    }
}