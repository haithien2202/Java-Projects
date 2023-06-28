package swen221.tests;
import org.junit.jupiter.api.Test;
import swen221.chessview.Position;
import swen221.chessview.moves.SinglePieceTake;
import swen221.chessview.pieces.Pawn;
import swen221.chessview.pieces.Piece;

import static org.junit.jupiter.api.Assertions.*;

class SinglePieceTakeTest {

    @Test
    public void toStringTest() {
        Piece pawnW = new Pawn(true);
        Piece pawnB = new Pawn(false);
        Position startPos = new Position(4,4);
        Position endPos = new Position(5,5);
        SinglePieceTake singlePieceTake = new SinglePieceTake(pawnW,pawnB,startPos,endPos);
        assertEquals("d4xe5", singlePieceTake.toString());
    }

}