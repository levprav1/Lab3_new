package org.example.manipulationBD;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public class ParserHTML {
    private ArrayList<String> countryLinks;// Список для хранения ссылок на страницы стран
    private HashMap<String, Integer> countryIdMap; // Создаем карту для хранения соответствия названия страны и ее id в базе данных
    private HashMap<String, Integer> siteIdMap;
    private HashMap<String, Integer> unitIdMap;// название unit и id site
    private TreeMap<String, Integer> companyIdMap;

    public void parseHTML2BD(Connector connector) throws SQLException, IOException, ParseException {
        countryLinks = new ArrayList<>();
        countryIdMap = new HashMap<>();
        siteIdMap = new HashMap<>();
        unitIdMap = new HashMap<>();
        companyIdMap = new TreeMap<>();

        fillCountryLinks();
        Connection connection = connector.getConnection();
        fillCountryIdMap(connection);
        fillSites(connection);
        fillUnits(connection);
        fillCompanies(connection);
    }

    private void fillCountryLinks() throws IOException {
        // Получаем страницу со списком стран
        Document doc = Jsoup.connect("https://pris.iaea.org/PRIS/CountryStatistics/CountryStatisticsLandingPage.aspx").get();
        // Ищем все ссылки на страницах стран
        Elements links = doc.select("a[href^=/PRIS/CountryStatistics/CountryDetails.aspx?current=]");
        // Проходимся по каждой ссылке и добавляем ее в список
        for (Element link : links) {
            String linkHref = link.attr("href");
            String countryLink = "https://pris.iaea.org" + linkHref;
            countryLinks.add(countryLink);
        }
        countryLinks.add("https://pris.iaea.org/PRIS/CountryStatistics/cDetails2.aspx?current=TW");
    }

    private void fillCountryIdMap(Connection connection) throws SQLException {
        // Получаем id для каждой страны из базы данных и добавляем в карту
        String sql = "SELECT country, id FROM public.countries";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                String countryName = resultSet.getString("country");
                int countryId = resultSet.getInt("id");
                countryIdMap.put(countryName, countryId);
            }
        }
    }
    private void fillSites(Connection connection) throws SQLException, IOException {
        int numUnits = 0;
        String insertSiteSql = "INSERT INTO public.sites (name, location, country_id) VALUES (?, ?, ?)";
        // Проходим по каждой стране и извлекаем данные о реакторах
        PreparedStatement insertSiteStatement = connection.prepareStatement(insertSiteSql);
        // Проходим по каждой стране и извлекаем данные о реакторах
        for (String countryLink : countryLinks) {
            // Получаем страницу страны
            Document countryDoc = Jsoup.connect(countryLink).get();
            // Получаем название страны из заголовка
            String countryName = countryDoc.selectFirst("#MainContent_MainContent_lblCountryName").text();
            String[] countryNameParts = countryName.split(",");
            countryName = countryNameParts[0].trim();
            // Получаем id страны из карты
            int countryId = countryIdMap.get(countryName);

            // Ищем таблицу с реакторами
            Element table = countryDoc.selectFirst("#content > div > table");
            // Извлекаем данные о реакторах
            Elements rows = table.select("tr");
            for (int i = 1; i < rows.size(); i++) { // Начинаем с 1, чтобы пропустить заголовок таблицы
                numUnits++;
                Element row = rows.get(i);
                Elements cols = row.select("td");
                String reactorName = cols.get(0).text().trim();
                // Обработка имени реактора
                String[] reactorNameParts = reactorName.split("-");
                String siteName;
                switch (reactorName) {
                    case "SUPER-PHENIX":
                        siteName = "SUPER-PHENIX";
                        break;
                    case "THTR-300":
                        siteName = "THTR-300";
                        break;
                    case "BREST-OD-300":
                        siteName = "BREST-OD-300";
                        break;
                    default:
                        siteName = (reactorNameParts.length <= 2) ? reactorNameParts[0].trim() : reactorNameParts[0].trim() + "-" + reactorNameParts[1].trim();;
                }
                if (!siteIdMap.containsKey(siteName)){
                    siteIdMap.put(siteName, (siteIdMap.isEmpty()) ? 1 : siteIdMap.size()+1);;
                    unitIdMap.put(reactorName.trim(), siteIdMap.get(siteName));
                    // Получаем местоположение реактора
                    String location = cols.get(3).text();
                    // Добавляем данные в подготовленный SQL-запрос
                    insertSiteStatement.setString(1, siteName);
                    insertSiteStatement.setString(2, location);
                    insertSiteStatement.setInt(3, countryId);
                    // Выполняем запрос
                    insertSiteStatement.executeUpdate();
                }else {
                    unitIdMap.put(reactorName.trim(), siteIdMap.get(siteName));
                }
            }
        }
    }

    private void fillUnits(Connection connection) throws SQLException, IOException, ParseException {
        String insertQuery = "INSERT INTO public.units (name, site_id, status, type, model, owner_id, operator_id, net_capacity, design_net_capacity, gross_capacity, thermal_capacity, construction_start_date, first_criticaly_date, first_grid_connection, commercial_operation_date, suspended_operation_date, end_suspended_operation_date, permanent_shutdown_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(insertQuery);
        int unit_id = 0;
        // Перебор ссылок на страницы
        for (int i = 1; i < 1125 || i == 2121 || i == 2127; i++) {
            try {
                Document unitDoc = Jsoup.connect("https://pris.iaea.org/PRIS/CountryStatistics/ReactorDetails.aspx?current=" + i).get();
                String unitName = unitDoc.selectFirst("#MainContent_MainContent_lblReactorName").text().trim();
                int site_id = unitIdMap.get(unitName);
                String status = unitDoc.select("#MainContent_MainContent_lblReactorStatus").text().trim();
                String type = unitDoc.select("#MainContent_MainContent_lblType").text().trim();
                String model = unitDoc.select("#MainContent_MainContent_lblModel").text().trim();
                String owner;
                try { owner = unitDoc.select("#MainContent_MainContent_hypOwnerUrl").text().trim();
                    if (owner.isEmpty()) throw new Exception();
                } catch (Exception e) {
                    owner = unitDoc.select("h5").get(3).text().trim();
                    if(owner.isEmpty()) owner= "No data";
                }
                if(owner.equals("Kozloduy Npp ,lc")) owner = "Kozloduy Nuclear power plant";
                if (!companyIdMap.containsKey(owner))
                    companyIdMap.put(owner, (companyIdMap.isEmpty()) ? 1 : companyIdMap.size() + 1);
                int ownerId = companyIdMap.get(owner);

                String operator;
                try {
                    operator = unitDoc.select("#MainContent_MainContent_hypOperatorUrl").text().trim();
                    if (operator.isEmpty()) throw new Exception();
                } catch (Exception e) {
                    operator = unitDoc.select("h5").get(4).text().trim();
                    if(operator.isEmpty()) operator= "No data";
                }
                if(operator.equals("Kozloduy Npp ,lc")) operator = "Kozloduy Nuclear power plant";
                if (!companyIdMap.containsKey(operator))
                    companyIdMap.put(operator, (companyIdMap.isEmpty()) ? 1 : companyIdMap.size() + 1);
                int operatorId = companyIdMap.get(operator);
                int net_capacity = Integer.parseInt(unitDoc.select("#MainContent_MainContent_lblNetCapacity").text().trim());
                int design_net_capacity = Integer.parseInt(unitDoc.select("#MainContent_MainContent_lblDesignNetCapacity").text().trim());
                int gross_capacity = Integer.parseInt(unitDoc.select("#MainContent_MainContent_lblGrossCapacity").text().trim());
                int thermal_capacity = Integer.parseInt(unitDoc.select("#MainContent_MainContent_lblThermalCapacity").text().trim());
                String construction_start_date = unitDoc.select("#MainContent_MainContent_lblConstructionStartDate").text().trim();
                String first_criticaly_date = unitDoc.select("#MainContent_MainContent_lblFirstCriticality").text().trim();
                String first_grid_connection = unitDoc.select("#MainContent_MainContent_lblGridConnectionDate").text().trim();
                String commercial_operation_date = unitDoc.select("#MainContent_MainContent_lblCommercialOperationDate").text().trim();
                String suspended_operation_date;
                try {
                    suspended_operation_date = unitDoc.select("#MainContent_MainContent_lblLongTermShutdownDate").text().trim();
                } catch (Exception e) {
                    suspended_operation_date = null;
                }
                String end_suspended_operation_date;
                try {
                    end_suspended_operation_date = unitDoc.select("#MainContent_MainContent_lblRestartDate").text().trim();
                } catch (Exception e) {
                    end_suspended_operation_date = null;
                }
                String permanent_shutdown_date;
                try {
                    permanent_shutdown_date = unitDoc.select("#MainContent_MainContent_lblPermanentShutdownDate").text().trim();
                } catch (Exception e) {
                    permanent_shutdown_date = null;
                }

                // Установка значений в запросе на вставку
                statement.setString(1, unitName);
                statement.setInt(2, site_id);
                statement.setString(3, status);
                statement.setString(4, type);
                statement.setString(5, model);
                statement.setInt(6, ownerId);
                statement.setInt(7, operatorId);
                statement.setInt(8, net_capacity);
                statement.setInt(9, design_net_capacity);
                statement.setInt(10, gross_capacity);
                statement.setInt(11, thermal_capacity);

                statement.setString(12, getDate(construction_start_date));
                statement.setString(13, getDate(first_criticaly_date));
                statement.setString(14, getDate(first_grid_connection));
                statement.setString(15, getDate(commercial_operation_date));
                statement.setString(16, getDate(suspended_operation_date));
                statement.setString(17, getDate(end_suspended_operation_date));
                statement.setString(18, getDate(permanent_shutdown_date));
                // Выполнение запроса на вставку
                statement.executeUpdate();
                unit_id++;
                fillOperatingHistory(unitDoc, unit_id, connection);
                if(i==1124) i = 2120;
                if(i==2121) i = 2126;
            }catch (Exception e){
            }

    }
}

	
	private void fillOperatingHistory(Document unitDoc, int unit_id, Connection connection) throws SQLException {

            Element table = unitDoc.selectFirst("#MainContent_MainContent_divPerformanceAndHistory > div:nth-child(4) > table");
            if (table != null) {
                // Prepare SQL statement
                String sql = "INSERT INTO public.operating_history (year, electricity_supplied, preference_unit_power, " +
                        "annual_time_line, operating_factor, energy_av_factor_annual, energy_av_factor_cummulative, " +
                        "load_factor_annual, load_factor_cummulative, unit_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement statement = connection.prepareStatement(sql);

                // Parse table rows
                Elements rows = table.select("tr");
                for (int i = 2; i < rows.size(); i++) { // Start from index 1 to skip header row
                    Element row = rows.get(i);
                    Elements cols = row.select("td");

                    // Set values for SQL statement
                    for (int j = 0; j < 9; j++) {
                        try {
                            String value = cols.get(j).text();

                            if (value.isEmpty()) {
                                statement.setObject(j + 1, null);
                            } else {
                                try {
                                    int valueInt = Integer.parseInt(value);
                                    statement.setInt(j + 1, valueInt);
                                } catch (NumberFormatException e) {
                                    try {
                                        double valueDouble = Double.parseDouble(value);
                                        statement.setDouble(j + 1, valueDouble);
                                    } catch (NumberFormatException e2) {
                                        statement.setObject(j + 1, null);
                                    }
                                }
                            }
                        }catch (Exception e) {
                            statement.setObject(j + 1, null);
                        }
                    }
                    statement.setInt(10, unit_id);
                    statement.addBatch();
                }

                // Execute batch insert
                statement.executeBatch();
                //connection.close();
            }
    }
	private String getDate(String dateString) throws ParseException {
		if(dateString.equals("N/A")|| dateString.equals("")) return null;
		else{
		SimpleDateFormat format = new SimpleDateFormat("dd MMM, yyyy", Locale.ENGLISH);
		SimpleDateFormat newFormat = new SimpleDateFormat("dd-MM-yyyy");

            java.util.Date utilDate = format.parse(dateString);
            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime()); // Преобразовываем util.Date в sql.Date

            String new_date = newFormat.format(sqlDate);
            return new_date;
		}
	}
	
	private void fillCompanies(Connection connection) throws SQLException {
		String insertSQL = "INSERT INTO public.companies (company) VALUES (?)";
        List<Map.Entry<String, Integer>> sortedEntries = new ArrayList<>(companyIdMap.entrySet());

        // Сортировка списка на основе значений Integer
        Collections.sort(sortedEntries, Map.Entry.comparingByValue());

            try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
                for (Map.Entry<String, Integer> entry : sortedEntries) {
                    preparedStatement.setString(1, entry.getKey());
                    preparedStatement.executeUpdate();
                }
            }
		
	}


}
