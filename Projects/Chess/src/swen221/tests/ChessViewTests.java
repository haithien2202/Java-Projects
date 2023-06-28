package swen221.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static swen221.tests.ChessViewTests.check;

import java.util.List;

import org.junit.Test;

import swen221.chessview.Board;
import swen221.chessview.ChessGame;
import swen221.chessview.Position;
import swen221.chessview.moves.Castling;
import swen221.chessview.moves.Check;
import swen221.chessview.moves.SinglePieceMove;
import swen221.chessview.pieces.Bishop;
import swen221.chessview.pieces.King;
import swen221.chessview.pieces.Knight;
import swen221.chessview.pieces.Pawn;
import swen221.chessview.pieces.Piece;
import swen221.chessview.pieces.Queen;
import swen221.chessview.pieces.Rook;

public class ChessViewTests {

	// ================================================
	// Valid Tests
	// ================================================

	@Test public void testValid_01() {
		String input =
			"a2-a3\n" +
			"";
		String output =
			"8|r|n|b|q|k|b|n|r|\n" +
			"7|p|p|p|p|p|p|p|p|\n" +
			"6|_|_|_|_|_|_|_|_|\n" +
			"5|_|_|_|_|_|_|_|_|\n" +
			"4|_|_|_|_|_|_|_|_|\n" +
			"3|P|_|_|_|_|_|_|_|\n" +
			"2|_|P|P|P|P|P|P|P|\n" +
			"1|R|N|B|Q|K|B|N|R|\n" +
			"  a b c d e f g h";

		check(input,output);
	}
	@Test public void testValid_02() {
		String input =
			"a2-a3 b7-b6\n" +
			"";
		String output =
			"8|r|n|b|q|k|b|n|r|\n" +
			"7|p|_|p|p|p|p|p|p|\n" +
			"6|_|p|_|_|_|_|_|_|\n" +
			"5|_|_|_|_|_|_|_|_|\n" +
			"4|_|_|_|_|_|_|_|_|\n" +
			"3|P|_|_|_|_|_|_|_|\n" +
			"2|_|P|P|P|P|P|P|P|\n" +
			"1|R|N|B|Q|K|B|N|R|\n" +
			"  a b c d e f g h";

		check(input,output);
	}
	@Test public void testValid_03() {
		String input =
			"a2-a4 b7-b5\n" +
			"";
		String output =
			"8|r|n|b|q|k|b|n|r|\n" +
			"7|p|_|p|p|p|p|p|p|\n" +
			"6|_|_|_|_|_|_|_|_|\n" +
			"5|_|p|_|_|_|_|_|_|\n" +
			"4|P|_|_|_|_|_|_|_|\n" +
			"3|_|_|_|_|_|_|_|_|\n" +
			"2|_|P|P|P|P|P|P|P|\n" +
			"1|R|N|B|Q|K|B|N|R|\n" +
			"  a b c d e f g h";

		check(input,output);
	}
	@Test public void testValid_04() {
		String input =
			"d2-d4 d7-d5\n" +
			"e2-e4 d5xe4\n" +
			"";
		String output =
			"8|r|n|b|q|k|b|n|r|\n" +
			"7|p|p|p|_|p|p|p|p|\n" +
			"6|_|_|_|_|_|_|_|_|\n" +
			"5|_|_|_|_|_|_|_|_|\n" +
			"4|_|_|_|P|p|_|_|_|\n" +
			"3|_|_|_|_|_|_|_|_|\n" +
			"2|P|P|P|_|_|P|P|P|\n" +
			"1|R|N|B|Q|K|B|N|R|\n" +
			"  a b c d e f g h";

		check(input,output);
	}
	@Test public void testValid_05() {
		String input =
			"d2-d3 d7-d5\n" +
			"e2-e4 d5xe4\n" +
			"d3xe4\n" +
			"";
		String output =
			"8|r|n|b|q|k|b|n|r|\n" +
			"7|p|p|p|_|p|p|p|p|\n" +
			"6|_|_|_|_|_|_|_|_|\n" +
			"5|_|_|_|_|_|_|_|_|\n" +
			"4|_|_|_|_|P|_|_|_|\n" +
			"3|_|_|_|_|_|_|_|_|\n" +
			"2|P|P|P|_|_|P|P|P|\n" +
			"1|R|N|B|Q|K|B|N|R|\n" +
			"  a b c d e f g h";

		check(input,output);
	}
	@Test public void testValid_06() {
		String input =
			"Nb1-c3\n" +
			"";
		String output =
			"8|r|n|b|q|k|b|n|r|\n" +
			"7|p|p|p|p|p|p|p|p|\n" +
			"6|_|_|_|_|_|_|_|_|\n" +
			"5|_|_|_|_|_|_|_|_|\n" +
			"4|_|_|_|_|_|_|_|_|\n" +
			"3|_|_|N|_|_|_|_|_|\n" +
			"2|P|P|P|P|P|P|P|P|\n" +
			"1|R|_|B|Q|K|B|N|R|\n" +
			"  a b c d e f g h";

		check(input,output);
	}
	@Test public void testValid_07() {
		String input =
			"Nb1-a3\n" +
			"";
		String output =
			"8|r|n|b|q|k|b|n|r|\n" +
			"7|p|p|p|p|p|p|p|p|\n" +
			"6|_|_|_|_|_|_|_|_|\n" +
			"5|_|_|_|_|_|_|_|_|\n" +
			"4|_|_|_|_|_|_|_|_|\n" +
			"3|N|_|_|_|_|_|_|_|\n" +
			"2|P|P|P|P|P|P|P|P|\n" +
			"1|R|_|B|Q|K|B|N|R|\n" +
			"  a b c d e f g h";

		check(input,output);
	}
	@Test public void testValid_08() {
		String input =
			"d2-d4 Nb8-c6\n" +
			"e2-e4 Nc6xd4\n" +
			"";
		String output =
			"8|r|_|b|q|k|b|n|r|\n" +
			"7|p|p|p|p|p|p|p|p|\n" +
			"6|_|_|_|_|_|_|_|_|\n" +
			"5|_|_|_|_|_|_|_|_|\n" +
			"4|_|_|_|n|P|_|_|_|\n" +
			"3|_|_|_|_|_|_|_|_|\n" +
			"2|P|P|P|_|_|P|P|P|\n" +
			"1|R|N|B|Q|K|B|N|R|\n" +
			"  a b c d e f g h";

		check(input,output);
	}
	@Test public void testValid_09() {
		String input =
			"Nb1-c3 e7-e5\n" +
			"e2-e3 e5-e4\n" +
			"Nc3xe4\n" +
			"";
		String output =
			"8|r|n|b|q|k|b|n|r|\n" +
			"7|p|p|p|p|_|p|p|p|\n" +
			"6|_|_|_|_|_|_|_|_|\n" +
			"5|_|_|_|_|_|_|_|_|\n" +
			"4|_|_|_|_|N|_|_|_|\n" +
			"3|_|_|_|_|P|_|_|_|\n" +
			"2|P|P|P|P|_|P|P|P|\n" +
			"1|R|_|B|Q|K|B|N|R|\n" +
			"  a b c d e f g h";

		check(input,output);
	}
	@Test public void testValid_10() {
		String input =
			"d2-d3 d7-d5\n" +
			"Bc1-g5 Bc8-g4\n" +
			"";
		String output =
			"8|r|n|_|q|k|b|n|r|\n" +
			"7|p|p|p|_|p|p|p|p|\n" +
			"6|_|_|_|_|_|_|_|_|\n" +
			"5|_|_|_|p|_|_|B|_|\n" +
			"4|_|_|_|_|_|_|b|_|\n" +
			"3|_|_|_|P|_|_|_|_|\n" +
			"2|P|P|P|_|P|P|P|P|\n" +
			"1|R|N|_|Q|K|B|N|R|\n" +
			"  a b c d e f g h";

		check(input,output);
	}
	@Test public void testValid_11() {
		String input =
			"d2-d3 d7-d5\n" +
			"Bc1-g5 Bc8-g4\n" +
			"Bg5-h4\n" +
			"";
		String output =
			"8|r|n|_|q|k|b|n|r|\n" +
			"7|p|p|p|_|p|p|p|p|\n" +
			"6|_|_|_|_|_|_|_|_|\n" +
			"5|_|_|_|p|_|_|_|_|\n" +
			"4|_|_|_|_|_|_|b|B|\n" +
			"3|_|_|_|P|_|_|_|_|\n" +
			"2|P|P|P|_|P|P|P|P|\n" +
			"1|R|N|_|Q|K|B|N|R|\n" +
			"  a b c d e f g h";

		check(input,output);
	}
	@Test public void testValid_12() {
		String input =
			"d2-d3 d7-d5\n" +
			"Bc1-g5 Bc8-g4\n" +
			"Bg5xe7\n" +
			"";
		String output =
			"8|r|n|_|q|k|b|n|r|\n" +
			"7|p|p|p|_|B|p|p|p|\n" +
			"6|_|_|_|_|_|_|_|_|\n" +
			"5|_|_|_|p|_|_|_|_|\n" +
			"4|_|_|_|_|_|_|b|_|\n" +
			"3|_|_|_|P|_|_|_|_|\n" +
			"2|P|P|P|_|P|P|P|P|\n" +
			"1|R|N|_|Q|K|B|N|R|\n" +
			"  a b c d e f g h";

		check(input,output);
	}
	@Test public void testValid_13() {
		String input =
			"e2-e4 e7-e5\n" +
			"Qd1-g4 Qd8-h4\n" +
			"Qg4-f4 Qh4-h6\n" +
			"Qf4-f6 Qh6xh2\n" +
			"";
		String output =
			"8|r|n|b|_|k|b|n|r|\n" +
			"7|p|p|p|p|_|p|p|p|\n" +
			"6|_|_|_|_|_|Q|_|_|\n" +
			"5|_|_|_|_|p|_|_|_|\n" +
			"4|_|_|_|_|P|_|_|_|\n" +
			"3|_|_|_|_|_|_|_|_|\n" +
			"2|P|P|P|P|_|P|P|q|\n" +
			"1|R|N|B|_|K|B|N|R|\n" +
			"  a b c d e f g h";

		check(input,output);
	}
	@Test public void testValid_14() {
		String input =
			"a2-a4 h7-h5\n" +
			"Ra1-a3 Rh8-h6\n" +
			"Ra3-d3 Rh6-g6\n" +
			"Rd3xd7\n" +
			"";
		String output =
			"8|r|n|b|q|k|b|n|_|\n" +
			"7|p|p|p|R|p|p|p|_|\n" +
			"6|_|_|_|_|_|_|r|_|\n" +
			"5|_|_|_|_|_|_|_|p|\n" +
			"4|P|_|_|_|_|_|_|_|\n" +
			"3|_|_|_|_|_|_|_|_|\n" +
			"2|_|P|P|P|P|P|P|P|\n" +
			"1|_|N|B|Q|K|B|N|R|\n" +
			"  a b c d e f g h";

		check(input,output);
	}

