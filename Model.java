// src: https://git.uwaterloo.ca/j2avery/cs349_f18_examples/blob/master/java/3-4-Undo/ShapeUndo/Model.java
// fileChooser: https://docs.oracle.com/javase/tutorial/uiswing/components/filechooser.html
// save&load: https://blog.csdn.net/cc_fys/article/details/78501136
import java.awt.geom.*;
import java.awt.*;
import javax.swing.undo.*;
import java.lang.reflect.Field;

import java.util.ArrayList;
import java.util.Vector;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Model {
    private ArrayList<Observer> observers;
    private Vector<Sprite> sprites;
    public static String FILE_EXT = ".json";
    public boolean isSaved = false;

    // Observer setup
    Model() {
        this.observers = new ArrayList<Observer>();
        this.sprites = new Vector<Sprite>();
    }

    public void addObserver(Observer observer) {
        this.observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        this.observers.remove(observer);
    }

    public void notifyObservers() {
        for (Observer observer: this.observers)
            observer.update(this);
    }

    // TODO: may need to change it to the blog way, since I don't
    // want to use the Serializable package on both FSM and Image layer
    // Menubar implementations
    public void save() {
        // fileChooser dialog
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save (ctrl-S)");
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("JSON files", "json"));
        int returnVal = fileChooser.showSaveDialog(null);
        if ( returnVal == JFileChooser.APPROVE_OPTION ) {
            File file = fileChooser.getSelectedFile();
            String filename = file.toString();
            if ( !filename.endsWith(FILE_EXT) ) {
                filename += FILE_EXT;
                file = new File(filename);
            }

            // save as JSON file
            try {
                file.createNewFile();
                FileOutputStream fileOut = new FileOutputStream(file, false);
                ObjectOutputStream out = new ObjectOutputStream(fileOut);
                out.writeObject(this.sprites);
                out.close();
                fileOut.close();

                this.isSaved = true;
                notifyObservers();
            } catch(Exception e) {
                e.printStackTrace();
            }
        } // if
    }

    @SuppressWarnings("unchecked")
    public void load() {
        // check if user want to save the current changes
        if (!isSaved) {
            int returnVal = JOptionPane.showConfirmDialog(null, "Will lose current changes! Save now?",
            "Warning!", JOptionPane.YES_NO_CANCEL_OPTION);

            if ( returnVal == JOptionPane.CANCEL_OPTION ) return;

            if ( returnVal == JOptionPane.YES_OPTION ) {
                 this.save();
                 this.clearScreen();
            } else if ( returnVal == JOptionPane.NO_OPTION ) {
                this.clearScreen();
                this.load();
            }
        } else {
            this.clearScreen();
        }

        // fileChooser dialog
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Load (ctrl-L)");
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("JSON files", "json"));
        int returnVal = fileChooser.showSaveDialog(null);
        if ( returnVal == JFileChooser.APPROVE_OPTION ) {
            File file = fileChooser.getSelectedFile();
            // load the given JSON file
            try {
                FileInputStream loadInputFile = new FileInputStream(file);
                ObjectInputStream load = new ObjectInputStream(loadInputFile);
                this.sprites = ((Vector<Sprite>) load.readObject());
                load.close();
                this.isSaved = true;
                notifyObservers();
            } catch(Exception e) {
                e.printStackTrace();
            }
        } // if
    }

    // FSM related functions
    // TODO BY ZOE: rm this later
    public void spriteModified() {
		this.isSaved = false;
	}

    public Vector<Sprite> getSprites() {
		return sprites;
	}

	public void clearScreen() {
		this.sprites.clear();
		notifyObservers();
	}

	public void reset() {
		this.sprites.clear();
		try {
			sprites.add(Main.makeSprite());
		} catch (IOException e) {
			e.printStackTrace();
		}
		notifyObservers();
	}


} // model
