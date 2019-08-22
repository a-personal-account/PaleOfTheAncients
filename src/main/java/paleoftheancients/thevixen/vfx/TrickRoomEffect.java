package paleoftheancients.thevixen.vfx;

import paleoftheancients.thevixen.TheVixenMod;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

import java.util.ArrayList;

public class TrickRoomEffect extends AbstractGameEffect {
    private static int columns = 4;
    private static float TIME_PER_ROW = 0.1F;
    private static float SQUARETIME = 0.3F;

    private static Texture img;

    private Color color;
    private float elapsedTime;
    private float width;
    private float height;

    private int step;

    ArrayList<ArrayList<Square>> squares;

    public TrickRoomEffect(Hitbox hb, Color startingcolor) {
        this.img = ImageMaster.loadImage(TheVixenMod.getResourcePath("vfx/square.png"));

        this.color = startingcolor;
        this.color.a = 0.5F;
        this.scale = (hb.width / img.getWidth()) / columns;

        this.width = img.getWidth() * this.scale;
        this.height = img.getHeight() * this.scale;

        int rows = (int)(hb.height / this.height);

        this.squares = new ArrayList<>();
        for(int i = 0; i < rows + columns - 1; i++) {
            ArrayList<Square> list = new ArrayList<>();
            this.squares.add(list);
            Square s = null;
            for(int j = Math.max(0, i + 1 - rows); j <= i && j < columns; j++) {
                if (s == null) {
                    s = new Square(hb.x + (j * width), hb.y + ((rows - Math.min(rows, i + 1)) * height));
                } else {
                    s = new Square(s.x + width, s.y + height);
                }
                list.add(s);
            }
        }

        this.width *= 1.02F;
        this.height *= 1.02F;
        this.scale *= 1.02F;

        this.elapsedTime = 0;
        this.step = 0;
    }

    public void update() {
        switch(step) {
            case 0:
                boolean done = true;
                for (int i = 0; i < squares.size(); i++) {
                    if (this.elapsedTime > TIME_PER_ROW * i) {
                        for (final Square s : squares.get(i)) {
                            s.update();
                            done &= s.rotation == 0;
                        }
                    } else {
                        done = false;
                    }
                }
                if (done && this.elapsedTime > TIME_PER_ROW * columns) {
                    step++;
                }
                break;

            case 1:
                color.a += Gdx.graphics.getDeltaTime() * 1;
                if(color.a >= 1.0F) {
                    step++;
                }
                break;

            case 2:
                color.a -= Gdx.graphics.getDeltaTime() * 2;
                if(color.a <= 0F) {
                    this.isDone = true;
                }
                break;
        }


        this.elapsedTime += Gdx.graphics.getDeltaTime();
    }

    public void render(SpriteBatch sb) {
        sb.setColor(this.color);
        for (int i = 0; i < squares.size(); i++) {
            for (final Square s : squares.get(i)) {
                s.render(sb);
            }
        }
        sb.setColor(Color.WHITE.cpy());
    }

    public void dispose() {
        img.dispose();
        img = null;
    }


    private class Square {
        private float x;
        private float y;
        private float squarescale;
        private float rotation;

        public Square(float x, float y) {
            this.x = x;
            this.y = y;
            this.squarescale = 0F;
            this.rotation = -90F;
        }

        public void update() {
            if(this.squarescale < 1F) {
                this.squarescale += Gdx.graphics.getDeltaTime() / SQUARETIME;
                this.rotation += 90F * Gdx.graphics.getDeltaTime() / SQUARETIME;
            } else {
                this.squarescale = 1;
                this.rotation = 0;
            }
        }

        public void render(SpriteBatch sb) {
            if(this.squarescale > 0) {
                sb.draw(img, x, y, width / 2, height / 2, width, height, this.squarescale, this.squarescale, this.rotation, 0, 0, img.getWidth(), img.getHeight(), false, false);
            }
        }
    }
}
