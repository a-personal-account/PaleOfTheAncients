package paleoftheancients.bard.helpers;

import com.evacipated.cardcrawl.mod.bard.cards.AbstractBardCard;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import javassist.*;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;
import javassist.expr.NewExpr;
import paleoftheancients.PaleMod;
import paleoftheancients.bard.notes.*;

import java.util.*;

public class CardNoteAllocator {
    private Map<UUID, List<AbstractNote>> carddata;
    private Map<String, Integer> otherCardBuffDebuff;
    private static CardNoteAllocator cna = null;

    public CardNoteAllocator() {
        otherCardBuffDebuff = new HashMap<>();
        carddata = new HashMap<>();
        for(final AbstractCard card : AbstractDungeon.player.drawPile.group) {
            carddata.put(card.uuid, determineNoteTypes(card));
        }
    }

    public static void initialize() {
        cna = new CardNoteAllocator();
    }
    public static void dispose() {
        cna.carddata.clear();
    }


    public static List<AbstractNote> getNotes(AbstractCard card) {
        if(cna == null) {
            initialize();
        }
        if(!cna.carddata.containsKey(card.uuid)) {
            cna.carddata.put(card.uuid, cna.determineNoteTypes(card));
        }
        return cna.carddata.get(card.uuid);
    }



    public List<AbstractNote> determineNoteTypes(AbstractCard card) {
        if (PaleMod.bardLoaded()) {
            List<AbstractNote> notes = new ArrayList<>();
            List<com.evacipated.cardcrawl.mod.bard.notes.AbstractNote> bardnotes;
            if(card instanceof AbstractBardCard && !((AbstractBardCard) card).getNotes().isEmpty()) {
                bardnotes = ((AbstractBardCard) card).getNotes();
            } else {
                bardnotes = AbstractBardCard.determineNoteTypes(card);
            }
            for(final com.evacipated.cardcrawl.mod.bard.notes.AbstractNote note : bardnotes) {
                AbstractNote an = null;
                switch(note.getClass().getSimpleName()) {
                    case "AttackNote":
                        an = AttackNote.get();
                        break;
                    case "BlockNote":
                        an = BlockNote.get();
                        break;
                    case "BuffNote":
                        an = BuffNote.get();
                        break;
                    case "DebuffNote":
                        an = DebuffNote.get();
                        break;
                    case "WildCardNote":
                        an = WildCardNote.get();
                        break;
                    case "RestNote":
                        an = RestNote.get();
                        break;

                    default:
                        throw new RuntimeException("Invalid Note: " + note.getClass().getName());
                }
                notes.add(an);
            }
            return notes;
        } else {
            List<AbstractNote> notes = new ArrayList<>();

            // Other mods can define notes for a card via CardTags
            for (AbstractCard.CardTags tag : card.tags) {
                AbstractNote note = MelodyManager.getNoteByTag(tag);
                if (note != null) {
                    notes.add(note);
                }
            }
            if (!notes.isEmpty()) {
                return notes;
            }

            // Otherwise, automatically assign values
            if (isBlockGainingCard(card)) {
                notes.add(BlockNote.get());
            }
            if (isDamageDealingCard(card)) {
                notes.add(AttackNote.get());
            }
            if (isBuffCard(card)) {
                notes.add(BuffNote.get());
            }
            if (isDebuffCard(card)) {
                notes.add(DebuffNote.get());
            }
            return notes;
        }
    }

    public boolean isDamageDealingCard(AbstractCard card) {
        return card.baseDamage >= 0;
    }

    public boolean isBlockGainingCard(AbstractCard card) {
        return card.baseBlock >= 0;
    }

    public boolean isBuffCard(AbstractCard card) {
        if (!otherCardBuffDebuff.containsKey(card.cardID)) {
            calculateBuffDebuff(card);
        }

        Integer buffType = otherCardBuffDebuff.get(card.cardID);
        if (buffType != null) {
            if ((buffType & 1) == 1) {
                return true;
            }
        }

        return false;
    }

