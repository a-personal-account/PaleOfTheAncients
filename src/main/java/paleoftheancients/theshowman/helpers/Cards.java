package paleoftheancients.theshowman.helpers;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import paleoftheancients.PaleMod;

import java.util.ArrayList;

public class Cards {
    public static final String[] IMG = new String[] {
            PaleMod.assetPath("images/TheShowman/vfx/cards/card_back_mk2.png"),
            PaleMod.assetPath("images/TheShowman/vfx/cards/card_face_clubs.png"),
            PaleMod.assetPath("images/TheShowman/vfx/cards/card_face_diamonds.png"),
            PaleMod.assetPath("images/TheShowman/vfx/cards/club_3.png"),
            PaleMod.assetPath("images/TheShowman/vfx/cards/club_7.png"),
            PaleMod.assetPath("images/TheShowman/vfx/cards/diamond_4.png"),
            PaleMod.assetPath("images/TheShowman/vfx/cards/diamond_8.png"),
            PaleMod.assetPath("images/TheShowman/vfx/cards/heart_2.png"),
            PaleMod.assetPath("images/TheShowman/vfx/cards/heart_6.png"),
            PaleMod.assetPath("images/TheShowman/vfx/cards/heart_ace.png"),
            PaleMod.assetPath("images/TheShowman/vfx/cards/spade_5.png"),
            PaleMod.assetPath("images/TheShowman/vfx/cards/spade_9.png")
    };
    private static Texture[] nums;

    public static Texture getCard(int n) {
        if(nums == null) {
            nums = new Texture[IMG.length];
        }
        if(nums[n] == null) {
            nums[n] = ImageMaster.loadImage(IMG[n]);
        }
        return nums[n];
    }
    public static int size() {
        return nums.length;
    }

    public static void dispose() {
        if(nums != null) {
            for (final Texture n : nums) {
                if (n != null) {
                    n.dispose();
                }
            }
            nums = null;
        }
    }

    public static void preload() {
        for(int i = 0; i < IMG.length; i++) {
            getCard(i);
        }
    }
}
