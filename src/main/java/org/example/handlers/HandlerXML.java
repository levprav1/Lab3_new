package org.example.handlers;

import org.example.reactorParser.ReactorParserXml;
import org.example.reactors.Reactor;

import java.util.ArrayList;

public class HandlerXML extends Handler {
    public HandlerXML() {
        setReactorParser(new ReactorParserXml());
    }

    @Override
    public ArrayList<Reactor> readFile(String path) {
        try {
            return getReactorParser().parse(path);
        } catch (Exception e){
            return getNeighbour().readFile(path);
        }
    }
}
