package Objects;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public abstract class MovableObject extends GameObjects {
    protected float dx;
    protected float dy;

    public MovableObject(float x, float y, int width, int height, float dx, float dy) {
        super(x, y, width, height);
        this.dx = dx;
        this.dy = dy;
    }

    @Override
    public abstract void render(Graphics g);

    @Override
    public abstract void update();
}
