package paleoftheancients.theshowman.ui;

import paleoftheancients.theshowman.monsters.TheShowmanBoss;
import basemod.abstracts.CustomEnergyOrb;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MonsterEnergyPanel {
    private float current_x;
    private float current_y;
    private CustomEnergyOrb orb;
    private TheShowmanBoss boss;

    public MonsterEnergyPanel(TheShowmanBoss boss) {
        super();
        this.boss = boss;
        //this.orb = new CustomEnergyOrb(null, null, null);

        this.current_x = this.boss.drawX - this.boss.hb.width;
        this.current_y = this.boss.drawY + this.boss.hb.height;
    }

    public void update() {
        //this.orb.updateOrb(this.boss.curenergy);
    }

    public void render(SpriteBatch sb) {
        //this.orb.renderOrb(sb, true, current_x, current_y);
    }
}
