package org.example.handlers;

import org.example.reactorParser.ReactorParserYaml;
import org.example.reactors.Reactor;

import java.util.ArrayList;

public class HandlerYAML extends Handler {
    public HandlerYAML() {
        setReactorParser(new ReactorParserYaml());
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