	// ================================================
	// Invalid Tests
	// ================================================

	@Test public void testInvalid_01() {
		String input =
			"a2-a4 a7-a4\n" +		
			"";
		String output =
			"8|r|n|b|q|k|b|n|r|\n" +
			"7|p|p|p|p|p|p|p|p|\n" +
			"6|_|_|_|_|_|_|_|_|\n" +
			"5|_|_|_|_|_|_|_|_|\n" +
			"4|P|_|_|_|_|_|_|_|\n" +
			"3|_|_|_|_|_|_|_|_|\n" +
			"2|_|P|P|P|P|P|P|P|\n" +
			"1|R|N|B|Q|K|B|N|R|\n" +
			"  a b c d e f g h";

		check(input,output);
	}
	
	@Test public void testInvalid_02() {
		String input =
			"a2-a4 b7-b5\n" +
			"a4xb5 h7-h5\n" +
			"";
		String output =
			"8|r|n|b|q|k|b|n|r|\n" +
			"7|p|_|p|p|p|p|p|_|\n" +
			"6|_|_|_|_|_|_|_|_|\n" +
			"5|_|P|_|_|_|_|_|p|\n" +
			"4|_|_|_|_|_|_|_|_|\n" +
			"3|_|_|_|_|_|_|_|_|\n" +
			"2|_|P|P|P|P|P|P|P|\n" +
			"1|R|N|B|Q|K|B|N|R|\n" +
			"  a b c d e f g h";

		check(input,output);
	}
	
