package paleoftheancients.reimu.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import paleoftheancients.PaleMod;
import paleoftheancients.helpers.AssetLoader;
import paleoftheancients.reimu.monsters.Reimu;
import paleoftheancients.reimu.monsters.YinYangOrb;

public class Position extends AbstractPower {

    public static final String POWER_ID = PaleMod.makeID("PositionPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private float originalY;
    private static float movement = Reimu.orbOffset;

    public Position(AbstractCreature owner, int amount) {
        name = NAME;
        ID = POWER_ID;

        this.owner = owner;
        this.amount = amount;

        type = PowerType.BUFF;
        isTurnBased = false;

        this.originalY = owner.drawY;

        this.region128 = new TextureAtlas.AtlasRegion(AssetLoader.loadImage(PaleMod.assetPath("images/reimu/powers/SpellCard84.png")), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(AssetLoader.loadImage(PaleMod.assetPath("images/reimu/powers/SpellCard32.png")), 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void onAfterCardPlayed(AbstractCard card) {
        boolean changed = false;
        if (card.type == AbstractCard.CardType.ATTACK) {
            if (amount > 1) {
                this.flash();
                amount--;
                owner.drawY -= movement;
                changed = true;
            }
        }
        if (card.type == AbstractCard.CardType.SKILL) {
            if (amount < 3) {
                this.flash();
                amount++;
                owner.drawY += movement;
                changed = true;
            }
        }
        updateDescription();
        if(changed) {
            atStartOfTurn();
        }
    }

    @Override
    public void atStartOfTurn() {
        Reimu reimu = (Reimu) AbstractDungeon.getCurrRoom().monsters.getMonster(Reimu.ID);
        if(reimu != null && reimu.rui.extralives == 1) {
            for(int i = 0; i < 2; i++) {
                YinYangOrb orb = reimu.orbs[i][amount - 1];
                if (orb != null) {
                    ShotTypeAmuletPower st = (ShotTypeAmuletPower) orb.getPower(ShotTypeAmuletPower.POWER_ID);
                    if (st != null) {
                        st.homing = true;
                        orb.reverse();
                    }
                }
            }
        }
    }

    @Override
    public void onRemove() {
        super.onRemove();
        owner.drawY = originalY;
    }

    public static int playerPosition() {
        int playerPosition = 1;
        AbstractPower ap = AbstractDungeon.player.getPower(Position.POWER_ID);
        if(ap != null) {
            playerPosition = ap.amount;
        }
        return playerPosition;
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }
}
