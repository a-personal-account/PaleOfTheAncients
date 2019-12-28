package paleoftheancients.reimu.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;

public class Bullet {
    private float x, y, vX, vY;
    private float rotation;
    private Color color;
    private boolean flipped;
    private AbstractCreature target, source;

    public Bullet(AbstractCreature target, AbstractCreature source, float angle, Color color) {
        x = source.hb.cX;
        y = source.hb.cY;
        rotation = -(float)Math.toDegrees(angle);
        vX = (float)Math.sin(angle);
        vY = (float)Math.cos(angle);

        this.color = color;
        this.source = source;
        this.target = target;
    }

    public boolean update() {
        x += vX * Gdx.graphics.getDeltaTime() * 600F * Settings.scale;
        y += vY * Gdx.graphics.getDeltaTime() * 600F * Settings.scale;

        if(!flipped) {
            try {
                if (vX > 0) {
                    if (x > source.hb.cX + source.hb.width) {
                        throw new ArithmeticException();
                    }
                } else {
                    if (x < source.hb.cX - source.hb.width) {
                        throw new ArithmeticException();
                    }
                }
                if (vY > 0) {
                    if (y > source.hb.cY + source.hb.width) {
                        throw new ArithmeticException();
                    }
                } else {
                    if (y < source.hb.cY - source.hb.width) {
                        throw new ArithmeticException();
                    }
                }
            } catch(ArithmeticException ex) {
                flipped = true;

                vX *= -1;
                vY *= -1;
                rotation += 180;
                if(rotation > 360) {
                    rotation -= 360;
                }

                if (target.hb.width / Math.abs(vX) * Math.abs(vY) > target.hb.width) {
                    x = target.hb.width / vY * vX;
                    y = target.hb.width * (vY > 0 ? -1 : 1);
                } else {
                    x = target.hb.width * (vX > 0 ? -1 : 1);
                    y = target.hb.width / vX * vY;
                }

                if(vY > 0 && Math.abs(Math.atan(vY / vX)) > Math.PI / 4) {
                    x *= -1;
                }
                if(vX > 0 && Math.abs(Math.atan(vX / vY)) > Math.PI / 4) {
                    y *= -1;
                }

                x += target.hb.cX;
                y += target.hb.cY;
            }
        } else {
            try {
                if (vX > 0) {
                    if (x > target.hb.cX + target.hb.width) {
                        throw new ArithmeticException();
                    }
                } else {
                    if (x < target.hb.cX - target.hb.width) {
                        throw new ArithmeticException();
                    }
                }
                if (vY > 0) {
                    if (y > target.hb.cY + target.hb.width) {
                        throw new ArithmeticException();
                    }
                } else {
                    if (y < target.hb.cY - target.hb.width) {
                        throw new ArithmeticException();
                    }
                }
            } catch(ArithmeticException ex) {
                return true;
            }
        }
        return false;
    }

    public void render(SpriteBatch sb, Texture bullet, int bulletwidth, int bulletheight, float scale) {
        sb.setColor(this.color);
        sb.draw(bullet, x - bulletwidth / 2F, y - bulletheight / 2F, bulletwidth / 2F, bulletheight / 2F, bulletwidth, bulletheight, scale, scale, this.rotation, 0, 0, bulletwidth, bulletheight, false, false);
        sb.setColor(Color.WHITE);
    }
}
