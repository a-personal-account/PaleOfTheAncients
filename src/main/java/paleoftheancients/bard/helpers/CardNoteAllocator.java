package paleoftheancients.bard.helpers;

import paleoftheancients.PaleMod;
import paleoftheancients.bard.notes.*;
import com.evacipated.cardcrawl.mod.bard.cards.AbstractBardCard;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import javassist.*;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;
import javassist.expr.NewExpr;

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
            List<AbstractNote> notes = new ArrayList();
            Iterator var2 = card.tags.iterator();

            while(var2.hasNext()) {
                AbstractCard.CardTags tag = (AbstractCard.CardTags)var2.next();
                AbstractNote note = MelodyManager.getNoteByTag(tag);
                if (note != null) {
                    notes.add(note);
                }
            }

            if (!notes.isEmpty()) {
                return notes;
            } else {
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


                if(notes.size() > 1) {
                    Collections.shuffle(notes, new Random(Settings.seed + AbstractDungeon.floorNum));
                }

                return notes;
            }
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

        Integer buffType = (Integer)otherCardBuffDebuff.get(card.cardID);
        return buffType != null && (buffType & 1) == 1;
    }

    public boolean isDebuffCard(AbstractCard card) {
        if (!otherCardBuffDebuff.containsKey(card.cardID)) {
            calculateBuffDebuff(card);
        }

        Integer buffType = (Integer)otherCardBuffDebuff.get(card.cardID);
        return buffType != null && (buffType & 2) == 2;
    }

    private void calculateBuffDebuff(final AbstractCard card) {
        try {
            ClassPool pool = Loader.getClassPool();
            CtClass ctClass = pool.get(card.getClass().getName());
            ctClass.defrost();

            CtMethod useMethod;
            try {
                useMethod = ctClass.getDeclaredMethod("use");
            } catch (NotFoundException var9) {
                otherCardBuffDebuff.putIfAbsent(card.cardID, 0);
                return;
            }

            final AbstractCard.CardTarget[] targetType = new AbstractCard.CardTarget[]{AbstractCard.CardTarget.NONE};
            CtConstructor[] var5 = ctClass.getConstructors();
            int var6 = var5.length;

            for(int var7 = 0; var7 < var6; ++var7) {
                CtConstructor ctor = var5[var7];
                if (ctor.callsSuper()) {
                    ctor.instrument(new ExprEditor() {
                        public void edit(FieldAccess f) {
                            if (f.getClassName().equals(AbstractCard.CardTarget.class.getName())) {
                                targetType[0] = AbstractCard.CardTarget.valueOf(f.getFieldName());
                            }

                        }
                    });
                }
            }

            useMethod.instrument(new ExprEditor() {
                public void edit(NewExpr e) {
                    try {
                        CtConstructor ctConstructor = e.getConstructor();
                        CtClass cls = ctConstructor.getDeclaringClass();
                        if (cls != null) {
                            CtClass parent = cls;

                            do {
                                parent = parent.getSuperclass();
                            } while(parent != null && !parent.getName().equals(AbstractPower.class.getName()));

                            if (parent != null && parent.getName().equals(AbstractPower.class.getName())) {
                                final int[] buffType = new int[]{0};
                                ExprEditor buffDebuffFinder = new ExprEditor() {
                                    public void edit(FieldAccess f) {
                                        if (f.getClassName().equals(AbstractPower.PowerType.class.getName())) {
                                            String var2 = f.getFieldName();
                                            byte var3 = -1;
                                            switch(var2.hashCode()) {
                                                case 2050131:
                                                    if (var2.equals("BUFF")) {
                                                        var3 = 0;
                                                    }
                                                    break;
                                                case 2012555348:
                                                    if (var2.equals("DEBUFF")) {
                                                        var3 = 1;
                                                    }
                                            }

                                            int[] var10000;
                                            switch(var3) {
                                                case 0:
                                                    var10000 = buffType;
                                                    var10000[0] |= 1;
                                                    break;
                                                case 1:
                                                    var10000 = buffType;
                                                    var10000[0] |= 2;
                                                    break;
                                                default:
                                                    System.out.println("idk what we found ??");
                                            }
                                        }

                                    }
                                };
                                ctConstructor.instrument(buffDebuffFinder);
                                CtMethod updateDescription = cls.getDeclaredMethod("updateDescription");
                                updateDescription.instrument(buffDebuffFinder);
                                if (buffType[0] == 0) {
                                    buffType[0] = 1;
                                }

                                if ((buffType[0] & 1) == 1 && (buffType[0] & 2) == 2) {
                                    switch(targetType[0]) {
                                        case SELF:
                                            buffType[0] = 1;
                                            break;
                                        case ENEMY:
                                        case ALL_ENEMY:
                                            buffType[0] = 2;
                                    }
                                }

                                otherCardBuffDebuff.compute(card.cardID, (k, v) -> {
                                    return (v == null ? 0 : v) | buffType[0];
                                });
                            }
                        }
                    } catch (CannotCompileException | NotFoundException var8) {
                    }

                }
            });
        } catch (CannotCompileException | NotFoundException var10) {
            var10.printStackTrace();
        }

        otherCardBuffDebuff.putIfAbsent(card.cardID, 0);
    }
}
