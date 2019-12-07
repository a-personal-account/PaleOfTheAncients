package paleoftheancients.vfx.BlurryLens;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.ui.panels.TopPanel;

public class BlurryTopPanelScrewer extends BlurryLensVFX {
    private int floornum;
    private String title, name;
    private boolean redkey, bluekey, greenkey;

    public BlurryTopPanelScrewer() {
        floornum = AbstractDungeon.floorNum;
        title = (String)ReflectionHacks.getPrivate(AbstractDungeon.topPanel, TopPanel.class, "title");
        name = (String)ReflectionHacks.getPrivate(AbstractDungeon.topPanel, TopPanel.class, "name");
        redkey = Settings.hasRubyKey;
        bluekey = Settings.hasSapphireKey;
        greenkey = Settings.hasEmeraldKey;
    }

    @Override
    public void update() {
        AbstractDungeon.player.displayGold = MathUtils.random(AbstractDungeon.player.gold * 2);
        AbstractDungeon.floorNum = MathUtils.random(floornum * 2);

        ReflectionHacks.setPrivate(AbstractDungeon.topPanel, TopPanel.class, "title", this.shuffleString(title));
        ReflectionHacks.setPrivate(AbstractDungeon.topPanel, TopPanel.class, "name", this.shuffleString(name));

        Settings.hasRubyKey = MathUtils.randomBoolean();
        Settings.hasSapphireKey = MathUtils.randomBoolean();
        Settings.hasEmeraldKey = MathUtils.randomBoolean();
    }

    private String shuffleString(String ref) {
        StringBuilder builder = new StringBuilder();
        do {
            int rand = MathUtils.random(ref.length() - 1);
            builder.append(ref.charAt(rand));
            ref = ref.substring(0, rand) + ref.substring(rand + 1);
        } while(!ref.isEmpty());

        return builder.toString();
    }

    @Override
    public void render(SpriteBatch sb) {

    }

    @Override
    public void dispose() {
        AbstractDungeon.player.displayGold = AbstractDungeon.player.gold;
        AbstractDungeon.floorNum = floornum;
        ReflectionHacks.setPrivate(AbstractDungeon.topPanel, TopPanel.class, "title", title);
        ReflectionHacks.setPrivate(AbstractDungeon.topPanel, TopPanel.class, "name", name);
        Settings.hasRubyKey = redkey;
        Settings.hasSapphireKey = bluekey;
        Settings.hasEmeraldKey = greenkey;
    }
}
