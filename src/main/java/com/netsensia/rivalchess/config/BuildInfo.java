package com.netsensia.rivalchess.config;

import com.netsensia.rivalchess.uci.UCIController;

public enum BuildInfo {

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
