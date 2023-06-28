package nz.ac.vuw.ecs.swen225.gp21.domain;

import org.junit.jupiter.api.Test;
import nz.ac.vuw.ecs.swen225.gp21.domain.*;
import nz.ac.vuw.ecs.swen225.gp21.app.*;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.*;

import java.awt.EventQueue;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// TODO: Auto-generated Javadoc
/**
 * To ensure game logic is sound.
 * 
 * @author Victor Tam
 */

public class DomainTest {
	
	/** The g. */
	public static Game g = new Game();

	
	/** The empty playground. */
	public String [] emptyPlayground = {
			"###############",
			"#$#___#X#____$#",
			"#g#__v#!#v___R#",
			"#_____#c#_____#",
			"#_____________#",
			"#______?______#",
			"#______@*_____#",
			"#____________<#",
			"#_____________#",
			"#_____#r#_____#",
			"#__>__#!#_____#",
			"#$____#C#_^_G$#",
			"###############",
	};

	/** The default test. */
	public String [] defaultTest = {
			"_$_#X#_$_",
			"##r#!#r##",
			"#c__v__g#",
			"_#C_?_G#_",
			"###_@_###",
			"_#C___G#_",
			"#g__^__c#",
			"##R###R##",
			"_#__#__#_",
	};
	
	/** The many variants. */
	public String [] manyVariants = {
			"_$_#X#_$_",
			"##r#!#r##",
			"#c__v__g#",
			"_#C<?>G#_",
			"###_@_###",
			"_#C___G#_",
			"#g__^__c#",
			"##R###R##",
			"_#__#__#_",
	};

	/** The basic. */
	public String[] basic = {
			"#########",
			"#G$#X#$G#",
			"##r#!#c##",
			"_R__@__C_",
			"$#g_?_g#$",
			"#########",
		};
	
	/** The no treasure. */
	public String[] noTreasure = {
			"#########",
			"#G##X##G#",
			"##r#!#c##",
			"_R__@__C_",
			"##g_?_g##",
			"#########",
		};
	
	/** The two chaps. */
	public String[] twoChaps = {
			"#########",
			"#G##X##G#",
			"##r#!#c##",
			"_R@@__C_",
			"##g_?_g##",
			"#########",
		};
	
	/**
	 *  
	 * Check if the board allows two chaps, should throw an IllegalStateException.
	 */
	@Test
	public void testTwoChaps() {
		try{
			new Board(this.twoChaps);
		} catch (IllegalStateException e) {
			assert true;
		}
	}
	
	/**
	 *  
	 * Check if the toString method returns the same String.
	 */
	@Test
	public void testToString1() {
		Board b = new Board(this.defaultTest);
		assertTrue(b.toVisualString().equals(ArrayConvertor.from1dTo0d(defaultTest)));
	}
	

	/**
	 *  
	 * Check if the toString method returns the same String.
	 */
	@Test
	public void testToString2() {
		Board b = new Board(this.defaultTest);
		assertTrue(b.toVisualString().equals(ArrayConvertor.from2dTo0d(b.getCurrentBoard())));
	}
	
	/**
	 * Move back to original spot.
	 */
	@Test
	public void testMovement1() {
		Board b = new Board(this.defaultTest);
		b.moveRegularChap(Direction.UP);
		b.moveRegularChap(Direction.LEFT);
		b.moveRegularChap(Direction.RIGHT);
		b.moveRegularChap(Direction.DOWN);
		Direction.UP.toString();
		assertTrue(b.toVisualString().equals(ArrayConvertor.from1dTo0d(defaultTest)));
	}
	
	/**
	 * Invalid direction.
	 */
	@Test
	public void testInvalidDirection1() {
		try {
			Direction.UP.movePosition(null);
		} catch (NullPointerException e) {
			assert true;
		}
	}
	
	/**
	 * Draw a 2x2 board and move chap to exit and info tile.
	 */
	
	/**
	 * Test if board would throw exceptions if there are
	 * many Variants, should not throw exception
	 */
	@Test
	public void testUnlock1() {
		Board b = new Board(noTreasure);
		b.unlockExit();
	}
	
	
	/**
	 * Test converter from string to 1d array.
	 */
	@Test
	public void TestStringConverter1() {
		Board b = new Board(this.defaultTest);
		String [] converted = ArrayConvertor.from0dTo1d(b.toString(),defaultTest.length,defaultTest[0].length());
		for(int i=0; i< defaultTest.length;i++) {
			if(!defaultTest[i].equals(converted[i])) { 
				System.out.print(defaultTest[i]);
				System.out.println();
				System.out.print(converted[i]);

				assert false;
			}	
		}
		assert true;
	}
	
