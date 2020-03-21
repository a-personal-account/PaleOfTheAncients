package paleoftheancients.bandit.board.spaces.symmetrical;

import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.vfx.CollectorStakeEffect;
import paleoftheancients.PaleMod;
import paleoftheancients.bandit.board.AbstractBoard;
import paleoftheancients.bandit.board.spaces.AbstractSpace;
import paleoftheancients.helpers.AssetLoader;

public class DebilitateSpace extends AbstractSpace {
    private static String[] TEXT = CardCrawlGame.languagePack.getUIString(PaleMod.makeID("DebilitateSpace")).TEXT;

    public DebilitateSpace(AbstractBoard board, int x, int y) {
        super(board, x, y);
        this.tex = AssetLoader.loadImage(PaleMod.assetPath("images/bandit/spaces/WeakSquare" + board.artStyle + ".png"));
        this.goodness = GOODNESS.GOOD;
    }

    @Override
    public void onLanded(AbstractCreature actor) {
        AbstractCreature target = symmetricTarget(actor);
        att(new ApplyPowerAction(target, actor, new VulnerablePower(target, 1, target == AbstractDungeon.player), 1));
        att(new ApplyPowerAction(target, actor, new WeakPower(target, 1, target == AbstractDungeon.player), 1));
        att(new ApplyPowerAction(target, actor, new FrailPower(target, 1, target == AbstractDungeon.player), 1));
    }

    @Override
    public void playVFX(AbstractCreature actor) {
        AbstractCreature target = symmetricTarget(actor);
        for(int i = 0; i < 3; i++) {
            att(new VFXAction(new CollectorStakeEffect(target.hb.cX, target.hb.cY), 0F));
        }
    }

    public String getHeaderText() {
        return TEXT[0];
    }
    public String getBodyText() {
        return TEXT[1] + 1 + TEXT[2];
    }
}