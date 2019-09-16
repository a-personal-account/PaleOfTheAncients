package paleoftheancients.thorton.monsters;

import basemod.ReflectionHacks;
import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.ui.panels.TopPanel;
import com.megacrit.cardcrawl.vfx.RainingGoldEffect;
import paleoftheancients.PaleMod;
import paleoftheancients.relics.SoulOfTheThorton;
import paleoftheancients.thorton.powers.DemotionPower;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Thorton extends CustomMonster {
    public static final String ID = PaleMod.makeID("Thorton");
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;

    private Map<Byte, EnemyMoveInfo> moves;
    private static final byte CRASHINGSTOCKS = 0;
    private static final byte CASHBUFFER = 1;
    private static final byte CASHBASH = 2;
    private static final byte COINSHIELD = 3;
    private static final byte COINCLASH = 4;
    private static final byte INVESTMENT = 5;
    private static final byte CREDITCARD = 6;
    private static final byte ROI = 7;
    private static final byte PENNYTOSS = 8;
    private static final byte FORTUNE = 9;
    private static final byte DEMOTION = 10;
    private static final byte ESCAPE = 11;

    private int gold;
    private int displaygold;
    private Hitbox goldHb;
    private float GOLD_NUM_OFFSET_X;

    private int crashingstocks_cost = 100;
    private int goldMultiplier, startingMultiplier = 3;
    private float hbOffsetX;
    private boolean demotionUsed = false;

    public Thorton() {
        super(NAME, ID, 120, 0.0F, -20.0F, 260.0F, 360.0F, PaleMod.assetPath("images/thorton/main.png"), 0.0F, 20.0F);

        this.moves = new HashMap<>();
        this.moves.put(CRASHINGSTOCKS, new EnemyMoveInfo(CRASHINGSTOCKS, Intent.ATTACK, 100, 0, false));
        this.moves.put(CASHBUFFER, new EnemyMoveInfo(CASHBUFFER, Intent.DEFEND_BUFF, 0, 0, false));
        this.moves.put(CASHBASH, new EnemyMoveInfo(CASHBASH, Intent.ATTACK_BUFF, 12, 0, false));
        this.moves.put(COINSHIELD, new EnemyMoveInfo(COINSHIELD, Intent.DEFEND_BUFF, -1, 0, false));
        this.moves.put(COINCLASH, new EnemyMoveInfo(COINCLASH, Intent.ATTACK_BUFF, 16, 0, false));
        this.moves.put(INVESTMENT, new EnemyMoveInfo(INVESTMENT, Intent.BUFF, -1, 0, false));
        this.moves.put(CREDITCARD, new EnemyMoveInfo(CREDITCARD, Intent.BUFF, -1, 0, false));
        this.moves.put(ROI, new EnemyMoveInfo(ROI, Intent.BUFF, -1, 0, false));
        this.moves.put(FORTUNE, new EnemyMoveInfo(FORTUNE, Intent.BUFF, -1, 0, false));
        this.moves.put(DEMOTION, new EnemyMoveInfo(DEMOTION, Intent.STRONG_DEBUFF, -1, 0, false));
        this.moves.put(PENNYTOSS, new EnemyMoveInfo(PENNYTOSS, Intent.ATTACK_DEFEND, 9, 0, false));
        this.moves.put(ESCAPE, new EnemyMoveInfo(ESCAPE, Intent.ESCAPE, -1, 0, false));

        this.flipHorizontal = true;

        this.gold = AbstractDungeon.monsterRng.random(1, 10);
        this.displaygold = this.gold;

        float GOLD_TIP_W = (float)ReflectionHacks.getPrivateStatic(TopPanel.class, "GOLD_TIP_W");
        float ICON_W = (float)ReflectionHacks.getPrivateStatic(TopPanel.class, "ICON_W");
        this.GOLD_NUM_OFFSET_X = (float)ReflectionHacks.getPrivateStatic(TopPanel.class, "GOLD_NUM_OFFSET_X");
        this.goldHb = new Hitbox(GOLD_TIP_W, ICON_W);
        this.goldHb.move(this.drawX + this.hb.width * 1 / 5, this.drawY + this.hb.height * 7 / 25);
        this.hbOffsetX = this.goldHb.width / 2;

        if(AbstractDungeon.ascensionLevel >= 19) {
            this.goldMultiplier = this.startingMultiplier + 3;
        } else if(AbstractDungeon.ascensionLevel >= 9) {
            this.goldMultiplier = this.startingMultiplier + 2;
        } else if(AbstractDungeon.ascensionLevel >= 4) {
            this.goldMultiplier = this.startingMultiplier + 1;
        } else {
            this.goldMultiplier = this.startingMultiplier;
        }
    }

    @Override
    public void usePreBattleAction() {
        for(int i = AbstractDungeon.getCurrRoom().rewards.size() - 1; i >= 0; i--) {
            if(AbstractDungeon.getCurrRoom().rewards.get(i).type == RewardItem.RewardType.GOLD) {
                AbstractDungeon.getCurrRoom().rewards.remove(i);
            }
        }
        if(!AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
            AbstractDungeon.getCurrRoom().rewards.add(new RewardItem(RelicLibrary.getRelic(SoulOfTheThorton.ID).makeCopy()));
        }
    }

    @Override
    public void takeTurn() {
        DamageInfo info = new DamageInfo(this, moves.get(this.nextMove).baseDamage, DamageInfo.DamageType.NORMAL);
        if(info.base > -1) {
            info.applyPowers(this, AbstractDungeon.player);
        }
        switch(this.nextMove) {
            case DEMOTION:
                this.demotionUsed = true;
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new DemotionPower(AbstractDungeon.player)));
                break;

            case CRASHINGSTOCKS:
                this.gold -= crashingstocks_cost;
                this.useFastAttackAnimation();
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                break;

            case CASHBUFFER:
                this.increaseGold(12);
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this, 10));
                break;
            case COINSHIELD:
                this.increaseGold(16);
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this, 16));
                break;

            case CASHBASH:
                this.increaseGold(6);
                this.useFastAttackAnimation();
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                break;
            case COINCLASH:
                this.increaseGold(8);
                this.useFastAttackAnimation();
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                break;

            case PENNYTOSS:
                this.increaseGold(9);
                this.useFastAttackAnimation();
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.FIRE));
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this, 9));
                break;

            case CREDITCARD:
                this.increaseGold(8);
                break;
            case ROI:
                this.increaseGold(20);
                break;
            case INVESTMENT:
                this.increaseGold(15);
                break;
            case FORTUNE:
                this.increaseGold(30);
                break;

            case ESCAPE:
                for(int i = AbstractDungeon.getCurrRoom().rewards.size() - 1; i >= 0; i--) {
                    if(AbstractDungeon.getCurrRoom().rewards.get(i).type != RewardItem.RewardType.CARD) {
                        AbstractDungeon.getCurrRoom().rewards.remove(i);
                    }
                }
                AbstractDungeon.actionManager.addToBottom(new EscapeAction(this));
                return;
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(int i) {
        if(!this.demotionUsed) {
            this.setMoveShortcut(DEMOTION);
        } else if(this.gold >= crashingstocks_cost) {
            this.setMoveShortcut(CRASHINGSTOCKS);
        } else if(this.lastMove(CRASHINGSTOCKS)) {
            this.setMoveShortcut(ESCAPE);
        } else {
            ArrayList<Byte> possibilities = new ArrayList<>();

            possibilities.add(CASHBUFFER);
            possibilities.add(CASHBASH);
            possibilities.add(CASHBASH);
            possibilities.add(COINSHIELD);
            possibilities.add(COINCLASH);
            possibilities.add(COINCLASH);
            possibilities.add(INVESTMENT);
            possibilities.add(CREDITCARD);
            possibilities.add(ROI);
            possibilities.add(PENNYTOSS);
            possibilities.add(FORTUNE);

            this.setMoveShortcut(possibilities.get(AbstractDungeon.monsterRng.random(possibilities.size() - 1)));
        }
    }
    @Override
    public void rollMove() {
        this.getMove(0);
    }

    private void setMoveShortcut(byte next) {
        EnemyMoveInfo info = this.moves.get(next);
        this.setMove(MOVES[next], next, info.intent, info.baseDamage, info.multiplier, info.isMultiDamage);
    }

    private void increaseGold(int gold) {
        gold *= this.goldMultiplier / this.startingMultiplier;
        this.gold += gold;
        AbstractDungeon.effectList.add(new RainingGoldEffect(gold));
    }

    @Override
    public void die() {
        if(this.gold > 0) {
            RewardItem ri = new RewardItem();
            ri.type = RewardItem.RewardType.GOLD;
            ri.incrementGold(this.gold);
            AbstractDungeon.getCurrRoom().rewards.add(ri);
        }

        super.die();
    }

    @Override
    public void update() {
        super.update();
        this.goldHb.update();
        this.updateGold();
    }

    @Override
    public void render(SpriteBatch sb) {
        super.render(sb);
        this.renderGold(sb);
    }

    private void updateGold() {
        if (gold < displayGold) {
            if (displayGold - gold > 99) {
                displayGold -= 10;
            } else if (displayGold - gold > 9) {
                displayGold -= 3;
            } else {
                --displayGold;
            }
        } else if (gold > displayGold) {
            if (gold - displayGold > 99) {
                displayGold += 10;
            } else if (gold - displayGold > 9) {
                displayGold += 3;
            } else {
                ++displayGold;
            }
        }
    }
    private void renderGold(SpriteBatch sb) {
        sb.setColor(Color.WHITE);
        float scale = Settings.scale;
        if (this.goldHb.hovered) {
            scale *= 1.2F;
        }
        sb.draw(ImageMaster.TP_GOLD, this.goldHb.cX - this.hbOffsetX + this.animX, this.goldHb.y + this.animY, 32.0F, 32.0F, 64.0F, 64.0F, scale, scale, 0.0F, 0, 0, 64, 64, false, false);

        Color c;
        if (AbstractDungeon.player.displayGold == AbstractDungeon.player.gold) {
            c = Settings.GOLD_COLOR;
        } else if (AbstractDungeon.player.displayGold > AbstractDungeon.player.gold) {
            c = Settings.RED_TEXT_COLOR;
        } else {
            c = Settings.GREEN_TEXT_COLOR;
        }
        FontHelper.renderFontLeftTopAligned(sb, FontHelper.topPanelInfoFont, Integer.toString(displayGold), this.goldHb.cX + GOLD_NUM_OFFSET_X - this.hbOffsetX + this.animX, this.goldHb.y + 40 * Settings.scale + this.animY, c);

        this.goldHb.render(sb);
    }

    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
        NAME = monsterStrings.NAME;
        MOVES = monsterStrings.MOVES;
        DIALOG = monsterStrings.DIALOG;
    }
}
