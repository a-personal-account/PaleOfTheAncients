package paleoftheancients.relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import paleoftheancients.PaleMod;
import paleoftheancients.helpers.AssetLoader;

public class SoulOfTheDefect extends CustomRelic {
    public static final String ID = PaleMod.makeID("SoulOfTheDefect");

    private static final RelicTier TIER = RelicTier.SPECIAL;
    private static final LandingSound SOUND = LandingSound.MAGICAL;

    public SoulOfTheDefect() {
        super(ID, AssetLoader.loadImage(PaleMod.assetPath("images/relics/defect.png")), AssetLoader.loadImage(PaleMod.assetPath("images/relics/outline/defect.png")), TIER, SOUND);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new SoulOfTheDefect();
    }

    public void onUseCard(AbstractCard card, UseCardAction action) {
        if(card.type == AbstractCard.CardType.POWER && this.counter > 0) {
            this.counter--;
            AbstractMonster m = null;
            if (action.target != null) {
                m = (AbstractMonster)action.target;
            }

            for(int i = 0; i < 2; i++) {

                AbstractCard tmp = card.makeSameInstanceOf();
                tmp.dontTriggerOnUseCard = true;
                AbstractDungeon.player.limbo.addToBottom(tmp);
                tmp.current_x = card.current_x;
                tmp.current_y = card.current_y;
                tmp.target_x = (float) Settings.WIDTH / 2.0F - 300.0F * Settings.scale;
                tmp.target_y = (float) Settings.HEIGHT / 2.0F;
                if (tmp.cost > 0) {
                    tmp.freeToPlayOnce = true;
                }

                if (m != null) {
                    tmp.calculateCardDamage(m);
                }

                tmp.purgeOnUse = true;
                AbstractDungeon.actionManager.cardQueue.add(new CardQueueItem(tmp, m, card.energyOnUse, true));
            }
        }
    }

    public void atBattleStart() {
        this.counter = 2;
    }

    public void onVictory() {
        this.counter = -1;
    }
}
