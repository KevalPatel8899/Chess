package com.example.chess;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {


    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testKnight() {
        Knight knight = new Knight("box01", "Black_1_Knight");
        String[] moves = new String[knight.chessMove().size()];
        moves = knight.chessMove().toArray(moves);

        String[] result = {"box22", "box13","box20"};

        assertArrayEquals(result, moves);
    }

    @Test
    public void testRook(){
        Rook rook = new Rook("box00","Black_1_Rook");

        String moves = rook.chessMove().get(13);
        String result = "box07";

        assertEquals(result, moves);
    }
}