Rival Chess Java Engine
=======================

This is a UCI-compatible engine. 

## Build

    ./gradlew build

The JAR file can then be found in the build/libs directtory.
    
## Play

Detailed instructions on the Universal Chess Interface (UCI) can be found [here](http://wbec-ridderkerk.nl/html/UCIProtocol.html).

Here is a brief example of how to use the command line interface.

    Hi
    ucinewgame
    position startpos
    go depth 3
    info currmove g1h3 currmovenumber 20 depth 3 score cp 25 pv g1f3 g8f6 e2e3  time 0 nodes 113 nps 0
    bestmove g1f3
    move e2e3
    go depth 3
    info currmove g1h3 currmovenumber 20 depth 3 score cp 25 pv g1f3 g8f6 e2e3  time 1 nodes 108 nps 108000
    bestmove g1f3

