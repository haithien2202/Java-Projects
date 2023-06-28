package nz.ac.vuw.ecs.swen225.gp21.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Static helper methods for converting arrays and lists.
 *
 * @author Victor Tam
 */
public class ArrayConvertor {
	
	/**
	 * Convert from 2D string array to 1D.
	 *
	 * @param og original 2D string
	 * @return 1D array
	 */
	public static String [] from2dTo1d(String [][] og){
		String [] result = new String[og.length];
		for (int i =0; i<og.length; i++) {
			StringBuilder sb = new StringBuilder();
			for (int j =0; j<og[0].length; j++) {
				sb.append(og[i][j]);
			}
			result[i]= sb.toString();
		}
		return result;
	}
	
	/**
	 * Convert from 1D string array to 2D.
	 *
	 * @param og original 1D string
	 * @return 2D array
	 */
	public static String [][] from1dTo2d(String [] og){
		String [][] result = new String[og.length][og[0].length()];
		for (int i =0; i<og.length; i++) {
			for (int j =0; j<og[0].length(); j++) {
				result[i][j]=og[i].substring(j, j+1);
			}
		}
		return result;
	}
	
	/**
	 * Convert/flatten from 2D string array to String.
	 *
	 * @param og original 2D string
	 * @return result String
	 */
	public static String from2dTo0d(String [][] og){
		StringBuilder result = new StringBuilder();
		for (int i =0; i<og.length; i++) {
			for (int j =0; j<og[0].length; j++) {
				result.append(og[i][j]);
			}
			result.append('\n');
		}
		return result.toString();
	}
	
	/**
	 * Convert/flatten from 1D string array to String.
	 *
	 * @param og original 1D string
	 * @return result String
	 */
	public static String from1dTo0d(String [] og){
		StringBuilder result = new StringBuilder();
		for (int i =0; i<og.length; i++) {
			result.append(og[i]);
			result.append('\n');
		}
		
		return result.toString();
	}
	
	/**
	 * Convert from String string array to 1D.
	 *
	 * @param og original string
	 * @param row the row
	 * @param col the col
	 * @return result 1D array String
	 */
	public static String[] from0dTo1d(String og, int row, int col){
		String [] result = new String[row];
		for (int i=0; i<row; i++) {
			StringBuilder sb = new StringBuilder();
			sb.append(og.substring(i*col, i*col+col));
			result[i]= sb.toString();
		}
		return result;
	}
	
	/**
	 * Converts List of Strings(key code) into a list of KeyTiles.
	 *
	 * @param sl the sl
	 * @return ktl
	 */
	public static List<KeyTile> fromStrToKT(List<String> sl){
		List <KeyTile> ktl = new ArrayList<>();
		for(String str: sl) {
			if(str.equals("C")) ktl.add(new KeyTile('C',-1,-1));
			else if(str.equals("G")) ktl.add(new KeyTile('G',-1,-1));
			else if(str.equals("R")) ktl.add(new KeyTile('R',-1,-1));
			else throw new IllegalArgumentException("Invalid colour in saved inventory.");
		}
		return ktl;
	}
	
	/**
	 * Checks if the input draws a rectangular board
	 * Not used as it might restrict creativity of the board layout.
	 *
	 * @param input 1d array
	 * @return true if the tileBoard is not a rectangular
	 */
	public static boolean checkIfRectangular(String[] input) {
		int uniform = input[0].length();
		for (int i =1; i<input.length; i++) {
			if (input[i].length() != uniform) return false;
		}
		return true;
	}

//	public static String visualiseToString(String [][] str, int x, int y) {
//			StringBuilder sb = new StringBuilder();
//			for (int i = 0; i < x; i++) {
//				NextSquare:
//				for (int j = 0; j < y; j++) {
//					Position p = new Position(j, i);
//					if(!variants.isEmpty()) {
//						for(VariantChap v: variants) {
//							if (v.getPosition().equals(p)) {
//								String str = null;
//								switch(v.getDirection()) {
//								case UP:str="^";break;
//								case RIGHT:str=">";break;
//								case DOWN:str="v";break;
//								case LEFT: str="<";break;
//								}
//								if (str==null) throw new IllegalArgumentException("Invalid code stored for variants");
//								else sb.append(str);
//								continue NextSquare; // one variant/actor per square
//							}
//						}
//					}
//					if (regularChap.getPosition().equals(p)) sb.append('@');
//					else sb.append(tileBoard[i][j].code());
//				}
//				sb.append('\n');
//			}
//			return sb.toString();
//		}
}
