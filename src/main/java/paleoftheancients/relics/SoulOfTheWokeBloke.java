package paleoftheancients.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.AwakenedEyeParticle;
import paleoftheancients.PaleMod;
import paleoftheancients.helpers.AssetLoader;
import paleoftheancients.helpers.OnReceivePowerRelic;

public class SoulOfTheWokeBloke extends CustomRelic implements OnReceivePowerRelic {
    public static final String ID = PaleMod.makeID("SoulOfTheWokeBloke");

    private static final RelicTier TIER = RelicTier.SPECIAL;
    private static final LandingSound SOUND = LandingSound.MAGICAL;

    private static final int STRENGTH = 2;

    private boolean active = true;

    public SoulOfTheWokeBloke() {
        super(ID, AssetLoader.loadImage(PaleMod.assetPath("images/relics/wokebloke.png")), AssetLoader.loadImage(PaleMod.assetPath("images/relics/outline/wokebloke.png")), TIER, SOUND);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + STRENGTH + DESCRIPTIONS[1];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new SoulOfTheWokeBloke();
    }

    @Override
    public void onTrigger(AbstractPower power) {
        if(active) {
            active = false;
            this.addToTop(new AbstractGameAction() {
                @Override
                public void update() {
                    this.isDone = true;
                    active = true;
                }
            });
            this.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, STRENGTH), STRENGTH));
        }
    }


    private float fireTimer = 0.0F;
    @Override
    public void update() {
        super.update();
        if (!Settings.hideRelics) {
            this.fireTimer -= Gdx.graphics.getDeltaTime();
            if (this.fireTimer < 0.0F) {
                this.fireTimer = 0.1F;
                AbstractGameEffect age = new WokeBlokeRelicEye(this.currentX, this.currentY, 0.3F * (this.hb.hovered ? 2F : 1F));
                AbstractDungeon.topLevelEffects.add(age);
            }
        }
    }
    @Override
    public void renderInTopPanel(SpriteBatch sb) {}

    class WokeBlokeRelicEye extends AwakenedEyeParticle {
        public WokeBlokeRelicEye(float x, float y, float scalechange) {
            super(x, y);
            this.scale *= scalechange;
        }
    }
}