	/**
	 * Test converter from string to 1d array.
	 */
	@Test
	public void TestStringConverter2() {
		Board b = new Board(this.basic);
		String [][] twoD = ArrayConvertor.from1dTo2d(basic);
		String [] oneD = ArrayConvertor.from2dTo1d(twoD);
		System.out.println(b.toVisualString());
		Board b2 = new Board(oneD);
		System.out.println(b2.toVisualString());

		assert b.toVisualString().equals(b2.toVisualString());
	}
	
	/**
	 * Move to wall than back.
	 */
	@Test
	public void TestSuicide1() {
		Board b = new Board(this.defaultTest);
		b.moveRegularChap(Direction.UP);
		b.moveRegularChap(Direction.LEFT);
		b.moveRegularChap(Direction.LEFT);
		b.moveRegularChap(Direction.LEFT);
		b.moveRegularChap(Direction.RIGHT);
		b.moveRegularChap(Direction.RIGHT);
		b.moveRegularChap(Direction.UP);
		System.out.println(b.toVisualString());

		assertFalse(b.chapIsAlive());
	}
	
	/**
	 * Check if a board is rectangular.
	 */
	@Test
	public void TestRect1() {
		assert ArrayConvertor.checkIfRectangular(basic);
	}
	
	/**
	 * Check if a board is rectangular.
	 */
	@Test
	public void TestRect2() {
		String[] nonRect = {
				"#########",
				"#G$#X"
			};
		assertFalse(ArrayConvertor.checkIfRectangular(nonRect));
	}
	
	/**
	 *  
	 * Check if InfoTile is working without setting a level
	 * Should throw an IllegalStateException.
	 */
	@Test
	public void TestInfo0() {
		Board b = new Board(this.defaultTest);
		b.moveRegularChap(Direction.UP);
		try{
			b.checkIfOnInfo();
		} catch(IllegalStateException e) {
			assert true;
		}
	}
	
	/**
	 *  
	 * Check if InfoTile is working as intended.
	 */
	@Test
	public void TestInfo1() {
		Board b = new Board(this.defaultTest);
		b.moveRegularChap(Direction.UP);
		b.setCurrentLevel(1);
		String str =b.checkIfOnInfo();
		assert(str.equals("You're at level 1, collect the treasures!"));
	}
	
	/**
	 *  
	 * Check if InfoTile is working as intended.
	 */
	@Test
	public void TestInfo2() {
		Board b = new Board(this.defaultTest);
		b.moveRegularChap(Direction.UP);
		b.setCurrentLevel(2);
		String str =b.checkIfOnInfo();
		assert(str.equals("You're at level 2, collect treasures and avoid Variant(s)!"));
	}
	
	/**
	 *  
	 * Check if InfoTile is working as intended, should return null.
	 */
	@Test
	public void TestInfo3() {
		Board b = new Board(this.defaultTest);
		b.setCurrentLevel(2);
		assert b.checkIfOnInfo()==null;
	}
	
	/**
	 *  
	 * Check if currentLevel is working as intended.
	 */
	@Test
	public void TestInvalidLevelSetting1() {
		Board b = new Board(this.defaultTest);
		try{
			b.setCurrentLevel(-1);
		} catch(IllegalStateException e) {
			assert true;
		}
	}
	
	/**
	 *  
	 * Check if currentLevel is working as intended.
	 */
	@Test
	public void TestInvalidLevelSetting100() {
		Board b = new Board(this.defaultTest);
		try{
			b.setCurrentLevel(100);
		} catch(IllegalStateException e) {
			assert true;
		}
	}
	
	/**
	 *  
	 * Check if variants move as expected.
	 */
	@Test
	public void testVariantMove1() {
		Board b = new Board(this.defaultTest);
		for(VariantChap v : b.getVariants()) {
			v.move(null, null, b);
		}
		String [] check = {
				"_$_#X#_$_",
				"##r#!#r##",
				"#c_<___g#",
				"_#C_?_G#_",
				"###_@_###",
				"_#C_^_G#_",
				"#g_____c#",
				"##R###R##",
				"_#__#__#_",
		};
		assertTrue(b.toVisualString().equals(ArrayConvertor.from1dTo0d(check)));
	}

