package org.example.collections;

import org.example.dataBD.Unit;
import org.example.reactors.Reactor;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ReactorsManipulation {
    private StorageBD storageBDInitial = new StorageBD();
    private  StorageBD storageBD = new StorageBD();
    private ReactorStorage reactorStorage = new ReactorStorage();

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
    }

    public String areDataGet() {
        String areExist = "Данные получены";
        if (storageBDInitial.getUnits().isEmpty()||storageBDInitial.getCountries().isEmpty()||storageBDInitial.getCompanies().isEmpty()
        ||storageBDInitial.getSites().isEmpty()||storageBDInitial.getRegions().isEmpty()) areExist = "Данные не получены";

        return areExist;
    }
    public String areDataReactorGet() {
        String areExist = "Файл с реакторами загружен";
        if (reactorStorage.getReactors().isEmpty()) areExist = "Файл с реакторами не загружен";

        return areExist;
    }

    public void filterUnitsInOperation(){
        storageBD.setUnits ((ArrayList<Unit>) storageBD.getUnits().stream()
                .filter(unit -> unit.getStatus().equals("in operation"))
                .collect(Collectors.toList()));
    }

//    public void addInfo2Units(){
//
//        Map<String, Double> reactorBurnupMap = reactorCollection.getReactors().stream()
//                .collect(Collectors.toMap(r -> r.getClassReactor(), r -> r.getBurnup()));
//
//        storageBD.getUnits().forEach(u -> {
//            u.setBurnup(reactorBurnupMap.getOrDefault(u.getClass_(), 0.0));
//            if(u.getBurnup()==0.0){
//                if(u.getClass_().equals("AGR")) u.setBurnup(reactorBurnupMap.getOrDefault("MAGNOX", 0.0));
//                else if(u.getClass_().equals("Hualong 1")) u.setBurnup(reactorBurnupMap.getOrDefault("CPR-1000", 0.0));
//                        else if(u.getClass_().equals("CNP-1000")) u.setBurnup(reactorBurnupMap.getOrDefault("CPR-1000", 0.0));
//                            else if(u.getClass_().substring(0,3).equals("PWR")) u.setBurnup(reactorBurnupMap.getOrDefault("PWR", 0.0));
//                                else if(u.getClass_().substring(0,4).equals("VVER")) u.setBurnup(reactorBurnupMap.getOrDefault("VVER-1200", 0.0));
//
//            }
//        });
//
//    }

//    public void addFuelConsumption(){
//        storageBD.getUnits().stream().filter(unit -> unit.getBurnup()!=0.0).forEach(u -> {
//            if(u.getLoad_factor() == 0) u.setLoad_factor(90);
//            u.setFuelConsumption(365*u.getThermal_capacity() / u.getBurnup() * u.getLoad_factor() * 0.01/1000);
//        });
//
//        Map<String, Double> reactorFirstLoadMap = reactorCollection.getReactors().stream()
//                .collect(Collectors.toMap(r -> r.getClassReactor(), r -> r.getFirst_load()));
//
//        storageBDInitial.getUnits().forEach(unit -> {
//            if(unit.getCommercial_operation()!=null&&unit.getCommercial_operation().substring(0, 4).equals("2023")){
//                storageBD.addUnit(unit);
//                int index = storageBD.getUnits().indexOf(unit);
//                storageBD.getUnits().get(index).setFuelConsumption(reactorFirstLoadMap.getOrDefault(unit.getClass_(), 0.0));
//                if(storageBD.getUnits().get(index).getClass_().equals("Hualong 1")) storageBD.getUnits().get(index)
//                        .setFuelConsumption(reactorFirstLoadMap.getOrDefault("CPR-1000", 0.0));
//                else if (storageBD.getUnits().get(index).getClass_().substring(0,4).equals("VVER"))storageBD.getUnits().get(index)
//                        .setFuelConsumption(reactorFirstLoadMap.getOrDefault("VVER-1200", 0.0));
//            }
//        });
//    }
//    public Map<String, Double> aggregateCountry(){
//
//        Map<Integer, Double> fuelConsumptionBySite = storageBD.getUnits().stream()
//                .collect(Collectors.groupingBy(Unit::getSite,
//                        Collectors.summingDouble(Unit::getFuelConsumption)));
//
//        Map<String, Double> countryFuelConsumption = new HashMap<>();
//
//        storageBD.getCountries().forEach(country -> {
//            double fuelConsumption = storageBD.getSites().stream()
//                    .filter(site -> site.getPlace() == country.getId())
//                    .mapToDouble(site -> fuelConsumptionBySite.getOrDefault(site.getId(),0.0))
//                    .sum();
//            countryFuelConsumption.put(country.getCountry_name(),fuelConsumption);
//        });
//
//        return countryFuelConsumption;
//    }
//
//    public Map<String, Double> aggregateRegion(){
//
//        Map<String, Double> regionFuelConsumption = new HashMap<>();
//
//        Map<String, Double> fuelConsumptionByCountry = aggregateCountry();
//        storageBD.getRegions().forEach(region -> {
//            double sumFuelConsumption = storageBD.getCountries().stream()
//                    .filter(country -> country.getRegion_id() == region.getId())
//                    .mapToDouble(country -> fuelConsumptionByCountry.getOrDefault(country.getCountry_name(), 0.0))
//                    .sum();
//            regionFuelConsumption.put(region.getRegion_name(), sumFuelConsumption);
//        });
//
//        return regionFuelConsumption;
//    }
//
//    public Map<String, Double> aggregateCompany(){
//
//        Map<Integer, Double> reactorsByOperator = storageBD.getUnits().stream()
//                .collect(Collectors.groupingBy(Unit::getOperator,
//                        Collectors.summingDouble(Unit::getFuelConsumption)));
//
//        Map<String, Double> companyFuelConsumption = new HashMap<>();
//
//        storageBD.getCompanies().forEach(company -> {
//            companyFuelConsumption.put(company.getCompanies_name(), reactorsByOperator.getOrDefault(company.getId(), 0.0));
//        });
//        return companyFuelConsumption;
//    }

}
