// TODO BY ZOE: assemble all the components
// MENU + BODY + IMAGE
import javax.swing.JMenuBar;
import java.io.IOException;
import javax.swing.JFrame;

public class Main {

	public static Sprite makeSprite() throws IOException {
		// torso
		Katarina torso = new Katarina(Sprite.Body.TORSO, "media/torso.png", 400, 120, 0.0);

		Katarina head = new Katarina(Sprite.Body.HEAD, "media/head.png", 25, -108, 50.0);
		head.setAnchorPoint(55, 120);

		// left upper arm
		Katarina leftUpperArm = new Katarina(Sprite.Body.UPPER_ARM, "media/leftUpperArm.png", -59, 14, 360.0); // done
		leftUpperArm.setAnchorPoint(70, 40);

		// left lower arm
		Katarina leftLowerArm = new Katarina(Sprite.Body.LOWER_ARM, "media/leftLowerArm.png", -90, 90, 135.0); // done
		leftLowerArm.setAnchorPoint(30, 15);

		// left hand
		Katarina leftHand = new Katarina(Sprite.Body.HAND, "media/leftHand.png", -35 , 90, 35.0); // done
		leftHand.setAnchorPoint(30, 5);

		// left upper leg
		Katarina leftUpperLeg = new Katarina(Sprite.Body.UPPER_LEG, "media/leftUpperLeg.png", -8, 208, 90.0); // done
		leftUpperLeg.setAnchorPoint(20, 80);

		// left lower leg
		Katarina leftLowerLeg = new Katarina(Sprite.Body.LOWER_LEG, "media/leftLowerLeg.png", 5, 203, 90.0); // done
		leftLowerLeg.setAnchorPoint(40, 15);

		// left foot
		Katarina leftFoot = new Katarina(Sprite.Body.LEFT_FOOT, "media/leftFoot.png", -10, 218, 35.0); // done
		leftFoot.setAnchorPoint(32, 6);

		// right upper leg
		Katarina rightUpperLeg = new Katarina(Sprite.Body.UPPER_LEG, "media/rightUpperLeg.png", 76, 222, 90.0); // done
		rightUpperLeg.setAnchorPoint(20, 20);


		// right lower leg
		Katarina rightLowerLeg = new Katarina(Sprite.Body.LOWER_LEG, "media/rightLowerLeg.png", 21, 188, 90.0); // done
		rightLowerLeg.setAnchorPoint(40, 15);

		// right foot
		Katarina rightFoot = new Katarina(Sprite.Body.RIGHT_FOOT, "media/rightFoot.png", 18, 194, 35.0); // done
		rightFoot.setAnchorPoint(32, 6);

		// right upper arm
		Katarina rightUpperArm = new Katarina(Sprite.Body.UPPER_ARM, "media/rightUpperArm.png", 120, 12, 360.0); // done
		rightUpperArm.setAnchorPoint(10, 40);

		// lower arm
		Katarina rightLowerArm = new Katarina(Sprite.Body.LOWER_ARM, "media/rightLowerArm.png", 72, 87, 135.0); // done
		rightLowerArm.setAnchorPoint(25, 15);

		// hand
		Katarina rightHand = new Katarina(Sprite.Body.HAND, "media/rightHand.png", 91, 88, 35.0); // current
		rightHand.setAnchorPoint(15, 5);

        // TODO BY ZOE: move the arms up up up
		// build scene graph
		torso.addChild(head);

		torso.addChild(leftUpperLeg);
		leftUpperLeg.addChild(leftLowerLeg);
		leftLowerLeg.addChild(leftFoot);

		torso.addChild(rightUpperLeg);
		rightUpperLeg.addChild(rightLowerLeg);
		rightLowerLeg.addChild(rightFoot);

		torso.addChild(leftUpperArm);
		leftUpperArm.addChild(leftLowerArm);
		leftLowerArm.addChild(leftHand);

		torso.addChild(rightUpperArm);
		rightUpperArm.addChild(rightLowerArm);
		rightLowerArm.addChild(rightHand);

		return torso;
	}

    public static void main( String[] args ) {
        int screenWidth = 1500, screenHeight = 900;
        Model model = new Model();
        model.reset();

        JFrame f = new JFrame();
        JMenuBar menu = new Menu(model);
        Canvas dollCanvas = new Canvas(model);

        f.setJMenuBar(menu);
        f.setTitle("PaperDoll");
		f.setSize(screenWidth, screenHeight);
		f.setResizable(false);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.getContentPane().add(dollCanvas);
        f.setVisible(true);

        model.addObserver(dollCanvas);
        model.notifyObservers();
    }
}