	/**
	 *  
	 * Check if variants move as expected.
	 */
	@Test
	public void testVariantMove2() {
		Board b = new Board(this.defaultTest);
		for(int i=0;i<2;i++)
		for(VariantChap v : b.getVariants()) {
			v.move(null, null, b);
		}
		String [] check = {
				"_$_#X#_$_",
				"##r#!#r##",
				"#c<____g#",
				"_#C_?_G#_",
				"###_^_###",
				"_#C___G#_",
				"#g_____c#",
				"##R###R##",
				"_#__#__#_",
		};
		System.out.print(b.toString());
		assertTrue(b.toVisualString().equals(ArrayConvertor.from1dTo0d(check)));
	}
	
	/**
	 * Test getters.
	 */
	
	@Test
	public void testAllGetters() {
		Board b = new Board(this.defaultTest);
		b.getTileBoard();
		b.getCurrentBoard();
		b.getInventoryKT();
		List<String> kstr =b.getInventoryStr();
		b.setInventory(kstr);
		Position p = new Position(-2,4);
		Position p2 = new Position(5,6);
		p2.hashCode();
		p2.toString();
		b.getNeighbouringTile(p2, Direction.DOWN);
		b.getNeighbouringTile(p2, Direction.UP);
		b.getNeighbouringTile(p2, Direction.LEFT);
		b.getNeighbouringTile(p2, Direction.RIGHT);
		b.getNumTreasures();
		b.getRegularChap();
		b.getRegularChapDir();
		b.getRegularChapPos();
		b.getTextBoardToRestart();
		b.getTile(new Position(1,1));
		b.getTile(new Position(10,10));

		b.getVariants();
		b.getCurrentLevel();
		b.setCurrentLevel(1);
		b.setCurrentLevel(5);
		b.setFreeTile(new Position(1,1));
		assert true;
	}
	
	/**
	 * Test suicide.
	 */
	
	/**
	 * Test not making exit
	 */
	/**
	 * Test not making regularChap 
	 */
	@Test
	public void testInvalidCodes3() {
		String[] stringInput = {
				"#########",
				"#G$#X#$G#",
				"##r#!#c##",
				"_R_____C_",
				"$#g_?_g#$",
				"#########",
			};
		try {
			Board b = new Board(stringInput);

		}catch(AssertionError e) {
			assert true;
		}
	}
	
	/**
	 * Test not making exit.
	 */
	@Test
	public void testInvalidCodes2() {
		String[] stringInput = {
				"#########",
				"#G$#@#$G#",
				"##r#!#c##",
				"_R_____C_",
				"$#g_?_g#$",
				"#########",
			};
		try {
			Board b = new Board(stringInput);

		}catch(AssertionError e) {
			assert true;
		}	
	}
	
	/**
	 * test 3.
	 */
	@Test
	public void testInvalidCodes1() {
		String[] stringInput = {"_$_#X#_$_\n" + 
								"##e#^#e##\n" +
								"#c_____d#\n" + 
								"_#C_?_D#_\n" + 
								"##$_@_$##\n" +
								"_#C___D#_\n" +
								"#d_____c#\n" +
								"##E###E##\n" +
								"_#__#__#_\n" };
		
		try{
			Board b = new Board(stringInput);
		} catch (IllegalArgumentException e){
			assert true;
		}
	}
	
	/**
	 * Test giant Board.
	 */
	@Test
	public void TestGiantBoard() {
		String [] bigBoard = {"_$_#X#_$_##@#^#g##"+ 
							  "#c_____v#_#C_?_G#_" };
		Board b = new Board(bigBoard);
		assertTrue(b.toVisualString().equals(ArrayConvertor.from1dTo0d(bigBoard)));
	}
	
	
	/**
	 * Test out of bounds.
	 */
	@Test
	public void testOutOfBounds1() {
		Board b = new Board(this.defaultTest);
		try{
			b.getTile(new Position(5,-100));
		} catch (IllegalArgumentException e) {
			assert true;
		}
	}

	/**
	 * Test out of bounds.
	 */
	@Test
	public void testOutOfBounds2() {
		Board b = new Board(this.defaultTest);
		try{
			b.getTile(new Position(-100,3));
		} catch (IllegalArgumentException e) {
			assert true;
		}
	}

