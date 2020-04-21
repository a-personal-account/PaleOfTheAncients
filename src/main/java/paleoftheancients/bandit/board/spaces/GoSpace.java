package paleoftheancients.bandit.board.spaces;

import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.vfx.combat.SweepingBeamEffect;
import paleoftheancients.PaleMod;
import paleoftheancients.bandit.board.AbstractBoard;
import paleoftheancients.helpers.AssetLoader;

public abstract class GoSpace extends AbstractSpace {
    protected static String[] TEXT = CardCrawlGame.languagePack.getUIString(PaleMod.makeID("GoSpace")).TEXT;
    public static int BASEDAMAGE = 25;

    public GoSpace(AbstractBoard board, int x, int y) {
        super(board, x, y);
        this.tex = AssetLoader.loadImage(PaleMod.assetPath("images/bandit/spaces/GoSquare" + board.artStyle + ".png"));
        this.goodness = AbstractSpace.GOODNESS.GOOD;
        this.triggersWhenPassed = true;
    }

    @Override
    public void playVFX(AbstractCreature actor) {
        att(new VFXAction(actor, new SweepingBeamEffect(actor.hb.cX, actor.hb.cY, actor.flipHorizontal), 0.4F));
        att(new SFXAction("ATTACK_DEFECT_BEAM"));
    }

    public String getHeaderText() {
        return TEXT[0];
    }

    @Override
    public int getDamageNumber(AbstractCreature actor) {
        return actor instanceof AbstractPlayer ? 0 : BASEDAMAGE;
    }
    @Override
    public int getSelfDamageNumber(AbstractCreature actor) {
        return actor instanceof AbstractPlayer ? BASEDAMAGE : 0;
    }
}