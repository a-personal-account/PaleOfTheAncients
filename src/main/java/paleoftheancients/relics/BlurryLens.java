package paleoftheancients.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.SpeechBubble;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;
import paleoftheancients.PaleMod;
import paleoftheancients.helpers.AssetLoader;
import paleoftheancients.vfx.BlurryLens.*;

import java.util.ArrayList;

public class BlurryLens extends CustomRelic implements ClickableRelic {
    public static final String ID = PaleMod.makeID("BlurryLens");

    private static final RelicTier TIER = RelicTier.SPECIAL;
    private static final LandingSound SOUND = LandingSound.CLINK;

    private boolean active;

    public BlurryLens() {
        super(ID, AssetLoader.loadImage(PaleMod.assetPath("images/relics/blurrylens.png")), AssetLoader.loadImage(PaleMod.assetPath("images/relics/outline/blurrylens.png")), TIER, SOUND);
        active = false;
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + DESCRIPTIONS[1];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new BlurryLens();
    }

    @Override
    public void onRightClick() {
        if(!active) {
            active = true;
            if(AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
                ArrayList<AbstractCreature> list = new ArrayList();
                list.addAll(AbstractDungeon.getCurrRoom().monsters.monsters);
                list.add(AbstractDungeon.player);
                for (final AbstractCreature ac : list) {
                    int rand = MathUtils.random(7);
                    if (rand % 2 < 1) {
                        AbstractDungeon.effectList.add(new BlurryCharacterFlimmer(ac));
                    }
                    if (rand % 4 < 2) {
                        AbstractDungeon.effectList.add(new BlurryCharacterJump(ac));
                    }
                    if (rand % 8 < 4 && ac instanceof AbstractPlayer) {
                        AbstractDungeon.effectList.add(new BlurryCharacterSlide(ac));
                    }
                }
                AbstractDungeon.effectList.add(new SpeechBubble(AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY, "Aw, something went wrong... NL please let the devs know!", true));
            }
            for(int i = 0; i < AbstractDungeon.player.relics.size(); ++i) {
                if(AbstractDungeon.player.relics.get(i).relicId != ID) {
                    AbstractDungeon.effectList.add(new BlurryRelicfallVFX(AbstractDungeon.player.relics.get(i), i));
                }
            }
            AbstractDungeon.effectList.add(new BlurryDiscoFeverVFX());
            AbstractDungeon.effectList.add(new BlurryTopPanelScrewer());
        } else {
            active = false;
            for(int i = AbstractDungeon.effectList.size() - 1; i >= 0; i--) {
                AbstractGameEffect age = AbstractDungeon.effectList.get(i);
                if(age instanceof BlurryLensVFX) {
                    ((BlurryLensVFX) age).reset();
                    AbstractDungeon.effectList.remove(i);
                }
            }
        }
    }
}
