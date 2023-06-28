package swen221.tests;
import org.junit.jupiter.api.Test;
import swen221.chessview.Position;
import swen221.chessview.moves.NonCheck;
import swen221.chessview.moves.SinglePieceMove;
import swen221.chessview.pieces.Pawn;

import static org.junit.jupiter.api.Assertions.*;

class NonCheckTests {

    @Test
    public void nonCheckValidInheritance(){
        SinglePieceMove move = new SinglePieceMove(new Pawn(true),new Position(4,4),new Position(5,5));
        NonCheck nonCheck = new NonCheck(move);
        assertEquals(nonCheck.move(),move);
    }

    @Test
    public void nonCheckValidToString(){
        SinglePieceMove move = new SinglePieceMove(new Pawn(true),new Position(4,4),new Position(5,5));
        NonCheck nonCheck = new NonCheck(move);
        assertEquals("d4-e5",nonCheck.toString());
    }

}