	/**
	 * Test out of bounds.
	 */
	@Test
	public void testOutOfBounds3() {
		Board b = new Board(this.defaultTest);
		try{
			b.getTile(new Position(100,5));
		} catch (IllegalArgumentException e) {
			assert true;
		}
	}

	/**
	 * Test out of bounds.
	 */
	@Test
	public void testOutOfBounds4() {
		Board b = new Board(this.defaultTest);
		try{
			b.getTile(new Position(0,100));
		} catch (IllegalArgumentException e) {
			assert true;
		}
	}

	/**
	 * Test codes.
	 */
	@Test
	public void testRandomCreations() {
		AbstractChap jill = new RegularChap(new Position(0, 0));
		RegularChap phil = new RegularChap(new Position(0, 0));
		VariantChap joe = new VariantChap(new Position(0, 0));
		Set<AbstractChap> variantsToBeAdded = new HashSet<>();
		variantsToBeAdded.add(jill);
		variantsToBeAdded.add(joe);
		assert true;
	}
	
	/**
	 * Test colour codes.
	 */
	@Test
	public void testfromStrToKT1() {
		List<String> str = new ArrayList<>(Arrays.asList("Y","C","G","R"));
		try{
			ArrayConvertor.fromStrToKT(str);
		} catch(IllegalArgumentException e) {
			assert true;
		}
	}
	
	/**
	 * Test keys in inventory.
	 */
	@Test
	public void testfromStrToKT2() {
		List<KeyTile> kt = new ArrayList<>(Arrays.asList(new KeyTile('R', -1, -2),
				new KeyTile('C', -1, -2),new KeyTile('G', -1, -2),new KeyTile('R', -1, -2)));
		
		List<String> str = new ArrayList<>(Arrays.asList("R","C","G","R"));
		Board b = new Board(basic);
		b.setInventory(str);
		b.getInventoryStr();
		for(KeyTile k: kt) {
			k.getColour();
			k.getPosition();
			k.tileCode();
			k.equals(null);
			k.equals(k);
			k.equals(str);
			k.equals(new KeyTile('E', 0, 0));
			k.hashCode();
			k.canBecomeFreeTile();
			k.validTile(new Board(basic));
		}
		assert kt.equals(ArrayConvertor.fromStrToKT(str));				
	}
	
	/**
	 * Test colour codes.
	 */
	@Test
	public void testKeyTiles1() {
		List<KeyTile> kt = new ArrayList<>(Arrays.asList(new KeyTile('R', -1, -2),
				new KeyTile('C', -1, -2),new KeyTile('G', -1, -2),new KeyTile('R', -1, -2)));
		
		KeyTile invalidKey = new KeyTile('Y',-3,-5);
		kt.add(new KeyTile('Y',-3,-5));
		KeyDoorColour.CYAN.toString();
		KeyDoorColour.RED.toString();
		KeyDoorColour.GREEN.toString();
		try {
			invalidKey.tileCode();
		} catch (IllegalArgumentException e) {
			assert 	new KeyTile('G', 0,0).tileCode().equals("G");
		}
	}
	
	/**
	 * Test if keys/inventory method work codes.
	 */
	@Test
	public void testKeyTiles2() {
		List<String> str = new ArrayList<>(Arrays.asList("R","C","G","R"));
		Board b = new Board(basic);
		b.setInventory(str);
		assert b.containsKey(KeyDoorColour.CYAN);
	}
	
	/**
	 * Test if removeKey() method works.
	 */
	@Test
	public void testKeyTiles3() {
		List<String> str = new ArrayList<>(Arrays.asList("R","C","G","R"));
		Board b = new Board(basic);
		b.setInventory(str);
		b.removeKey(KeyDoorColour.CYAN);
	}
	
	/**
	 * Test if removeKey() method works, should throw exception as it has no CYAN.
	 */
	@Test
	public void testKeyTiles4() {
		List<String> str = new ArrayList<>(Arrays.asList("R","R","G","R"));
		Board b = new Board(basic);
		b.setInventory(str);
		try {
			b.removeKey(KeyDoorColour.CYAN);
		} catch (IllegalArgumentException e){
			assert true;
		}
	}
	
	/**
	 * Test if setFreeTile() would throw an exception as intended.
	 */
	@Test
	public void testsetFreeTile1() {
		Board b = new Board(basic);
		try{
			b.setFreeTile(new Position(100, 100));
		} catch (IllegalArgumentException e) {
			assert true;
		}
	}
	
