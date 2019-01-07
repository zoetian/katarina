// src: https://git.uwaterloo.ca/j2avery/cs349_f18_examples/blob/master/java/3-4-Undo/ShapeUndo/MainMenuView.java

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.*;
import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

// a main menu view
public class Menu extends JMenuBar {
    // all menu items
    private JMenuItem undoMenuItem;
    private JMenuItem redoMenuItem;
    private JMenuItem saveMenuItem;
    private JMenuItem loadMenuItem;

    // the model that this view is showing
    private Model model;

    public Menu(Model model_) {
        // set the model
        model = model_;
        this.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        // create a menu "File"
        JMenu fileMenu = new JMenu("File");
        this.add(fileMenu);

        // create all menu items and add them to the file menu
        JMenuItem resetMenuItem = new JMenuItem("Reset");
        JMenuItem saveMenuItem = new JMenuItem("Save");
        JMenuItem loadMenuItem = new JMenuItem("Load");
        JMenuItem quitMenuItem = new JMenuItem("Quit");

        // menu controller for reset
        resetMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, KeyEvent.CTRL_DOWN_MASK));
        resetMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                model.reset();
            }
        });

        // menu controller for save
        saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
        saveMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                model.save();
            }
        });

        // menu controller for load
        loadMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, KeyEvent.CTRL_DOWN_MASK));
        loadMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                model.load();
            }
        });

        // menu controller for quit
        quitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, KeyEvent.CTRL_DOWN_MASK));
        quitMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                System.exit(0);
            }
        });

        fileMenu.add(resetMenuItem);
        fileMenu.add(saveMenuItem);
        fileMenu.add(loadMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(quitMenuItem);
    } // public

   public void update(Object observable) {
       // undoMenuItem.setEnabled(model.canUndo());
       // redoMenuItem.setEnabled(model.canRedo());

       // if (model.isSaved()) {
		// 	MenuItem_SAVE.setEnabled(false);
		// } else {
		// 	MenuItem_SAVE.setEnabled(true);
		// }
   }

}
