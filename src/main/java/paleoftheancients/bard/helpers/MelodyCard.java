package paleoftheancients.bard.helpers;

import paleoftheancients.PaleMod;
import paleoftheancients.bard.notes.AbstractNote;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;

import java.util.List;
import java.util.function.Consumer;

public class MelodyCard extends CustomCard {
    public static final String ID = PaleMod.makeID("MelodyCard");
    private static final int COST = -2;
    public List<AbstractNote> notes;
    public boolean consumeNotes;
    private Consumer<Boolean> playCallback;

    public MelodyCard(String name, String description, List<AbstractNote> notes, CardType type, int magicNumber) {
        this(name, new RegionName((String)null), description, notes, type, CardTarget.NONE, magicNumber, (Consumer)null);
    }

    public MelodyCard(String name, RegionName img, String description, List<AbstractNote> notes, CardTarget target, int magicNumber, Consumer<Boolean> playCallback) {
        this(name, img, description, notes, CardType.POWER, target, magicNumber, playCallback);
    }

    public MelodyCard(String name, RegionName img, String description, List<AbstractNote> notes, CardType type, CardTarget target, int magicNumber, Consumer<Boolean> playCallback) {
        super(ID, name, img, -2, description, type, CardColor.CURSE, CardRarity.SPECIAL, target);
        this.consumeNotes = true;
        this.notes = notes;
        this.playCallback = playCallback;
        this.baseMagicNumber = this.magicNumber = magicNumber;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        if (this.playCallback != null) {
            AbstractDungeon.effectList.add(new ShowCardBrieflyEffect(this));
            this.playCallback.accept(this.consumeNotes);
        }
    }

    public boolean canUpgrade() {
        return false;
    }

    public void upgrade() {
    }

    public AbstractCard makeCopy() {
        return new MelodyCard(this.name, new RegionName(this.assetUrl), this.rawDescription, this.notes, this.target, this.baseMagicNumber, this.playCallback);
    }
}
