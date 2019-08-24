package paleoftheancients.theshowman.bosscards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import paleoftheancients.PaleMod;
import paleoftheancients.theshowman.monsters.TheShowmanBoss;

import java.util.ArrayList;

public abstract class AbstractShowmanCard extends CustomCard {
    private static final float SMALL = 0.3F;
    private static final float BIG = 1F;
    private float baseWidth;
    private float baseHeight;

    public boolean selected;
    public AbstractCard exhaustsCard;
    public int multiplier;
    protected int exhaustPriority;
    public AbstractCard toExhaust;
    public AbstractMonster.Intent intent;
    protected TheShowmanBoss owner;
    public boolean exhaustTrigger;

    public AbstractShowmanCard(String id, String name, String img, int cost, String rawDescription, CardType type, CardRarity rarity, CardTarget target, TheShowmanBoss owner, AbstractMonster.Intent intent) {
        super(id, name, img, cost, rawDescription, type, TheShowmanBoss.Enums.PALE_COLOR_PURPLE, rarity, target);
        this.init(owner, intent);
    }

    public AbstractShowmanCard(String id, String name, RegionName img, int cost, String rawDescription, CardType type, CardRarity rarity, CardTarget target, TheShowmanBoss owner, AbstractMonster.Intent intent) {
        super(id, name, img, cost, rawDescription, type, TheShowmanBoss.Enums.PALE_COLOR_PURPLE, rarity, target);
        this.init(owner, intent);
    }

    protected static String assetPath(String path) {
        return PaleMod.assetPath("images/TheShowman/" + path);
    }

    private void init(TheShowmanBoss owner, AbstractMonster.Intent intent) {
        this.drawScale = SMALL;
        this.baseHeight = this.hb.height * this.drawScale;
        this.baseWidth = this.hb.width * this.drawScale;
        this.owner = owner;
        this.multiplier = -1;
        this.intent = intent;
        this.exhaustTrigger = false;
    }

    @Override
    public void update() {
        this.current_x = MathHelper.cardLerpSnap(this.current_x, this.target_x);
        this.current_y = MathHelper.cardLerpSnap(this.current_y, this.target_y);
        this.hb.move(this.current_x, this.current_y + this.baseHeight / 2);
        this.hb.update();
        if(this.hb.justHovered) {
            this.hb.resize(this.baseWidth / SMALL, this.baseHeight / SMALL);
            this.targetDrawScale = BIG;
        } else if(!this.hb.hovered) {
            this.targetDrawScale = SMALL;
        }
        if(this.drawScale != this.targetDrawScale) {
            this.drawScale = MathHelper.cardScaleLerpSnap(this.drawScale, this.targetDrawScale);
            if(Math.abs(this.drawScale - this.targetDrawScale) < 0.05F) {
                //this.drawScale = this.targetDrawScale;
            }
        }
    }


    public void use(AbstractPlayer p, AbstractMonster m) {
        this.use(this.owner.hand.group, p, m);
    }
    public void use(ArrayList<AbstractCard> availableExhaustCards, AbstractPlayer p, AbstractMonster m) {
        this.applyPowers();
        if(this.baseDamage > 0) {
            int iterations = (this.multiplier > -1 ? this.multiplier : 1);
            for(int i = 0; i < iterations; i++) {
                AbstractDungeon.actionManager.addToBottom(new DamageAction(p, new DamageInfo(m, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.NONE));
            }
        }
        if(this.baseBlock > 0) {
            AbstractDungeon.actionManager.addToBottom(new GainBlockAction(m, m, this.block));
        }
    }

    @Override
    public void applyPowers() {
        DamageInfo info = new DamageInfo(this.owner, this.baseDamage, this.damageTypeForTurn);
        info.applyPowers(this.owner, AbstractDungeon.player);
        this.damage = info.output;
        if(this.baseDamage != this.damage) {
            this.isDamageModified = true;
        }
        this.block = this.baseBlock;
        if(this.baseBlock != this.block) {
            this.isBlockModified = true;
        }
        this.exhaustPriority = (this.damage + this.block) / 2;
        if(this.multiplier > 1) {
            this.exhaustPriority *= this.multiplier;
        }
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        this.applyPowers();
    }

    @Override
    public void upgrade() {
        if(!this.upgraded) {
            this.upgradeName();
            if(this.baseDamage > 0) {
                this.upgradeDamage(this.baseDamage / 3);
            }
            if(this.baseBlock > 0) {
                this.upgradeBlock(this.baseBlock / 3);
            }
            if(this.baseMagicNumber > 0) {
                this.upgradeMagicNumber(1);
            }
        }
    }

    @Override
    public boolean hasEnoughEnergy() {
        return this.selected;
    }
    @Override
    public boolean cardPlayable(AbstractMonster m) {
        return true;
    }

    public int getPriority(ArrayList<AbstractCard> availableExhaustCards, int availableEnergy, int byrdHits) {
        int priority = 0;
        if(this.baseBlock > 0) {
            priority += this.baseBlock;
        }
        if(this.baseDamage > 0) {
            priority += this.damage * (this.multiplier > -1 ? this.multiplier : 1) * ((byrdHits > 0 && byrdHits <= this.multiplier) ? 5 : 1);
        }

        return priority;
    }

    public int getExhaustPriority() {
        return this.exhaustPriority;
    }
}
