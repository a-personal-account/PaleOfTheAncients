package paleoftheancients.savefields;

import basemod.BaseMod;
import basemod.abstracts.CustomSavable;
import basemod.interfaces.PostInitializeSubscriber;
import basemod.interfaces.StartActSubscriber;
import com.megacrit.cardcrawl.dungeons.*;

import java.util.HashMap;
import java.util.Map;

public class BreadCrumbs implements CustomSavable<Map<Integer, String>>,
        PostInitializeSubscriber,
        StartActSubscriber {

    private Map<Integer, String> breadCrumbs = new HashMap<>();
    private static BreadCrumbs bc;
    public static Map<Integer, String> getBreadCrumbs() {
        return bc.breadCrumbs;
    }

    public static void initialize() {
        BaseMod.subscribe(bc = new BreadCrumbs());
    }

    @Override
    public void receivePostInitialize() {
        BaseMod.addSaveField("ActLikeIt:breadCrumbs", bc);

        ElitesSlain.initialize(); //Doing this means I don't have to subscribe ElitesSlain to BaseMod.
    }

    @Override
    public void receiveStartAct() {
        if(AbstractDungeon.id != null) {
            if (AbstractDungeon.actNum <= 1) {
                BaseMod.logger.info("breadcrumbs cleared!");
                breadCrumbs.clear();
            }
            switch (AbstractDungeon.id) {
                case Exordium.ID:
                case TheCity.ID:
                case TheBeyond.ID:
                case TheEnding.ID:
                    break;

                default:
                    //Add the ID of the dungeon into the map, keyd by the actnumber. This is for the score display at the end of the run.
                    BaseMod.logger.info("Adding to breadcrumbs: " + AbstractDungeon.id + " (" + AbstractDungeon.actNum + ")");
                    breadCrumbs.put(AbstractDungeon.actNum, AbstractDungeon.id);
                    break;
            }
        }
    }


    @Override
    public Map<Integer, String> onSave() {
        BaseMod.logger.info("Saving breadcrumbs Map with size: " + breadCrumbs.size());
        return breadCrumbs;
    }

    @Override
    public void onLoad(Map<Integer, String> loaded) {
        if (loaded != null) {
            breadCrumbs = loaded;
        } else {
            breadCrumbs = new HashMap<>();
        }
        BaseMod.logger.info("Loading breadcrumbs Map with size: " + breadCrumbs.size());
    }
}