    public boolean isDebuffCard(AbstractCard card) {
        if (!otherCardBuffDebuff.containsKey(card.cardID)) {
            calculateBuffDebuff(card);
        }

        Integer buffType = otherCardBuffDebuff.get(card.cardID);
        if (buffType != null) {
            if ((buffType & 2) == 2) {
                return true;
            }
        }

        return false;
    }

    private void calculateBuffDebuff(final AbstractCard card) {
        try {
            ClassPool pool = Loader.getClassPool();
            CtClass ctClass = pool.get(card.getClass().getName());
            ctClass.defrost();
            CtMethod useMethod;
            try {
                useMethod = ctClass.getDeclaredMethod("use");
            } catch (NotFoundException ignore) {
                // This card doesn't have a `use` method, skip it
                // I blame Infinite Spire
                otherCardBuffDebuff.putIfAbsent(card.cardID, 0);
                return;
            }

            final AbstractCard.CardTarget[] targetType = {AbstractCard.CardTarget.NONE};
            for (CtConstructor ctor : ctClass.getConstructors()) {
                if (ctor.callsSuper()) {
                    ctor.instrument(new ExprEditor()
                    {
                        @Override
                        public void edit(FieldAccess f)
                        {
                            if (f.getClassName().equals(AbstractCard.CardTarget.class.getName())) {
                                targetType[0] = AbstractCard.CardTarget.valueOf(f.getFieldName());
                            }
                        }
                    });
                }
            }

            useMethod.instrument(new ExprEditor() {
                @Override
                public void edit(NewExpr e)
                {
                    try {
                        CtConstructor ctConstructor = e.getConstructor();
                        CtClass cls = ctConstructor.getDeclaringClass();
                        if (cls != null) {
                            CtClass parent = cls;
                            do {
                                parent = parent.getSuperclass();
                            } while (parent != null && !parent.getName().equals(AbstractPower.class.getName()));
                            if (parent != null && parent.getName().equals(AbstractPower.class.getName())) {
                                // found a power
                                final int[] buffType = {0};
                                ExprEditor buffDebuffFinder = new ExprEditor() {
                                    @Override
                                    public void edit(FieldAccess f)
                                    {
                                        if (f.getClassName().equals(AbstractPower.PowerType.class.getName())) {
                                            switch (f.getFieldName()) {
                                                case "BUFF":
                                                    buffType[0] |= 1;
                                                    break;
                                                case "DEBUFF":
                                                    buffType[0] |= 2;
                                                    break;
                                                default:
                                                    System.out.println("idk what we found ??");
                                                    break;
                                            }
                                        }
                                    }
                                };
                                ctConstructor.instrument(buffDebuffFinder);
                                // Because StrengthPower sets its BUFF/DEBUFF status in updateDescription
                                // Others probably do too
                                CtMethod updateDescription = cls.getDeclaredMethod("updateDescription");
                                updateDescription.instrument(buffDebuffFinder);

                                if (buffType[0] == 0) {
                                    // powers default to being a buff
                                    buffType[0] = 1;
                                }
                                if ((buffType[0] & 1) == 1 && (buffType[0] & 2) == 2) {
                                    // Both buff and debuff, guess at which based on CardTarget
                                    switch (targetType[0]) {
                                        case SELF:
                                            buffType[0] = 1;
                                            break;
                                        case ENEMY:
                                        case ALL_ENEMY:
                                            buffType[0] = 2;
                                            break;
                                    }
                                }
                                otherCardBuffDebuff.compute(card.cardID, (k,v) -> (v == null ? 0 : v) | buffType[0]);
                            }
                        }
                    } catch (NotFoundException | CannotCompileException ignored) {
                    }
                }
            });
        } catch (NotFoundException | CannotCompileException e) {
            e.printStackTrace();
        }
        otherCardBuffDebuff.putIfAbsent(card.cardID, 0);
    }
}
