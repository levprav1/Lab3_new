package org.example.collections;

import org.example.dataBD.*;

import java.util.ArrayList;


public class StorageBD {
    private ArrayList<Company> companies;
    private ArrayList<Country> countries;
    private ArrayList<Site> sites;
    private ArrayList<Unit> units;
    private ArrayList<Region> regions;
    private ArrayList<OperatingHistory> operatingHistories;

    public StorageBD(ArrayList<Unit> units, ArrayList<OperatingHistory> operatingHistories, ArrayList<Company> companies, ArrayList<Country> countries, ArrayList<Site> sites, ArrayList<Region> regions) {
        this.companies = companies;
        this.operatingHistories = operatingHistories;
        this.countries = countries;
        this.sites = sites;
        this.units = units;
        this.regions = regions;
    }

    public StorageBD() {
    }

    public void addUnit(Unit unit){
        units.add(unit);
    }
    public ArrayList<Company> getCompanies() {
        return companies;
    }

    public void setCompanies(ArrayList<Company> companies) {
        this.companies = companies;
    }

    public ArrayList<Country> getCountries() {
        return countries;
    }

    public void setCountries(ArrayList<Country> countries) {
        this.countries = countries;
    }

    public ArrayList<Site> getSites() {
        return sites;
    }

    public void setSites(ArrayList<Site> sites) {
        this.sites = sites;
    }

    public ArrayList<Unit> getUnits() {
        return units;
    }

    public void setUnits(ArrayList<Unit> units) {
        this.units = units;
    }

    public ArrayList<Region> getRegions() {
        return regions;
    }

    public void setRegions(ArrayList<Region> regions) {
        this.regions = regions;
    }

    public ArrayList<OperatingHistory> getOperatingHistories() {
        return operatingHistories;
    }

    public void setOperatingHistories(ArrayList<OperatingHistory> operatingHistories) {
        this.operatingHistories = operatingHistories;
    }

    public StorageBD copy() {
        return new StorageBD(getUnits(),getOperatingHistories(),getCompanies(),getCountries(),getSites(),getRegions());
    }
}
