package com.netsensia.rivalchess.uci;

import com.netsensia.rivalchess.engine.search.Search;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.junit.Assert.assertTrue;

public class UciControllerTest {

    Search search;
    UCIController uciController;
    ByteArrayOutputStream outSpy;

    @Before
    public void setUp() throws InterruptedException {

        outSpy = new ByteArrayOutputStream();

        search = new Search(new PrintStream(outSpy));

        RivalUCI.startEngineTimer(search);
        search.setHashSizeMB(32);

        new Thread(search).start();

        uciController = new UCIController(search, 1, new PrintStream(outSpy));

        new Thread(uciController).start();

        SECONDS.sleep(2);

    }

    @Test
    public void testUciOkResponse() {
        uciController.processUCICommand("uci");

        assertTrue(outSpy.toString().contains("uciok"));
    }

    @Test
    public void testGenerateMove() {

        uciController.processUCICommand("ucinewgame");
        uciController.processUCICommand("position startpos");
        uciController.processUCICommand("go depth 3");

        await().atMost(10, SECONDS).until(() -> outSpy.toString().contains("bestmove g1f3"));

        assertTrue(outSpy.toString().contains("bestmove g1f3"));
    }

    @Test
    public void testRegularPosition() {

        uciController.processUCICommand("ucinewgame");
        uciController.processUCICommand("position fen r3nrk1/2p2p1p/p1p1b1p1/2NpPq2/3R4/P1N1Q3/1PP2PPP/4R1K1 w - - 0 1");
        uciController.processUCICommand("go depth 9");

        await().atMost(10, SECONDS).until(() -> outSpy.toString().contains("bestmove g2g4"));

        assertTrue(outSpy.toString().contains("bestmove g2g4"));
    }

    @Test
    public void test13_0_3_fail_position() {

        uciController.processUCICommand("ucinewgame");
        uciController.processUCICommand("position fen 2Q5/P3kq2/5p1P/3b4/2p1p3/2P2N2/2P2PP1/6K1 b - -");
        uciController.processUCICommand("go depth 8");

        await().atMost(30, SECONDS).until(() -> outSpy.toString().contains("bestmove e4f3"));

        assertTrue(outSpy.toString().contains("bestmove e4f3"));
    }

    @Test
    public void testIsReadyCommand() {
        uciController.processUCICommand("isready");

        await().atMost(10, SECONDS).until(() -> outSpy.toString().contains("readyok"));

        assertTrue(outSpy.toString().contains("readyok"));

    }

    @Test
    public void testUciCommand() {
        uciController.processUCICommand("uci");

        await().atMost(10, SECONDS).until(() -> outSpy.toString().contains("Chris Moreton"));

        assertTrue(outSpy.toString().contains("uciok"));

    }

}