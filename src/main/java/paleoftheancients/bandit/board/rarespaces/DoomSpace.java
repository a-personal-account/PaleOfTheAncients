package paleoftheancients.bandit.board.rarespaces;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import paleoftheancients.PaleMod;
import paleoftheancients.bandit.board.AbstractBoard;
import paleoftheancients.bandit.board.spaces.AbstractSpace;
import paleoftheancients.finarubossu.vfx.DamageCurvy;
import paleoftheancients.finarubossu.vfx.DamageLine;
import paleoftheancients.helpers.AssetLoader;

public class DoomSpace extends AbstractSpace {
    private static String[] TEXT = CardCrawlGame.languagePack.getUIString(PaleMod.makeID("DoomSpace")).TEXT;
    public static int DAMAGE = 500;

    public DoomSpace(AbstractBoard board, int x, int y) {
        super(board, x, y);
        this.tex = AssetLoader.loadImage(PaleMod.assetPath("images/bandit/spaces/DeathSquare" + board.artStyle + ".png"));
        this.goodness = GOODNESS.REALLYBAD;
        this.explodeOnUse = true;
    }

    public void onLanded(AbstractCreature actor) {
        att(new DamageAction(actor, new DamageInfo(actor, DAMAGE, DamageInfo.DamageType.THORNS)));
    }

    @Override
    public void playVFX(AbstractCreature actor) {
        splat();
        splat();
        splat();
        splat();
        splat();
    }

    public void splat() {
        for (int i = 0; i < this.maxLines; i++) {
            AbstractDungeon.effectList.add(new DamageLine(this.hb.cX, this.hb.cY, Color.BLACK.cpy(), ((this.stride * i + MathUtils.random(-this.stride, this.stride)) + this.offset)));
            if (i % 2 == 0)
                AbstractDungeon.effectList.add(new DamageCurvy(this.hb.cX, this.hb.cY, Color.BLACK.cpy()));
        }
    }

    public String getHeaderText() {
        return TEXT[0];
    }
    public String getBodyText() {
        return TEXT[1] + DAMAGE + TEXT[2] + BASETEXT[2];
    }

    @Override
    public int getSelfDamageNumber(AbstractCreature actor) {
        return DAMAGE;
    }
}