package paleoftheancients.reimu.monsters;

import com.megacrit.cardcrawl.cards.DamageInfo;

public class ReimuThree extends ReimuPhase {
    private static final byte FANTASYSEAL = 0;
    private static final byte HOMINGAMULET = 1;
    private static final byte HAKUREIAMULET = 2;

    public ReimuThree() {

    }

    @Override
    public void takeTurn(Reimu reimu, ReimuMoveInfo rmi, DamageInfo info) {
        switch (reimu.nextMove) {

        }
    }

    @Override
    public void getMove(Reimu reimu, final int num) {

    }

    @Override
    public void setDeathbombIntent(Reimu reimu) {
        reimu.setMoveShortcut(FANTASYSEAL);
    }
}
