package nz.ac.vuw.ecs.swen225.gp21.app;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

// TODO: Auto-generated Javadoc
/**
 * The Class SaveState.
 */
public class SaveState {
	
	/** The file name. */
	public static String fileName;
	
	/** The level. */
	public static int level;
	
	/**
	 * Save the state when exiting the program.
	 *
	 * @param state the state
	 * @param fileName2 the file name 2
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void saveSaveState(int state, String fileName2) throws IOException {
		 String path = "src" + Main.fs + "nz" + Main.fs + "ac" + Main.fs + "vuw" + Main.fs + "ecs" + Main.fs + "swen225" + Main.fs + "gp21" + Main.fs + "app" + Main.fs + "saveState.txt";
		 @SuppressWarnings("resource")
		 BufferedWriter writer = new BufferedWriter(new FileWriter(path));
		 String toSave = null;
		 if (state == 1) {
			 toSave = String.valueOf(state) +"\n" + String.valueOf(Main.g.getBoard().getCurrentLevel());
		 }else if (state == 2) {
			 toSave = String.valueOf(state) +"\n" + String.valueOf(Main.g.getBoard().getCurrentLevel()) + "\n" + fileName2;
		 }else if (state == 0) {
			 toSave = "0";
		 }
		 writer.write(toSave);
		 writer.close();
	}
	
	/**
	 * Get the last saved state when opening program.
	 *
	 * @return the int
	 */
	public static int loadSaveState() {
		 int state = 0;
		 String path = "src" + Main.fs + "nz" + Main.fs + "ac" + Main.fs + "vuw" + Main.fs + "ecs" + Main.fs + "swen225" + Main.fs + "gp21" + Main.fs + "app" + Main.fs + "saveState.txt";
		 File file = new File(path);
		 Scanner sc = null;
		 try {
			 sc  = new Scanner(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  state = sc.nextInt();
		  if (state > 0 ) level = sc.nextInt();
		  if (state == 2) fileName = sc.next() + " " + sc.next() + " " + sc.next();
		 return state;
	}
}
