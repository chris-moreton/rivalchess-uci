package com.netsensia.rivalchess.uci;

import com.netsensia.rivalchess.config.Uci;
import com.netsensia.rivalchess.engine.search.Search;

import java.util.Timer;

public final class RivalUCI {
    @SuppressWarnings("squid:S106")
    private static final Search rivalSearch = new Search(System.out);

    public static void startEngineTimer(Search rivalSearch) {
        rivalSearch.setUciMode(true);
        EngineMonitor m_monitor = new EngineMonitor(rivalSearch);
        new Timer().schedule(m_monitor, Uci.UCI_TIMER_INTERVAL_MILLIS.getValue(), Uci.UCI_TIMER_INTERVAL_MILLIS.getValue());
    }

    public static void main(String[] args) {

        int timeMultiple;
        if (args.length > 1 && args[0].equals("tm")) {
            timeMultiple = Integer.parseInt(args[1]);
        } else {
            timeMultiple = 1;
        }

        startEngineTimer(rivalSearch);
        rivalSearch.setHashSizeMB(32);

        new Thread(rivalSearch).start();

        @SuppressWarnings("squid:S106")
        UCIController uciController = new UCIController(
                rivalSearch,
                timeMultiple,
                System.out);

        new Thread(uciController).start();
    }
}
