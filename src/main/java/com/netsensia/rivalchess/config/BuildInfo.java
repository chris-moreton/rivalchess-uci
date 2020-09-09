package com.netsensia.rivalchess.config;

import com.netsensia.rivalchess.uci.UCIController;

public enum BuildInfo {

    VERSION ("31.0.0"),
    BUILD (UCIController.class.getPackage().getImplementationVersion()),
    ;

    private String value;

    private BuildInfo(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
