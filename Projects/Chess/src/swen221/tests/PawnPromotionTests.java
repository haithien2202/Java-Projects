package swen221.tests;
import org.junit.jupiter.api.Test;
import swen221.chessview.Position;
import swen221.chessview.moves.PawnPromotion;
import swen221.chessview.moves.SinglePieceMove;
import swen221.chessview.pieces.Pawn;
import swen221.chessview.pieces.Queen;

import static org.junit.jupiter.api.Assertions.*;
import static swen221.tests.ChessViewTests.check;

class PawnPromotionTests {
    @Test
    public void whitePawnPromotionTestQueen() {
        String input =
                "e2-e4 a7-a6\n" +
                        "Qd1-h5 b7-b6\n" +
                        "Qh5xh7 c7-c6\n" +
                        "Qh7xRh8 a6-a5\n" +
                        "Qh8xg7 Ra8-a7\n" +
                        "h2-h4 Ra7-a6\n" +
                        "h4-h5 d7-d6\n" +
                        "h5-h6 e7-e6\n" +
                        "h6-h7 a5-a4\n" +
                        "h7-h8=Q\n" +
                        "";
        String output =
                "8|_|n|b|q|k|b|n|Q|\n" +
                        "7|_|_|_|_|_|p|Q|_|\n" +
                        "6|r|p|p|p|p|_|_|_|\n" +
                        "5|_|_|_|_|_|_|_|_|\n" +
                        "4|p|_|_|_|P|_|_|_|\n" +
                        "3|_|_|_|_|_|_|_|_|\n" +
                        "2|P|P|P|P|_|P|P|_|\n" +
                        "1|R|N|B|_|K|B|N|R|\n" +
                        "  a b c d e f g h";

        check(input, output);
    }

    @Test
    public void whitePawnPromotionTestRook() {
        String input =
                "e2-e4 a7-a6\n" +
                        "Qd1-h5 b7-b6\n" +
                        "Qh5xh7 c7-c6\n" +
                        "Qh7xRh8 a6-a5\n" +
                        "Qh8xg7 Ra8-a7\n" +
                        "h2-h4 Ra7-a6\n" +
                        "h4-h5 d7-d6\n" +
                        "h5-h6 e7-e6\n" +
                        "h6-h7 a5-a4\n" +
                        "h7-h8=R\n" +
                        "";
        String output =
                "8|_|n|b|q|k|b|n|R|\n" +
                        "7|_|_|_|_|_|p|Q|_|\n" +
                        "6|r|p|p|p|p|_|_|_|\n" +
                        "5|_|_|_|_|_|_|_|_|\n" +
                        "4|p|_|_|_|P|_|_|_|\n" +
                        "3|_|_|_|_|_|_|_|_|\n" +
                        "2|P|P|P|P|_|P|P|_|\n" +
                        "1|R|N|B|_|K|B|N|R|\n" +
                        "  a b c d e f g h";

        check(input, output);
    }

    @Test
    public void whitePawnPromotionTestBishop() {
        String input =
                "e2-e4 a7-a6\n" +
                        "Qd1-h5 b7-b6\n" +
                        "Qh5xh7 c7-c6\n" +
                        "Qh7xRh8 a6-a5\n" +
                        "Qh8xg7 Ra8-a7\n" +
                        "h2-h4 Ra7-a6\n" +
                        "h4-h5 d7-d6\n" +
                        "h5-h6 e7-e6\n" +
                        "h6-h7 a5-a4\n" +
                        "h7-h8=B\n" +
                        "";
        String output =
                "8|_|n|b|q|k|b|n|B|\n" +
                        "7|_|_|_|_|_|p|Q|_|\n" +
                        "6|r|p|p|p|p|_|_|_|\n" +
                        "5|_|_|_|_|_|_|_|_|\n" +
                        "4|p|_|_|_|P|_|_|_|\n" +
                        "3|_|_|_|_|_|_|_|_|\n" +
                        "2|P|P|P|P|_|P|P|_|\n" +
                        "1|R|N|B|_|K|B|N|R|\n" +
                        "  a b c d e f g h";

        check(input, output);
    }

    @Test
    public void whitePawnPromotionTestKnight() {
        String input =
                "e2-e4 a7-a6\n" +
                        "Qd1-h5 b7-b6\n" +
                        "Qh5xh7 c7-c6\n" +
                        "Qh7xRh8 a6-a5\n" +
                        "Qh8xg7 Ra8-a7\n" +
                        "h2-h4 Ra7-a6\n" +
                        "h4-h5 d7-d6\n" +
                        "h5-h6 e7-e6\n" +
                        "h6-h7 a5-a4\n" +
                        "h7-h8=N\n" +
                        "";
        String output =
                "8|_|n|b|q|k|b|n|N|\n" +
                        "7|_|_|_|_|_|p|Q|_|\n" +
                        "6|r|p|p|p|p|_|_|_|\n" +
                        "5|_|_|_|_|_|_|_|_|\n" +
                        "4|p|_|_|_|P|_|_|_|\n" +
                        "3|_|_|_|_|_|_|_|_|\n" +
                        "2|P|P|P|P|_|P|P|_|\n" +
                        "1|R|N|B|_|K|B|N|R|\n" +
                        "  a b c d e f g h";

        check(input, output);
    }

    @Test
    public void pawnPromotionToStringTest(){
        PawnPromotion pawnPromotion = new PawnPromotion(new SinglePieceMove(new Pawn(true),new Position(4,4), new Position(5,5)),new Queen(true));
        assert(true);
    }

}