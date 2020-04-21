package paleoftheancients.bandit.board.spaces.symmetrical;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.WhirlwindEffect;
import paleoftheancients.PaleMod;
import paleoftheancients.bandit.board.AbstractBoard;
import paleoftheancients.bandit.board.BanditBoard;
import paleoftheancients.bandit.board.spaces.AbstractSpace;
import paleoftheancients.helpers.AssetLoader;

public class DamageSpace extends AbstractSpace {
    private static String[] TEXT = CardCrawlGame.languagePack.getUIString(PaleMod.makeID("DamageSpace")).TEXT;
    public static int BASEDAMAGE = 10;

    public DamageSpace(AbstractBoard board, int x, int y) {
        super(board, x, y);
        this.tex = AssetLoader.loadImage(PaleMod.assetPath("images/bandit/spaces/DamageSquare" + board.artStyle + ".png"));
        this.goodness = GOODNESS.GOOD;
    }

    @Override
    public void onLanded(AbstractCreature actor) {
        if(board instanceof BanditBoard && actor == ((BanditBoard) board).owner) {
            att(new DamageAction(AbstractDungeon.player, new DamageInfo(((BanditBoard) board).owner, BASEDAMAGE, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.FIRE));
        } else {
            att(new DamageAllEnemiesAction(AbstractDungeon.player, DamageInfo.createDamageMatrix(BASEDAMAGE, true), DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.FIRE));
        }
    }

    @Override
    public void playVFX(AbstractCreature actor) {
        att(new VFXAction(new WhirlwindEffect(), 0.0F));
        att(new SFXAction("ATTACK_WHIRLWIND"));
    }

    public String getHeaderText() {
        return TEXT[0];
    }
    public String getBodyText() {
        return TEXT[1] + BASEDAMAGE + TEXT[2];
    }

    @Override
    public int getDamageNumber(AbstractCreature actor) {
        return BASEDAMAGE;
    }
}