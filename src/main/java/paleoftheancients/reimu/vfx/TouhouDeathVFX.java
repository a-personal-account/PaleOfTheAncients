package paleoftheancients.reimu.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import paleoftheancients.PaleMod;
import paleoftheancients.reimu.monsters.Reimu;

public class TouhouDeathVFX extends AbstractGameEffect {
    private float timer;
    private int count;

    private Reimu reimu;

    public TouhouDeathVFX(Reimu reimu) {
        this.reimu = reimu;
        this.timer = 0F;
        this.count = 8;
    }

    @Override
    public void update() {
        timer -= Gdx.graphics.getDeltaTime();
        if(timer <= 0F) {
            timer = 0.25F;
            if(count-- > 0) {
                CardCrawlGame.sound.playV(PaleMod.makeID("touhou_attack"), 0.15F);
            } else {
                CardCrawlGame.sound.playV(PaleMod.makeID("touhou_defeat"), 0.15F);
                this.isDone = true;
            }
        }
    }

    @Override
    public void render(SpriteBatch sb) {

    }

    @Override
    public void dispose() {

    }
}
