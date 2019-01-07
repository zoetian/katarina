// src: https://git.uwaterloo.ca/j2avery/cs349_f18_examples/blob/master/java/2-8-Transformation/scene_graph/Canvas.java
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.geom.AffineTransform;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import java.awt.Paint;
import java.awt.GradientPaint;

/**
 * A canvas that draws sprites.
 *
 * Michael Terry & Jeff Avery
 */
public class Canvas extends JPanel implements Observer {

	private Vector<Sprite> sprites = new Vector<Sprite>(); // All sprites we're managing
	private Sprite interactiveSprite = null; // Sprite with which user is interacting
    private Model model;

	public Canvas(Model model) {
        super();
        this.model = model;
        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createLineBorder(Color.PINK));

		// Install our event handlers
		this.addMouseListener(new MouseAdapter() {
			public void mousePressed(java.awt.event.MouseEvent e) {
				handleMousePress(e);
			}

			public void mouseReleased(MouseEvent e) {
				handleMouseReleased(e);
			}
		});
		this.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {
				handleMouseDragged(e);
			}
		});
	}

	// Handle mouse press events
	private void handleMousePress(java.awt.event.MouseEvent e) {
        // from model
        model.spriteModified();
        Vector<Sprite> sprites = model.getSprites();

		for (Sprite sprite : sprites) {
			interactiveSprite = sprite.getSpriteHit(e);
			if (interactiveSprite != null) {
				interactiveSprite.handleMouseDownEvent(e);
				break;
			}
		}
	}


	// Handle mouse released events
	private void handleMouseReleased(MouseEvent e) {
		if (interactiveSprite != null) {
			interactiveSprite.handleMouseUp(e);
			repaint();
		}
		interactiveSprite = null;
	}

	// Handle mouse dragged events
	private void handleMouseDragged(MouseEvent e) {
		if (interactiveSprite != null) {
			interactiveSprite.handleMouseDragEvent(e);
			repaint();
		}
	}

	// Add a top-level sprite to the canvas
	public void addSprite(Sprite s) {
		sprites.add(s);
	}

	// Paint our canvas
	public void paint(Graphics g) {
        Color c1 = new Color(39,60,83);
        Color c2 = new Color(61,62,82);
        int w = getWidth();
        int h = getHeight();

        // Paint a gradient from top to bottom
        GradientPaint gp = new GradientPaint(0, 0, c1, 0, h, c2 );
        Graphics2D g2d = (Graphics2D)g;
        g2d.setPaint(gp);

		g2d.fillRect(0, 0, this.getWidth(), this.getHeight());
		g2d.setColor(Color.BLACK);

		Vector<Sprite> sprites = model.getSprites();
		for (Sprite sprite : sprites) {
			sprite.draw((Graphics2D) g);
		}
	}

    @Override
	public void update(Object observable) {
		repaint();
	}
}
