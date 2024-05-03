package org.example.handlers;

import org.example.reactorParser.ReactorParser;
import org.example.reactors.Reactor;

import java.util.ArrayList;

public abstract class Handler {
    private Handler neighbour;
    private ReactorParser reactorParser;

    public ReactorParser getReactorParser() {
        return reactorParser;
    }

    public void setReactorParser(ReactorParser reactorParser) {
        this.reactorParser = reactorParser;
    }

    public Handler getNeighbour() {
        return neighbour;
    }

    public void setNeighbour(Handler neighbour) {
        this.neighbour = neighbour;
    }

    public abstract ArrayList<Reactor> readFile(String path);
}
