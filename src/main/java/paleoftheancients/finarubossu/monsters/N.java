package paleoftheancients.finarubossu.monsters;

import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.*;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.VoidCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.relics.MarkOfTheBloom;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.AwakenedEyeParticle;
import com.megacrit.cardcrawl.vfx.CollectorStakeEffect;
import com.megacrit.cardcrawl.vfx.combat.*;
import paleoftheancients.PaleMod;
import paleoftheancients.bard.helpers.AssetLoader;
import paleoftheancients.dungeons.PaleOfTheAncients;
import paleoftheancients.finarubossu.actions.GuaranteePowerApplicationAction;
import paleoftheancients.finarubossu.powers.*;
import paleoftheancients.finarubossu.vfx.BackgroundMonster;
import paleoftheancients.thevixen.cards.status.BossBurn;
import paleoftheancients.thevixen.helpers.RandomPoint;

import java.util.HashMap;
import java.util.Map;

public class N extends AbstractMonster {
    public static final String atlasFilepath = "images/npcs/neow/skeleton.atlas";
    public static final String ID = PaleMod.makeID("N");
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;

    private Texture emptypixel;

    private Map<Byte, EnemyMoveInfo> moves = new HashMap<>();

    private Bone[] eyes;

    private float backgroundmonstertimer = 0;
    private float eyetimer = 0;
    private static final int TOTALEYES = 3;
    private int openEyes = 0;
    private int turncounter = 1;

    private static final byte AWAKEN = Byte.MAX_VALUE;
    private static final byte ALLDEBUFFS = 1;
    private static final byte PRISMATICBEAM = 2;
    private static final byte PRISMATICLASERS = 3;
    private static final byte FLAMES = 4;
    private static final byte NAAIIIILS = 5;
    private static final byte VICEGRIP = 6;

    private int strengthEveryXTurns;
    private Eye[] eyemonsters;

    public N() {
        super(NAME, ID, 1200, 0, 30, (1920 - 1534) * 2 - 20, 800 - 230 + 20, null, 0, 0);

        this.drawX = 1534.0F * Settings.scale;
        this.drawY = 280.0F * Settings.scale;
        this.dialogX = -this.hb.width / 2;
        this.dialogY = -this.hb.height / 3;
        this.hb.width = 0;
        this.loadAnimation(atlasFilepath, "images/npcs/neow/skeleton.json", 1F);
        this.state.setAnimation(0, "idle", true);
        this.state.setTimeScale(1.0F);

        this.moves.put(AWAKEN, new EnemyMoveInfo(AWAKEN, Intent.UNKNOWN, -1, -1, false));
        this.moves.put(ALLDEBUFFS, new EnemyMoveInfo(ALLDEBUFFS, Intent.DEBUFF, -1, -1, false));
        this.moves.put(PRISMATICBEAM, new EnemyMoveInfo(PRISMATICBEAM, Intent.ATTACK_DEBUFF, 8, 3, true));
        this.moves.put(PRISMATICLASERS, new EnemyMoveInfo(PRISMATICLASERS, Intent.ATTACK, 12, 3, true));
        this.moves.put(FLAMES, new EnemyMoveInfo(FLAMES, Intent.ATTACK_DEBUFF, 30, -1, false));
        this.moves.put(NAAIIIILS, new EnemyMoveInfo(NAAIIIILS, Intent.ATTACK, 5, 5, true));
        this.moves.put(VICEGRIP, new EnemyMoveInfo(VICEGRIP, Intent.ATTACK, 40, -1, false));

        this.intentHb.move(this.intentHb.cX, this.intentHb.cY - 100 * Settings.scale);

        if(AbstractDungeon.ascensionLevel >= 19) {
            this.strengthEveryXTurns = 2;
        } else if(AbstractDungeon.ascensionLevel >= 9) {
            this.strengthEveryXTurns = 3;
        } else {
            this.strengthEveryXTurns = 4;
        }

        this.hideHealthBar();
        this.halfDead = true;
    }

    @Override
    protected void loadAnimation(String atlasUrl, String skeletonUrl, float scale) {
        this.atlas = AssetLoader.loader.loadAtlas(atlasUrl);
        SkeletonJson json = new SkeletonJson(this.atlas);

        json.setScale(Settings.scale / scale);
        SkeletonData skeletonData = json.readSkeletonData(Gdx.files.internal(skeletonUrl));
        this.skeleton = new Skeleton(skeletonData);
        this.skeleton.setColor(Color.WHITE);
        this.stateData = new AnimationStateData(skeletonData);
        this.state = new AnimationState(this.stateData);

        this.eyes = new Bone[]{
                this.skeleton.findBone("el3"),
                this.skeleton.findBone("el2"),
                this.skeleton.findBone("el1")
        };
    }

