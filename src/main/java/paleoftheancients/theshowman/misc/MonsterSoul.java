package paleoftheancients.theshowman.misc;

import paleoftheancients.theshowman.monsters.TheShowmanBoss;
import basemod.ReflectionHacks;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.Soul;
import com.megacrit.cardcrawl.core.Settings;

import java.lang.reflect.Method;

public class MonsterSoul extends Soul {
    private Method setSharedVariables;
    private final float START_VELOCITY;
    private TheShowmanBoss boss;

    public MonsterSoul(TheShowmanBoss boss) {
        super();

        this.boss = boss;

        try {
            this.setSharedVariables = Soul.class.getDeclaredMethod("setSharedVariables");
            this.setSharedVariables.setAccessible(true);
        } catch(NoSuchMethodException ex) {
            ex.printStackTrace();
        }
        START_VELOCITY = (float) ReflectionHacks.getPrivateStatic(Soul.class, "START_VELOCITY") / 4;
    }

    @Override
    public void discard(AbstractCard card, boolean visualOnly) {
        this.card = card;
        this.group = this.boss.discardpile;
        if (!visualOnly) {
            this.group.addToTop(card);
        }

        ReflectionHacks.setPrivate(this, Soul.class, "pos", new Vector2(card.current_x, card.current_y));
        ReflectionHacks.setPrivate(this, Soul.class, "target", new Vector2(this.boss.discardPilePanel.privateHb.cX, this.boss.discardPilePanel.privateHb.cY));
        this.invokeSharedVariables();
        ReflectionHacks.setPrivate(this, Soul.class, "rotation", card.angle + 270.0F);
        ReflectionHacks.setPrivate(this, Soul.class, "rotateClockwise", false);
        if (Settings.FAST_MODE) {
            ReflectionHacks.setPrivate(this, Soul.class, "currentSpeed", START_VELOCITY * MathUtils.random(4.0F, 6.0F));
        } else {
            ReflectionHacks.setPrivate(this, Soul.class, "currentSpeed", START_VELOCITY * MathUtils.random(1.0F, 4.0F));
        }

    }

    @Override
    public void discard(AbstractCard card) {
        this.discard(card, false);
    }

    @Override
    public void shuffle(AbstractCard card, boolean isInvisible) {
        ReflectionHacks.setPrivate(this, Soul.class, "isInvisible", isInvisible);
        this.card = card;
        this.group = this.boss.drawpile;
        this.group.addToTop(card);
        ReflectionHacks.setPrivate(this, Soul.class, "pos", new Vector2(this.boss.discardPilePanel.privateHb.cX, this.boss.discardPilePanel.privateHb.cY));
        ReflectionHacks.setPrivate(this, Soul.class, "target", new Vector2(this.boss.drawPilePanel.privateHb.cX, this.boss.drawPilePanel.privateHb.cY));
        this.invokeSharedVariables();
        ReflectionHacks.setPrivate(this, Soul.class, "rotation", MathUtils.random(260.0F, 310.0F));
        if (Settings.FAST_MODE) {
            ReflectionHacks.setPrivate(this, Soul.class, "currentSpeed", START_VELOCITY * MathUtils.random(8.0F, 12.0F));
        } else {
            ReflectionHacks.setPrivate(this, Soul.class, "currentSpeed", START_VELOCITY * MathUtils.random(2.0F, 5.0F));
        }

        ReflectionHacks.setPrivate(this, Soul.class, "rotateClockwise", true);
        ReflectionHacks.setPrivate(this, Soul.class, "spawnStutterTimer", MathUtils.random(0.0F, 0.12F));
    }

    @Override
    public void onToDeck(AbstractCard card, boolean randomSpot, boolean visualOnly) {
        this.card = card;
        this.group = this.boss.drawpile;
        if (!visualOnly) {
            if (randomSpot) {
                this.group.addToRandomSpot(card);
            } else {
                this.group.addToTop(card);
            }
        }

        ReflectionHacks.setPrivate(this, Soul.class, "pos", new Vector2(card.current_x, card.current_y));
        ReflectionHacks.setPrivate(this, Soul.class, "target", new Vector2(this.boss.drawPilePanel.privateHb.cX, this.boss.drawPilePanel.privateHb.cY));
        this.invokeSharedVariables();
        ReflectionHacks.setPrivate(this, Soul.class, "rotation", card.angle + 270.0F);
        ReflectionHacks.setPrivate(this, Soul.class, "rotateClockwise", true);
    }

    @Override
    public void onToDeck(AbstractCard card, boolean randomSpot) {
        this.onToDeck(card, randomSpot, false);
    }

    @Override
    public void onToBottomOfDeck(AbstractCard card) {
        this.card = card;
        this.group = this.boss.drawpile;
        this.group.addToBottom(card);
        ReflectionHacks.setPrivate(this, Soul.class, "pos", new Vector2(card.current_x, card.current_y));
        ReflectionHacks.setPrivate(this, Soul.class, "target", new Vector2(this.boss.drawPilePanel.privateHb.cX, this.boss.drawPilePanel.privateHb.cY));
        this.invokeSharedVariables();
        ReflectionHacks.setPrivate(this, Soul.class, "rotation", card.angle + 270.0F);
        ReflectionHacks.setPrivate(this, Soul.class, "rotateClockwise", true);
    }


    private void invokeSharedVariables() {
        try {
            setSharedVariables.invoke(this);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
}
