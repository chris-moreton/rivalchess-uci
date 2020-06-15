package com.netsensia.rivalchess.uci;

import java.io.PrintStream;
import java.util.TimerTask;

import com.netsensia.rivalchess.enums.SearchState;
import com.netsensia.rivalchess.engine.core.search.Search;

import static com.netsensia.rivalchess.util.ChessBoardConversionKt.getSimpleAlgebraicMoveFromCompactMove;

public class EngineMonitor extends TimerTask {
    private final Search engine;
    private static PrintStream out;

    public EngineMonitor(Search engine) {
        this.engine = engine;
    }

    public static void setPrintStream(PrintStream out) {
        EngineMonitor.out = out;
    }

    public static void sendUCI(String s) {
        out.println(s);
    }

    public void printInfo() {
        int depth = engine.getIterativeDeepeningDepth();
        sendUCI(
                "info" +
                        " currmove " + getSimpleAlgebraicMoveFromCompactMove(engine.getCurrentDepthZeroMove()) +
                        " currmovenumber " + engine.getCurrentDepthZeroMoveNumber() +
                        " depth " + depth +
                        " score " + engine.getCurrentScoreHuman() +
                        " pv " + engine.getCurrentPath().toString().trim() +
                        " time " + engine.getSearchDuration() +
                        " nodes " + engine.getNodes() +
                        " nps " + engine.getNodesPerSecond());
    }

    public void run() {
        engine.setMillisSetByEngineMonitor(System.currentTimeMillis());

        if (engine.isOkToSendInfo()) {
            SearchState state = engine.getEngineState();
            if (state == SearchState.SEARCHING && !engine.abortingSearch) {
                printInfo();
            }
        }
    }
}
