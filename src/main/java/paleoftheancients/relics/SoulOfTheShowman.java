package paleoftheancients.relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import paleoftheancients.PaleMod;

public class SoulOfTheShowman extends CustomRelic {
    public static final String ID = PaleMod.makeID("SoulOfTheShowman");

    private static final RelicTier TIER = RelicTier.SPECIAL;
    private static final LandingSound SOUND = LandingSound.MAGICAL;

    public SoulOfTheShowman() {
        super(ID, ImageMaster.loadImage(PaleMod.assetPath("images/relics/showman.png")), ImageMaster.loadImage(PaleMod.assetPath("images/relics/outline/showman.png")), TIER, SOUND);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new SoulOfTheShowman();
    }

    @Override
    public void onExhaust(AbstractCard card) {
        switch(card.type) {
            case CURSE:
            case STATUS:
                break;

            default:
                this.flash();
                AbstractCard tmp = card.makeSameInstanceOf();
                AbstractDungeon.player.limbo.addToBottom(tmp);
                tmp.current_x = card.current_x;
                tmp.current_y = card.current_y;
                tmp.target_x = (float) Settings.WIDTH / 2.0F - 300.0F * Settings.scale;
                tmp.target_y = (float)Settings.HEIGHT / 2.0F;
                if (tmp.cost > 0) {
                    tmp.freeToPlayOnce = true;
                }

                AbstractMonster m = AbstractDungeon.getCurrRoom().monsters.getRandomMonster(true);
                if (m != null) {
                    tmp.calculateCardDamage(m);
                }

                tmp.purgeOnUse = true;
                AbstractDungeon.actionManager.cardQueue.add(new CardQueueItem(tmp, m, card.energyOnUse, true));
                break;
        }
    }
}