	@Test public void testInvalid_03() {
		String input =
			"b1-a4" +
			"";
		String output =
			"8|r|n|b|q|k|b|n|r|\n" +
			"7|p|p|p|p|p|p|p|p|\n" +
			"6|_|_|_|_|_|_|_|_|\n" +
			"5|_|_|_|_|_|_|_|_|\n" +
			"4|_|_|_|_|_|_|_|_|\n" +
			"3|_|_|_|_|_|_|_|_|\n" +
			"2|P|P|P|P|P|P|P|P|\n" +
			"1|R|N|B|Q|K|B|N|R|\n" +
			"  a b c d e f g h";

		check(input,output);
	}
	
	@Test public void testValid_15() {
		String input =
			"Nb1-c3\n" +
			"";
		String output =
			"8|r|n|b|q|k|b|n|r|\n" +
			"7|p|p|p|p|p|p|p|p|\n" +
			"6|_|_|_|_|_|_|_|_|\n" +
			"5|_|_|_|_|_|_|_|_|\n" +
			"4|_|_|_|_|_|_|_|_|\n" +
			"3|_|_|N|_|_|_|_|_|\n" +
			"2|P|P|P|P|P|P|P|P|\n" +
			"1|R|_|B|Q|K|B|N|R|\n" +
			"  a b c d e f g h";

		check(input,output);
	}
	
