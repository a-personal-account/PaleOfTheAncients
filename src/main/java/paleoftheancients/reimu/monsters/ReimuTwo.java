package paleoftheancients.reimu.monsters;

import com.megacrit.cardcrawl.cards.DamageInfo;

public class ReimuTwo extends ReimuPhase {
    private static final byte DUPLEXBARRIER = 0;
    private static final byte PERSUASIONNEEDLE = 1;
    private static final byte EXTERMINATION = 2;

    public ReimuTwo() {

    }

    @Override
    public void takeTurn(Reimu reimu, ReimuPhase.ReimuMoveInfo rmi, DamageInfo info) {
        switch (reimu.nextMove) {

        }
    }

    @Override
    public void getMove(Reimu reimu, final int num) {

    }

    @Override
    public void setDeathbombIntent(Reimu reimu) {
        reimu.setMoveShortcut(DUPLEXBARRIER);
    }
}
