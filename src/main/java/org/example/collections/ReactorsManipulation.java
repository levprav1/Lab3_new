package org.example.collections;

import org.example.dataBD.Country;
import org.example.dataBD.OperatingHistory;
import org.example.dataBD.Site;
import org.example.dataBD.Unit;
import org.example.reactors.Reactor;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class ReactorsManipulation {
    private StorageBD storageBDInitial = new StorageBD();
    private  StorageBD storageBD = new StorageBD();
    private ReactorStorage reactorStorage = new ReactorStorage();
    private boolean dataPrepared = false;

    public ReactorsManipulation() {
    }

    public ReactorsManipulation(StorageBD storageBD, ReactorStorage reactorStorage) {
        this.storageBDInitial = storageBD;
        this.storageBD = storageBD.copy();
        this.reactorStorage = reactorStorage;
    }

    public ReactorsManipulation(StorageBD storageBDInitial) {
        this.storageBDInitial = storageBDInitial;
    }

    public ReactorStorage getReactorStorage() {
        return reactorStorage;
    }

    public void setReactorStorage(ReactorStorage reactorStorage) {
        this.reactorStorage = reactorStorage;
    }
    public void setReactors(ArrayList<Reactor> reactors) {
        reactorStorage.setReactors(reactors);
    }

    public StorageBD getStorageBD() {
        return storageBD;
    }


    public void setStorageBDInitial(StorageBD storageBD) {
        this.storageBDInitial = storageBD;
        this.storageBD = storageBD.copy();
        dataPrepared = false;
    }

    public boolean areDataGet() {
        boolean areExist = true;
        try {
            if (storageBDInitial.getUnits().isEmpty()||storageBDInitial.getCountries().isEmpty()||storageBDInitial.getCompanies().isEmpty()
                    ||storageBDInitial.getSites().isEmpty()||storageBDInitial.getRegions().isEmpty()||storageBDInitial.getOperatingHistories().isEmpty()||reactorStorage.getReactors().isEmpty())
                areExist = false;
        } catch (Exception e) {
            areExist = false;
        }

        return areExist;
    }
    public void prepareData() {
        if(!dataPrepared){
            filterUnits();
            addBurnup2Units();
            addFistLoad2Units();
            setFuelConsumptionMap();
            dataPrepared = true;
        }
    }
    public void filterUnits(){
        storageBD.setUnits ((ArrayList<Unit>) storageBD.getUnits().stream()
                .filter(unit -> ((unit.getPermanent_shutdown_date() == null) || (getYear(unit.getPermanent_shutdown_date()) >= 2014) )&& !unit.getType().equals("HTGR"))
                .collect(Collectors.toList()));
    }

    public void addBurnup2Units(){
        Map<String, Double> reactorBurnupMap = reactorStorage.getReactors().stream()
                .collect(Collectors.toMap(r -> r.getName(), r -> r.getBurnup()));

        storageBD.getUnits().forEach(u -> {
            u.setBurnup(reactorBurnupMap.getOrDefault(u.getType(), 0.0));
            if(u.getBurnup()==0.0){
                switch (u.getType()) {
                    case "LWGR", "GCR" -> u.setBurnup(reactorBurnupMap.getOrDefault("MAGNOX", 0.0));
                    case "FBR" -> u.setBurnup(reactorBurnupMap.getOrDefault("BN", 0.0));
                }
            }
        });

    }

    public void addFistLoad2Units(){

        Map<String, Double> reactorFirstLoadMap = reactorStorage.getReactors().stream()
                .collect(Collectors.toMap(r -> r.getName(), r -> r.getFirst_load()));

        storageBD.getUnits().forEach(u -> {
            u.setFirst_load(reactorFirstLoadMap.getOrDefault(u.getType(), 0.0));
            if(u.getFirst_load()==0.0){
                switch (u.getType()) {
                    case "LWBR", "GCR" -> u.setFirst_load(reactorFirstLoadMap.getOrDefault("MAGNOX", 0.0));
                    case "FBR" -> u.setFirst_load(reactorFirstLoadMap.getOrDefault("BN", 0.0));
                    default -> u.setFirst_load(3 * 0.85 * u.getThermal_capacity() / u.getBurnup());
                }

            }
        });

    }

    private TreeMap<Integer, Double> getDefaultMap(){
        TreeMap<Integer, Double> defaultMap = new TreeMap<Integer, Double>();
        for (int i = 2014; i<=2024; i++){
            defaultMap.put(i,0.0);
        }
        return defaultMap;
    }

    private TreeMap<Integer, Double> getLoadFactorAnnualMap(int unit_id){
        TreeMap<Integer, Double> loadFactorMap = (TreeMap<Integer, Double>) storageBD.getOperatingHistories().stream()
                .filter(oh -> oh.getUnit_id() == unit_id)
                .collect(Collectors.toMap(
                        OperatingHistory::getYear, // Ключ - год
                        OperatingHistory::getLoad_factor_annual, // Значение - load factor
                        (existing, replacement) -> existing, // Если есть дублирующиеся ключи, оставляем существующее значение
                        TreeMap::new));
        return loadFactorMap;
    }

    public void setFuelConsumptionMap(){

        for(Unit unit : storageBD.getUnits()){
            switch(unit.getStatus()){
                case "Operational": setOperationFuelConsumption(unit);
                    break;
                case "Under Construction": setUnderConstrFuelConsumption(unit);
                    break;
                case "Suspended Operation": setSusOperFuelConsumption(unit);
                    break;
                case "Permanent Shutdown": setPermanentShutFuelConsumption(unit);
                    break;
                default: setUnderConstrFuelConsumption(unit);
                    break;
            }

        }

    }

    public void setOperationFuelConsumption(Unit unit){
        int firstGridConnectionYear = getYear(unit.getFirst_grid_connection());
        int end_suspended_operation_year = getYear(unit.getEnd_suspended_operation_date());
        // Создаем TreeMap для хранения данных о потреблении топлива
        TreeMap<Integer, Double> loadFactorMap = getLoadFactorAnnualMap(unit.getId());
        TreeMap<Integer, Double> fuelConsumption = getDefaultMap();
        if(end_suspended_operation_year < 2014){
            if (firstGridConnectionYear < 2014) {
                // Заполняем данные о потреблении топлива для последующих лет
                for (int i = 2014; i <= 2024; i++) {
                    double loadFactor = getLoadFactorAVG(loadFactorMap, i);
                    double consumption = unit.getThermal_capacity() / unit.getBurnup() * loadFactor* 0.01;
                    fuelConsumption.put(i, consumption);
                }
            } else {
                fuelConsumption.put(firstGridConnectionYear, unit.getFirst_load());
                // Заполняем данные о потреблении топлива для последующих лет
                for (int i = firstGridConnectionYear + 1; i <= 2024; i++) {
                    double loadFactor = getLoadFactorAVG(loadFactorMap, i);
                    double consumption = unit.getThermal_capacity() / unit.getBurnup() * loadFactor* 0.01;
                    fuelConsumption.put(i, consumption);
                }
            }
        }else{
            for (int i = end_suspended_operation_year; i <= 2024; i++) {
                double loadFactor = getLoadFactorAVG(loadFactorMap, i);
                double consumption = unit.getThermal_capacity() / unit.getBurnup() * loadFactor* 0.01;
                fuelConsumption.put(i, consumption);
            }
        }
        unit.setFuel_consumption(fuelConsumption);
    }

    // Метод для получения коэффициента загрузки из Map
    private static double getLoadFactorAVG(TreeMap<Integer, Double> map, int year) {
        // Проверяем, есть ли значение для указанного года в Map
        // Если значение отсутствует, возвращаем дефолтное значение 85
        return map.getOrDefault(year, 85.0);
    }

    private static double getLoadFactorNull(TreeMap<Integer, Double> map, int year) {

        return map.getOrDefault(year, 0.0);
    }

    private int getYear(String date){
        try{
            String[] parts = date.split("-");
            // Получаем третий элемент массива, который содержит год, и преобразуем его в целочисленное значение
            return Integer.parseInt(parts[2]);
        }catch(Exception e){
            return -1;
        }
    }

    public void setUnderConstrFuelConsumption(Unit unit){
        TreeMap<Integer, Double> fuelConsumption = getDefaultMap();
        unit.setFuel_consumption(fuelConsumption);
    }

    public void setSusOperFuelConsumption(Unit unit){
        // Создаем TreeMap для хранения данных о потреблении топлива
        TreeMap<Integer, Double> loadFactorMap = getLoadFactorAnnualMap(unit.getId());
        TreeMap<Integer, Double> fuelConsumption = getDefaultMap();

        // Заполняем данные о потреблении топлива для последующих лет
        for (int i = 2014; i <= 2024; i++) {
            double loadFactor = getLoadFactorNull(loadFactorMap, i);
            double consumption = unit.getThermal_capacity() / unit.getBurnup() * loadFactor* 0.01;
            fuelConsumption.put(i, consumption);
        }

        unit.setFuel_consumption(fuelConsumption);
    }

    public void setPermanentShutFuelConsumption(Unit unit){
        // Создаем TreeMap для хранения данных о потреблении топлива
        TreeMap<Integer, Double> loadFactorMap = getLoadFactorAnnualMap(unit.getId());
        TreeMap<Integer, Double> fuelConsumption = getDefaultMap();
        int shutdownYear = getYear(unit.getPermanent_shutdown_date());
        // Заполняем данные о потреблении топлива для последующих лет
        for (int i = 2014; i <= shutdownYear; i++) {
            double loadFactor = getLoadFactorNull(loadFactorMap, i);
            double consumption = unit.getThermal_capacity() / unit.getBurnup() * loadFactor* 0.01;
            fuelConsumption.put(i, consumption);
        }

        unit.setFuel_consumption(fuelConsumption);
    }
    public DefaultTableModel aggregateCompany(){
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Компания");
        model.addColumn("Объем ежегодного потребления, т.");
        model.addColumn("Год");



        storageBD.getCompanies().forEach(company -> {
            TreeMap<Integer, Double> aggregatedFuelConsumption = new TreeMap<>();
            storageBD.getUnits().stream()
                    .filter(unit -> unit.getOperator_id() == company.getId())
                    .forEach(unit -> unit.getFuel_consumption().forEach((year, consumption) ->
                            aggregatedFuelConsumption.merge(year, consumption, Double::sum)
                    ));

            aggregatedFuelConsumption.forEach((year, consumption) ->
                    model.addRow(new Object[]{company.getCompany(), consumption, year})
            );
        });
        return model;
    }

    public DefaultTableModel aggregateCountry(){
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Страна");
        model.addColumn("Объем ежегодного потребления, т.");
        model.addColumn("Год");

        storageBD.getCountries().forEach(country -> {
            TreeMap<Integer, Double> aggregatedFuelConsumption = new TreeMap<>();
            storageBD.getUnits().stream()
                    .filter(unit -> {
                        int siteId = unit.getSite_id();
                        if(storageBD.getSites().get(siteId-1).getCountry_id() == country.getId()) return true;
                        else return false;
                    })
                    .forEach(unit -> unit.getFuel_consumption().forEach((year, consumption) ->
                            aggregatedFuelConsumption.merge(year, consumption, Double::sum)
                    ));

            aggregatedFuelConsumption.forEach((year, consumption) ->
                    model.addRow(new Object[]{country.getCountry(), consumption, year})
            );
        });

        return model;
    }

    public DefaultTableModel aggregateRegion(){
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Регион");
        model.addColumn("Объем ежегодного потребления, т.");
        model.addColumn("Год");

        storageBD.getRegions().forEach(region -> {
            TreeMap<Integer, Double> aggregatedFuelConsumption = new TreeMap<>();
            storageBD.getUnits().stream()
                    .filter(unit -> {
                        int siteId = unit.getSite_id();
                        int countryId = storageBD.getSites().get(siteId-1).getCountry_id();
                        if(storageBD.getCountries().get(countryId-1).getRegion_id() == region.getId()) return true;
                        else return false;
                    })
                    .forEach(unit -> unit.getFuel_consumption().forEach((year, consumption) ->
                            aggregatedFuelConsumption.merge(year, consumption, Double::sum)
                    ));

            aggregatedFuelConsumption.forEach((year, consumption) ->
                    model.addRow(new Object[]{region.getRegion_name(), consumption, year})
            );
        });

        return model;
    }

}
