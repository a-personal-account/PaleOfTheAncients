package paleoftheancients.theshowman.monsters;

import paleoftheancients.PaleMod;
import paleoftheancients.dungeons.PaleOfTheAncients;
import paleoftheancients.theshowman.actions.DiscardShowmanCardAction;
import paleoftheancients.theshowman.actions.DiscardShowmanHandAction;
import paleoftheancients.theshowman.actions.ExhaustShowmanCardAction;
import paleoftheancients.theshowman.bosscards.*;
import paleoftheancients.theshowman.misc.DummyOrb;
import paleoftheancients.theshowman.misc.MonsterSoulGroup;
import paleoftheancients.theshowman.ui.MonsterDiscardPilePanel;
import paleoftheancients.theshowman.ui.MonsterDrawPilePanel;
import paleoftheancients.theshowman.ui.MonsterEnergyPanel;
import paleoftheancients.theshowman.ui.MonsterExhaustPanel;
import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.actions.common.SpawnMonsterAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.powers.FlightPower;
import com.megacrit.cardcrawl.vfx.SpeechBubble;
import com.megacrit.cardcrawl.vfx.cardManip.ExhaustCardEffect;

import java.util.ArrayList;
import java.util.Collections;

public class TheShowmanBoss extends CustomMonster {
    public static final String ID = PaleMod.makeID("TheShowmanBoss");
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;

    public CardGroup drawpile = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
    public CardGroup exhaustpile = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
    public CardGroup discardpile = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
    public CardGroup hand = new CardGroup(CardGroup.CardGroupType.HAND);

    public MonsterSoulGroup soulGroup;

    public MonsterExhaustPanel exhaustPanel;
    public MonsterDrawPilePanel drawPilePanel;
    public MonsterDiscardPilePanel discardPilePanel;
    public MonsterEnergyPanel energyPanel;
    private boolean initialized;

    private ArrayList<DummyMonster> cards;

    private ShowmanStage stage;
    private int baseenergy;
    public int curenergy;
    public int tempenergy;

    private Texture emptypixel;

    private int drawnThisTurn = 0;
    private int handsize = 3;
    private static final int EXHAUSTRELIC = 3;
    private int cardUse;

    private boolean upgraded;
    private int turncounter;

    public TheShowmanBoss() {
        super(NAME, ID, 600, 0.0F, -15.0F, 300.0F, 230.0F, PaleMod.assetPath("images/misc/emptypixel.png"), 0.0F, 0.0F);

        //this.animation = new BraixenAnimation(this, TheVixenMod.getResourcePath("spriter/thevixen.scml"), AbstractDungeon.aiRng.random(4095) == 0);

        this.dialogX = (this.drawX - 30.0F * Settings.scale);
        this.dialogY = (this.drawY + 140.0F * Settings.scale);

        this.type = EnemyType.NORMAL;

        this.flipHorizontal = true;
        this.stage = null;
        this.baseenergy = 3;
        this.tempenergy = 0;
        this.curenergy = this.baseenergy;
        this.cardUse = EXHAUSTRELIC;
        this.initialized = false;
        this.upgraded = false;
        this.turncounter = 0;
    }

    @Override
    public void usePreBattleAction() {
        PaleOfTheAncients.playTempMusic(PaleMod.makeID("tellurtownautumn"));

        this.drawpile.addToTop(new Columbify(this));
        this.drawpile.addToTop(new CrashingLights(this));
        this.drawpile.addToTop(new CrashingLights(this));
        this.drawpile.addToTop(new DapperFlourish(this));
        this.drawpile.addToTop(new ExaggeratedArmSweeps(this));
        this.drawpile.addToTop(new ForMyNextTrick(this));
        this.drawpile.addToTop(new GrossDisplay(this));
        this.drawpile.addToTop(new IsThisYourCard(this));
        this.drawpile.addToTop(new ReappearingTrick(this));
        this.drawpile.addToTop(new Showstopper(this));
        this.drawpile.addToTop(new SybilFlourish(this));
        this.drawpile.addToTop(new WillingVolunteer(this));
        Collections.shuffle(this.drawpile.group, AbstractDungeon.monsterRng.random);

        cards = new ArrayList<>();
        emptypixel = ImageMaster.loadImage(PaleMod.assetPath("images/misc/emptypixel.png"));

        this.exhaustPanel = new MonsterExhaustPanel(this);
        this.discardPilePanel = new MonsterDiscardPilePanel(this);
        this.drawPilePanel = new MonsterDrawPilePanel(this);
        this.energyPanel = new MonsterEnergyPanel(this);
        this.soulGroup = new MonsterSoulGroup(this);
        this.initialized = true;

        if(AbstractDungeon.ascensionLevel >= 4) {
            this.handsize++;
        }
        if(AbstractDungeon.ascensionLevel >= 9) {
            this.baseenergy++;
        }
        if(AbstractDungeon.ascensionLevel >= 19) {
            this.handsize++;
        }
    }

