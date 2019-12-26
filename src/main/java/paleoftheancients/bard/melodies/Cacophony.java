package paleoftheancients.bard.melodies;

import com.megacrit.cardcrawl.powers.BlurPower;
import paleoftheancients.PaleMod;
import paleoftheancients.bard.powers.StunPlayerPower;
import basemod.abstracts.CustomCard;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.ShockWaveEffect;

public class Cacophony extends AbstractMelody {
    public static final String ID = PaleMod.makeID("Cacophony");

    private static final String doot = CardCrawlGame.languagePack.getCardStrings(PaleMod.makeID("Doot")).NAME;

    public Cacophony() {
        super(ID, AbstractCard.CardTarget.SELF);
        this.type = AbstractCard.CardType.SKILL;
    }

    protected CustomCard.RegionName getRegionName() {
        return new CustomCard.RegionName("status/dazed");
    }

    public void play(AbstractCreature source) {
        this.addToBottom(new TalkAction(source, "@" + doot + "@", 1f, 2f));
        this.addToBottom(new VFXAction(source, new ShockWaveEffect(source.hb.cX, source.hb.cY, Color.WHITE.cpy(), ShockWaveEffect.ShockWaveType.NORMAL), Settings.FAST_MODE ? 0.3f : 1.5f));
        this.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new StunPlayerPower()));
        this.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new BlurPower(AbstractDungeon.player, 1)));
    }

    public AbstractMelody makeCopy() {
        return new Cacophony();
    }
}
