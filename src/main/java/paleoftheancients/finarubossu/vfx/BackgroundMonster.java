package paleoftheancients.finarubossu.vfx;

import paleoftheancients.bard.monsters.BardBoss;
import paleoftheancients.ironcluck.monsters.IronCluck;
import basemod.ReflectionHacks;
import basemod.abstracts.CustomMonster;
import basemod.abstracts.CustomPlayer;
import basemod.animations.AbstractAnimation;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.spine.Bone;
import com.esotericsoftware.spine.Skeleton;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.beyond.*;
import com.megacrit.cardcrawl.monsters.city.*;
import com.megacrit.cardcrawl.monsters.ending.SpireShield;
import com.megacrit.cardcrawl.monsters.ending.SpireSpear;
import com.megacrit.cardcrawl.monsters.exordium.*;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

import java.util.ArrayList;

public class BackgroundMonster extends AbstractGameEffect {
    private AbstractCreature ac;
    private Skeleton skeleton;
    private Texture img;
    private AbstractAnimation animation;
    private float rotationVelocity;
    private Vector2 direction;

    public BackgroundMonster() {
        this.ac = RandomEntity();
        this.ac.flipHorizontal = MathUtils.randomBoolean();


        this.direction = new Vector2(MathUtils.random(100, 200), MathUtils.random(-40, 40));
        this.direction = direction.nor();
        if(MathUtils.randomBoolean()) {
            this.direction.x *= -1;
        }
        this.direction.x *= Settings.scale * 50F;
        this.direction.y *= Settings.scale * 50F;

        float multiplier = Settings.WIDTH / 2F / direction.x * 1.3F;
        this.ac.drawX = Settings.WIDTH / 2F - direction.x * multiplier;
        this.ac.drawY = Settings.HEIGHT * 2F / 3F + MathUtils.random(-Settings.HEIGHT, Settings.HEIGHT) / 3F - direction.y * multiplier;

        this.rotation = MathUtils.random(-360F, 360F);
        this.rotationVelocity = MathUtils.random(30F, 50F);
        if(MathUtils.randomBoolean()) {
            this.rotationVelocity *= -1;
        }

        boolean isPlayer = ac instanceof AbstractPlayer;

        this.skeleton = (Skeleton) ReflectionHacks.getPrivate(ac, AbstractCreature.class, "skeleton");
        if(this.skeleton != null) {
            this.ac.state.update(Gdx.graphics.getDeltaTime());
            this.ac.state.apply(this.skeleton);
            this.skeleton.updateWorldTransform();

            String[] shadows = new String[]{"shadow", "Shadow"};
            Bone shadow;
            for(final String s : shadows) {
                shadow = this.skeleton.findBone(s);
                if (shadow != null) {
                    shadow.setScale(0F);
                }
            }
        }
        this.img = isPlayer ? ((AbstractPlayer) ac).img : ((Texture) ReflectionHacks.getPrivate(ac, AbstractMonster.class, "img"));

        if(this.skeleton == null && this.img == null && (isPlayer && ac instanceof CustomPlayer || (!isPlayer && ac instanceof CustomMonster))) {
            this.animation = (AbstractAnimation) ReflectionHacks.getPrivate(ac, isPlayer ? CustomPlayer.class : CustomMonster.class, "animation");
        }

        this.color = Color.WHITE.cpy().sub(0, 0, 0, MathUtils.random(0.4F, 0.7F));
        this.renderBehind = MathUtils.randomBoolean(0.8F);
    }

