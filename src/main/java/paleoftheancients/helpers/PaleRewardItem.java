package paleoftheancients.helpers;

import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import paleoftheancients.PaleMod;
import paleoftheancients.relics.PaleRelic;
import paleoftheancients.thevixen.vfx.FireBurstParticleEffectCopyPaste;

import java.util.ArrayList;

public class PaleRewardItem extends RewardItem {
    private Texture chain;
    private ArrayList<AbstractGameEffect> effects;
    private float particleTimer;
    private boolean takeAble;
    public PaleRewardItem(AbstractRelic relic) {
        super(relic);

        this.effects = (ArrayList<AbstractGameEffect>) ReflectionHacks.getPrivate(this, RewardItem.class, "effects");
        this.particleTimer = 0F;
        this.takeAble = PaleRelic.takeAble();
        if(!takeAble) {
            chain = AssetLoader.loadImage(PaleMod.assetPath("images/ui/rewards/chain.png"));
        }
    }

    @Override
    public void update() {
        super.update();

        if(takeAble) {
            particleTimer -= Gdx.graphics.getDeltaTime();
            if (particleTimer <= 0F) {
                particleTimer = 0.08F;
                float gravity = 180.0F * Settings.scale;
                for (int i = 0; i < 3; i++) {
                    effects.add(new FireBurstParticleEffectCopyPaste(Color.PURPLE.cpy(), this.hb.x + MathUtils.random(this.hb.width), this.hb.y, gravity));
                    effects.add(new FireBurstParticleEffectCopyPaste(Color.PURPLE.cpy(), this.hb.x + MathUtils.random(this.hb.width), this.hb.y + this.hb.height, gravity));
                }
                effects.add(new FireBurstParticleEffectCopyPaste(Color.PURPLE.cpy(), this.hb.x + this.hb.width, this.hb.y + MathUtils.random(this.hb.height), gravity));
                effects.add(new FireBurstParticleEffectCopyPaste(Color.PURPLE.cpy(), this.hb.x, this.hb.y + MathUtils.random(this.hb.height), gravity));
            }
        } else {
            effects.clear();
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        super.render(sb);

        if(takeAble) {
            if(this.hb.hovered) {
                for(final AbstractGameEffect age : effects) {
                    age.render(sb);
                }
            }
        } else {
            sb.draw(chain, (float)Settings.WIDTH / 2.0F - 232.0F, this.y - 49.0F, 232.0F, 49.0F, 464.0F, 98.0F, Settings.scale * 1.03F, Settings.scale * 1.15F, 0.0F, 0, 0, 464, 98, false, false);
            sb.draw(chain, (float)Settings.WIDTH / 2.0F - 232.0F, this.y - 49.0F, 232.0F, 49.0F, 464.0F, 98.0F, Settings.scale * 1.03F, Settings.scale * 1.15F, 0.0F, 0, 0, 464, 98, false, true);
        }
    }
}