	@Test public void testInvalid_invalidTake2() {
		// two white moves in a row
		String input =
			"Ng1-h3 h7-h5\n" +
			"e2-e4 g7-g5\n" +
			"Bf1-e2 f7-f5\n" +
			"O-O h3xg5\n" +
			"";
		String output =
			"8|r|n|b|q|k|b|n|r|\n" +
			"7|p|p|p|p|p|_|_|_|\n" +
			"6|_|_|_|_|_|_|_|_|\n" +
			"5|_|_|_|_|_|p|p|p|\n" +
			"4|_|_|_|_|P|_|_|_|\n" +
			"3|_|_|_|_|_|_|_|N|\n" +
			"2|P|P|P|P|B|P|P|P|\n" +
			"1|R|N|B|Q|_|R|K|_|\n" +
			"  a b c d e f g h";

		check(input,output);
	}
	
	
	@Test public void testInvalid_oppositeCastling() {
		//castling
		String input =
			"Ng1-h3 h7-h5\n" +
			"e2-e4 g7-g5\n" +
			"Bf1-e2 f7-f5\n" +
			"O-O-O\n" +
			"";
		String output =
			"8|r|n|b|q|k|b|n|r|\n" +
			"7|p|p|p|p|p|_|_|_|\n" +
			"6|_|_|_|_|_|_|_|_|\n" +
			"5|_|_|_|_|_|p|p|p|\n" +
			"4|_|_|_|_|P|_|_|_|\n" +
			"3|_|_|_|_|_|_|_|N|\n" +
			"2|P|P|P|P|B|P|P|P|\n" +
			"1|R|N|B|Q|K|_|_|R|\n" +
			"  a b c d e f g h";

		check(input,output);
	}
	@Test public void testInvalid_blackMovesFirst() {
		//castling
		String input =
				"e7-e5\n" +
				"";
			String output =
				"8|r|n|b|q|k|b|n|r|\n" +
				"7|p|p|p|p|p|p|p|p|\n" +
				"6|_|_|_|_|_|_|_|_|\n" +
				"5|_|_|_|_|_|_|_|_|\n" +
				"4|_|_|_|_|_|_|_|_|\n" +
				"3|_|_|_|_|_|_|_|_|\n" +
				"2|P|P|P|P|P|P|P|P|\n" +
				"1|R|N|B|Q|K|B|N|R|\n" +
				"  a b c d e f g h";

			check(input,output);
	}
	
