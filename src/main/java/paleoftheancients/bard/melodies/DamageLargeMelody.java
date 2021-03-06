package paleoftheancients.bard.melodies;

import paleoftheancients.PaleMod;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class DamageLargeMelody extends AbstractMelody {
    public static final String ID = PaleMod.makeID("DamageLarge");

    public DamageLargeMelody() {
        super(ID, AbstractCard.CardTarget.SELF);
        this.type = AbstractCard.CardType.ATTACK;
        this.baseMagicNumber = this.magicNumber = AbstractMelody.m(18);
    }

    protected CustomCard.RegionName getRegionName() {
        return new CustomCard.RegionName("red/attack/immolate");
    }

    public void play(AbstractCreature source) {
        this.addToBottom(new DamageAction(AbstractDungeon.player, new DamageInfo(source, this.magicNumber, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
    }

    public AbstractMelody makeCopy() {
        return new DamageLargeMelody();
    }
}
