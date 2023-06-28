package swen221.tests;
import org.junit.jupiter.api.Test;
import swen221.chessview.Position;
import swen221.chessview.Round;
import swen221.chessview.moves.SinglePieceMove;
import swen221.chessview.pieces.Pawn;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class RoundTests {

    @Test
    public void roundToStringTest(){
        SinglePieceMove white = new SinglePieceMove(new Pawn(true),new Position(2,1),new Position(4,1));
        SinglePieceMove black = new SinglePieceMove(new Pawn(true),new Position(7,1),new Position(5,1));
        Round round = new Round(white,black);
        assertEquals("a2-a4 a7-a5",round.toString());
    }

}