	@Test public void testInvalid_noWhiteMove() {
		//castling
		String input =
				"e7-e5 e5-e4\n" +
				"";
			String output =
				"8|r|n|b|q|k|b|n|r|\n" +
				"7|p|p|p|p|p|p|p|p|\n" +
				"6|_|_|_|_|_|_|_|_|\n" +
				"5|_|_|_|_|_|_|_|_|\n" +
				"4|_|_|_|_|_|_|_|_|\n" +
				"3|_|_|_|_|_|_|_|_|\n" +
				"2|P|P|P|P|P|P|P|P|\n" +
				"1|R|N|B|Q|K|B|N|R|\n" +
				"  a b c d e f g h";

			check(input,output);
	}
	@Test public void testInvalid_noBlackMove() {
		//castling
		String input =
				"e2-e4 e4-e5\n" +
				"";
			String output =
				"8|r|n|b|q|k|b|n|r|\n" +
				"7|p|p|p|p|p|p|p|p|\n" +
				"6|_|_|_|_|_|_|_|_|\n" +
				"5|_|_|_|_|_|_|_|_|\n" +
				"4|_|_|_|_|P|_|_|_|\n" +
				"3|_|_|_|_|_|_|_|_|\n" +
				"2|P|P|P|P|_|P|P|P|\n" +
				"1|R|N|B|Q|K|B|N|R|\n" +
				"  a b c d e f g h";

			check(input,output);
	}
	@Test public void testInvalid_king1() {
		String input =
			"a2-a4 k8-k7\n" +
			"";
		String output =
			"8|r|n|b|q|k|b|n|r|\n" +
			"7|p|p|p|p|p|p|p|p|\n" +
			"6|_|_|_|_|_|_|_|_|\n" +
			"5|_|_|_|_|_|_|_|_|\n" +
			"4|P|_|_|_|_|_|_|_|\n" +
			"3|_|_|_|_|_|_|_|_|\n" +
			"2|_|P|P|P|P|P|P|P|\n" +
			"1|R|N|B|Q|K|B|N|R|\n" +
			"  a b c d e f g h";

		check(input,output);
	}
	
	@Test public void testInvalid_enP1() {
		String input =
			"b2-b4 h7-h5\n" +
			"b4-b5 c7-c5\n" +
			"b5xc6ep h5xg4ep\n" +
			"";
		String output =
			"8|r|n|b|q|k|b|n|r|\n" +
			"7|p|p|_|p|p|p|p|_|\n" +
			"6|_|_|P|_|_|_|_|_|\n" +
			"5|_|_|_|_|_|_|_|p|\n" +
			"4|_|_|_|_|_|_|_|_|\n" +
			"3|_|_|_|_|_|_|_|_|\n" +
			"2|P|_|P|P|P|P|P|P|\n" +
			"1|R|N|B|Q|K|B|N|R|\n" +
			"  a b c d e f g h";

		check(input,output);
	}
	
	
	@Test public void testInvalid_enP2() {
		String input =
			"b2-b4 h7-h5\n" +
			"b4-b5 c7-c5\n" +
			"b5xc6ep h5-h4\n" +
			"g2-g3 h4xg3ep\n" +
			"";
		String output =
			"8|r|n|b|q|k|b|n|r|\n" +
			"7|p|p|_|p|p|p|p|_|\n" +
			"6|_|_|P|_|_|_|_|_|\n" +
			"5|_|_|_|_|_|_|_|_|\n" +
			"4|_|_|_|_|_|_|_|p|\n" +
			"3|_|_|_|_|_|_|P|_|\n" +
			"2|P|_|P|P|P|P|_|P|\n" +
			"1|R|N|B|Q|K|B|N|R|\n" +
			"  a b c d e f g h";

		check(input,output);
	}
	
	 @Test
	    public void toStringCheckTest(){
	        Piece pawn = new Pawn(true);
	        Position startPos = new Position(4,4);
	        Position endPos = new Position(5,5);
	        SinglePieceMove move = new SinglePieceMove(pawn,startPos,endPos);
	        Check check = new Check(move);
	        assertEquals("d4-e5+",check.toString());
	    }

