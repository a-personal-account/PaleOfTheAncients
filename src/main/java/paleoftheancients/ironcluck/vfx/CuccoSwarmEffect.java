package paleoftheancients.ironcluck.vfx;

import paleoftheancients.PaleMod;
import paleoftheancients.ironcluck.monsters.IronCluck;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

import java.util.ArrayList;

public class CuccoSwarmEffect extends AbstractGameEffect {

    private AbstractCreature ac;
    private int amount;

    private ArrayList<Cucco> cuccos;
    private float clucktimer;

    private IronCluck ic;

    public CuccoSwarmEffect(AbstractCreature target, int amount) {
        this.ac = target;
        this.amount = amount;

        cuccos = new ArrayList<>();
        for(int i = 0; i < amount; i++) {
            cuccos.add(new Cucco(i));
        }
        CardCrawlGame.sound.playA(PaleMod.makeID("cuccostart"), MathUtils.random(-0.1F, 0.1F));

        ic = new IronCluck(9F);
        ic.hideHealthBar();
    }

    public void update() {
        clucktimer -= Gdx.graphics.getDeltaTime();
        if(clucktimer < 0F) {
            CardCrawlGame.sound.playA(PaleMod.makeID("cucco" + MathUtils.random(1, 2)), MathUtils.random(-0.2F, 0.2F));
        }

        ic.update();

        boolean doneForGood = true;
        for(int i = cuccos.size() - 1; i >= 0; i--) {
            doneForGood = false;
            Cucco c = cuccos.get(i);
            c.update();
            if(c.isDone) {
                cuccos.remove(i);
            }
        }
        if(doneForGood) {
            this.isDone = true;
        }
    }

    public void render(SpriteBatch sb) {
        for(final Cucco c : cuccos) {
            c.render(sb);
        }
    }

    public void dispose() {
        ic.dispose();
    }


    class Cucco {
        Vector2 direction;
        float rotationvelocity;
        float rotation;
        float delay;
        float drawX, drawY;
        boolean flipHorizontal;
        boolean isDone;

        public Cucco(int index) {

            delay = 0.2F * index + MathUtils.random(-0.1F, 0.1F);

            if(MathUtils.randomBoolean(0.05F)) {
                rotationvelocity = MathUtils.random(-(float)Math.PI * 4, (float)Math.PI * 4);
            } else {
                rotationvelocity = 0;
            }
            direction = new Vector2(MathUtils.random(-1F, 1F), MathUtils.random(-1F, 1F));
            direction = direction.nor();
            direction.x *= 1000 * Settings.scale;
            direction.y *= 1000 * Settings.scale;
            direction.x *= MathUtils.random(1F, 1.2F);
            direction.y *= MathUtils.random(1F, 1.2F);

            drawX = ac.hb.cX - direction.x;
            drawY = ac.hb.cY - direction.y;

            flipHorizontal = direction.x > 0;
            isDone = false;

            this.rotation = direction.angle();
        }

        public void update() {
            if(delay > 0F) {
                delay -= Gdx.graphics.getDeltaTime();
            } else {
                if(rotationvelocity != 0) {
                    rotation += rotationvelocity * Gdx.graphics.getDeltaTime();
                }
                drawX += direction.x * Gdx.graphics.getDeltaTime();
                drawY += direction.y * Gdx.graphics.getDeltaTime();

                if(((direction.x > 0 && drawX > ac.drawX + direction.x) || (direction.x <= 0 && drawX <= ac.drawX + direction.x)) ||
                        ((direction.y > 0 && drawY > ac.drawY + direction.y) || (direction.y <= 0 && drawY <= ac.drawY + direction.y))) {
                    isDone = true;
                }
            }
        }

        public void render(SpriteBatch sb) {
            if(delay <= 0) {
                ic.drawX = drawX;
                ic.drawY = drawY;
                ic.flipHorizontal = flipHorizontal;
                ic.setRotation(rotation);
                ic.render(sb);
            }
        }
    }
}
