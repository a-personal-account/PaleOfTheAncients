package paleoftheancients.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;

@SpirePatch(clz = AbstractCard.class, method = SpirePatch.CLASS)
public class CollectorRelicField {
    public static SpireField<Boolean> isBottled = new SpireField<>(() -> false);

    @SpirePatch(clz = AbstractCard.class, method = "makeStatEquivalentCopy")
    public static class MakeStatEquivalentCopy {
        public static AbstractCard Postfix(AbstractCard result, AbstractCard self) {
            isBottled.set(result, isBottled.get(self));
            return result;
        }
    }

    @SpirePatch(clz = CardGroup.class, method = "getPurgeableCards")
    public static class UnPurgeable {
        public static CardGroup Postfix(CardGroup result, CardGroup __instance) {
            for(int i = result.group.size() - 1; i >= 0; i--) {
                if (isBottled.get(result.group.get(i))) {
                    result.group.remove(i);
                }
            }
            return result;
        }
    }
}