	    @Test
	    public void correctMoveUsedTest(){
	        Piece pawn = new Pawn(true);
	        Position startPos = new Position(4,4);
	        Position endPos = new Position(5,5);
	        SinglePieceMove move = new SinglePieceMove(pawn,startPos,endPos);
	        Check check = new Check(move);
	        assertEquals(move,check.move());
	    }
	    
	    @Test
	    public void queenSideExpectedStringTest(){
	        Castling castling = new Castling(true,true);
	        assertEquals("O-O",castling.toString());
	    }
	    
	    @Test
	    public void kingSideExpectedStringTest(){
	        Castling castling = new Castling(true,false);
	        assertEquals("O-O-O",castling.toString());
	    }
	    
	    @Test
	    public void checkBlackQueenSideCastling(){
	            String input =
	                    "a2-a4 a7-a5\n" +
	                            "b2-b4 b7-b5\n" +
	                            "c2-c4 c7-c5\n" +
	                            "d2-d4 d7-d5\n" +
	                            "e2-e4 e7-e5\n" +
	                            "f2-f4 f7-f5\n" +
	                            "g2-g4 g7-g5\n" +
	                            "h2-h4 h7-h5\n" +
	                            "Ra1-a2 Nb8-a6\n" +
	                            "Bc1-d2 Bc8-d7\n" +
	                            "Ra2-a3 Qd8-c7\n" +
	                            "Rh1-h2 O-O-O\n" +
	                            "";
	            String output =
	                            "8|_|_|k|r|_|b|n|r|\n" +
	                            "7|_|_|q|b|_|_|_|_|\n" +
	                            "6|n|_|_|_|_|_|_|_|\n" +
	                            "5|p|p|p|p|p|p|p|p|\n" +
	                            "4|P|P|P|P|P|P|P|P|\n" +
	                            "3|R|_|_|_|_|_|_|_|\n" +
	                            "2|_|_|_|B|_|_|_|R|\n" +
	                            "1|_|N|_|Q|K|B|N|_|\n" +
	                            "  a b c d e f g h";
	            check(input,output);
	    }

	    @Test
	    public void checkBlackKingSideCastling(){
	        String input =
	        "a2-a4 a7-a5\n" +
	                "b2-b4 b7-b5\n" +
	                "c2-c4 c7-c5\n" +
	                "d2-d4 d7-d5\n" +
	                "e2-e4 e7-e5\n" +
	                "f2-f4 f7-f5\n" +
	                "g2-g4 g7-g5\n" +
	                "h2-h4 h7-h5\n" +
	                "Ra1-a2 Ng8-h6\n" +
	                "Bc1-d2 Bf8-e7\n" +
	                "Ra2-a3 O-O\n" +
	                "";
	        String output =
	                        "8|r|n|b|q|_|r|k|_|\n" +
	                        "7|_|_|_|_|b|_|_|_|\n" +
	                        "6|_|_|_|_|_|_|_|n|\n" +
	                        "5|p|p|p|p|p|p|p|p|\n" +
	                        "4|P|P|P|P|P|P|P|P|\n" +
	                        "3|R|_|_|_|_|_|_|_|\n" +
	                        "2|_|_|_|B|_|_|_|_|\n" +
	                        "1|_|N|_|Q|K|B|N|R|\n" +
	                        "  a b c d e f g h";
	        check(input,output);
	    }

