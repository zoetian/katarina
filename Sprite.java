// src: https://git.uwaterloo.ca/j2avery/cs349_f18_examples/blob/master/java/2-8-Transformation/scene_graph/Sprite.java

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.Vector;
import java.io.Serializable;
import java.awt.geom.Rectangle2D;

public abstract class Sprite implements Serializable {

    // Tracks our current interaction mode after a mouse-down
    protected enum Action {
        IDLE,
        DRAGGING,
        ROTATING,
        SCALING_AND_ROTATING
    }

    protected enum Body {
       HEAD, TORSO, UPPER_ARM, LOWER_ARM, HAND, UPPER_LEG, LOWER_LEG, LEFT_FOOT, RIGHT_FOOT
   }

   // S.R.T matrices
    protected AffineTransform scaleMatrix = new AffineTransform();
    protected AffineTransform rotateMatrix = new AffineTransform();
    protected AffineTransform translateMatrix = new AffineTransform();
    protected double x, y;
    protected int anchorX, anchorY;
    protected double maxRotate;
    protected double rotated;
    protected double willRotate;
    protected double scaled;
    protected String filePath;
    protected Rectangle2D vertex = null;

    private Sprite parent = null;                                       // Pointer to our parent
    private Vector<Sprite> children = new Vector<Sprite>();             // Holds all of our children
    private AffineTransform transform = new AffineTransform();          // Our transformation matrix
    protected Point2D lastPoint = null;                                 // Last mouse point
    protected Action action = Action.IDLE;                              // current state
    protected Body bodyPart = null;                                     // clicked/ triggered body part

    public double MIN_SCALE = 0.5;
    public double MAX_SCALE = 2.5;

    public Sprite() {}

    public Sprite(Sprite parent) {
        if (parent != null) {
            parent.addChild(this);
        }
    }

    public void addChild(Sprite s) {
        children.add(s);
        s.setParent(this);
    }

    public Sprite getParent() {
        return parent;
    }

    private void setParent(Sprite s) {
        this.parent = s;
    }

    public abstract boolean pointInside(Point2D p);

    protected void handleMouseDownEvent(MouseEvent e) {
        lastPoint = e.getPoint();
        if (e.getButton() == MouseEvent.BUTTON1) {
            if ( this.bodyPart == Body.TORSO ) {
                action = Action.DRAGGING;
                // System.out.println("[1] clicked torso");
            } else if ( this.bodyPart == Body.UPPER_LEG || this.bodyPart == Body.LOWER_LEG ) {
                action = Action.SCALING_AND_ROTATING;
            } else {
                action = Action.ROTATING;
            }
        }
        // Handle rotation, scaling mode depending on input
    }

    protected void handleMouseDragEvent(MouseEvent e) {
        Point2D oldPoint = lastPoint;
        Point2D newPoint = e.getPoint();
        switch (action) {
            case IDLE:
                ; // no-op (shouldn't get here)
                break;
            case DRAGGING:
                double x_diff = newPoint.getX() - oldPoint.getX();
                double y_diff = newPoint.getY() - oldPoint.getY();
                translateMatrix.translate(x_diff, y_diff);
                break;
            case ROTATING:
                rotate(oldPoint, newPoint);
                break;
            case SCALING_AND_ROTATING:
                rotate(oldPoint, newPoint); // TOOD BY ZOE: when submit: double check this part!!!
                scale(oldPoint, newPoint);
                break;
        }
        lastPoint = e.getPoint();
    }

    protected void handleMouseUp(MouseEvent e) {
        action = Action.IDLE;
        this.willRotate = this.rotated;
    }

    public void rotate(Point2D oldPoint, Point2D newPoint) {

		AffineTransform fullTrans = this.getFullTransform();
		Point2D anchor = new Point2D.Float(this.anchorX, this.anchorY);
		fullTrans.transform(anchor, anchor);
        double a1 = Math.toDegrees(Math.atan((oldPoint.getX() - anchor.getX()) / (oldPoint.getY() - anchor.getY())));
		if ( ((oldPoint.getX() > anchor.getX()) && (oldPoint.getY() < anchor.getY()) )
            || ( (oldPoint.getX() <= anchor.getX())  && (oldPoint.getY() < anchor.getY()) )) {
			a1 += 180;
		}
        double a2 = Math.toDegrees(Math.atan((newPoint.getX() - anchor.getX()) / (newPoint.getY() - anchor.getY())));
		if (((newPoint.getX() > anchor.getX()) && (newPoint.getY() < anchor.getY()) )
            || ((newPoint.getX() <= anchor.getX())  && (newPoint.getY() < anchor.getY()) )) {
			a2 += 180;
		}
		double delta = a2 - a1;
		delta = (360 - delta) % 360;
		if (delta > 180) {
			delta -= 360;
		}

		if (!(this.maxRotate != 360 && (this.rotated == this.maxRotate && delta > 0)
				|| this.rotated == -this.maxRotate && delta < 0) ) {
			double theta = 0.0;
			this.willRotate += delta;
			if (this.maxRotate == 360 || (this.willRotate <= this.maxRotate
					&& this.willRotate >= -this.maxRotate)) {
				this.willRotate %= 360;
				theta = Math.toRadians(delta);
			} else if (this.willRotate > this.maxRotate) {
					this.willRotate = this.maxRotate;
					theta = Math.toRadians(this.maxRotate) - Math.toRadians(this.rotated);
            } else if (this.willRotate < -this.maxRotate) {
					this.willRotate = -this.maxRotate;
					theta = Math.toRadians(-this.maxRotate) - Math.toRadians(this.rotated);
			}
			rotateMatrix.rotate(theta, this.anchorX, this.anchorY);
			this.rotated = this.willRotate;
		}
    }