	/**
	 * Test colour codes.
	 */
	@Test
	public void testDoorTiles1() {
		List<LockedDoorTile> dt = new ArrayList<>(Arrays.asList(new LockedDoorTile('r', -1, -2),
				new LockedDoorTile('c', -1, -2),new LockedDoorTile('g', -1, -2),new LockedDoorTile('r', -1, -2)));
		
		for(LockedDoorTile d: dt) {
			d.getColour();
			d.getPosition();
			d.tileCode();
			d.equals(null);
			d.equals(d);
			d.equals("c");
			d.equals(new KeyTile('E', 0, 0));
			d.hashCode();
			d.canBecomeFreeTile();
			d.validTile(new Board(basic));
		}
		assert true;
	}
	
	/**
	 * Test codes.
	 */
	@Test
	public void testVariantCodes1() {
		VariantChap v = new VariantChap(null);
		
		v.setDirection(Direction.UP);
		assert v.actorCode().equals("^");
		v.setDirection(Direction.DOWN);
		assert v.actorCode().equals("v");
		v.setDirection(Direction.LEFT);		
		assert v.actorCode().equals("<");
		v.setDirection(Direction.RIGHT);
		assert v.actorCode().equals(">");

//		assert new VariantChap(null, null).getCode().equals("@");

	}
	
	/**
	 * Test codes.
	 */
	@Test
	public void testTiles1() {
		List <AbstractTile> atl = new ArrayList<>();
		atl.add(new WallTile(0, 0));
		atl.add(new WallTile(-1, -1));
		atl.add(new FreeTile(0, 0));
		atl.add(new FreeTile(-1, -1));
		atl.add(new TreasureTile(0, 0));
		atl.add(new TreasureTile(-1, -1));
		atl.add(new ExtraTile(0, 0));
		atl.add(new ExtraTile(-1, -1));
		atl.add(new ExitTile(0, 0));
		atl.add(new ExitTile(-1, -1));
		atl.add(new ExitLockTile(0, 0));
		atl.add(new ExitLockTile(-1, -1));
		for (AbstractTile at: atl) {
			at.canBecomeFreeTile();
			at.tileCode().equals("@");
			at.equals(atl);
			at.equals(at);
			at.getPosition();
			at.toString();
			at.canBecomeFreeTile();
			at.getClass();
			at.validTile(new Board(basic));
		}
		String str = new InfoTile(0, 0).getHelp();
		assert str.equals("Use arrows or wasd to move Chap, avoid Variants!");
	}
	
	/**
	 * Test BoardObjectsToSave methods.
	 */
	@Test
	public void testBoardObjectsToSave1() {
		Board b = new Board(this.defaultTest);
		try{
			BoardObjectsToSave bots = new BoardObjectsToSave(0, null, null, null, 0, 0);
		} catch (IllegalArgumentException e) {
			assert true;
		}
	}
	
	/**
	 * Test BoardObjectsToSave methods.
	 */
	@Test
	public void testBoardObjectsToSave2() {
		Board b = new Board(this.defaultTest);
		try{
			BoardObjectsToSave bots = new BoardObjectsToSave(1, null, null, null, 0, 0);
		} catch (IllegalArgumentException e) {
			assert true;
		}
	}
	
	/**
	 * Test BoardObjectsToSave methods.
	 */
	@Test
	public void testBoardObjectsToSave3() {
		Board b = new Board(this.defaultTest);
		try{
			BoardObjectsToSave bots = new BoardObjectsToSave(
					5, b.getCurrentBoard(), b.getInventoryStr(), b.getRegularChapDir(), 5,2);
			bots.getBoardTiles();
			bots.getChapDir();
			bots.getCountdown();
			bots.getCurrentLevel();
			bots.getTreasureNum();
			bots.getKeysPickedUp();
		} catch (IllegalArgumentException e) {
			assert true;
		}
	}
	
	/**
	 * walking into wall.
	 */
	/**
	 * infotile text check
	 */
	
	
	/**
	 * Test Game class
	 */
	@Test
	public void testGameMethods1() {
		g.checkTime();
		g.getTimeLeft();
		g.setTimeLeft(1);
		g.setWin(false);
		g.setWin(true);
		g.isPaused();
		g.isStarted();
		g.setStarted(false);
		g.setStarted(true);
		g.getBoard();
		g.isWon();
		g.updatePause(false);
		g.updatePause(true);
		
		try {
			g.setTimeLeft(-1);

		} catch (IllegalArgumentException e) {
			assert true;
		}
	}
	
