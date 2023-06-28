package swen221.tests;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static swen221.tests.ChessViewTests.check;

class SinglePieceMoveTest {

    @Test
    public void pawnFailedRepeatedDoubleMove() {
        String input =
                "e2-e4 a7-a5\n" +
                        "e4-e6\n" +
                        "";
        String output =
                "8|r|n|b|q|k|b|n|r|\n" +
                        "7|_|p|p|p|p|p|p|p|\n" +
                        "6|_|_|_|_|_|_|_|_|\n" +
                        "5|p|_|_|_|_|_|_|_|\n" +
                        "4|_|_|_|_|P|_|_|_|\n" +
                        "3|_|_|_|_|_|_|_|_|\n" +
                        "2|P|P|P|P|_|P|P|P|\n" +
                        "1|R|N|B|Q|K|B|N|R|\n" +
                        "  a b c d e f g h";

        check(input, output);
    }

    @Test
    public void pawnFailedRepeatedDoubleMoveWithGap() {
        String input =
                "e2-e4 a7-a5\n" +
                        "e4-e5 a5-a4\n" +
                        "";
        String output =
                "8|r|n|b|q|k|b|n|r|\n" +
                        "7|_|p|p|p|p|p|p|p|\n" +
                        "6|_|_|_|_|_|_|_|_|\n" +
                        "5|_|_|_|_|P|_|_|_|\n" +
                        "4|p|_|_|_|_|_|_|_|\n" +
                        "3|_|_|_|_|_|_|_|_|\n" +
                        "2|P|P|P|P|_|P|P|P|\n" +
                        "1|R|N|B|Q|K|B|N|R|\n" +
                        "  a b c d e f g h";

        check(input, output);
    }
    @Test
    public void incorrectPawnMovementTooFarMovementOutOfBoard(){
        String input =
                "e2-e138\n";
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
    }

    @Test   
    public void kingMovementTest01() {
        String input =
                "e2-e4 e7-e5\n" +
                        "Ke1-e2 h7-h6\n" +
                        "Ke2-f3 g7-g6\n" +
                        "Kf3-e3 d7-d6\n" +
                        "Ke3-f3 c7-c6\n" +
                        "Kf3-e2 b7-b6\n" +
                        "Ke2-e1 a7-a6\n" +
                        "Ke1-e2 f7-f6\n" +
                        "Ke2-d3 h6-h5\n" +
                        "Kd3-e2 g6-g5\n" +
                        "";
        String output =
                "8|r|n|b|q|k|b|n|r|\n" +
                        "7|_|_|_|_|_|_|_|_|\n" +
                        "6|p|p|p|p|_|p|_|_|\n" +
                        "5|_|_|_|_|p|_|p|p|\n" +
                        "4|_|_|_|_|P|_|_|_|\n" +
                        "3|_|_|_|_|_|_|_|_|\n" +
                        "2|P|P|P|P|K|P|P|P|\n" +
                        "1|R|N|B|Q|_|B|N|R|\n" +
                        "  a b c d e f g h";

        check(input, output);
    }
    public void incorrectPawnMovementTooFarMovementInsideBoard(){
        String input =
                "e2-e6\n";
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
    }
}