    public void scale(Point2D oldPoint, Point2D newPoint) {
        Point2D anchor = new Point2D.Float(this.anchorX, this.anchorY);

		double oldDistance = Math.sqrt((oldPoint.getX()-anchor.getX())*(oldPoint.getX()-anchor.getX()) + (oldPoint.getY()-anchor.getY())*(oldPoint.getY()-anchor.getY()) );
		double newDistance = Math.sqrt((newPoint.getX()-anchor.getX())*(newPoint.getX()-anchor.getX()) + (newPoint.getY()-anchor.getY())*(newPoint.getY()-anchor.getY()) );

		double theta0 = Math.toRadians(this.rotated);
		double theta1 = Math.atan2( oldPoint.getX() - this.anchorX, this.anchorY - oldPoint.getY() );
		double delta0 = theta1 - theta0;
		double theta2 = Math.atan2( newPoint.getX() - this.anchorX, this.anchorY - newPoint.getY() );
		double delta1 = theta2 - theta0;
		double willScale = (Math.cos(delta1) * newDistance) / (Math.cos(delta0) * oldDistance);

        if ( this.scaled * willScale >= MIN_SCALE && this.scaled*willScale <= MAX_SCALE ) {
            this.scaled *= willScale;
        } else if (willScale > 1) {
                willScale = MAX_SCALE = this.scaled;
        } else if (willScale < 1){
                willScale = MIN_SCALE = this.scaled;
        }
        scaleMatrix.scale(1, willScale);

        Sprite foot = this.children.get(0);
        if (this.bodyPart == Body.UPPER_LEG) {
            foot.scaled = this.scaled;
            foot = foot.children.get(0);
        }
        foot.scaled = willScale;
        foot.scaleMatrix.scale(1, 1/willScale);
    } // scale

    // return The sprite that was hit, or null if no sprite was hit
    public Sprite getSpriteHit(MouseEvent e) {
        for (Sprite sprite : children) {
            Sprite s = sprite.getSpriteHit(e);
            if (s != null) {
                return s;
            }
        }
        if (this.pointInside(e.getPoint())) {
            return this;
        }
        return null;
    }

    // Returns the full transform to this object from the root
    public AffineTransform getFullTransform() {
        AffineTransform returnTransform = new AffineTransform();
        Sprite curSprite = this;
        while (curSprite != null) {
            returnTransform.preConcatenate(curSprite.getLocalTransform());
            curSprite = curSprite.getParent();
        }
        return returnTransform;
    }

    // Returns our local transform
    public AffineTransform getLocalTransform() {
        // return (AffineTransform)transform.clone();
        AffineTransform TRSMatrix = new AffineTransform();
		TRSMatrix.concatenate(translateMatrix);
		TRSMatrix.concatenate(rotateMatrix);
		TRSMatrix.concatenate(scaleMatrix);
		return TRSMatrix;
    }

    // Performs an arbitrary transform on this sprite
    public void transform(AffineTransform t) {
        transform.concatenate(t);
    }

    // Draws the sprite. This method will call drawSprite after
    // the transform has been set up for this sprite.
    public void draw(Graphics2D g) {
        AffineTransform oldTransform = g.getTransform();

        // Set to our transform
        Graphics2D g2 = g;
        AffineTransform currentAT = g2.getTransform();
        currentAT.concatenate(this.getFullTransform());
        g2.setTransform(currentAT);

        // Draw the sprite (delegated to sub-classes)
        this.drawSprite(g);

        // Restore original transform
        g.setTransform(oldTransform);

        // Draw children
        for (Sprite sprite : children) {
            sprite.draw(g);
        }
    }

    /**
     * The method that actually does the sprite drawing. This method
     * is called after the transform has been set up in the draw() method.
     * Sub-classes should override this method to perform the drawing.
     */
    protected abstract void drawSprite(Graphics2D g);
}
