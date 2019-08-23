package paleoftheancients.theshowman.helpers;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import paleoftheancients.PaleMod;

import java.util.ArrayList;

public class Numbers {
    private static Texture[] nums;

    public static Texture getNum(int n) {
        if(nums == null) {
            nums = new Texture[10];
        }
        if(nums[n] == null) {
            nums[n] = ImageMaster.loadImage(PaleMod.assetPath("images/TheShowman/vfx/numbers/" + n + ".png"));
        }
        return nums[n];
    }

    public static void dispose() {
        for(final Texture n : nums) {
            if(n != null) {
                n.dispose();
            }
        }
        nums = null;
    }

    public static Texture[] getWholeNumber(int n) {
        ArrayList<Integer> digits = new ArrayList<>();
        for(; n > 0; n /= 10) {
            digits.add(n % 10);
        }
        Texture[] result = new Texture[digits.size()];
        for(int i = 0; i < result.length; i++) {
            result[i] = getNum(digits.get(digits.size() - 1));
            digits.remove(digits.size() - 1);
        }
        return result;
    }
}
