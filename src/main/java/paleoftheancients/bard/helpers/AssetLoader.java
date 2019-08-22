package paleoftheancients.bard.helpers;

import paleoftheancients.PaleMod;
import basemod.ReflectionHacks;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class AssetLoader {
    private AssetManager assets = new AssetManager();

    public static AssetLoader loader = new AssetLoader();

    public AssetLoader() {
    }

    public Texture loadImage(String fileName) {
        if (!this.assets.isLoaded(fileName, Texture.class)) {
            TextureLoader.TextureParameter param = new TextureLoader.TextureParameter();
            param.minFilter = Texture.TextureFilter.Linear;
            param.magFilter = Texture.TextureFilter.Linear;
            this.assets.load(fileName, Texture.class, param);

            try {
                this.assets.finishLoadingAsset(fileName);
            } catch (GdxRuntimeException var4) {
                return null;
            }
        }

        return (Texture)this.assets.get(fileName, Texture.class);
    }

    public TextureAtlas loadAtlas(String fileName) {
        if (!this.assets.isLoaded(fileName, TextureAtlas.class)) {
            this.assets.load(fileName, TextureAtlas.class);
            this.assets.finishLoadingAsset(fileName);
        }

        return this.assets.get(fileName, TextureAtlas.class);
    }
    public static boolean isLoaded(String fileName, Class type) {
        return loader.assets.isLoaded(fileName, type);
    }
    public static boolean isLoading(String fileName) {
        for(final AssetDescriptor assetDesc : (Array<AssetDescriptor>) ReflectionHacks.getPrivate(loader.assets, AssetManager.class, "loadQueue")) {
            if (assetDesc.fileName.equals(fileName)) {
                return true;
            }
        }
        return false;
    }
    public void loadAtlasAsync(String fileName) {
        if (!this.assets.isLoaded(fileName, TextureAtlas.class)) {
            this.assets.load(fileName, TextureAtlas.class);
        }
    }
    public static void unLoad(String fileName) {
        loader.assets.unload(fileName);
    }

    public static void preloadAtlas(String filename) {
        if(!isLoading(filename) && !isLoaded(filename, TextureAtlas.class)) {
            loader.loadAtlasAsync(filename);
        }
    }

    public static TextureAtlas NoteAtlas() {
        return loader.loadAtlas(PaleMod.assetPath("images/bard/notes/notes.atlas"));
    }
}
