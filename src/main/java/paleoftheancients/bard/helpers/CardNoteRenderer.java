package paleoftheancients.bard.helpers;

import paleoftheancients.PaleMod;
import paleoftheancients.bard.notes.AbstractNote;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

import java.util.*;

public class CardNoteRenderer extends AbstractGameEffect {

    private Texture bagpipes;
    private Map<UUID, AllTheCardNeeds> carddata;

    public CardNoteRenderer() {
        carddata = new HashMap<>();

        bagpipes = ImageMaster.loadImage(PaleMod.assetPath("images/bard/cardui/card_royal_bagpipes.png"));
    }

    @Override
    public void update() {
        CardGroup[] cgs = new CardGroup[] {
                AbstractDungeon.player.hand,
                AbstractDungeon.player.drawPile,
                AbstractDungeon.player.discardPile,
                AbstractDungeon.player.exhaustPile,
                AbstractDungeon.player.limbo
        };
        for (final CardGroup cg : cgs) {
            for (final AbstractCard card : cg.group) {
                if (!carddata.containsKey(card.uuid)) {
                    carddata.put(card.uuid, new AllTheCardNeeds(card));
                }
            }
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        if(!AbstractDungeon.player.isDead) {
            for (final AbstractCard __instance : AbstractDungeon.player.hand.group) {
                //AbstractCard __instance = data.card;
                //List<AbstractNote> notes = data.notes;
                if (!carddata.containsKey(__instance.uuid)) {
                    continue;
                }
                List<AbstractNote> notes = carddata.get(__instance.uuid).notes;

                sb.draw(bagpipes, __instance.current_x - 256.0F, __instance.current_y - 256.0F, 256.0F, 256.0F, 512.0F, 768.0F, __instance.drawScale * Settings.scale, __instance.drawScale * Settings.scale, __instance.angle, 0, 0, 512, 768, false, false);
                Color oldColor = sb.getColor();
                float offsetX = -(32.0F * (float) notes.size() / 2.0F);

                for (Iterator var7 = notes.iterator(); var7.hasNext(); offsetX += 32.0F) {
                    AbstractNote note = (AbstractNote) var7.next();
                    Vector2 offset = new Vector2(offsetX, 212.0F);
                    offset.rotate(__instance.angle);
                    offset.scl(__instance.drawScale * Settings.scale);
                    sb.setColor(note.color());
                    sb.draw(note.getTexture(), __instance.current_x + offset.x, __instance.current_y + offset.y, 0.0F, 0.0F, (float) note.getTexture().originalWidth, (float) note.getTexture().originalHeight, __instance.drawScale * Settings.scale * 1.1F, __instance.drawScale * Settings.scale * 1.1F, __instance.angle);
                }

                sb.setColor(oldColor);
            }
        }
    }

    @Override
    public void dispose() {
        bagpipes.dispose();
        carddata.clear();
    }

    class AllTheCardNeeds {
        AbstractCard card;
        List<AbstractNote> notes;

        public AllTheCardNeeds(AbstractCard card) {
            this.card = card;
            notes = CardNoteAllocator.getNotes(card);
        }

        private float calcx(AbstractCard card) {
            return card.current_x + card.hb.width / 2 * (float)Math.cos(Math.toRadians(card.angle)) - card.hb.height / 2 * (float)Math.sin(Math.toRadians(card.angle));
        }
        private float calcy(AbstractCard card) {
            return card.current_y + card.hb.width / 2 * (float)Math.sin(Math.toRadians(card.angle)) + card.hb.height / 2 * (float)Math.cos(Math.toRadians(card.angle));
        }
    }
}