	    @Test
	    public void checkWhiteQueenSideCastling() {
	        String input =
	                "a2-a4 a7-a5\n" +
	                        "b2-b4 b7-b5\n" +
	                        "c2-c4 c7-c5\n" +
	                        "d2-d4 d7-d5\n" +
	                        "e2-e4 e7-e5\n" +
	                        "f2-f4 f7-f5\n" +
	                        "g2-g4 g7-g5\n" +
	                        "h2-h4 h7-h5\n" +
	                        "Nb1-c3 Nb8-c6\n" +
	                        "Bc1-a3 Bc8-a6\n" +
	                        "Qd1-d3 Qd8-d6\n" +
	                        "O-O-O\n"+
	                        "";
	        String output =
	                "8|r|_|_|_|k|b|n|r|\n" +
	                        "7|_|_|_|_|_|_|_|_|\n" +
	                        "6|b|_|n|q|_|_|_|_|\n" +
	                        "5|p|p|p|p|p|p|p|p|\n" +
	                        "4|P|P|P|P|P|P|P|P|\n" +
	                        "3|B|_|N|Q|_|_|_|_|\n" +
	                        "2|_|_|_|_|_|_|_|_|\n" +
	                        "1|_|_|K|R|_|B|N|R|\n" +
	                        "  a b c d e f g h";

	        check(input, output);
	    }

	    @Test
	    public void kingWhiteSideCastling(){
	        String input =
	                "a2-a4 a7-a5\n" +
	                        "b2-b4 b7-b5\n" +
	                        "c2-c4 c7-c5\n" +
	                        "d2-d4 d7-d5\n" +
	                        "e2-e4 e7-e5\n" +
	                        "f2-f4 f7-f5\n" +
	                        "g2-g4 g7-g5\n" +
	                        "h2-h4 h7-h5\n" +
	                        "Ng1-h3 Ng8-h6\n" +
	                        "Bf1-g2 Bf8-g7\n" +
	                        "O-O\n"+
	                        "";
	        String output =
	                "8|r|n|b|q|k|_|_|r|\n" +
	                        "7|_|_|_|_|_|_|b|_|\n" +
	                        "6|_|_|_|_|_|_|_|n|\n" +
	                        "5|p|p|p|p|p|p|p|p|\n" +
	                        "4|P|P|P|P|P|P|P|P|\n" +
	                        "3|_|_|_|_|_|_|_|N|\n" +
	                        "2|_|_|_|_|_|_|B|_|\n" +
	                        "1|R|N|B|Q|_|R|K|_|\n" +
	                        "  a b c d e f g h";

	        check(input, output);
	    }
	    
	    @Test
	    public void kingsideCastlingCheckTest(){
	        String input =
	                "a2-a4 f7-f5\n"+
	                        "Ra1-a3 a7-a5\n" +
	                        "Ra3-f3 Ra8-a7\n" +
	                        "Rf3xf5 Ra7-a8\n" +
	                        "Rf5xBf8 Ke8xRf8\n" +
	                        "g2-g4 Ra8-a6\n" +
	                        "Ng1-f3 Ra6-f6\n" +
	                        "b2-b3 Rf6xNf3\n" +
	                        "b3-b4 Rf3xf2\n" +
	                        "b4-b5 Rf2-f5\n" +
	                        "Bf1-h3 Rf5-c5\n" +
	                        "O-O+\n" +
	                        "";
	        String output =
	                        "8|_|n|b|q|_|k|n|r|\n" +
	                        "7|_|p|p|p|p|_|p|p|\n" +
	                        "6|_|_|_|_|_|_|_|_|\n" +
	                        "5|p|P|r|_|_|_|_|_|\n" +
	                        "4|P|_|_|_|_|_|P|_|\n" +
	                        "3|_|_|_|_|_|_|_|B|\n" +
	                        "2|_|_|P|P|P|_|_|P|\n" +
	                        "1|_|N|B|Q|_|R|K|_|\n" +
	                        "  a b c d e f g h";
	        check(input, output);
	    }
	    
	    @Test
	    public void kingsideCastlingCheckTest2(){
	        String input =
	                		"d2-d4 e7-e5\n"+
	                        "d4xe5 Qd8-h4\n" +
	                        "Qd1xd7+ Ke8xd7\n" +
	                        "Nb1-c3 a7-a8\n" +
	                        "Bc1-e3 f7-f6\n" +               
	                        "O-O-O+\n" +
	                        "";
	        String output =
	                        "8|r|n|b|_|_|b|n|r|\n" +
	                        "7|_|p|p|k|_|_|p|p|p|\n" +
	                        "6|_|_|_|_|_|p|_|_|\n" +
	                        "5|p|_|_|_|P|_|_|_|\n" +
	                        "4|_|_|_|_|_|_|_|q|\n" +
	                        "3|_|_|N|_|B|_|_|_|\n" +
	                        "2|P|P|P|_|P|P|P|P|\n" +
	                        "1|_|_|K|R|_|B|N|R|\n" +
	                        "  a b c d e f g h";
	        check(input, output);
	    }
	    
