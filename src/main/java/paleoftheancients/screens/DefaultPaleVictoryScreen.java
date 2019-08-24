package paleoftheancients.screens;

import basemod.ReflectionHacks;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.cutscenes.NeowEye;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

public class DefaultPaleVictoryScreen extends PaleVictoryScreen {
    @Override
    protected String watDo() {
        for(int i = 0; i < 3; i++) {
            int index;
            do {
                index = MathUtils.random(this.eyes.length - 1);
            } while (this.eyes[index].stopped);
            float x = (float) ReflectionHacks.getPrivate(eyes[index], NeowEye.class, index % 2 == 1 ? "leftX" : "rightX");
            float y = (float) ReflectionHacks.getPrivate(eyes[index], NeowEye.class, "y");
            AbstractDungeon.topLevelEffects.add(new FlashAtkImgEffect(x + 128F * Settings.scale, y + 128F * Settings.scale, attacks[MathUtils.random(attacks.length - 1)]));
            this.eyes[index].stop();
        }
        return charStrings.TEXT[this.currentDialog];
    }
}
