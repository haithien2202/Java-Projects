package swen221.tests;
import org.junit.jupiter.api.Test;

import swen221.chessview.Board;
import swen221.chessview.ChessGame;
import swen221.chessview.Position;
import swen221.chessview.moves.EnPassant;
import swen221.chessview.moves.SinglePieceMove;
import swen221.chessview.pieces.Pawn;

import static org.junit.jupiter.api.Assertions.*;
import static swen221.tests.ChessViewTests.check;

import java.util.List;

class EnPassantTests {

    @Test
    public void blackEnPassantTest() {
        String input =
                "e2-e4 c7-c5\n" +
                        "e4-e5 c5-c4\n" +
                        "d2-d4 c4xd3ep\n" +
                        "";
        String output =
                "8|r|n|b|q|k|b|n|r|\n" +
                        "7|p|p|_|p|p|p|p|p|\n" +
                        "6|_|_|_|_|_|_|_|_|\n" +
                        "5|_|_|_|_|P|_|_|_|\n" +
                        "4|_|_|_|_|_|_|_|_|\n" +
                        "3|_|_|_|p|_|_|_|_|\n" +
                        "2|P|P|P|_|_|P|P|P|\n" +
                        "1|R|N|B|Q|K|B|N|R|\n" +
                        "  a b c d e f g h";

        check(input, output);
    }

    @Test
    public void blackFailedEnPassantTest() {
        String input =
                "e2-e4 c7-c5\n" +
                        "e4-e5 c5-c4\n" +
                        "d2-d4 c4xe2ep\n" +
                        "";
        String output =
                "8|r|n|b|q|k|b|n|r|\n" +
                        "7|p|p|_|p|p|p|p|p|\n" +
                        "6|_|_|_|_|_|_|_|_|\n" +
                        "5|_|_|_|_|P|_|_|_|\n" +
                        "4|_|_|p|P|_|_|_|_|\n" +
                        "3|_|_|_|_|_|_|_|_|\n" +
                        "2|P|P|P|_|_|P|P|P|\n" +
                        "1|R|N|B|Q|K|B|N|R|\n" +
                        "  a b c d e f g h";

        check(input, output);
    }

    @Test
    public void whiteEnPassantTest() {
        String input =
                "c2-c4 h7-h5\n" +
                        "c4-c5 b7-b5\n" +
                        "c5xb6ep\n" +
                        "";
        String output =
                "8|r|n|b|q|k|b|n|r|\n" +
                        "7|p|_|p|p|p|p|p|_|\n" +
                        "6|_|P|_|_|_|_|_|_|\n" +
                        "5|_|_|_|_|_|_|_|p|\n" +
                        "4|_|_|_|_|_|_|_|_|\n" +
                        "3|_|_|_|_|_|_|_|_|\n" +
                        "2|P|P|_|P|P|P|P|P|\n" +
                        "1|R|N|B|Q|K|B|N|R|\n" +
                        "  a b c d e f g h";

        check(input, output);
    }

    @Test
    public void nullTakenPieceTest() {
        String input =
                "c2-c4 h7-h5\n" +
                        "c4-c5 h5-h4\n" +
                        "c5xb6ep\n"+
                        "";
        String output =
                "8|r|n|b|q|k|b|n|r|\n" +
                        "7|p|p|p|p|p|p|p|_|\n" +
                        "6|_|_|_|_|_|_|_|_|\n" +
                        "5|_|_|P|_|_|_|_|_|\n" +
                        "4|_|_|_|_|_|_|_|p|\n" +
                        "3|_|_|_|_|_|_|_|_|\n" +
                        "2|P|P|_|P|P|P|P|P|\n" +
                        "1|R|N|B|Q|K|B|N|R|\n" +
                        "  a b c d e f g h";
        check(input, output);
    }

    @Test
    public void occupiedNewPositionTest() {
        String input =
                "c2-c4 b7-b6\n" +
                        "c4-c5 h7-h5\n" +
                        "c5xb6ep\n"+
                        "";
        String output =
                "8|r|n|b|q|k|b|n|r|\n" +
                        "7|p|_|p|p|p|p|p|_|\n" +
                        "6|_|p|_|_|_|_|_|_|\n" +
                        "5|_|_|P|_|_|_|_|p|\n" +
                        "4|_|_|_|_|_|_|_|_|\n" +
                        "3|_|_|_|_|_|_|_|_|\n" +
                        "2|P|P|_|P|P|P|P|P|\n" +
                        "1|R|N|B|Q|K|B|N|R|\n" +
                        "  a b c d e f g h";
        check(input, output);
    }

    @Test
    public void invalidColMovementEPTest() {
        String input =
                "c2-c4 h7-h5\n" +
                        "c4-c5 h5-h4\n" +
                        "c5xa6ep\n"+
                        "";
        String output =
                "8|r|n|b|q|k|b|n|r|\n" +
                        "7|p|p|p|p|p|p|p|_|\n" +
                        "6|_|_|_|_|_|_|_|_|\n" +
                        "5|_|_|P|_|_|_|_|_|\n" +
                        "4|_|_|_|_|_|_|_|p|\n" +
                        "3|_|_|_|_|_|_|_|_|\n" +
                        "2|P|P|_|P|P|P|P|P|\n" +
                        "1|R|N|B|Q|K|B|N|R|\n" +
                        "  a b c d e f g h";
        check(input, output);
    }

    @Test
    public void invalidDirectionEPTest() {
        String input =
                "c2-c4 h7-h5\n" +
                        "c4-c5 h5-h4\n" +
                        "c5xb4ep\n"+
                        "";
        String output =
                "8|r|n|b|q|k|b|n|r|\n" +
                        "7|p|p|p|p|p|p|p|_|\n" +
                        "6|_|_|_|_|_|_|_|_|\n" +
                        "5|_|_|P|_|_|_|_|_|\n" +
                        "4|_|_|_|_|_|_|_|p|\n" +
                        "3|_|_|_|_|_|_|_|_|\n" +
                        "2|P|P|_|P|P|P|P|P|\n" +
                        "1|R|N|B|Q|K|B|N|R|\n" +
                        "  a b c d e f g h";
        check(input, output);
    }

    @Test
    public void enPassantToStringTest(){
        EnPassant enPassant = new EnPassant(new SinglePieceMove(new Pawn(true),new Position(4,4),new Position(5,5)));
        assertEquals("d4xe5ep",enPassant.toString());
    }
    
}