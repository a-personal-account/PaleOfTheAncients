package paleoftheancients.theshowman.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import paleoftheancients.theshowman.monsters.TheShowmanBoss;

public class MonsterEnergyPanel {
    private float current_x;
    private float current_y;
    private ShowmanEnergyOrb orb;
    private TheShowmanBoss boss;

    public MonsterEnergyPanel(TheShowmanBoss boss) {
        super();
        this.boss = boss;
        this.orb = new ShowmanEnergyOrb(this.boss);

        this.current_x = this.boss.drawX - this.boss.hb.width * 1.2F;
        this.current_y = this.boss.drawY + this.boss.hb.height * 1.3F;
    }

    public void update() {
        if(this.orb != null) {
            this.orb.updateOrb(this.boss.curenergy);
        }
    }

    public void render(SpriteBatch sb) {
        if(this.orb != null) {
            this.orb.renderOrb(sb, true, current_x, current_y);
        }
    }

    public void dispose() {
        this.orb.dispose();
        this.orb = null;
    }
}
