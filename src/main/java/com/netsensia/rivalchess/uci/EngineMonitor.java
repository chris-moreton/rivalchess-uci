package com.netsensia.rivalchess.uci;

import java.io.PrintStream;
import java.util.List;
import java.util.TimerTask;

import com.netsensia.rivalchess.engine.board.BoardExtensionsKt;
import com.netsensia.rivalchess.engine.search.Search;
import com.netsensia.rivalchess.engine.search.SearchPath;
import com.netsensia.rivalchess.enums.SearchState;
import com.netsensia.rivalchess.model.Board;
import com.netsensia.rivalchess.model.Move;
import com.netsensia.rivalchess.model.util.BoardUtils;

import static com.netsensia.rivalchess.util.ChessBoardConversionKt.getSimpleAlgebraicMoveFromCompactMove;

public class EngineMonitor extends TimerTask {
    private final Search engine;
    private static PrintStream out;
    private final Board board;

    public EngineMonitor(final Search engine) {
        this.engine = engine;
        board = Board.fromFen(engine.getFen());
    }

    public static void setPrintStream(PrintStream out) {
        EngineMonitor.out = out;
    }

    public static void sendUCI(String s) {
        out.println(s);
    }

    private String pathAsString(final SearchPath path) {
        Board localBoard = board;
        final StringBuilder s = new StringBuilder();
        for (int i=0; i<path.height; i++) {
            final int pathMove = path.move[i];
            if (pathMove == 0) return s.toString();
            try {
                final String algebraicMove = getSimpleAlgebraicMoveFromCompactMove(pathMove);
                final Move move = new Move(algebraicMove);
                final List<Move> localLegalMoves = BoardUtils.getLegalMoves(localBoard);
                if (!localLegalMoves.contains(move)) return s.toString();
                localBoard = Board.fromMove(localBoard, move);
                s.append(algebraicMove).append(" ");
            } catch (final Exception e) {
                return s.toString();
            }
        }
        return s.toString();
    }

    public void printInfo() {
        int depth = engine.getIterativeDeepeningDepth();
        String algebraicMove = getSimpleAlgebraicMoveFromCompactMove(engine.getCurrentDepthZeroMove());

        final String algebraicPath = pathAsString(engine.getCurrentPath()).trim();
        if (!algebraicPath.equals("")) {
            sendUCI(
                    "info" +
                    " currmove " + algebraicMove +
                    " currmovenumber " + engine.getCurrentDepthZeroMoveNumber() +
                    " depth " + depth +
                    " score " + engine.getCurrentScoreHuman() +
                    " pv " + algebraicPath +
                    " time " + engine.getSearchDuration() +
                    " nodes " + engine.getNodes() +
                    " nps " + engine.getNodesPerSecond());
        }
    }

    public void run() {
        if (engine.isOkToSendInfo()) {
            SearchState state = engine.getEngineState();
            if (state == SearchState.SEARCHING && !engine.abortingSearch) {
                printInfo();
            }
        }
    }
}