    @Override
    public void update() {
        this.ac.drawX += direction.x * Gdx.graphics.getDeltaTime();
        this.ac.drawY += direction.y * Gdx.graphics.getDeltaTime();
        this.rotation += this.rotationVelocity * Gdx.graphics.getDeltaTime();

        this.ac.updateAnimations();

        if((direction.x < 0 && ac.drawX < -Settings.WIDTH / 3) || (direction.x > 0 && ac.drawX > Settings.WIDTH * 1.3F)) {
            this.isDone = true;
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        if(this.animation != null) {
            sb.setColor(this.color);
            this.animation.setFlip(ac.flipHorizontal, ac.flipVertical);
            this.animation.renderSprite(sb, ac.drawX, ac.drawY);
            sb.setColor(Color.WHITE.cpy());
        } else if(this.skeleton != null) {
            float angle = (float)Math.toRadians(this.rotation);

            this.skeleton.getRootBone().setRotation(this.rotation);
            this.skeleton.updateWorldTransform();
            this.skeleton.setPosition(
                    (float)(Math.cos(angle) * (this.ac.drawX - this.ac.hb.cX) - Math.sin(angle) * (this.ac.drawY - this.ac.hb.cY) + ac.hb.cX),
                    (float)(Math.sin(angle) * (this.ac.drawX - this.ac.hb.cX) + Math.cos(angle) * (this.ac.drawY - this.ac.hb.cY) + ac.hb.cY));
            this.skeleton.setColor(this.color);
            this.skeleton.setFlip(false, ac.flipVertical);
            if(ac.flipHorizontal) {
                this.skeleton.getRootBone().setScaleX(-Math.abs(this.skeleton.getRootBone().getScaleX()));
            }
            sb.end();
            CardCrawlGame.psb.begin();
            AbstractCreature.sr.draw(CardCrawlGame.psb, this.skeleton);
            CardCrawlGame.psb.end();
            sb.begin();
            sb.setBlendFunction(770, 771);
        } else {
            sb.setColor(this.color);
            sb.draw(this.img, ac.drawX, ac.drawY, this.img.getWidth() * Settings.scale / 2F, this.img.getHeight() * Settings.scale, this.img.getWidth() * Settings.scale, this.img.getHeight() * Settings.scale, 1F, 1F, this.rotation, 0, 0, this.img.getWidth(), this.img.getHeight(), ac.flipHorizontal, ac.flipVertical);
            sb.setColor(Color.WHITE.cpy());
        }
    }

    @Override
    public void dispose() {
        if(this.ac instanceof AbstractMonster) {
            ((AbstractMonster) this.ac).dispose();
        } else if(this.ac instanceof AbstractPlayer) {
            ((AbstractPlayer) this.ac).dispose();
        }
    }



    private static AbstractCreature RandomEntity() {
        ArrayList<RandomEntityGetter> possibilities = new ArrayList<>();

        possibilities.add(() -> new LouseNormal(0, 0));
        possibilities.add(() -> new LouseDefensive(0, 0));
        possibilities.add(() -> new Lagavulin(false));
        possibilities.add(() -> new GremlinNob(0, 0));
        possibilities.add(() -> new Sentry(0, 0));
        possibilities.add(() -> new GremlinFat(0, 0));
        possibilities.add(() -> new GremlinThief(0, 0));
        possibilities.add(() -> new GremlinWarrior(0, 0));
        possibilities.add(() -> new GremlinWizard(0, 0));
        possibilities.add(() -> new GremlinTsundere(0, 0));
        possibilities.add(() -> new Cultist(0, 0, false));
        possibilities.add(() -> new AcidSlime_L(0, 0));
        possibilities.add(() -> new AcidSlime_M(0, 0));
        possibilities.add(() -> new AcidSlime_S(0, 0, 0));
        possibilities.add(() -> new SpikeSlime_L(0, 0));
        possibilities.add(() -> new SpikeSlime_M(0, 0));
        possibilities.add(() -> new SpikeSlime_S(0, 0, 0));
        possibilities.add(() -> new FungiBeast(0, 0));
        possibilities.add(() -> new JawWorm(0, 0));
        possibilities.add(() -> new Looter(0, 0));
        possibilities.add(() -> new SlaverBlue(0, 0));
        possibilities.add(() -> new SlaverRed(0, 0));
        possibilities.add(() -> new TheGuardian());
        possibilities.add(() -> new SlimeBoss());

        possibilities.add(() -> new BanditBear(0, 0));
        possibilities.add(() -> new BanditLeader(0, 0));
        possibilities.add(() -> new BanditPointy(0, 0));
        possibilities.add(() -> new BookOfStabbing());
        possibilities.add(() -> new BronzeAutomaton());
        possibilities.add(() -> new BronzeOrb(0, 0, 0));
        possibilities.add(() -> new Byrd(0, 0));
        possibilities.add(() -> new Centurion(0, 0));
        possibilities.add(() -> new Champ());
        possibilities.add(() -> new Chosen(0, 0));
        possibilities.add(() -> new GremlinLeader());
        possibilities.add(() -> new Healer(0, 0));
        possibilities.add(() -> new Mugger(0, 0));
        possibilities.add(() -> new ShelledParasite(0, 0));
        possibilities.add(() -> new SnakePlant(0, 0));
        possibilities.add(() -> new Snecko(0, 0));
        possibilities.add(() -> new SphericGuardian(0, 0));
        possibilities.add(() -> new Taskmaster(0, 0));
        possibilities.add(() -> new TheCollector());
        possibilities.add(() -> new TorchHead(0, 0));

        possibilities.add(() -> new AwakenedOne(0, 0));
        possibilities.add(() -> new Darkling(0, 0));
        possibilities.add(() -> new Deca());
        possibilities.add(() -> new Donu());
        possibilities.add(() -> new Exploder(0, 0));
        possibilities.add(() -> new GiantHead());
        possibilities.add(() -> new Maw(0, 0));
        possibilities.add(() -> new Nemesis());
        possibilities.add(() -> new OrbWalker(0, 0));
        possibilities.add(() -> new Reptomancer());
        possibilities.add(() -> new Repulsor(0, 0));
        possibilities.add(() -> new SnakeDagger(0, 0));
        possibilities.add(() -> new Spiker(0, 0));
        possibilities.add(() -> new SpireGrowth());
        possibilities.add(() -> new TimeEater());
        possibilities.add(() -> new Transient());
        possibilities.add(() -> new WrithingMass());

        possibilities.add(() -> new SpireShield());
        possibilities.add(() -> new SpireSpear());


        possibilities.add(() -> new BardBoss());
        //possibilities.add(() -> new TheVixenBoss());
        //possibilities.add(() -> new TheShowmanBoss());
        possibilities.add(() -> new IronCluck());


        possibilities.add(() -> new SpineAnimationEntity("images/characters/ironclad/idle/skeleton.atlas", "images/characters/ironclad/idle/skeleton.json", 1.0F, 220.0F, 290.0F));
        possibilities.add(() -> new SpineAnimationEntity("images/characters/theSilent/idle/skeleton.atlas", "images/characters/theSilent/idle/skeleton.json", 1.0F, 240.0F, 240.0F));
        possibilities.add(() -> new SpineAnimationEntity("images/characters/defect/idle/skeleton.atlas", "images/characters/defect/idle/skeleton.json", 1.0F, 240.0F, 244.0F));

        possibilities.add(() -> new SpineAnimationEntity("images/npcs/merchant/skeleton.atlas", "images/npcs/merchant/skeleton.json", 1.0F, 240.0F, 244.0F));



        return possibilities.get(MathUtils.random(possibilities.size() - 1)).get();
    }

    interface RandomEntityGetter {
        public AbstractCreature get();
    }

    static class SpineAnimationEntity extends AbstractMonster {
        public SpineAnimationEntity(String atlas, String skeleton, float scale, float width, float height) {
            super("", "", 0, 0, 0, width, height, null, 0, 0);
            this.loadAnimation(atlas, skeleton, scale);
        }

        @Override
        public void takeTurn() {}

        @Override
        protected void getMove(int i) {}
    }
}
