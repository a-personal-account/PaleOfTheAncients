package paleoftheancients.patches;

import basemod.ReflectionHacks;
import basemod.patches.com.megacrit.cardcrawl.screens.mainMenu.ColorTabBar.ColorTabBarFix;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.screens.mainMenu.ColorTabBar;
import com.megacrit.cardcrawl.screens.mainMenu.TabBarListener;
import paleoftheancients.theshowman.monsters.TheShowmanBoss;

import java.util.ArrayList;

@SpirePatch(
        clz = ColorTabBarFix.Ctor.class,
        method = "Postfix"
)
public class RemoveCompendiumColorPatch {
    public static void Postfix(ColorTabBar __instance, TabBarListener delegate) {
        ArrayList<ColorTabBarFix.ModColorTab> modTabs = (ArrayList<ColorTabBarFix.ModColorTab>)ReflectionHacks.getPrivateStatic(ColorTabBarFix.Fields.class, "modTabs");
        for(int i = 0; i < modTabs.size(); i++) {
            ColorTabBarFix.ModColorTab mct = modTabs.get(i);
            if(mct.color == TheShowmanBoss.Enums.PALE_COLOR_PURPLE) {
                modTabs.remove(i);
                break;
            }
        }
    }
}
