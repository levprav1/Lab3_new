package org.example.handlers;

import org.example.reactorParser.ReactorParserJson;
import org.example.reactors.Reactor;

import java.util.ArrayList;

public class HandlerJSON extends Handler {
    public HandlerJSON() {
        setReactorParser(new ReactorParserJson());
    }

    @Override
    public ArrayList<Reactor> readFile(String path) {
        try {
            return getReactorParser().parse(path);
        } catch (Exception e) {
            return getNeighbour().readFile(path);
        }
    }
}
