package paleoftheancients.helpers;

import paleoftheancients.bard.helpers.AssetLoader;
import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.esotericsoftware.spine.*;
import com.evacipated.cardcrawl.modthespire.lib.SpireOverride;
import com.megacrit.cardcrawl.characters.AnimatedNpc;
import com.megacrit.cardcrawl.core.Settings;

public class PreloadNpc extends AnimatedNpc {
    public PreloadNpc(float x, float y, String atlasUrl, String skeletonUrl, String trackName) {
        super(x, y, atlasUrl, skeletonUrl, trackName);
    }

    @SpireOverride
    protected void loadAnimation(String atlasUrl, String skeletonUrl, float scale) {
        TextureAtlas atlas = AssetLoader.loader.loadAtlas(atlasUrl);
        ReflectionHacks.setPrivate(this, AnimatedNpc.class, "atlas", atlas);
        SkeletonJson json = new SkeletonJson(atlas);
        json.setScale(Settings.scale / scale);
        SkeletonData skeletonData = json.readSkeletonData(Gdx.files.internal(skeletonUrl));
        this.skeleton = new Skeleton(skeletonData);
        this.skeleton.setColor(Color.WHITE);
        AnimationStateData stateData = new AnimationStateData(skeletonData);
        ReflectionHacks.setPrivate(this, AnimatedNpc.class, "stateData", stateData);
        ReflectionHacks.setPrivate(this, AnimatedNpc.class, "state", new AnimationState(stateData));
    }

    @Override
    public void dispose() {

    }
}
