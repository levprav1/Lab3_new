package org.example.manipulationBD;

import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import java.util.ArrayList;

import org.example.collections.StorageBD;
import org.example.readersBD.*;

import javax.swing.*;

public class BuilderBD {
    private Connector connector = new Connector();
    private ArrayList<String> tablesCreation;
    private ArrayList<String> tablesDelete;
    private ArrayList<ReaderBD> readerBDS = new ArrayList<>();
    private String[] tableNames;
    private ExcelToDatabase excelToDatabase = new ExcelToDatabase();
    private ParserHTML parserHTML = new ParserHTML();

    public BuilderBD() {
        this.tableNames = new String[]{"public.units", "public.operating_history","public.sites", "public.countries", "public.companies", "public.regions"};
        tablesCreation = new ArrayList<>();
        tablesCreation.add("CREATE TABLE IF NOT EXISTS public.units ("
                + "id SERIAL PRIMARY KEY,"
                + "name TEXT,"
                + "site_id SMALLINT,"
                + "status TEXT,"
                + "type TEXT,"
                + "model TEXT,"
				+ "owner_id SMALLINT,"
				+ "operator_id SMALLINT,"
				+ "net_capacity SMALLINT,"
				+ "design_net_capacity SMALLINT,"
				+ "gross_capacity SMALLINT,"
				+ "thermal_capacity SMALLINT,"
				+ "construction_start_date TEXT,"
				+ "first_criticaly_date TEXT,"
				+ "first_grid_connection TEXT,"
				+ "commercial_operation_date TEXT,"
                + "suspended_operation_date TEXT,"
                + "end_suspended_operation_date TEXT,"
				+ "permanent_shutdown_date TEXT"
				+ ")");
		tablesCreation.add("CREATE TABLE IF NOT EXISTS public.operating_history (" +
                "id SERIAL PRIMARY KEY, " +
				"year SMALLINT, " +
				"electricity_supplied NUMERIC," +
				"preference_unit_power SMALLINT, " +
				"annual_time_line SMALLINT, " +
				"operating_factor NUMERIC," +
				"energy_av_factor_annual NUMERIC," +
				"energy_av_factor_cummulative NUMERIC," +
				"load_factor_annual NUMERIC," +
				"load_factor_cummulative NUMERIC," +
				"unit_id SMALLINT )");
        tablesCreation.add("CREATE TABLE IF NOT EXISTS public.sites (" +
                "id SERIAL PRIMARY KEY, " +
                "name TEXT, " +
				"location TEXT, " +
                "country_id SMALLINT ) ");
        tablesCreation.add("CREATE TABLE IF NOT EXISTS public.countries ("
                + "id SERIAL PRIMARY KEY,"
                + "country TEXT,"
                + "region_id SMALLINT"
                + ");");
        tablesCreation.add("CREATE TABLE IF NOT EXISTS public.regions ("
                + "id SERIAL PRIMARY KEY,"
                + "region TEXT"
                + ");");
        tablesCreation.add("CREATE TABLE IF NOT EXISTS public.companies ("
                + "id SERIAL PRIMARY KEY,"
                + "company TEXT"
                + ");");

        tablesDelete = new ArrayList<>();
        tablesDelete.add("DROP TABLE IF EXISTS public.units;");
		tablesDelete.add("DROP TABLE IF EXISTS public.operating_history;");
        tablesDelete.add("DROP TABLE IF EXISTS public.sites;");
        tablesDelete.add("DROP TABLE IF EXISTS public.countries;");
        tablesDelete.add("DROP TABLE IF EXISTS public.companies;");
        tablesDelete.add("DROP TABLE IF EXISTS public.regions;");

		readerBDS.add(new ReaderCountries());
		readerBDS.add(new ReaderRegions());
        readerBDS.add(new ReaderUnits());
		readerBDS.add(new ReaderOperatingHistory());
		readerBDS.add(new ReaderSites());
		readerBDS.add(new ReaderCompanies());
    }

    public String areTablesExist() throws SQLException {
        String areExist = "БД создана";
        Statement stmt = connector.createStatement();
            for (String tableName : tableNames) {
                ResultSet resultSet = stmt.executeQuery("SELECT COUNT(*) FROM " + tableName);
                if (!resultSet.next()) areExist = "БД не создана";
            }

        return areExist;
    }

    public String areDataExist() {
        String areExist = "Данные загружены в БД";

        try (Statement stmt = connector.createStatement()) {
            for (String tableName : tableNames) {
                ResultSet resultSet = stmt.executeQuery("SELECT COUNT(*) FROM " + tableName);
                if (!resultSet.next()) areExist = "Данные не загружены в БД";
                else {
                    int rowCount = resultSet.getInt(1);
                    if (rowCount == 0) areExist = "Данные не загружены в БД";
                }
            }
        }catch (SQLException e) {
            areExist = "Данные не загружены в БД";
        }

        return areExist;
    }

    public void createBD() throws SQLException {
        Statement stmt = connector.createStatement();
            tablesCreation.forEach(table -> {
                try{
                stmt.executeUpdate(table);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });

    }

    public void deleteBD() throws SQLException {
        Statement stmt = connector.createStatement();

        tablesDelete.forEach(table -> {
            try {
                stmt.executeUpdate(table);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "Oшибка", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    public void fillBD() throws SQLException, IOException, ParseException {
        excelToDatabase.createTables(connector);
        parserHTML.parseHTML2BD(connector);
    }
    public StorageBD getDataFromBD() throws SQLException {
        StorageBD storageBD = new StorageBD();
        for (ReaderBD r : readerBDS) {
            r.getData(connector, storageBD);
        }
        return storageBD;
    }

}
