package swen221.tests;
import org.junit.jupiter.api.Test;
import swen221.chessview.Position;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class PositionTest {

    @Test
    public void checkEqualPosition() {
        Position pos1 = new Position(1,1);
        assertEquals(true,pos1.equals(new Position(1,1)));
    }

    @Test
    public void checkUnEqualPosition(){
        Position pos1 = new Position(1,1);
        assertEquals(false,pos1.equals(new Position(3,3)));
    }

    @Test
    public void checkIncorrectEqualInput(){
        Position pos1 = new Position(1,1);
        assertEquals(false,pos1.equals(new Point(3,3)));
    }

    @Test
    public void checkCorrectHashCodeCreation(){
        int row = 2, col = 2;
        int hashCode = row^col;
        Position position = new Position(row,col);
        assertEquals(hashCode,position.hashCode());
    }
}