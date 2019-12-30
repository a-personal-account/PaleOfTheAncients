package paleoftheancients.reimu.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import paleoftheancients.PaleMod;
import paleoftheancients.helpers.AssetLoader;

import java.util.ArrayList;

public class HakureiAmuletVFX extends AbstractDamagingVFX {
    public static final String path = "images/reimu/vfx/amulet_hakurei.png";
    private Texture image;
    private int imagewidth, imageheight;

    private AbstractCreature source;
    private boolean finished;

    private ArrayList<Seal> seals = new ArrayList<>();

    public HakureiAmuletVFX(AbstractCreature target, AbstractCreature source, DamageInfo info, int num) {
        this(target, source, info, num, 8);
    }
    public HakureiAmuletVFX(AbstractCreature target, AbstractCreature source, DamageInfo info, int num, int amuletcount) {
        super(target, info, num);
        this.image = AssetLoader.loadImage(PaleMod.assetPath(path));
        this.imagewidth = image.getWidth();
        this.imageheight = image.getHeight();

        this.source = source;

        this.rotation = MathUtils.random(6.3F);

        this.setDelay();
        this.finished = false;

        CardCrawlGame.sound.playV(PaleMod.makeID("touhou_shot"), 0.25F);
        while(amuletcount-- > 0) {
            seals.add(new Seal());
        }
    }

    @Override
    public void update() {
        for(int i = seals.size() - 1; i >= 0; i--) {
            if(seals.get(i).update()) {
                seals.remove(i);
                if(seals.isEmpty() && num <= 0) {
                    this.isDone = true;
                    endDelay();
                }

                if(!finished) {
                    finished = true;

                    endDelay();
                    for(; num > 0; num--) {
                        AbstractDungeon.actionManager.addToTop(new DamageAction(target, info, AbstractGameAction.AttackEffect.NONE));
                    }
                }
            }
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setColor(Color.WHITE);
        for(final Seal s : seals) {
            s.render(sb);
        }
    }

    @Override
    public void dispose() {

    }

    class Seal {
        private float phase, curvedistance, targetX, targetY, startingrads, endrads;
        public float x, y;
        private float rotation;

        public Seal() {
            this.rotation =  MathUtils.random(360F);

            curvedistance = MathUtils.random(source.hb.height * 0.5F, source.hb.height * 5F);
            if(MathUtils.randomBoolean()) { curvedistance *= -1; }

            targetX = AbstractDungeon.player.hb.cX + MathUtils.random(-AbstractDungeon.player.hb.width, AbstractDungeon.player.hb.width) / 3F;
            targetY = AbstractDungeon.player.hb.cY + MathUtils.random(-AbstractDungeon.player.hb.height, AbstractDungeon.player.hb.height) / 3F;
            float factor = (float)Math.sqrt(Math.pow(targetX - source.hb.cX, 2) + Math.pow(targetY - source.hb.cY, 2));

            x = (targetX + source.hb.cX) / 2F + (targetY - source.hb.cY) * curvedistance / factor;
            y = (targetY + source.hb.cY) / 2F - (targetX - source.hb.cX) * curvedistance / factor;

            curvedistance = (float)Math.sqrt(Math.pow(x - source.hb.cX, 2) + Math.pow(y - source.hb.cY, 2));

            endrads = -(float)Math.PI / 2 - (float)Math.atan((y - targetY) / (x - targetX));
            phase = (float)Math.PI / 2 - (float)Math.atan((y - source.hb.cY) / (x - source.hb.cX));
            if(y > source.hb.cY) {
                endrads += Math.PI * 2;
            }
        }

        public boolean update() {
            rotation += Gdx.graphics.getDeltaTime() * 150F;

            phase = MathUtils.lerp(phase, endrads, Gdx.graphics.getDeltaTime() * 4);

            if(atDestination()) {
                AbstractDungeon.effectsQueue.add(new FlashAtkImgEffect(calcX(), calcY(), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                return true;
            }

            return false;
        }

        public void render(SpriteBatch sb) {
            float curX = calcX();
            float curY = calcY();
            sb.draw(image, curX, curY, imagewidth / 2F, imageheight / 2F, imagewidth, imageheight, scale, scale, this.rotation, 0, 0, imagewidth, imageheight, false, false);
        }

        private float calcX() {
            return x + (float)Math.sin(phase) * curvedistance - imagewidth / 2F;
        }
        private float calcY() {
            return y + (float)Math.cos(phase) * curvedistance - imageheight / 2F;
        }

        public boolean atDestination() {
            return Math.abs(endrads - phase) < 0.1F;
        }
    }


    public static void disposeAll() {
        AbstractDamagingVFX.disposeTry(PaleMod.assetPath(path));
    }
}
