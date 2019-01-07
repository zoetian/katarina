// src: https://git.uwaterloo.ca/j2avery/cs349_f18_examples/blob/master/java/2-8-Transformation/scene_graph/RectangleSprite.java
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import java.io.Serializable;

public class Katarina extends Sprite implements Serializable {
	protected RectangularShape shape = null;

	public Katarina(Body bodyPart, String filePath, int x, int y, double maxRotate) throws IOException {
		super();
		this.x = x;
		this.y = y;
		this.bodyPart = bodyPart;
		this.maxRotate = maxRotate;
		this.scaled = 1.0;
		this.filePath = filePath;

		URL url = getClass().getResource(filePath);
		Image spriteImage = ImageIO.read(url);
		shape = new Rectangle2D.Double(0, 0, spriteImage.getWidth(null),
				spriteImage.getHeight(null));
		this.translateMatrix.translate(x, y);
	}

	public void setAnchorPoint(int x, int y) {
		this.anchorX = x;
		this.anchorY = y;
	}

	public boolean pointInside(Point2D p) {
		AffineTransform fullTransform = this.getFullTransform();
		AffineTransform inverseTransform = null;
		try {
			inverseTransform = fullTransform.createInverse();
		} catch (NoninvertibleTransformException e) {
			e.printStackTrace();
		}
		Point2D newPoint = (Point2D) p.clone();
		inverseTransform.transform(newPoint, newPoint);
		return shape.contains(newPoint);
	}

	protected void drawSprite(Graphics2D g) {
		g.setColor(Color.PINK);
		Image spriteImage = null;
		try {
			spriteImage = ImageIO.read(getClass().getResource(this.filePath));
		} catch (IOException e) {
			e.printStackTrace();
		}
		g.drawImage(spriteImage, null, null);
	}
}