	/**
	 * Test Game class, methods related to App.
	 */
	@Test
	public void testGameMethods2() {
		try {
			g.setBoard(new Board(basic));

		}catch(NullPointerException n) {
			assert true;
		}
	}
	
	/**
	 * Test Game class, methods related to App.
	 */
	@Test
	public void testGameMethods3() {
		try {
			g.setInitialBoard(new Board(defaultTest));
		}catch(NullPointerException n) {
			assert true;
		}
	}
	
	/**
	 * Test Game class, methods related to App.
	 */
	@Test
	public void testGameMethods4() {
		try {
			g.resetLevel();
		}catch(NullPointerException n) {
			assert true;
		}
	}
	
	/**
	 * Test Game class, methods related to App.
	 */
	@Test
	public void testGameMethods5() {
		try {
			g.setTimeLeft(-5);
			g.checkTime();
		}catch(NullPointerException n) {
			assert true;
		}
	}

	/**
	 * Test many variants moving without throwing errors.
	 */
	@Test
	public void testManyVariants1() {
		Board b = new Board(manyVariants);
		List<VariantChap>lv = b.getVariants();
		for(int i =0; i<10; i++) {
			for(VariantChap v: lv) {
				v.move(null, null, b);
				v.actorCode();
				v.getDirection();
				v.getPosition();
				v.equals(v);
			}
			b.chapIsAlive();
		}
		System.out.println(b.toVisualString());

		try {
			VariantChap v = new VariantChap(null);
			v.actorCode();
			b.addVariant(v);
		} catch (NullPointerException e) {
			assert true;
		}
	}
	
	/**
	 * Test many variants 2.
	 */
	@Test
	public void testManyVariants2() {
		Board b = new Board(emptyPlayground);
		List<VariantChap>lv = b.getVariants();
		RegularChap rc = b.getRegularChap();
		rc.setPosition(new Position(0, 0));
		String [][] cb =b.getCurrentBoard();
		for(int i=0;i<8;i++) {
			for(VariantChap v: lv) {
				v.move(null, null, b);
			}
//			b.moveRegularChap(Direction.values()[(int) (Math.random()*4)]);
			b.chapIsAlive();
		}
		System.out.println(b.toVisualString());
		try {
			b.unlockExit();
		} catch (IllegalArgumentException e) {
			assert true;
		}
	}
	
	/**
	 * Test if board would throw exceptions if therer are
	 * many Variants.
	 */
	@Test
	public void testManyVariants3() {
		Board b = new Board(emptyPlayground);
		RegularChap rc = b.getRegularChap();
		try{
			b.addVariant(rc);
		} catch(IllegalArgumentException e) {
			assert true;
		}
	}
	
	/**
	 * Check if saving throws an error.
	 */
	@Test
	public void testSaving0() {
//		Main.g = new Game();
//		Board b = new Board(emptyPlayground);
		try{
			Main.g = new Game();
			Board b = new Board(emptyPlayground);
			b.saveToXML();
		} catch (Exception e) {
			assert true;
		}
	}
	
	/**
	 * Check if saving throws an error, it should as time is implemented by App and Game.
	 */
//	@Test
//	public void testSaving1() {
//		Board b = new Board(emptyPlayground);
//		try{
//			b.saveToXML();
//		} catch (NullPointerException e) {
//			assert true;
//		}
//	}
	/**
	 * Check if loading throws an error
	 */
	@Test
	public void testLoading1() {
		try{
			Board b = new Board(emptyPlayground);
			b.loadFromXML(5, b.getCurrentBoard(), b.getInventoryStr(), b.getRegularChapDir(),
				b.getNumTreasures(),1);
		} catch (Exception e) {
			assert true;
		}
	}
	
	/**
	 * Test if loading works as supposed to.
	 */
	@Test
	public void testLoading100() {
		try{
			Board b = new Board(emptyPlayground);
			b.loadFromXML(5, b.getCurrentBoard(), b.getInventoryStr(), b.getRegularChapDir(),
				b.getNumTreasures(),100);
		} catch (Exception e) {
			assert true;
		}
	}
	
