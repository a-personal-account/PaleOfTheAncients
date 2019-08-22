package paleoftheancients.theshowman.misc;

import paleoftheancients.theshowman.monsters.TheShowmanBoss;
import basemod.ReflectionHacks;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.Soul;
import com.megacrit.cardcrawl.cards.SoulGroup;
import com.megacrit.cardcrawl.core.Settings;

import java.util.ArrayList;
import java.util.Iterator;

public class MonsterSoulGroup extends SoulGroup {
    private ArrayList<Soul> privateSouls;
    private TheShowmanBoss boss;

    public MonsterSoulGroup(TheShowmanBoss boss) {
        super();
        this.privateSouls = (ArrayList<Soul>) ReflectionHacks.getPrivate(this, SoulGroup.class, "souls");
        this.privateSouls.clear();

        this.boss = boss;
    }

    @Override
    public void discard(AbstractCard card, boolean visualOnly) {
        this.boss.hand.group.remove(card);

        boolean needMoreSouls = true;
        Iterator var4 = this.privateSouls.iterator();

        while(var4.hasNext()) {
            Soul s = (Soul)var4.next();
            if (s.isReadyForReuse) {
                card.untip();
                card.unhover();
                s.discard(card, visualOnly);
                needMoreSouls = false;
                break;
            }
        }

        if (needMoreSouls) {
            Soul s = new MonsterSoul(boss);
            s.discard(card, visualOnly);
            this.privateSouls.add(s);
        }

    }

    @Override
    public void discard(AbstractCard card) {
        this.discard(card, false);
    }

    @Override
    public void shuffle(AbstractCard card, boolean isInvisible) {
        this.boss.discardpile.group.remove(card);

        card.untip();
        card.unhover();
        card.darken(true);
        card.shrink(true);
        boolean needMoreSouls = true;
        Iterator var4 = this.privateSouls.iterator();

        while(var4.hasNext()) {
            Soul s = (Soul)var4.next();
            if (s.isReadyForReuse) {
                s.shuffle(card, isInvisible);
                needMoreSouls = false;
                break;
            }
        }

        if (needMoreSouls) {
            Soul s = new MonsterSoul(boss);
            s.shuffle(card, isInvisible);
            this.privateSouls.add(s);
        }

    }

    @Override
    public void onToBottomOfDeck(AbstractCard card) {
        boolean needMoreSouls = true;
        Iterator var3 = this.privateSouls.iterator();

        while(var3.hasNext()) {
            Soul s = (Soul)var3.next();
            if (s.isReadyForReuse) {
                card.untip();
                card.unhover();
                s.onToBottomOfDeck(card);
                needMoreSouls = false;
                break;
            }
        }

        if (needMoreSouls) {
            Soul s = new MonsterSoul(boss);
            s.onToBottomOfDeck(card);
            this.privateSouls.add(s);
        }

    }

    @Override
    public void onToDeck(AbstractCard card, boolean randomSpot, boolean visualOnly) {
        boolean needMoreSouls = true;
        Iterator var5 = this.privateSouls.iterator();

        while(var5.hasNext()) {
            Soul s = (Soul)var5.next();
            if (s.isReadyForReuse) {
                card.untip();
                card.unhover();
                s.onToDeck(card, randomSpot, visualOnly);
                needMoreSouls = false;
                break;
            }
        }

        if (needMoreSouls) {
            Soul s = new MonsterSoul(boss);
            s.onToDeck(card, randomSpot, visualOnly);
            this.privateSouls.add(s);
        }

    }

    @Override
    public void onToDeck(AbstractCard card, boolean randomSpot) {
        this.onToDeck(card, randomSpot, false);
    }


    public void shuffleNewlyCreatedCard(AbstractCard card) {
        card.current_x = MathUtils.random(Settings.WIDTH);
        card.current_y = MathUtils.random(Settings.HEIGHT);
        card.target_x = card.current_x;
        card.target_y = card.current_y;
        this.onToDeck(card, true, false);
    }


    public boolean monsterIsActive() {
        for(final Soul s : this.privateSouls) {
            if(!s.isReadyForReuse) {
                return false;
            }
        }

        return true;
    }
}
