package org.example.reactors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;

public class Reactor {
    private String name;
    private String classReactor;
    private double burnup;
    private double kpd;
    private double enrichment;
    private double thermal_capacity;
    private double electrical_capacity;
    private int life_time;
    private double first_load;
    private String source;

    public Reactor(String name, String classReactor, double burnup, double kpd, double enrichment, double thermal_capacity, double electrical_capacity, int life_time, double first_load, String source) {
        this.name = name;
        this.classReactor = classReactor;
        this.burnup = burnup;
        this.kpd = kpd;
        this.enrichment = enrichment;
        this.thermal_capacity = thermal_capacity;
        this.electrical_capacity = electrical_capacity;
        this.life_time = life_time;
        this.first_load = first_load;
        this.source = source;
    }

    public Reactor() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassReactor() {
        return classReactor;
    }

    public void setClassReactor(String classReactor) {
        this.classReactor = classReactor;
    }

    public double getBurnup() {
        return burnup;
    }

    public void setBurnup(double burnup) {
        this.burnup = burnup;
    }

    public double getKpd() {
        return kpd;
    }

    public void setKpd(double kpd) {
        this.kpd = kpd;
    }

    public double getEnrichment() {
        return enrichment;
    }

    public void setEnrichment(double enrichment) {
        this.enrichment = enrichment;
    }

    public double getThermal_capacity() {
        return thermal_capacity;
    }

    public void setThermal_capacity(double thermal_capacity) {
        this.thermal_capacity = thermal_capacity;
    }

    public double getElectrical_capacity() {
        return electrical_capacity;
    }

    public void setElectrical_capacity(double electrical_capacity) {
        this.electrical_capacity = electrical_capacity;
    }

    public int getLife_time() {
        return life_time;
    }

    public void setLife_time(int life_time) {
        this.life_time = life_time;
    }

    public double getFirst_load() {
        return first_load;
    }

    public void setFirst_load(double first_load) {
        this.first_load = first_load;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public MutableTreeNode getNode() {
        DefaultMutableTreeNode reactorNode = new DefaultMutableTreeNode(getName());
        reactorNode.add(new DefaultMutableTreeNode("Класс: "+getClassReactor()));
        reactorNode.add(new DefaultMutableTreeNode("КПД: "+getKpd()));
        reactorNode.add(new DefaultMutableTreeNode("Обогащение: "+getEnrichment()));
        reactorNode.add(new DefaultMutableTreeNode("Тепловая мощность: "+getThermal_capacity()));
        reactorNode.add(new DefaultMutableTreeNode("Энергетическая мощность: "+getElectrical_capacity()));
        reactorNode.add(new DefaultMutableTreeNode("Время жизни: "+getLife_time()));
        reactorNode.add(new DefaultMutableTreeNode("Начальная загрузка: "+getFirst_load()));
        reactorNode.add(new DefaultMutableTreeNode("Источник с расширением: "+getSource()));
        return reactorNode;
    }
}
