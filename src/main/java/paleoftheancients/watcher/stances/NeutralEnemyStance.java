package paleoftheancients.watcher.stances;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.stances.NeutralStance;

public class NeutralEnemyStance extends AbstractEnemyStance {
    public NeutralEnemyStance(AbstractMonster owner) {
        super(owner);
        this.ID = NeutralStance.STANCE_ID;
    }

    @Override
    public void updateDescription() {
        this.description = getStanceString(NeutralStance.class).DESCRIPTION[0];
    }

    @Override
    public void render(SpriteBatch sb) {
    }
}