    @Override
    public void usePreBattleAction() {
        PaleOfTheAncients.playTempMusicInstantly(PaleMod.makeID("undyne"));

        this.emptypixel = ImageMaster.loadImage(PaleMod.assetPath("images/misc/emptypixel.png"));

        boolean markofthebloom = AbstractDungeon.player.hasRelic(MarkOfTheBloom.ID);
        if(markofthebloom) {
            AbstractDungeon.player.getRelic(MarkOfTheBloom.ID).flash();
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, 10), 10));
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new DexterityPower(AbstractDungeon.player, 3), 3));
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new ArtifactPower(AbstractDungeon.player, 3), 3));
        }

        this.eyemonsters = new Eye[]{
                new EyeOfSlumber(emptypixel, 0, 0),
                new EyeOfDisremembrance(emptypixel, 0, 0),
                new EyeOfRebirth(emptypixel, 0, 0)
        };
        AbstractPower[] p = new AbstractPower[]{
                new GazeOne(null),
                new GazeTwo(null),
                new GazeThree(null)
        };
        for(int i = 0; i < this.eyemonsters.length; i++) {
            this.eyemonsters[i].powers.add(p[i]);
            p[i].owner = this.eyemonsters[i];
            this.eyemonsters[i].refresh();
            AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(this.eyemonsters[i], false, AbstractDungeon.getCurrRoom().monsters.monsters.size()));
        }

        this.halfDead = true;

        AbstractDungeon.actionManager.addToBottom(new TalkAction(this, markofthebloom ? DIALOG[3] : DIALOG[MathUtils.random(0, 2)]));
    }

    @Override
    public void takeTurn() {
        if(this.nextMove == Byte.MIN_VALUE) {
            return;
        }

        DamageInfo info = null;
        if(this.moves.containsKey(this.nextMove)) {
            info = new DamageInfo(this, this.moves.get(this.nextMove).baseDamage, DamageInfo.DamageType.NORMAL);
            if (info.base > -1) {
                info.applyPowers(this, AbstractDungeon.player);
            }
        }
        switch(this.nextMove) {
            case AWAKEN:
                this.halfDead = false;
                this.showHealthBar();
                this.openEyes = 3;
                this.turncounter = 1;
                this.hb.width = this.hb_w * Settings.scale;
                this.healthBarUpdatedEvent();
                for(final Eye e : eyemonsters) {
                    AbstractDungeon.actionManager.addToBottom(new SuicideAction(e));
                }
                AbstractDungeon.actionManager.addToBottom(new GuaranteePowerApplicationAction(AbstractDungeon.player, this, new PlayerGaze(AbstractDungeon.player, this)));
                AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[MathUtils.random(4, 6)]));
                for(final AbstractGameEffect mo : AbstractDungeon.effectList) {
                    if(mo instanceof BackgroundMonster) {
                        ((BackgroundMonster) mo).accelerate();
                    }
                }
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new MillenialFerocityPower(this)));
                break;

            case ALLDEBUFFS: {
                AbstractGameEffect debuffs = new HeartMegaDebuffEffect();
                Color color = Color.SCARLET.cpy();
                color.a = 0F;
                ReflectionHacks.setPrivate(debuffs, AbstractGameEffect.class, "color", color);
                AbstractDungeon.actionManager.addToBottom(new VFXAction(debuffs));

                int intensity = AbstractDungeon.ascensionLevel >= 9 ? 2 : 1;
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new VulnerablePower(AbstractDungeon.player, intensity, true), intensity));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new FrailPower(AbstractDungeon.player, intensity, true), intensity));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new WeakPower(AbstractDungeon.player, intensity, true), intensity));
                break;
            }
            case PRISMATICBEAM: {
                AbstractDungeon.actionManager.addToBottom(new SFXAction("ATTACK_MAGIC_BEAM_SHORT", 0.5F));

                Color[] c = new Color[] {Color.GOLD, Color.BLUE, Color.RED};
                AbstractGameEffect age;
                for(int i = 0; i < c.length; i++) {
                    age = new SmallLaserEffect(skeleton.getX() + eyes[i].getWorldX(), skeleton.getY() + eyes[i].getWorldY(), AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY);
                    ReflectionHacks.setPrivate(age, SmallLaserEffect.class, "color", c[i]);
                    AbstractDungeon.effectList.add(age);
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.NONE, true));
                }
                int intensity = AbstractDungeon.ascensionLevel >= 9 ? 2 : 1;
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(new VoidCard(), intensity, true, true));
                break;
            }
            case PRISMATICLASERS: {
                Color[] c = new Color[] {Color.GOLD, Color.BLUE, Color.RED};
                AbstractGameEffect age;
                AbstractDungeon.actionManager.addToBottom(new WaitAction(0.3F));
                for(int i = 0; i < c.length; i++) {
                    age = new SweepingBeamEffect(skeleton.getX() + eyes[i].getWorldX(), skeleton.getY() + eyes[i].getWorldY(), true);
                    ReflectionHacks.setPrivate(age, SweepingBeamEffect.class, "color", c[i]);
                    AbstractDungeon.effectList.add(age);
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.NONE, true));
                }
                break;
            }
            case FLAMES: {
                for(int i = 0; i < 3; i++) {
                    AbstractDungeon.effectList.add(new RedFireballEffect(
                            RandomPoint.x(this.hb), RandomPoint.y(this.hb),
                            AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, 3));
                }
                AbstractDungeon.actionManager.addToBottom(new WaitAction(0.3F));

                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.NONE));
                AbstractCard bossburn = new BossBurn(AbstractDungeon.ascensionLevel > 4 ? 2 : 1);
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(bossburn, 1, true, true));
                break;
            }
            case NAAIIIILS: {
                int multiplier = this.moves.get(this.nextMove).multiplier;
                for(int i = 0; i < multiplier; i++) {
                    for(int j = 0; j < 2; j++) {
                        AbstractDungeon.effectList.add(new CollectorStakeEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY));
                    }
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.NONE, true));
                }
                break;
            }
            case VICEGRIP:
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new ViceCrushEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY), 0.5F));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.NONE, true));
                break;
        }

        if(!this.halfDead && this.turncounter++ % this.strengthEveryXTurns == 0) {
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, 1), 1));
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(int num) {
        if(this.halfDead) {
            this.setMove(Byte.MIN_VALUE, Intent.NONE);
        } else {
            Map<Byte, EnemyMoveInfo> stuff = new HashMap<>();
            stuff.putAll(this.moves);
            stuff.remove(AWAKEN);
            for(int i = 1; i < 4 && i < this.moveHistory.size(); i++) {
                stuff.remove(this.moveHistory.get(this.moveHistory.size() - i));
            }
            this.setMoveShortcut((Byte)stuff.keySet().toArray()[AbstractDungeon.monsterRng.random(stuff.size() - 1)]);
        }
    }
    @Override
    public void rollMove() {
        this.getMove(0);
    }

    private void setMoveShortcut(byte next) {
        EnemyMoveInfo info = this.moves.get(next);
        this.setMove(next, info.intent, info.baseDamage, info.multiplier, info.isMultiDamage);
    }

    @Override
    public void die() {
        CardCrawlGame.stopClock = true;
        this.onBossVictoryLogic();
        this.onFinalBossVictoryLogic();
        super.die();
    }

    @Override
    public void dispose() {
        if(this.atlas != null) {
            AssetLoader.unLoad(atlasFilepath);
            this.atlas = null;
        }
        super.dispose();
        this.openEyes = -3;
        BackgroundMonster.masterDispose();
    }

    public void awaken() {
        this.setMoveShortcut(AWAKEN);
        this.createIntent();
        for(final AbstractMonster mo : this.eyemonsters) {
            AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
                @Override
                public void update() {
                    this.isDone = true;
                    mo.powers.clear();
                }
            });
        }
    }

    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
        NAME = monsterStrings.NAME;
        MOVES = monsterStrings.MOVES;
        DIALOG = monsterStrings.DIALOG;
    }

    @Override
    public void update() {
        super.update();

        if(this.eyemonsters != null) {
            for (int i = 0; i < this.eyemonsters.length; i++) {
                this.eyemonsters[i].drawX = this.skeleton.getX() + this.eyes[i].getWorldX();
                this.eyemonsters[i].drawY = this.skeleton.getY() + this.eyes[i].getWorldY() - this.eyemonsters[i].hb.height * 2 / 3;
                this.eyemonsters[i].refresh();
            }
        } else {
            this.usePreBattleAction();
        }

        if(openEyes >= 0) {
            this.backgroundmonstertimer -= Gdx.graphics.getDeltaTime();
            if (this.backgroundmonstertimer <= 0F) {
                this.backgroundmonstertimer = this.openEyes > 0 ? 0.4F : 2.5F;
                AbstractDungeon.effectList.add(new BackgroundMonster(this.openEyes > 0));
            }
        }
        this.eyetimer -= Gdx.graphics.getDeltaTime();
        if(this.eyetimer <= 0F) {
            this.eyetimer = 0.1F;
            for(int i = 0; i < this.openEyes && i < TOTALEYES; i++) {
                AbstractDungeon.effectList.add(new AwakenedEyeParticle(this.skeleton.getX() + this.eyes[i].getWorldX(), this.skeleton.getY() + this.eyes[i].getWorldY()));
            }
        }
    }
}