    public void resetOrbPositions() {
        this.resetOrbPositions(this.hand.size());
    }
    public void resetOrbPositions(int count) {
        for(int i = 0; i < count; i++) {
            cards.add(new DummyMonster(0, 0, 0, 100, emptypixel, this));
        }
        for(int i = this.cards.size() - 1; i >= count; i--) {
            cards.remove(0);
        }

        AbstractOrb p = new DummyOrb();
        for(int i = 0; i < count; i++) {
            DummyMonster card = cards.get(i);
            p.setSlot(i, 3);
            card.drawX = p.tX - AbstractDungeon.player.drawX + this.drawX;
            card.drawY = p.tY - (AbstractDungeon.player.drawY + AbstractDungeon.player.hb_h / 2.0F) + this.drawY + this.hb_h;
            card.refresh();
        }
    }

    @Override
    public void takeTurn() {
        this.tempenergy = 0;
        if(stage == null || stage.isDead) {
            if (stage == null) {
                stage = new ShowmanStage(100, this);
                AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(stage, true));
                AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[0]));
            } else {
                stage.maxHealth += 20;
                AbstractDungeon.actionManager.addToBottom(new HealAction(stage, this, stage.maxHealth));
            }
        } else {
            if(this.intent != Intent.NONE) {
                this.upgraded = true;
                this.baseenergy++;
                this.handsize += 2;
                CardGroup[] groups = new CardGroup[]{
                        this.hand, this.drawpile, this.discardpile, this.exhaustpile
                };
                for(final CardGroup group : groups) {
                    for(final AbstractCard card : group.group) {
                        card.upgrade();
                    }
                }
                AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[2]));
            } else {
                int hasTrigger = -1;
                ArrayList<AbstractShowmanCard> toUse = new ArrayList<>();
                for (int i = this.hand.size() - 1; i >= 0; i--) {
                    AbstractCard card = this.hand.group.get(i);
                    if (card.isGlowing) {
                        toUse.add((AbstractShowmanCard) card);

                        if (((AbstractShowmanCard) card).exhaustTrigger) {
                            hasTrigger = toUse.size() - 1;
                        }
                    }
                }
                ArrayList<AbstractCard> residue = new ArrayList<>();
                residue.addAll(this.hand.group);
                residue.removeAll(toUse);

                if (toUse.size() >= this.cardUse && hasTrigger > -1 && this.cardUse - 1 != hasTrigger) {
                    AbstractShowmanCard tmp;
                    tmp = toUse.get(hasTrigger);
                    toUse.set(hasTrigger, toUse.get(this.cardUse - 1));
                    toUse.set(this.cardUse - 1, tmp);
                }
                for (int i = 0; i < toUse.size(); i++) {
                    AbstractShowmanCard card = toUse.get(i);
                    card.use(residue, AbstractDungeon.player, this);
                    if (card.purgeOnUse) {
                        this.hand.removeCard(card);
                    } else if (--this.cardUse == 0 || card.exhaust) {
                        if (this.cardUse == 0) {
                            this.cardUse = EXHAUSTRELIC;
                        }
                        AbstractDungeon.actionManager.addToBottom(new ExhaustShowmanCardAction(this, card));
                    } else {
                        AbstractDungeon.actionManager.addToBottom(new DiscardShowmanCardAction(this, card));
                    }
                    AbstractDungeon.actionManager.addToBottom(new WaitAction(2F));
                }
            }
        }

        this.turncounter++;
        this.drawnThisTurn = 0;
        AbstractDungeon.actionManager.addToBottom(new DiscardShowmanHandAction(this));
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    public void rollMove() {
        this.getMove(0);
    }
    @Override
    protected void getMove(int num) {
        if(!upgraded && (this.currentHealth < this.maxHealth / 2 || this.turncounter >= 10)) {
            this.discardHand();
            this.setMove(MOVES[0], (byte)0, Intent.BUFF);
        } else if(initialized) {
            if (!this.soulGroup.monsterIsActive()) {
                AbstractDungeon.actionManager.addToTop(new RollMoveAction(this));
                return;
            }
            if (stage != null && !stage.isDead) {
                for (; this.drawnThisTurn < this.handsize && !(this.drawpile.isEmpty() && this.discardpile.isEmpty()); this.drawnThisTurn++) {
                    if (this.drawpile.isEmpty() && !this.discardpile.isEmpty()) {
                        while (!this.discardpile.isEmpty()) {
                            this.soulGroup.shuffle(this.discardpile.getBottomCard(), false);
                        }
                        AbstractDungeon.actionManager.addToTop(new RollMoveAction(this));
                        return;
                    }
                    AbstractCard card = this.drawpile.getTopCard();
                    card.lighten(true);
                    this.hand.addToTop(card);
                    this.drawpile.removeTopCard();
                    card.current_x = this.drawPilePanel.privateHb.cX;
                    card.current_y = this.drawPilePanel.privateHb.cY;
                }
                this.curenergy = this.baseenergy + this.tempenergy;
                bestOne();
                for (int i = 0; i < this.hand.size(); i++) {
                    AbstractCard card = this.hand.group.get(i);
                    card.target_x = cards.get(i).drawX;
                    card.target_y = cards.get(i).drawY + cards.get(i).effect.y;
                }
            }
        } else {
            this.setMove((byte)0, Intent.NONE);
        }
    }

    public void exhaustCard(AbstractCard card) {
        this.hand.removeCard(card);
        this.exhaustpile.addToTop(card);
        card.isGlowing = false;
        card.triggerOnExhaust();
        AbstractDungeon.effectList.add(new ExhaustCardEffect(card));
    }
    public void exhaustHand() {
        while(!this.hand.isEmpty()) {
            this.exhaustCard(this.hand.getBottomCard());
        }
    }
    public void discardCard(AbstractCard card) {
        card.isGlowing = false;
        if(card.isEthereal) {
            this.exhaustCard(card);
        } else {
            this.soulGroup.discard(card);
        }
    }
    public void discardHand() {
        while(!this.hand.isEmpty()) {
            this.discardCard(this.hand.getTopCard());
        }
    }

    @Override
    public void update() {
        super.update();
        for(final AbstractCard card : this.hand.group) {
            card.update();
        }
        for(int i = 0; i < this.hand.size(); i++) {
            if(!((AbstractShowmanCard) this.hand.group.get(i)).hb.hovered) {
                this.cards.get(i).update();
            }
        }
        this.exhaustPanel.updatePositions();
        this.discardPilePanel.updatePositions();
        this.drawPilePanel.updatePositions();
        this.energyPanel.update();
        this.soulGroup.update();
    }

    @Override
    public void applyPowers() {
        for(final AbstractMonster mo : this.cards) {
            mo.applyPowers();
        }
        for(final AbstractCard card : this.hand.group) {
            card.applyPowers();
        }
        super.applyPowers();
    }

    @Override
    public void damage(DamageInfo info) {
        /*
        if ((info.type != DamageInfo.DamageType.THORNS) && (
                info.output > this.currentBlock)) {
            BraixenAnimation sa = (BraixenAnimation)this.animation;
            sa.damage();
        }*/
        super.damage(info);
    }

    @Override
    public void die() {
        AbstractDungeon.effectList.add(new SpeechBubble(this.hb.cX + this.dialogX, this.hb.cY + this.dialogY, 3F, DIALOG[MathUtils.random(DIALOG.length - 1)], this.isPlayer));
        this.useFastShakeAnimation(5.0F);
        CardCrawlGame.screenShake.rumble(4.0F);
        PaleOfTheAncients.resumeMainMusic();
        super.die();
    }

    @Override
    public void render(SpriteBatch sb) {
        super.render(sb);

        this.exhaustPanel.render(sb);
        this.drawPilePanel.render(sb);
        this.discardPilePanel.render(sb);
        this.energyPanel.render(sb);

        int i = 0;
        for(final AbstractCard card : this.hand.group) {
            if(card.isGlowing) {
                cards.get(i).render(sb);
            }

            card.render(sb);
            i++;
        }
    }

    private ArrayList<AbstractShowmanCard> bestOne() {
        for(final AbstractCard card : this.hand.group) {
            card.applyPowers();
            ((AbstractShowmanCard) card).isGlowing = false;
        }

        int byrdHits = 0;
        if(AbstractDungeon.player.hasPower(FlightPower.POWER_ID)) {
            byrdHits = AbstractDungeon.player.getPower(FlightPower.POWER_ID).amount;
        }

        ArrayList<AbstractShowmanCard> bestCombo = new ArrayList<>();
        ArrayList<AbstractShowmanCard> usedCards;
        int bestpriority = -1;
        for(final AbstractCard card : this.hand.group) {
            if(card.costForTurn > -2 && ((AbstractShowmanCard)card).getPriority(this.hand.group, this.baseenergy + this.tempenergy, byrdHits) > 0) {
                int[] priority = new int[]{0};
                usedCards = new ArrayList<>();
                usedCards = recur(usedCards, (AbstractShowmanCard) card, this.curenergy, priority, byrdHits);
                if (priority[0] > bestpriority) {
                    bestpriority = priority[0];
                    bestCombo = usedCards;
                }
            }
        }
        for(final AbstractShowmanCard card : bestCombo) {
            card.isGlowing = true;
        }
        this.resetOrbPositions();
        for(int i = 0; i < this.hand.size(); i++) {
            AbstractShowmanCard c = (AbstractShowmanCard) this.hand.group.get(i);
            if(c.isGlowing) {
                this.cards.get(i).setMove((byte)0, c.intent, c.damage, c.multiplier, c.multiplier > 0);
            } else {
                this.cards.get(i).setMove((byte)0, Intent.NONE);
            }
            this.cards.get(i).createIntent();
        }
        return bestCombo;
    }

    private ArrayList<AbstractShowmanCard> recur(ArrayList<AbstractShowmanCard> checked, AbstractShowmanCard currentCard, int energy, int[] priority, int byrdHits) {
        int[] tmppriority;
        boolean eligible;
        checked.add(currentCard);
        ArrayList<AbstractCard> exclusions = new ArrayList<>();
        exclusions.addAll(this.hand.group);
        exclusions.removeAll(checked);
        int bestPriority = priority[0] + currentCard.getPriority(exclusions, energy, byrdHits);
        if(currentCard.cost > -1) {
            energy -= currentCard.costForTurn;
        } else {
            energy = 0;
        }
        int basePriority = bestPriority;
        ArrayList<AbstractShowmanCard> bestCombo = checked;
        for(final AbstractCard card : this.hand.group) {
            if(card.costForTurn > -2 && energy - card.costForTurn >= 0 && ((AbstractShowmanCard)card).getPriority(exclusions, energy, byrdHits) > 0) {
                eligible = true;
                for (final AbstractShowmanCard alreadyIn : checked) {
                    if (alreadyIn == card) {
                        eligible = false;
                        break;
                    }
                }
                if (eligible) {
                    ArrayList<AbstractShowmanCard> tmp = new ArrayList<>();
                    tmp.addAll(checked);
                    if(card instanceof AbstractShowmanExhaustingCard) {
                        tmp.add((AbstractShowmanCard)((AbstractShowmanExhaustingCard)card).toExhaust);
                    }
                    tmppriority = new int[]{basePriority};
                    tmp = recur(tmp, (AbstractShowmanCard) card, energy, tmppriority, byrdHits);
                    if (basePriority + tmppriority[0] > bestPriority) {
                        bestCombo = tmp;
                        bestPriority = basePriority + tmppriority[0];
                    }
                }
            }
        }
        priority[0] = bestPriority;
        if(bestCombo.size() > 1) {
            Collections.shuffle(bestCombo, AbstractDungeon.monsterRng.random);
        }
        return bestCombo;
    }

    public void stageStun() {
        this.setMove((byte)0, Intent.STUN);
        this.createIntent();
        this.discardHand();
    }

    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
        NAME = monsterStrings.NAME;
        MOVES = monsterStrings.MOVES;
        DIALOG = monsterStrings.DIALOG;
    }
}