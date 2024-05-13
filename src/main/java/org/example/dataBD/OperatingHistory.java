package org.example.dataBD;

public class OperatingHistory {
    private int id;
    private int year;
    private double electricity_supplied;
    private int preference_unit_power;
    private int annual_time_line;
    private double operating_factor;
    private double energy_av_factor_annual;
    private double energy_av_factor_cummulative;
    private double load_factor_annual;
    private double load_factor_cummulative;
    private int unit_id;

    public OperatingHistory(int id, int year, double electricity_supplied, int preference_unit_power, int annual_time_line, double operating_factor, double energy_av_factor_annual, double energy_av_factor_cummulative, double load_factor_annual, double load_factor_cummulative, int unit_id) {
        this.id = id;
        this.year = year;
        this.electricity_supplied = electricity_supplied;
        this.preference_unit_power = preference_unit_power;
        this.annual_time_line = annual_time_line;
        this.operating_factor = operating_factor;
        this.energy_av_factor_annual = energy_av_factor_annual;
        this.energy_av_factor_cummulative = energy_av_factor_cummulative;
        this.load_factor_annual = load_factor_annual;
        this.load_factor_cummulative = load_factor_cummulative;
        this.unit_id = unit_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public double getElectricity_supplied() {
        return electricity_supplied;
    }

    public void setElectricity_supplied(double electricity_supplied) {
        this.electricity_supplied = electricity_supplied;
    }

    public int getPreference_unit_power() {
        return preference_unit_power;
    }

    public void setPreference_unit_power(int preference_unit_power) {
        this.preference_unit_power = preference_unit_power;
    }

    public int getAnnual_time_line() {
        return annual_time_line;
    }

    public void setAnnual_time_line(int annual_time_line) {
        this.annual_time_line = annual_time_line;
    }

    public double getOperating_factor() {
        return operating_factor;
    }

    public void setOperating_factor(double operating_factor) {
        this.operating_factor = operating_factor;
    }

    public double getEnergy_av_factor_annual() {
        return energy_av_factor_annual;
    }

    public void setEnergy_av_factor_annual(double energy_av_factor_annual) {
        this.energy_av_factor_annual = energy_av_factor_annual;
    }

    public double getEnergy_av_factor_cummulative() {
        return energy_av_factor_cummulative;
    }

    public void setEnergy_av_factor_cummulative(double energy_av_factor_cummulative) {
        this.energy_av_factor_cummulative = energy_av_factor_cummulative;
    }

    public double getLoad_factor_annual() {
        return load_factor_annual;
    }

    public void setLoad_factor_annual(double load_factor_annual) {
        this.load_factor_annual = load_factor_annual;
    }

    public double getLoad_factor_cummulative() {
        return load_factor_cummulative;
    }

    public void setLoad_factor_cummulative(double load_factor_cummulative) {
        this.load_factor_cummulative = load_factor_cummulative;
    }

    public int getUnit_id() {
        return unit_id;
    }

    public void setUnit_id(int unit_id) {
        this.unit_id = unit_id;
    }
}
