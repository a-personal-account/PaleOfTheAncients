package paleoftheancients.helpers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import paleoftheancients.dungeons.PaleOfTheAncients;

public class PlayTempMusicAction extends AbstractGameAction {
    private String key;

    public PlayTempMusicAction(String key, float duration) {
        this.key = key;
        this.duration = duration;
        this.startDuration = this.duration;
    }

    public void update() {
        if(this.duration == this.startDuration) {
            PaleOfTheAncients.playTempMusic(key);
        }
        this.tickDuration();
    }
}
