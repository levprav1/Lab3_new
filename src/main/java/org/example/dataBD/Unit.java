package org.example.dataBD;

import java.util.TreeMap;

public class Unit {
    private int id;
    private int site_id;
    private String name;
    private String status;
    private String type;
    private String model;
    private int owner_id;
    private int operator_id;
    private int net_capacity;
    private int design_net_capacity;
    private int gross_capacity;
    private int thermal_capacity;
    private String construction_start_date;
    private String first_criticaly_date;
    private String first_grid_connection;
    private String commercial_operation_date;
    private String suspended_operation_date;
    private String end_suspended_operation_date;
    private String permanent_shutdown_date;
    private double burnup;
    private double first_load;
    private TreeMap<Integer,Double> fuel_consumption;


    public Unit(int id, int site_id, String name, String status, String type, String model, int owner_id, int operator_id, int net_capacity, int design_net_capacity, int gross_capacity, int thermal_capacity, String construction_start_date, String first_criticaly_date, String first_grid_connection, String commercial_operation_date, String suspended_operation_date, String end_suspended_operation_date, String permanent_shutdown_date) {
        this.id = id;
        this.site_id = site_id;
        this.name = name;
        this.status = status;
        this.type = type;
        this.model = model;
        this.owner_id = owner_id;
        this.operator_id = operator_id;
        this.net_capacity = net_capacity;
        this.design_net_capacity = design_net_capacity;
        this.gross_capacity = gross_capacity;
        this.thermal_capacity = thermal_capacity;
        this.construction_start_date = construction_start_date;
        this.first_criticaly_date = first_criticaly_date;
        this.first_grid_connection = first_grid_connection;
        this.commercial_operation_date = commercial_operation_date;
        this.suspended_operation_date = suspended_operation_date;
        this.end_suspended_operation_date = end_suspended_operation_date;
        this.permanent_shutdown_date = permanent_shutdown_date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSite_id() {
        return site_id;
    }

    public void setSite_id(int site_id) {
        this.site_id = site_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(int owner_id) {
        this.owner_id = owner_id;
    }

    public int getOperator_id() {
        return operator_id;
    }

    public void setOperator_id(int operator_id) {
        this.operator_id = operator_id;
    }

    public int getNet_capacity() {
        return net_capacity;
    }

    public void setNet_capacity(int net_capacity) {
        this.net_capacity = net_capacity;
    }

    public int getDesign_net_capacity() {
        return design_net_capacity;
    }

    public void setDesign_net_capacity(int design_net_capacity) {
        this.design_net_capacity = design_net_capacity;
    }

    public int getGross_capacity() {
        return gross_capacity;
    }

    public void setGross_capacity(int gross_capacity) {
        this.gross_capacity = gross_capacity;
    }

    public int getThermal_capacity() {
        return thermal_capacity;
    }

    public void setThermal_capacity(int thermal_capacity) {
        this.thermal_capacity = thermal_capacity;
    }

    public String getConstruction_start_date() {
        return construction_start_date;
    }

    public void setConstruction_start_date(String construction_start_date) {
        this.construction_start_date = construction_start_date;
    }

    public String getFirst_criticaly_date() {
        return first_criticaly_date;
    }

    public void setFirst_criticaly_date(String first_criticaly_date) {
        this.first_criticaly_date = first_criticaly_date;
    }

    public String getFirst_grid_connection() {
        return first_grid_connection;
    }

    public void setFirst_grid_connection(String first_grid_connection) {
        this.first_grid_connection = first_grid_connection;
    }

    public String getCommercial_operation_date() {
        return commercial_operation_date;
    }

    public void setCommercial_operation_date(String commercial_operation_date) {
        this.commercial_operation_date = commercial_operation_date;
    }

    public String getSuspended_operation_date() {
        return suspended_operation_date;
    }

    public void setSuspended_operation_date(String suspended_operation_date) {
        this.suspended_operation_date = suspended_operation_date;
    }

    public String getEnd_suspended_operation_date() {
        return end_suspended_operation_date;
    }

    public void setEnd_suspended_operation_date(String end_suspended_operation_date) {
        this.end_suspended_operation_date = end_suspended_operation_date;
    }

    public String getPermanent_shutdown_date() {
        return permanent_shutdown_date;
    }

    public void setPermanent_shutdown_date(String permanent_shutdown_date) {
        this.permanent_shutdown_date = permanent_shutdown_date;
    }

    public double getBurnup() {
        return burnup;
    }

    public void setBurnup(double burnup) {
        this.burnup = burnup;
    }

    public double getFirst_load() {
        return first_load;
    }

    public void setFirst_load(double first_load) {
        this.first_load = first_load;
    }

    public TreeMap<Integer, Double> getFuel_consumption() {
        return fuel_consumption;
    }

    public void setFuel_consumption(TreeMap<Integer, Double> fuel_consumption) {
        this.fuel_consumption = fuel_consumption;
    }
}
