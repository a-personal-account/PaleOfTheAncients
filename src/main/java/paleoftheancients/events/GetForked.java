package paleoftheancients.events;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.events.GenericEventDialog;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.MarkOfTheBloom;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.ui.buttons.ProceedButton;
import paleoftheancients.PaleMod;
import paleoftheancients.dungeons.PaleOfTheAncients;
import paleoftheancients.patches.GoToNextDungeonPatch;
import paleoftheancients.relics.Timepiece;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class GetForked extends AbstractImageEvent {
    public static final String ID = PaleMod.makeID("ForkInTheRoad");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final String BASE_IMG = PaleMod.MOD_ID + "/images/events/heart.jpg";

    private boolean chosen = false;

    public GetForked() {
        super(NAME, DESCRIPTIONS[0], BASE_IMG);

        String dialogue = DESCRIPTIONS[1];
        dialogue += DESCRIPTIONS[AbstractDungeon.player.hasRelic(MarkOfTheBloom.ID) ? 2 : 3];
        dialogue += DESCRIPTIONS[4];
        dialogue = Format(dialogue, "#y~", "~");

        String ifDialogue = Format(DESCRIPTIONS[DESCRIPTIONS.length - 3], "#r@", "@");

        this.body += dialogue + DESCRIPTIONS[DESCRIPTIONS.length - 4] + ifDialogue + DESCRIPTIONS[DESCRIPTIONS.length - 2];

        imageEventText.setDialogOption(OPTIONS[0]);
        imageEventText.setDialogOption(OPTIONS[1]);
    }

    @Override
    protected void buttonEffect(int buttonPressed) {

        switch(buttonPressed) {
            case 0:
                if(!chosen) {
                    chosen = true;
                    imageEventText.updateBodyText(DESCRIPTIONS[DESCRIPTIONS.length - 1]);
                    imageEventText.clearAllDialogs();
                    imageEventText.setDialogOption(OPTIONS[2]);
                    (new Timepiece()).instantObtain();
                } else {
                    nextDungeon(PaleOfTheAncients.ID);
                }
                break;

            case 1:
                try {
                    Method yuckyPrivateMethod = ProceedButton.class.getDeclaredMethod("goToVictoryRoomOrTheDoor");
                    yuckyPrivateMethod.setAccessible(true);
                    yuckyPrivateMethod.invoke(new ProceedButton());
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    public static void nextDungeon(String dungeonID) {
        //Set the stage for the next act.
        CardCrawlGame.nextDungeon = dungeonID;

        AbstractDungeon.rs = AbstractDungeon.RenderScene.NORMAL;
        if (AbstractDungeon.currMapNode.room instanceof GoToNextDungeonPatch.ForkEventRoom && ((GoToNextDungeonPatch.ForkEventRoom) AbstractDungeon.currMapNode.room).originalRoom != null) {
            AbstractDungeon.currMapNode.room = ((GoToNextDungeonPatch.ForkEventRoom) AbstractDungeon.currMapNode.room).originalRoom;
        }
        GenericEventDialog.hide();

        CardCrawlGame.mode = CardCrawlGame.GameMode.GAMEPLAY;
        AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
        AbstractDungeon.fadeOut();
        AbstractDungeon.isDungeonBeaten = true;
    }


    public static String Format(String subject, String prefix, String suffix) {
        subject = subject.replaceAll("(?<!\\sNL)(?!\\sNL)\\s+", suffix + ' ' + prefix);
        subject = subject.replaceAll("NL\\s+", "NL " + prefix);
        subject = subject.replaceAll("\\s+NL", suffix + " NL");
        return prefix + subject + suffix;
    }
}