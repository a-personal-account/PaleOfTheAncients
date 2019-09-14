package paleoftheancients.theshowman.monsters;

import basemod.ReflectionHacks;
import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.actions.common.SuicideAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.FlightPower;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.vfx.SpeechBubble;
import com.megacrit.cardcrawl.vfx.cardManip.ExhaustCardEffect;
import paleoftheancients.PaleMod;
import paleoftheancients.dungeons.PaleOfTheAncients;
import paleoftheancients.relics.SoulOfTheShowman;
import paleoftheancients.theshowman.actions.DiscardShowmanCardAction;
import paleoftheancients.theshowman.actions.DiscardShowmanHandAction;
import paleoftheancients.theshowman.actions.ExhaustShowmanCardAction;
import paleoftheancients.theshowman.bosscards.*;
import paleoftheancients.theshowman.helpers.Cards;
import paleoftheancients.theshowman.helpers.Numbers;
import paleoftheancients.theshowman.helpers.ShowmanAnimation;
import paleoftheancients.theshowman.misc.DummyOrb;
import paleoftheancients.theshowman.misc.MonsterSoulGroup;
import paleoftheancients.theshowman.powers.OnDiscardHandPower;
import paleoftheancients.theshowman.ui.MonsterDiscardPilePanel;
import paleoftheancients.theshowman.ui.MonsterDrawPilePanel;
import paleoftheancients.theshowman.ui.MonsterEnergyPanel;
import paleoftheancients.theshowman.ui.MonsterExhaustPanel;

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

    public ShowmanStage stage;
    public int baseenergy;
    public int curenergy;
    public int tempenergy;

    private Texture emptypixel;

    private int drawnThisTurn = 0;
    public int handsize = 3;
    private static final int EXHAUSTRELIC = 3;
    private int cardUse;

    public boolean upgraded;
    private int turncounter;

    public TheShowmanBoss() {
        super(NAME, ID, 700, 0.0F, -15.0F, 300.0F, 300.0F, PaleMod.assetPath("images/misc/emptypixel.png"), 0.0F, 0.0F);

        this.animation = new ShowmanAnimation(PaleMod.assetPath("images/TheShowman/character/Spriter.scml"));

        this.dialogX = (-30.0F * Settings.scale);
        this.dialogY = (100.0F * Settings.scale);

        this.type = EnemyType.BOSS;

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


        cards = new ArrayList<>();
        emptypixel = ImageMaster.loadImage(PaleMod.assetPath("images/misc/emptypixel.png"));

        this.exhaustPanel = new MonsterExhaustPanel(this);
        this.discardPilePanel = new MonsterDiscardPilePanel(this);
        this.drawPilePanel = new MonsterDrawPilePanel(this);
        this.energyPanel = new MonsterEnergyPanel(this);
        this.soulGroup = new MonsterSoulGroup(this);
        this.initialized = true;
        this.hand.addToBottom(new SpeechCard(this));
        this.bestOne();
        this.refreshHandLayout();

        if(AbstractDungeon.ascensionLevel >= 4) {
            this.handsize++;
        }
        if(AbstractDungeon.ascensionLevel >= 9) {
            this.baseenergy++;
        }
        if(AbstractDungeon.ascensionLevel >= 19) {
            this.handsize++;
        }
        AbstractDungeon.getCurrRoom().rewards.add(new RewardItem(RelicLibrary.getRelic(SoulOfTheShowman.ID).makeCopy()));
    }

    public void resetOrbPositions() {
        this.resetOrbPositions(this.hand.size());
    }
    public void resetOrbPositions(int count) {
        for(int i = 0; i < count; i++) {
            DummyMonster mo = new DummyMonster(0, 0, 0, 100, emptypixel);
            cards.add(mo);
            mo.setMove((byte)0, Intent.NONE, -1, -1, false);
        }
        for(int i = this.cards.size() - 1; i >= count; i--) {
            cards.remove(0);
        }

        AbstractOrb p = new DummyOrb();
        for(int i = 0; i < count; i++) {
            DummyMonster card = cards.get(i);
            p.setSlot(i, this.hand.size());
            card.drawX = p.tX - AbstractDungeon.player.drawX + this.drawX;
            card.drawY = p.tY - (AbstractDungeon.player.drawY + AbstractDungeon.player.hb_h / 2.0F) + this.drawY + this.hb_h;
            card.refresh();
        }
    }

    @Override
    public void takeTurn() {
        this.tempenergy = 0;

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
        boolean useAttackAnimation = false;
        for (int i = 0; i < toUse.size(); i++) {
            AbstractShowmanCard card = toUse.get(i);
            useAttackAnimation |= card.baseDamage > 0;
            card.use(residue, AbstractDungeon.player, this);
            this.hand.removeCard(card);
            if(card.purgeOnUse) {
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
        if(useAttackAnimation) {
            ((ShowmanAnimation) this.animation).attack();
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
        if(initialized) {
            if (!this.soulGroup.monsterIsActive()) {
                AbstractDungeon.actionManager.addToTop(new RollMoveAction(this));
                return;
            }

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
            if(this.stage != null && !this.stage.isDying && !upgraded && (this.currentHealth < this.maxHealth * 2 / 3 || this.turncounter >= 10)) {
                AbstractCard secondact = new SecondAct(this);
                this.hand.addToBottom(secondact);
                this.upgraded = true;
                secondact.current_x = this.drawPilePanel.privateHb.cX;
                secondact.current_y = this.drawPilePanel.privateHb.cY;
            }
            bestOne();
            this.refreshHandLayout();
        } else {
            this.setMove((byte)0, Intent.NONE);
        }
    }

    public void refreshHandLayout() {
        for (int i = 0; i < this.hand.size(); i++) {
            AbstractCard card = this.hand.group.get(i);
            card.target_x = cards.get(i).drawX;
            card.target_y = cards.get(i).drawY + cards.get(i).effect.y;
        }
    }

    public void exhaustCard(AbstractCard card) {
        this.hand.removeCard(card);
        this.exhaustpile.addToTop(card);
        card.isGlowing = false;
        card.costForTurn = card.cost;
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
        card.costForTurn = card.cost;
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

        for(final AbstractPower p : this.powers) {
            if(p instanceof OnDiscardHandPower) {
                ((OnDiscardHandPower) p).onDiscardHand();
            }
        }
    }

    @Override
    public void update() {
        super.update();
        for(final AbstractCard card : this.hand.group) {
            card.update();
        }
        for(int i = 0; i < this.hand.size() && i < this.cards.size(); i++) {
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
        for(int i = 0; i < this.hand.size(); i++) {
            AbstractCard card = this.hand.group.get(i);
            AbstractMonster mo = this.cards.get(i);
            mo.applyPowers();
            card.applyPowers();
            if(card.damage != mo.getIntentDmg()) {
                ReflectionHacks.setPrivate(mo, AbstractMonster.class, "intentDmg", card.damage);
            }
        }
        super.applyPowers();
    }

    @Override
    public void damage(DamageInfo info) {
        if ((info.type != DamageInfo.DamageType.THORNS) && (
                info.output > this.currentBlock)) {
            ((ShowmanAnimation) this.animation).damage();
        }
        super.damage(info);
    }

    @Override
    public void die() {
        AbstractDungeon.effectList.add(new SpeechBubble(this.hb.cX + this.dialogX, this.hb.cY + this.dialogY, 3F, DIALOG[MathUtils.random(7, 9)], this.isPlayer));
        this.useFastShakeAnimation(5.0F);
        CardCrawlGame.screenShake.rumble(4.0F);
        PaleOfTheAncients.resumeMainMusic();
        super.die();
        for(final AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
            AbstractDungeon.actionManager.addToBottom(new SuicideAction(mo));
        }
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

    @Override
    public void dispose() {
        Cards.dispose();
        Numbers.dispose();
    }

    private ArrayList<AbstractShowmanCard> bestOne() {
        this.curenergy = this.baseenergy + this.tempenergy;
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
        int curcardprio;
        for(final AbstractCard card : this.hand.group) {
            if(card.costForTurn > -2 && (curcardprio = ((AbstractShowmanCard)card).getPriority(this.hand.group, this.baseenergy + this.tempenergy, byrdHits)) > 0) {
                int[] priority = new int[]{curcardprio};

                ArrayList<AbstractShowmanCard> exhausted = new ArrayList<>();
                if(card instanceof AbstractShowmanExhaustingCard) {
                    exhausted.add((AbstractShowmanCard)((AbstractShowmanExhaustingCard) card).toExhaust);
                }

                usedCards = new ArrayList<>();
                usedCards = recur(usedCards, exhausted, (AbstractShowmanCard) card, this.curenergy, priority, byrdHits);
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
        boolean attacking = false;
        for(int i = 0; i < this.hand.size(); i++) {
            AbstractShowmanCard c = (AbstractShowmanCard) this.hand.group.get(i);
            if(c.isGlowing) {
                this.cards.get(i).setMove((byte)0, c.intent, c.damage, c.multiplier, c.multiplier > 0);
                if(c.baseDamage > 0) {
                    attacking = true;
                }
            } else {
                this.cards.get(i).setMove((byte)0, Intent.NONE);
            }
            this.cards.get(i).createIntent();
        }
        this.setMove((byte)0, Intent.NONE, attacking ? 1 : -1);
        this.createIntent();
        return bestCombo;
    }

    private ArrayList<AbstractShowmanCard> recur(ArrayList<AbstractShowmanCard> checked, ArrayList<AbstractShowmanCard> exhausted, AbstractShowmanCard currentCard, int energy, int[] priority, int byrdHits) {
        int[] tmppriority;
        checked.add(currentCard);
        ArrayList<AbstractCard> exhaustible = new ArrayList<>();
        exhaustible.addAll(this.hand.group);
        exhaustible.removeAll(checked);
        exhaustible.removeAll(exhausted);
        if(currentCard.cost > -1) {
            energy -= currentCard.costForTurn;
        } else {
            energy = 0;
        }
        int bestPriority = priority[0];
        int basePriority = bestPriority;
        ArrayList<AbstractShowmanCard> bestCombo = checked;
        int curcardprio;
        for(final AbstractCard card : exhaustible) {
            if(card.costForTurn > -2 && energy - card.costForTurn >= 0 && (curcardprio = ((AbstractShowmanCard)card).getPriority(exhaustible, energy, byrdHits)) > 0) {
                tmppriority = new int[]{basePriority + curcardprio};

                ArrayList<AbstractShowmanCard> tmpexhaust = exhausted;
                if(card instanceof AbstractShowmanExhaustingCard) {
                    tmpexhaust = new ArrayList<>();
                    tmpexhaust.addAll(exhausted);
                    tmpexhaust.add((AbstractShowmanCard)((AbstractShowmanExhaustingCard)card).toExhaust);
                }

                ArrayList<AbstractShowmanCard> tmp = new ArrayList<>();
                tmp.addAll(checked);
                tmp = recur(tmp, tmpexhaust, (AbstractShowmanCard) card, energy, tmppriority, byrdHits);
                if (basePriority + tmppriority[0] > bestPriority) {
                    bestCombo = tmp;
                    bestPriority = basePriority + tmppriority[0];
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
        SetTheStage sts = new SetTheStage(this, this.hand, this.drawpile, this.discardpile);
        sts.current_x = this.drawPilePanel.current_x;
        sts.current_y = this.drawPilePanel.current_y;

        this.hand.clear();
        this.drawpile.clear();
        this.discardpile.clear();
        this.hand.addToBottom(sts);

        this.bestOne();
        this.refreshHandLayout();

        AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[MathUtils.random(3)]));
    }

    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
        NAME = monsterStrings.NAME;
        MOVES = monsterStrings.MOVES;
        DIALOG = monsterStrings.DIALOG;
    }


    public static class Enums {
        @SpireEnum
        public static AbstractPlayer.PlayerClass PALE_THE_SHOWMAN;
        @SpireEnum(
                name = "PALE_SHOWMAN_PURPLE_COLOR"
        )
        public static AbstractCard.CardColor PALE_COLOR_PURPLE;
        @SpireEnum(
                name = "PALE_SHOWMAN_PURPLE_COLOR"
        )
        public static CardLibrary.LibraryType PALE_LIBRARY_COLOR;

        public Enums() {
        }
    }
}