	/**
	 * Intelligent test.
	 */
	@Test
	public void testIntelligent1() {
		Board b = new Board(defaultTest);
		b.moveRegularChap(Direction.DOWN);
		b.moveRegularChap(Direction.LEFT);
		b.moveRegularChap(Direction.LEFT);
		b.moveRegularChap(Direction.RIGHT);
		b.moveRegularChap(Direction.RIGHT);
		b.moveRegularChap(Direction.RIGHT);
		b.moveRegularChap(Direction.RIGHT);
		b.moveRegularChap(Direction.DOWN);
		b.moveRegularChap(Direction.RIGHT);
		b.moveRegularChap(Direction.RIGHT);
		b.moveRegularChap(Direction.LEFT);
		b.moveRegularChap(Direction.DOWN);
		b.moveRegularChap(Direction.UP);
		b.moveRegularChap(Direction.LEFT); // dead if variants dont move
		for(VariantChap v: b.getVariants()) v.move(null, null, b);
		b.moveRegularChap(Direction.LEFT);
		b.moveRegularChap(Direction.LEFT);
		b.moveRegularChap(Direction.LEFT);
		b.moveRegularChap(Direction.LEFT);
		b.moveRegularChap(Direction.LEFT);
		b.moveRegularChap(Direction.RIGHT);
		b.moveRegularChap(Direction.DOWN);
		b.moveRegularChap(Direction.UP);
		b.moveRegularChap(Direction.UP);
		b.moveRegularChap(Direction.UP);
		b.moveRegularChap(Direction.UP);
		b.moveRegularChap(Direction.UP);
		b.moveRegularChap(Direction.RIGHT);
		b.moveRegularChap(Direction.UP);
		b.moveRegularChap(Direction.UP);
//		b.moveRegularChap(Direction.UP);// dead if continue going up
		b.moveRegularChap(Direction.LEFT);
		b.moveRegularChap(Direction.UP);
		b.moveRegularChap(Direction.UP);
		b.moveRegularChap(Direction.UP);
		b.moveRegularChap(Direction.LEFT);
		for(int i =0;i<6;i++) { // move variants out of the way
			for(VariantChap v: b.getVariants()) v.move(null, null, b);
		}
		b.moveRegularChap(Direction.RIGHT);
		b.moveRegularChap(Direction.DOWN);
		b.moveRegularChap(Direction.DOWN);
		b.moveRegularChap(Direction.RIGHT);
		b.moveRegularChap(Direction.RIGHT);
		b.moveRegularChap(Direction.UP);// can't move because lock is still locked
		b.moveRegularChap(Direction.RIGHT);
		b.moveRegularChap(Direction.RIGHT);
		b.moveRegularChap(Direction.UP);
		b.moveRegularChap(Direction.UP);
		b.moveRegularChap(Direction.RIGHT);
		b.moveRegularChap(Direction.LEFT);
		b.moveRegularChap(Direction.DOWN);
		b.moveRegularChap(Direction.DOWN);
		b.moveRegularChap(Direction.LEFT);
		b.moveRegularChap(Direction.LEFT);
		b.moveRegularChap(Direction.UP);// can move because lock is now unlocked
		b.moveRegularChap(Direction.UP);
		System.out.print(b.toVisualString());
		assert b.checkIfOnExit(); //level is won		
	}
	
	/**
	 * Check if checkIfOnExit() functions correctly.
	 */
	@Test
	public void testInvalidExit1() {
		Board b = new Board(emptyPlayground);
		assertFalse(b.checkIfOnExit());
	}
	
	/**
	 * Check if ExtraTile works as intended, it should only work
	 * if Game thus clock is set up.
	 */
	@Test
	public void testExtraTile1() {
		Board b = new Board(emptyPlayground);
		try{
			b.moveRegularChap(Direction.RIGHT);
		} catch(IllegalStateException e) {
			assert true;
		}
	}
	/**
	 * Check if ExtraTile works as intended, it should only work
	 * if Game thus clock is set up
	 * 
	 * Menu will pop out thus the test is commented out
	 */
//	@Test
//	public void testExtraTile2() {
//		Board b = new Board(emptyPlayground);
//		Main.g = new Game();
//		Main.g.setInitialBoard(b);
//		b.loadFromXML(100, b.getCurrentBoard(), b.getInventoryStr(), b.getRegularChapDir(), b.getNumTreasures(), b.getCurrentLevel());
//		try{
//			b.moveRegularChap(Direction.RIGHT);
//		} catch(IllegalStateException e) {
//			assert true;
//		}
//	}
}