	    @Test
	    public void instanceOfPieceN(){
	    	String s = new SinglePieceMove(new Knight(true), new Position (1,1), new Position(2,1)).toString();
	    	assertEquals(s,"Na1-a2");
	    }
	    
	    @Test
	    public void instanceOfPieceR(){
	    	String s = new SinglePieceMove(new Rook(true), new Position (1,1), new Position(2,1)).toString();
	    	assertEquals(s,"Ra1-a2");
	    }
	    
	    @Test
	    public void instanceOfPieceK(){
	    	String s = new SinglePieceMove(new King(true), new Position (1,1), new Position(2,1)).toString();
	    	assertEquals(s,"Ka1-a2");
	    }
	    
	    @Test
	    public void instanceOfPieceQ(){
	    	String s = new SinglePieceMove(new Queen(true), new Position (1,1), new Position(2,1)).toString();
	    	assertEquals(s,"Qa1-a2");
	    }
	    
	    @Test
	    public void instanceOfPieceB(){
	    	String s = new SinglePieceMove(new Bishop(true), new Position (1,1), new Position(2,1)).toString();
	    	assertEquals(s,"Ba1-a2");
	    }
	    
	    @Test
	    public void checkTest(){
	    String input = // non king-side castle to check black king
				"Nb1-c3 e7-e5\n" +
				"d2-d4 Qd8-h4\n"+
				"Bc1-h6 d7-d6\n"+
				"d4xe5 Ke8-d8\n"+
				"Qd1-d4 Qh4xQd4\n"+
				"e5xd6 Qd4xd6\n"+
				"h2-h3 Qd6-g6\n"+
				"O-O-O+\n"+
				"";
			String output =
				"8|r|n|b|k|_|b|n|r|\n" +
				"7|p|p|p|_|_|p|p|p|\n" +
				"6|_|_|_|_|_|_|q|B|\n" +
				"5|_|_|_|_|_|_|_|_|\n" +
				"4|_|_|_|_|_|_|_|_|\n" +
				"3|_|_|N|_|_|_|_|P|\n" +
				"2|P|P|P|_|P|P|P|_|\n" +
				"1|_|_|K|R|_|B|N|R|\n" +
				"  a b c d e f g h";

			check(input,output);
	    }
	    
	    @Test public void testCheckWhiteKing1() {
			String input = //check white king
				"d2-d4 d7-d5\n" +
				"e2-e4 d5xe4\n" +
				"Ke1-d2 e4-e3\n"+
				"Kd2xe3 Qd8-d5\n"+
				"Ke3-e2 Qd5-b5+\n"+
				"";
			String output =
				"8|r|n|b|_|k|b|n|r|\n" +
				"7|p|p|p|_|p|p|p|p|\n" +
				"6|_|_|_|_|_|_|_|_|\n" +
				"5|_|q|_|_|_|_|_|_|\n" +
				"4|_|_|_|P|_|_|_|_|\n" +
				"3|_|_|_|_|_|_|_|_|\n" +
				"2|P|P|P|_|K|P|P|P|\n" +
				"1|R|N|B|Q|_|B|N|R|\n" +
				"  a b c d e f g h";

			check(input,output);
		}


	// The following provides a simple helper method for all tests.
	public static void check(String input, String expectedOutput) {
		try {
			ChessGame game = new ChessGame(input);
			List<Board> boards = game.boards();
			if (boards.isEmpty()) {
				fail("test failed with insufficient boards on input: " + input);
			}
			String actualOutput = boards.get(boards.size() - 1).toString();
			assertEquals(expectedOutput, actualOutput);
		} catch (Exception e) {
			fail("test failed because of exception on input: " + input);
		}
	}
}
